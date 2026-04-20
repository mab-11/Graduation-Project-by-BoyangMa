package com.pxw.control;

import com.pxw.pojo.CourseTable;
import com.pxw.pojo.Task;
import com.pxw.service.CourseTableService;
import com.pxw.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pxw on 2022/4/22 16:02
 *
 * @author pxw
 */


@RestController
@RequestMapping("courseTable")
public class CourseTableController {

    @Autowired
    private CourseTableService courseTableService;

    @Autowired
    private ExperimentService experimentService;

    @RequestMapping("selectAll")
    public String selectAll(@RequestParam Integer currentPage, @RequestParam Integer pageSize){
        return courseTableService.selectAll(currentPage,pageSize);
    }

    @RequestMapping("selectByCondition")
    public String selectByCondition(@RequestBody CourseTable courseTable, @RequestParam Integer currentPage, @RequestParam Integer pageSize){
        System.out.println(courseTable.getTask().getTeacher());
        return courseTableService.selectByCondition(courseTable,currentPage,pageSize);
    }

    @RequestMapping("selectEmptyRoom")
    public String selectEmptyRoom(@RequestBody CourseTable courseTable, @RequestParam Integer currentPage, @RequestParam Integer pageSize){
        return courseTableService.selectEmptyRoom(courseTable,currentPage,pageSize);
    }

    @RequestMapping("selectTask")
    public String selectTask(@RequestBody Task task,@RequestParam Integer currentPage, @RequestParam Integer pageSize){
        return courseTableService.selectTask(task,currentPage,pageSize);
    }

    @RequestMapping("selectTable")
    public String selectByTable(@RequestBody CourseTable courseTable){
        return courseTableService.selectTable(courseTable);
    }


    @RequestMapping("deleteByIds")
    public String delete(@RequestBody int[] ids) {
        courseTableService.deleteByIds(ids);
        return "success";
    }

    @RequestMapping("add")
    public String add(@RequestBody CourseTable courseTable){
        return courseTableService.add(courseTable);
    }

    @RequestMapping("update")
    public String update(@RequestBody CourseTable courseTable){
        return courseTableService.update(courseTable);
    }


    @RequestMapping("reArrangeCourse")
    public String reArrangeCourse(){
        return courseTableService.GA();
    };

    /**
     * Run comparison experiment (fixed parameter GA vs adaptive GA)
     */
    @RequestMapping("runComparison")
    public String runComparison(@RequestParam(required = false, defaultValue = "Comparison experiment") String name){
        try {
            return experimentService.runComparison(name);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\", \"type\": \"" + e.getClass().getName() + "\"}";
        }
    }

    /**
     * Get convergence curve data (for plotting)
     */
    @RequestMapping("getConvergenceData")
    public String getConvergenceData(){
        return experimentService.getConvergenceData();
    }

    /**
     * Get experiment history
     */
    @RequestMapping("getHistory")
    public String getHistory(){
        return experimentService.getHistory();
    }

    /**
     * Clear history data
     */
    @RequestMapping("clearHistory")
    public String clearHistory(){
        return experimentService.clearHistory();
    }

    /**
     * Multi-scale test
     */
    @RequestMapping("runMultiScaleTest")
    public String runMultiScaleTest(){
        return experimentService.runMultiScaleTest();
    }

    /**
     * Parameter sensitivity test
     */
    @RequestMapping("runParameterTest")
    public String runParameterTest(){
        return experimentService.runParameterTest();
    }

    /**
     * Run basic course scheduling algorithm
     */
    @RequestMapping("reArrangeCourseBasic")
    public String reArrangeCourseBasic(){
        return courseTableService.GABasic();
    };

}
