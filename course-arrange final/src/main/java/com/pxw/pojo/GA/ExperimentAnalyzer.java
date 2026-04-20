package com.pxw.pojo.GA;

import java.util.*;

/**
 * Experiment Data Statistics and Comparison Analyzer
 * Used for paper experiment data collection and comparison analysis
 */
public class ExperimentAnalyzer {

    // Store experiment history data
    private List<ExperimentRecord> experimentHistory = new ArrayList<>();

    // Current experiment generation data (for convergence curve plotting)
    private List<GenerationData> currentGenerationData = new ArrayList<>();

    /**
     * Experiment Record
     */
    public static class ExperimentRecord {
        private String experimentName;
        private String algorithmType;
        private int populationSize;
        private double mutationRate;
        private double crossoverRate;
        private int maxGenerations;
        private int actualGenerations;
        private double finalFitness;
        private double hardPenalty;
        private double softScore;
        private double resourceUtil;
        private long executionTime;
        private boolean success;
        private Date timestamp;

        // Constructor
        public ExperimentRecord(String experimentName, String algorithmType) {
            this.experimentName = experimentName;
            this.algorithmType = algorithmType;
            this.timestamp = new Date();
        }

        // Getters and Setters
        public String getExperimentName() { return experimentName; }
        public void setExperimentName(String experimentName) { this.experimentName = experimentName; }
        public String getAlgorithmType() { return algorithmType; }
        public void setAlgorithmType(String algorithmType) { this.algorithmType = algorithmType; }
        public int getPopulationSize() { return populationSize; }
        public void setPopulationSize(int populationSize) { this.populationSize = populationSize; }
        public double getMutationRate() { return mutationRate; }
        public void setMutationRate(double mutationRate) { this.mutationRate = mutationRate; }
        public double getCrossoverRate() { return crossoverRate; }
        public void setCrossoverRate(double crossoverRate) { this.crossoverRate = crossoverRate; }
        public int getMaxGenerations() { return maxGenerations; }
        public void setMaxGenerations(int maxGenerations) { this.maxGenerations = maxGenerations; }
        public int getActualGenerations() { return actualGenerations; }
        public void setActualGenerations(int actualGenerations) { this.actualGenerations = actualGenerations; }
        public double getFinalFitness() { return finalFitness; }
        public void setFinalFitness(double finalFitness) { this.finalFitness = finalFitness; }
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
        public Date getTimestamp() { return timestamp; }
        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    }

    /**
     * Generation Data (for convergence curve)
     */
    public static class GenerationData {
        private int generation;
        private double bestFitness;
        private double avgFitness;
        private double worstFitness;
        private double hardPenalty;
        private double diversity;

        public GenerationData(int generation, double bestFitness, double avgFitness,
                            double worstFitness, double hardPenalty, double diversity) {
            this.generation = generation;
            this.bestFitness = bestFitness;
            this.avgFitness = avgFitness;
            this.worstFitness = worstFitness;
            this.hardPenalty = hardPenalty;
            this.diversity = diversity;
        }

        public int getGeneration() { return generation; }
        public double getBestFitness() { return bestFitness; }
        public double getAvgFitness() { return avgFitness; }
        public double getWorstFitness() { return worstFitness; }
        public double getHardPenalty() { return hardPenalty; }
        public double getDiversity() { return diversity; }
    }

    /**
     * Comparison Result
     */
    public static class ComparisonResult {
        private String experimentName;
        private Map<String, Double> metrics;
        private String winner;
        private double improvement;

        public ComparisonResult(String experimentName) {
            this.experimentName = experimentName;
            this.metrics = new HashMap<>();
        }

        public void addMetric(String key, double value) {
            metrics.put(key, value);
        }

        public String toJson() {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            sb.append("  \"experimentName\": \"").append(experimentName).append("\",\n");
            sb.append("  \"metrics\": {\n");
            int count = 0;
            for (Map.Entry<String, Double> entry : metrics.entrySet()) {
                sb.append("    \"").append(entry.getKey()).append("\": ").append(entry.getValue());
                if (++count < metrics.size()) sb.append(",");
                sb.append("\n");
            }
            sb.append("  },\n");
            sb.append("  \"winner\": \"").append(winner).append("\",\n");
            sb.append("  \"improvement\": ").append(improvement).append("\n");
            sb.append("}");
            return sb.toString();
        }

        public String getExperimentName() { return experimentName; }
        public Map<String, Double> getMetrics() { return metrics; }
        public void setWinner(String winner) { this.winner = winner; }
        public String getWinner() { return winner; }
        public void setImprovement(double improvement) { this.improvement = improvement; }
        public double getImprovement() { return improvement; }
    }

    /**
     * Run Complete Comparison Experiment
     */
    public ComparisonResult runComparisonExperiment(Bootstrap bootstrap, String experimentName) {
        ComparisonResult result = new ComparisonResult(experimentName);

        System.out.println("========== Starting Comparison Experiment: " + experimentName + " ==========");

        // Experiment 1: Fixed Parameter GA
        System.out.println("\n[1/2] Running Fixed Parameter GA...");
        ExperimentRecord fixedResult = runFixedParameterGA(bootstrap, 1000);

        // Experiment 2: Adaptive GA
        System.out.println("\n[2/2] Running Adaptive GA...");
        ExperimentRecord adaptiveResult = runAdaptiveGA(bootstrap, 1000);

        // Record results
        experimentHistory.add(fixedResult);
        experimentHistory.add(adaptiveResult);

        // Calculate comparison metrics
        result.addMetric("fixed.finalFitness", fixedResult.getFinalFitness());
        result.addMetric("fixed.generations", fixedResult.getActualGenerations());
        result.addMetric("fixed.executionTime", fixedResult.getExecutionTime());
        result.addMetric("fixed.success", fixedResult.isSuccess() ? 1.0 : 0.0);

        result.addMetric("adaptive.finalFitness", adaptiveResult.getFinalFitness());
        result.addMetric("adaptive.generations", adaptiveResult.getActualGenerations());
        result.addMetric("adaptive.executionTime", adaptiveResult.getExecutionTime());
        result.addMetric("adaptive.success", adaptiveResult.isSuccess() ? 1.0 : 0.0);

        // Calculate improvement
        double fitnessImprovement = 0;
        if (fixedResult.getFinalFitness() > 0) {
            fitnessImprovement = (adaptiveResult.getFinalFitness() - fixedResult.getFinalFitness())
                / fixedResult.getFinalFitness() * 100;
        }

        int genImprovement = fixedResult.getActualGenerations() - adaptiveResult.getActualGenerations();

        // Determine winner
        String winner = adaptiveResult.getFinalFitness() >= fixedResult.getFinalFitness()
            ? "Adaptive GA" : "Fixed GA";

        result.setWinner(winner);
        result.setImprovement(fitnessImprovement);

        // Print comparison results
        System.out.println("\n========== Comparison Results ==========");
        System.out.printf("Fixed GA     - Fitness: %.4f, Gen: %d, Time: %dms, Success: %s%n",
            fixedResult.getFinalFitness(), fixedResult.getActualGenerations(),
            fixedResult.getExecutionTime(), fixedResult.isSuccess());
        System.out.printf("Adaptive GA  - Fitness: %.4f, Gen: %d, Time: %dms, Success: %s%n",
            adaptiveResult.getFinalFitness(), adaptiveResult.getActualGenerations(),
            adaptiveResult.getExecutionTime(), adaptiveResult.isSuccess());
        System.out.printf("Improvement: %.2f%%, Gen Reduction: %d%n", fitnessImprovement, genImprovement);
        System.out.println("Winner: " + winner);

        return result;
    }

    /**
     * Run Fixed Parameter GA
     */
    private ExperimentRecord runFixedParameterGA(Bootstrap bootstrap, int maxGenerations) {
        ExperimentRecord record = new ExperimentRecord("Fixed GA", "Fixed");
        record.setPopulationSize(100);
        record.setMutationRate(0.01);
        record.setCrossoverRate(0.9);
        record.setMaxGenerations(maxGenerations);

        long startTime = System.currentTimeMillis();

        GA ga = new GA(100, 0.01, 0.9, 2, 5);
        Population population = ga.initPopulation(bootstrap);
        ga.evalPopulationFitness(population, bootstrap);

        int generation = 0;
        currentGenerationData.clear();

        while (!ga.isEnd(generation, maxGenerations) && !ga.isEnd(population)) {
            // Record generation data
            recordGenerationData(generation, population, ga);

            population = ga.crossover(population, bootstrap);
            population = ga.mutate(population, bootstrap);
            ga.evalPopulationFitness(population, bootstrap);
            generation++;
        }

        long endTime = System.currentTimeMillis();

        Individual best = population.getFittest(0);
        record.setActualGenerations(generation);
        record.setFinalFitness(best.getFitness());
        record.setExecutionTime(endTime - startTime);
        record.setSuccess(best.getFitness() == 1.0);

        return record;
    }

    /**
     * Run Adaptive GA
     */
    private ExperimentRecord runAdaptiveGA(Bootstrap bootstrap, int maxGenerations) {
        ExperimentRecord record = new ExperimentRecord("Adaptive GA", "Adaptive");
        record.setPopulationSize(100);
        record.setMutationRate(0.05);
        record.setCrossoverRate(0.85);
        record.setMaxGenerations(maxGenerations);

        long startTime = System.currentTimeMillis();

        AdaptiveGA ga = new AdaptiveGA(100, 0.05, 0.85, 5, 5);
        Population population = ga.initPopulation(bootstrap);
        ga.evalPopulationFitness(population, bootstrap);

        int generation = 0;
        currentGenerationData.clear();

        while (!ga.isEnd(generation, maxGenerations) && !ga.isEnd(population)) {
            // Record generation data
            recordGenerationData(generation, population, ga);

            // Adaptive parameter adjustment
            ga.adaptParameters(population);

            population = ga.crossover(population, bootstrap);
            population = ga.mutate(population, bootstrap);
            ga.evalPopulationFitness(population, bootstrap);
            generation++;
        }

        long endTime = System.currentTimeMillis();

        Individual best = population.getFittest(0);
        record.setActualGenerations(generation);
        record.setFinalFitness(best.getFitness());
        record.setHardPenalty(best.getHardPenalty());
        record.setSoftScore(best.getSoftScore());
        record.setResourceUtil(best.getResourceUtil());
        record.setExecutionTime(endTime - startTime);
        record.setSuccess(best.getHardPenalty() == 0);

        return record;
    }

    /**
     * Record generation data
     */
    private void recordGenerationData(int generation, Population population, Object ga) {
        double bestFitness = population.getFittest(0).getFitness();

        // Calculate average fitness
        double totalFitness = 0;
        double worstFitness = Double.MAX_VALUE;
        for (Individual indiv : population.getPopulation()) {
            totalFitness += indiv.getFitness();
            if (indiv.getFitness() < worstFitness) {
                worstFitness = indiv.getFitness();
            }
        }
        double avgFitness = totalFitness / population.size();

        // Calculate diversity
        double diversity = 0;
        if (ga instanceof AdaptiveGA) {
            diversity = ((AdaptiveGA) ga).calcPopulationDiversity(population);
        }

        double hardPenalty = population.getFittest(0).getHardPenalty();

        currentGenerationData.add(new GenerationData(
            generation, bestFitness, avgFitness, worstFitness, hardPenalty, diversity
        ));
    }

    /**
     * Get convergence curve data (JSON format, for frontend plotting)
     */
    public String getConvergenceDataJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"generations\": [");

        for (int i = 0; i < currentGenerationData.size(); i++) {
            GenerationData gd = currentGenerationData.get(i);
            if (i > 0) sb.append(", ");
            sb.append(gd.getGeneration());
        }
        sb.append("],\n");

        sb.append("  \"bestFitness\": [");
        for (int i = 0; i < currentGenerationData.size(); i++) {
            GenerationData gd = currentGenerationData.get(i);
            if (i > 0) sb.append(", ");
            sb.append(String.format("%.4f", gd.getBestFitness()));
        }
        sb.append("],\n");

        sb.append("  \"avgFitness\": [");
        for (int i = 0; i < currentGenerationData.size(); i++) {
            GenerationData gd = currentGenerationData.get(i);
            if (i > 0) sb.append(", ");
            sb.append(String.format("%.4f", gd.getAvgFitness()));
        }
        sb.append("],\n");

        sb.append("  \"hardPenalty\": [");
        for (int i = 0; i < currentGenerationData.size(); i++) {
            GenerationData gd = currentGenerationData.get(i);
            if (i > 0) sb.append(", ");
            sb.append(String.format("%.2f", gd.getHardPenalty()));
        }
        sb.append("],\n");

        sb.append("  \"diversity\": [");
        for (int i = 0; i < currentGenerationData.size(); i++) {
            GenerationData gd = currentGenerationData.get(i);
            if (i > 0) sb.append(", ");
            sb.append(String.format("%.6f", gd.getDiversity()));
        }
        sb.append("]\n");

        sb.append("}");
        return sb.toString();
    }

    /**
     * Get experiment history (JSON format)
     */
    public String getHistoryJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"experiments\": [\n");

        for (int i = 0; i < experimentHistory.size(); i++) {
            ExperimentRecord r = experimentHistory.get(i);
            if (i > 0) sb.append(",\n");
            sb.append("    {\n");
            sb.append("      \"name\": \"").append(r.getExperimentName()).append("\",\n");
            sb.append("      \"algorithm\": \"").append(r.getAlgorithmType()).append("\",\n");
            sb.append("      \"generations\": ").append(r.getActualGenerations()).append(",\n");
            sb.append("      \"fitness\": ").append(String.format("%.4f", r.getFinalFitness())).append(",\n");
            sb.append("      \"executionTime\": ").append(r.getExecutionTime()).append(",\n");
            sb.append("      \"success\": ").append(r.isSuccess()).append("\n");
            sb.append("    }");
        }

        sb.append("\n  ]\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Clear history data
     */
    public void clearHistory() {
        experimentHistory.clear();
        currentGenerationData.clear();
    }

    /**
     * Get current convergence curve data
     */
    public List<GenerationData> getCurrentGenerationData() {
        return currentGenerationData;
    }

    /**
     * Get experiment history
     */
    public List<ExperimentRecord> getExperimentHistory() {
        return experimentHistory;
    }
}
