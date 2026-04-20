package com.pxw.service.impl;

import com.pxw.mapper.CourseTableMapper;
import com.pxw.mapper.RoomMapper;
import com.pxw.mapper.TaskMapper;
import com.pxw.pojo.*;
import com.pxw.pojo.GA.*;
import com.pxw.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Experiment service implementation
 */
@Service
public class ExperimentServiceImpl implements ExperimentService {

    @Autowired
    private CourseTableMapper courseTableMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private TaskMapper taskMapper;

    // Experiment analyzer (save state)
    private ExperimentAnalyzer analyzer = new ExperimentAnalyzer();

    @Override
    public String runComparison(String experimentName) {
        // Get data
        Bootstrap bootstrap = getBootstrap();

        // Run comparison experiment
        ExperimentAnalyzer.ComparisonResult result = analyzer.runComparisonExperiment(bootstrap, experimentName);

        return result.toJson();
    }

    @Override
    public String getConvergenceData() {
        return analyzer.getConvergenceDataJson();
    }

    @Override
    public String getHistory() {
        return analyzer.getHistoryJson();
    }

    @Override
    public String clearHistory() {
        analyzer.clearHistory();
        return "{\"status\": \"success\"}";
    }

    @Override
    public String runMultiScaleTest() {
        Bootstrap bootstrap = getBootstrap();

        StringBuilder result = new StringBuilder();
        result.append("{\n");
        result.append("  \"scales\": [\n");

        int[] populationSizes = {50, 100, 150, 200};
        int[] mutationRates = {50, 100, 150, 200};

        for (int i = 0; i < populationSizes.length; i++) {
            int popSize = populationSizes[i];

            System.out.println("Testing population size: " + popSize);

            AdaptiveGA ga = new AdaptiveGA(popSize, 0.05, 0.85, 5, 5);
            Population population = ga.initPopulation(bootstrap);
            ga.evalPopulationFitness(population, bootstrap);

            int generation = 0;
            long startTime = System.currentTimeMillis();

            while (!ga.isEnd(generation, 500) && !ga.isEnd(population)) {
                ga.adaptParameters(population);
                population = ga.crossover(population, bootstrap);
                population = ga.mutate(population, bootstrap);
                ga.evalPopulationFitness(population, bootstrap);
                generation++;
            }

            long execTime = System.currentTimeMillis() - startTime;
            double fitness = population.getFittest(0).getFitness();

            if (i > 0) result.append(",\n");
            result.append("    {\n");
            result.append("      \"populationSize\": ").append(popSize).append(",\n");
            result.append("      \"generations\": ").append(generation).append(",\n");
            result.append("      \"fitness\": ").append(String.format("%.4f", fitness)).append(",\n");
            result.append("      \"executionTime\": ").append(execTime).append("\n");
            result.append("    }");
        }

        result.append("\n  ]\n");
        result.append("}");

        return result.toString();
    }

    @Override
    public String runParameterTest() {
        Bootstrap bootstrap = getBootstrap();

        StringBuilder result = new StringBuilder();
        result.append("{\n");
        result.append("  \"parameters\": [\n");

        // Test different mutation rates
        double[] mutationRates = {0.01, 0.03, 0.05, 0.1, 0.2};

        for (int i = 0; i < mutationRates.length; i++) {
            double mutRate = mutationRates[i];

            System.out.println("Testing mutation rate: " + mutRate);

            AdaptiveGA ga = new AdaptiveGA(100, mutRate, 0.85, 5, 5);
            Population population = ga.initPopulation(bootstrap);
            ga.evalPopulationFitness(population, bootstrap);

            int generation = 0;

            while (!ga.isEnd(generation, 500) && !ga.isEnd(population)) {
                ga.adaptParameters(population);
                population = ga.crossover(population, bootstrap);
                population = ga.mutate(population, bootstrap);
                ga.evalPopulationFitness(population, bootstrap);
                generation++;
            }

            double fitness = population.getFittest(0).getFitness();

            if (i > 0) result.append(",\n");
            result.append("    {\n");
            result.append("      \"mutationRate\": ").append(mutRate).append(",\n");
            result.append("      \"generations\": ").append(generation).append(",\n");
            result.append("      \"fitness\": ").append(String.format("%.4f", fitness)).append("\n");
            result.append("    }");
        }

        result.append("\n  ]\n");
        result.append("}");

        return result.toString();
    }

    /**
     * Get Bootstrap data
     */
    private Bootstrap getBootstrap() {
        List<Room> rooms = roomMapper.selectAll();
        List<Task> tasks = taskMapper.selectAll();
        List<Timeslot> timeslots = courseTableMapper.selectAllTimeslot();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.setRooms(rooms);
        bootstrap.setTimeslots(timeslots);
        bootstrap.setTasks(tasks);

        return bootstrap;
    }
}
