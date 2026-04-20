package com.pxw.pojo.GA;

import java.util.*;

/**
 * Experiment Runner
 * Used for executing simulation experiments and comparison analysis
 */
public class ExperimentRunner {

    // Experiment configuration
    private int numExperiments = 10;  // Number of experiment repetitions
    private int maxGenerations = 1000; // Maximum generations

    // Test dataset configuration
    private static final int[][] TEST_SCENARIOS = {
        {30, 10, 20},   // Small: tasks, rooms, timeslots
        {80, 20, 25},   // Medium
        {150, 40, 25},  // Large
        {300, 60, 25}   // Very Large
    };

    public ExperimentRunner() {}

    public ExperimentRunner(int numExperiments, int maxGenerations) {
        this.numExperiments = numExperiments;
        this.maxGenerations = maxGenerations;
    }

    /**
     * Run single experiment
     */
    public ExperimentResult runSingleExperiment(Bootstrap bootstrap, AdaptiveGA ga) {
        long startTime = System.currentTimeMillis();

        // Initialize population
        Population population = ga.initPopulation(bootstrap);
        ga.evalPopulationFitness(population, bootstrap);

        int generation = 0;
        boolean found = false;

        // Evolution loop
        while (!ga.isEnd(generation, maxGenerations) && !ga.isEnd(population)) {
            // Adaptive parameter adjustment
            ga.adaptParameters(population);

            // Crossover
            population = ga.crossover(population, bootstrap);

            // Mutation
            population = ga.mutate(population, bootstrap);

            // Evaluate fitness
            ga.evalPopulationFitness(population, bootstrap);

            generation++;

            // Check if feasible solution found
            if (population.getFittest(0).getHardPenalty() == 0) {
                found = true;
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Get best solution
        Individual best = population.getFittest(0);
        Bootstrap resultBootstrap = new Bootstrap(bootstrap);
        resultBootstrap.createTable(best);

        // Build result
        ExperimentResult result = new ExperimentResult();
        result.setGeneration(generation);
        result.setFitness(best.getFitness());
        result.setHardPenalty(best.getHardPenalty());
        result.setSoftScore(best.getSoftScore());
        result.setResourceUtil(best.getResourceUtil());
        result.setExecutionTime(executionTime);
        result.setSuccess(found);
        result.setFoundGeneration(found ? generation : -1);

        return result;
    }

    /**
     * Run multiple experiments and calculate average
     */
    public ExperimentResult runMultipleExperiments(Bootstrap bootstrap, AdaptiveGA ga) {
        List<ExperimentResult> results = new ArrayList<>();

        for (int i = 0; i < numExperiments; i++) {
            System.out.println("Running experiment " + (i + 1) + "/" + numExperiments);
            ExperimentResult result = runSingleExperiment(bootstrap, ga);
            results.add(result);
        }

        // Calculate average
        return calculateAverageResult(results);
    }

    /**
     * Calculate average result
     */
    private ExperimentResult calculateAverageResult(List<ExperimentResult> results) {
        ExperimentResult avgResult = new ExperimentResult();

        double totalFitness = 0;
        double totalHardPenalty = 0;
        double totalSoftScore = 0;
        double totalResourceUtil = 0;
        double totalExecutionTime = 0;
        int successCount = 0;
        int totalGeneration = 0;

        for (ExperimentResult r : results) {
            totalFitness += r.getFitness();
            totalHardPenalty += r.getHardPenalty();
            totalSoftScore += r.getSoftScore();
            totalResourceUtil += r.getResourceUtil();
            totalExecutionTime += r.getExecutionTime();
            if (r.isSuccess()) {
                successCount++;
                totalGeneration += r.getFoundGeneration();
            }
        }

        avgResult.setFitness(totalFitness / results.size());
        avgResult.setHardPenalty(totalHardPenalty / results.size());
        avgResult.setSoftScore(totalSoftScore / results.size());
        avgResult.setResourceUtil(totalResourceUtil / results.size());
        avgResult.setExecutionTime((long) (totalExecutionTime / results.size()));
        avgResult.setSuccessRate((double) successCount / results.size() * 100);
        avgResult.setSuccess(successCount > 0);

        if (successCount > 0) {
            avgResult.setGeneration(totalGeneration / successCount);
        }

        return avgResult;
    }

    /**
     * Comparison experiment: Fixed vs Adaptive parameters
     */
    public void compareParameters(Bootstrap bootstrap) {
        System.out.println("========== Parameter Comparison Experiment ==========");

        // Fixed Parameter GA
        System.out.println("\n--- Fixed Parameter GA ---");
        GA fixedGA = new GA(100, 0.01, 0.9, 2, 5);
        // Run fixed parameter (simplified version)
        runBasicGA(bootstrap, fixedGA, "Fixed");

        // Adaptive GA
        System.out.println("\n--- Adaptive GA ---");
        AdaptiveGA adaptiveGA = new AdaptiveGA(100, 0.05, 0.85, 5, 5);
        ExperimentResult adaptiveResult = runMultipleExperiments(bootstrap, adaptiveGA);
        printResult(adaptiveResult);
    }

    /**
     * Run basic GA (using original implementation)
     */
    private void runBasicGA(Bootstrap bootstrap, GA ga, String label) {
        Population population = ga.initPopulation(bootstrap);
        ga.evalPopulationFitness(population, bootstrap);

        int generation = 0;
        while (!ga.isEnd(generation, maxGenerations) && !ga.isEnd(population)) {
            population = ga.crossover(population, bootstrap);
            population = ga.mutate(population, bootstrap);
            ga.evalPopulationFitness(population, bootstrap);
            generation++;
        }

        Individual best = population.getFittest(0);
        System.out.printf("%s - Gen: %d, Fitness: %.4f, Conflicts: %d%n",
                label, generation, best.getFitness(),
                (int)(1.0 / best.getFitness() - 1));
    }

    /**
     * Print experiment results
     */
    public void printResult(ExperimentResult result) {
        System.out.println("========== Experiment Results ==========");
        System.out.printf("Avg Fitness: %.4f%n", result.getFitness());
        System.out.printf("Avg Hard Penalty: %.4f%n", result.getHardPenalty());
        System.out.printf("Avg Soft Score: %.4f%n", result.getSoftScore());
        System.out.printf("Avg Resource Util: %.4f%n", result.getResourceUtil());
        System.out.printf("Avg Time: %d ms%n", result.getExecutionTime());
        System.out.printf("Success Rate: %.2f%%%n", result.getSuccessRate());
        System.out.printf("Avg Gen: %d%n", result.getGeneration());
    }

    /**
     * Run all comparison experiments
     */
    public void runAllExperiments(Bootstrap bootstrap) {
        System.out.println("========== Starting Experiments ==========");

        // Experiment 1: Scale testing
        System.out.println("\n========== Scale Testing ==========");
        for (int[] scenario : TEST_SCENARIOS) {
            System.out.printf("Scale: Tasks=%d, Rooms=%d, Timeslots=%d%n",
                    scenario[0], scenario[1], scenario[2]);
            // TODO: Generate test data based on scale
        }

        // Experiment 2: Parameter comparison
        compareParameters(bootstrap);

        System.out.println("\n========== Experiments Complete ==========");
    }

    /**
     * Generate JSON format result
     */
    public String toJsonResult(ExperimentResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(String.format("  \"fitness\": %.4f,\n", result.getFitness()));
        sb.append(String.format("  \"hardPenalty\": %.4f,\n", result.getHardPenalty()));
        sb.append(String.format("  \"softScore\": %.4f,\n", result.getSoftScore()));
        sb.append(String.format("  \"resourceUtilization\": %.4f,\n", result.getResourceUtil()));
        sb.append(String.format("  \"executionTime\": %d,\n", result.getExecutionTime()));
        sb.append(String.format("  \"successRate\": %.2f,\n", result.getSuccessRate()));
        sb.append(String.format("  \"convergenceGeneration\": %d\n", result.getGeneration()));
        sb.append("}");
        return sb.toString();
    }

    // Getter/Setter
    public int getNumExperiments() {
        return numExperiments;
    }

    public void setNumExperiments(int numExperiments) {
        this.numExperiments = numExperiments;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    /**
     * Experiment Result class
     */
    public static class ExperimentResult {
        private double fitness;
        private double hardPenalty;
        private double softScore;
        private double resourceUtil;
        private long executionTime;
        private boolean success;
        private double successRate;
        private int generation;
        private int foundGeneration;

        // Getters and Setters
        public double getFitness() { return fitness; }
        public void setFitness(double fitness) { this.fitness = fitness; }
        public double getHardPenalty() { return hardPenalty; }
        public void setHardPenalty(double hardPenalty) { this.hardPenalty = hardPenalty; }
        public double getSoftScore() { return softScore; }
        public void setSoftScore(double softScore) { this.softScore = softScore; }
        public double getResourceUtil() { return resourceUtil; }
        public void setResourceUtil(double resourceUtil) { this.resourceUtil = resourceUtil; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public int getGeneration() { return generation; }
        public void setGeneration(int generation) { this.generation = generation; }
        public int getFoundGeneration() { return foundGeneration; }
        public void setFoundGeneration(int foundGeneration) { this.foundGeneration = foundGeneration; }
    }
}
