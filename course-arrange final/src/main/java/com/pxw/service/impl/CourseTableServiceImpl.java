package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.CourseTableMapper;
import com.pxw.mapper.RoomMapper;
import com.pxw.mapper.TaskMapper;
import com.pxw.pojo.CourseTable;
import com.pxw.pojo.GA.GA;
import com.pxw.pojo.GA.Population;
import com.pxw.pojo.GA.Bootstrap;
import com.pxw.pojo.GA.AdaptiveGA;
import com.pxw.pojo.GA.Individual;
import com.pxw.pojo.GA.ExperimentRunner;
import com.pxw.pojo.Room;
import com.pxw.pojo.Task;
import com.pxw.pojo.Timeslot;
import com.pxw.service.CourseTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pxw on 2022/4/24 14:48
 *
 * @author pxw
 */

@Service
public class CourseTableServiceImpl implements CourseTableService {

    @Autowired
    private CourseTableMapper courseTableMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private TaskMapper taskMapper;


    @Override
    public String selectAll(Integer currentPage, Integer pageSize) {
        return null;
    }

    @Override
    public String selectByCondition(CourseTable courseTable, Integer currentPage, Integer pageSize) {
        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<CourseTable> courseTables = courseTableMapper.selectByCondition(courseTable);
        System.out.println(courseTables.toString());

        // Pagination info
        PageInfo<CourseTable> courseTablePageInfo = new PageInfo<>(courseTables);
        System.out.println(courseTablePageInfo);

        // Avoid duplicate reference $ref, frontend no data display
        return JSON.toJSONString(courseTablePageInfo, SerializerFeature.DisableCircularReferenceDetect);

    }

    @Override
    public String selectEmptyRoom(CourseTable courseTable, Integer currentPage, Integer pageSize) {

        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<CourseTable> courseTables = courseTableMapper.selectEmptyRoom(courseTable);

        // Pagination info
        PageInfo<CourseTable> courseTablePageInfo = new PageInfo<>(courseTables);
        System.out.println(courseTablePageInfo);
        // Avoid duplicate reference $ref, frontend no data display
        return JSON.toJSONString(courseTablePageInfo, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public String selectTable(CourseTable courseTable) {
        // Query condition is empty, do not call query.
        if ( (null == courseTable.getTask().getCclasses().getClassesName() ||
                courseTable.getTask().getCclasses().getClassesName().equals("")) &&
                (courseTable.getTask().getTeacher().getTeacherName()==null ||
                        courseTable.getTask().getTeacher().getTeacherName().equals(""))&&
                (courseTable.getRoom().getRoomName()==null||
                        courseTable.getRoom().getRoomName().equals("")))
            return "";
        // Conditional query
        List<CourseTable> courseTables = courseTableMapper.selectTable(courseTable);
        String jsonString = JSON.toJSONString(courseTables, SerializerFeature.DisableCircularReferenceDetect);
        System.out.println(jsonString);
        return jsonString;

    }

    @Override
    public String selectTask(Task task,Integer currentPage,Integer pageSize) {
        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<Task> tasks = courseTableMapper.selectTask(task);

        // Pagination info
        PageInfo<Task> taskPageInfo = new PageInfo<>(tasks);
        System.out.println(taskPageInfo);
        // Avoid duplicate reference $ref, frontend no data display
        return JSON.toJSONString(taskPageInfo, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public void deleteByIds(int[] ids) {
        courseTableMapper.deleteByIds(ids);
    }

    @Override
    public String update(CourseTable courseTable) {

        String result = isClash(courseTable);
         if (result.equals("success")){
             courseTableMapper.update(courseTable);
             return "success";
         }else {
             return result;
         }


    }

    @Override
    public String add(CourseTable courseTable) {
        String result = isClash(courseTable);
        if (result.equals("success")){
            courseTableMapper.addCourseTable(courseTable);
            return "success";
        }else {
            return result;
        }
    }

    public String isClash(CourseTable courseTable){
        // Judge whether there is conflict
        // Class time conflict
        String[] split = courseTable.getTask().getCclasses().getClassesName().split(",");
        Integer timeId = courseTable.getTimeslot().getId();
        for (String classesName : split){
            // Query result is empty but class is already created not null, should judge size
            if (courseTableMapper.selectByCondition2(classesName, timeId, null).size() != 0){
                return "Operation failed, selected result conflicts with existing course schedule! (Class time)";
            }
        }
        Integer teacherId = courseTable.getTask().getTeacher().getId();
        // Teacher time conflict
        if (courseTableMapper.selectByCondition2(null,timeId,teacherId).size() != 0 ){
            return "Operation failed, selected result conflicts with existing course schedule! (Teacher time)";
        }

        return "success";
    }

    // Course scheduling algorithm - multi-objective optimization version
    @Override
    public String GA(){

        // Initialize timetable management class
        List<Room> rooms = roomMapper.selectAll();
        List<Task> tasks = taskMapper.selectAll();
        List<Timeslot> timeslots = courseTableMapper.selectAllTimeslot();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setRooms(rooms);
        bootstrap.setTimeslots(timeslots);
        bootstrap.setTasks(tasks);

        // Use adaptive genetic algorithm
        // Parameters: population size 100, mutation rate 0.05 (initial), crossover rate 0.85, elite individuals 5, tournament 5
        AdaptiveGA ga = new AdaptiveGA(100, 0.05, 0.85, 5, 5);

        // Initialize population
        Population population = ga.initPopulation(bootstrap);
        // Calculate population fitness (using multi-objective fitness calculator)
        ga.evalPopulationFitness(population, bootstrap);

        // Track generation
        int generation = 1;
        // Course scheduling result flag
        boolean flag = false;

        // Start evolution loop
        // Add termination condition - hard constraints fully satisfied or reached max generation
        while (!ga.isEnd(generation, 1000) && !ga.isEnd(population)) {

            // Adaptive parameter adjustment (dynamic adjustment based on population diversity)
            ga.adaptParameters(population);

            // Print current state
            if (generation % 50 == 0) {
                Individual best = population.getFittest(0);
                System.out.printf("G%d Best fitness: %.4f | HardPenalty: %.2f | SoftScore: %.2f | Util: %.2f%n",
                        generation, best.getFitness(),
                        best.getHardPenalty(), best.getSoftScore(), best.getResourceUtil());
                ga.printParameters();
            }

            // Crossover (multiple crossover operators)
            population = ga.crossover(population, bootstrap);

            // Mutation (multiple mutation operators)
            population = ga.mutate(population, bootstrap);

            // Calculate population fitness
            ga.evalPopulationFitness(population, bootstrap);

            generation++;
        }

        // Get best solution
        Individual bestIndividual = population.getFittest(0);

        // Judge course scheduling result (hard constraint penalty 0 means fully satisfied)
        if (bestIndividual.getHardPenalty() == 0) {
            flag = true;
        }

        // Print final result
        bootstrap.createTable(bestIndividual);
        System.out.println();
        System.out.println("=== Course scheduling result ===");
        System.out.println("Solution found in " + generation + " generations");
        System.out.printf("Final fitness: %.4f%n", bestIndividual.getFitness());
        System.out.printf("Hard constraint penalty: %.2f%n", bestIndividual.getHardPenalty());
        System.out.printf("Soft constraint score: %.2f%n", bestIndividual.getSoftScore());
        System.out.printf("Resource utilization: %.2f%n", bestIndividual.getResourceUtil());
        System.out.printf("Clashes: %d%n", bootstrap.calcClashes());
        System.out.println();

        // Course scheduling success
        if (flag) {
            // Insert into database
            // Delete previous course scheduling result
            courseTableMapper.deleteAll();
            CourseTable[] tables = bootstrap.getTables();
            courseTableMapper.addCourseTables(tables);

            return "success";
        }

        return "fail";
    }

    /**
     * Course scheduling algorithm - basic version (keep original implementation)
     */
    public String GABasic(){

        List<Room> rooms = roomMapper.selectAll();
        List<Task> tasks = taskMapper.selectAll();
        List<Timeslot> timeslots = courseTableMapper.selectAllTimeslot();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setRooms(rooms);
        bootstrap.setTimeslots(timeslots);
        bootstrap.setTasks(tasks);

        // Initialize GA(population size, mutation rate, crossover rate, elite individual count, tournament individual count)
        GA ga = new GA(100, 0.01, 0.9, 2, 5);
        // Initialize population
        Population population = ga.initPopulation(bootstrap);
        // Calculate population fitness
        ga.evalPopulationFitness(population, bootstrap);
        // Track generation
        int generation = 1;
        // Course scheduling result flag
        boolean flag = false;

        // Start evolution loop
        while (!ga.isEnd(generation, 1000) && !ga.isEnd(population)){

            // Print fitness
            System.out.println("G" +generation +" Best fitness: "+population.getFittest(0).getFitness());

            // Crossover Tournament uniform crossover
            population = ga.crossover(population, bootstrap);

            // Mutation
            population = ga.mutate(population, bootstrap);
            // Calculate population fitness
            ga.evalPopulationFitness(population, bootstrap);

            generation++;

        }
        // Judge course scheduling result
        if (population.getFittest(0).getFitness()==1.0) {
            flag=true;
        }
        // Print final fitness
        // Create timetable
        bootstrap.createTable(population.getFittest(0));
        System.out.println();
        System.out.println("Solution found in "+generation +" generations");
        System.out.println("Final solution fitness: "+population.getFittest(0).getFitness());
        System.out.println("Clashes: "+ bootstrap.calcClashes());
        System.out.println();

        // Course scheduling success
        if (flag){
            // Insert into database
            // Delete previous course scheduling result
            courseTableMapper.deleteAll();
            CourseTable[] tables = bootstrap.getTables();
            courseTableMapper.addCourseTables(tables);

            return "success";
        }

        return "fail";
    }

    /**
     * Run experiment comparison
     */
    public String runExperiment() {
        List<Room> rooms = roomMapper.selectAll();
        List<Task> tasks = taskMapper.selectAll();
        List<Timeslot> timeslots = courseTableMapper.selectAllTimeslot();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setRooms(rooms);
        bootstrap.setTimeslots(timeslots);
        bootstrap.setTasks(tasks);

        // Create experiment runner
        ExperimentRunner runner = new ExperimentRunner(10, 1000);

        // Run comparison experiment
        runner.compareParameters(bootstrap);

        return "Experiment completed";
    }


}
