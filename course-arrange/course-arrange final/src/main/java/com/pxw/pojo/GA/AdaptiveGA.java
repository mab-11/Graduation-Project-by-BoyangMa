package com.pxw.pojo.GA;

import java.util.Random;

/**
 * Adaptive genetic algorithm
 * Contains multiple selection, crossover, mutation operators
 * Supports adaptive parameter adjustment to avoid premature convergence
 */
public class AdaptiveGA {
    // Basic parameters
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    private int tournamentSize;

    // Adaptive parameters
    private double minMutationRate = 0.001;
    private double maxMutationRate = 0.3;
    private double initialMutationRate = 0.05;
    private double initialCrossoverRate = 0.85;

    // Premature detection threshold
    private static final double PREMATURE_THRESHOLD = 0.001;

    private Random random;

    public AdaptiveGA(int populationSize, double mutationRate, double crossoverRate,
                      int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
        this.random = new Random();
    }

    /**
     * Initialize population
     */
    public Population initPopulation(Bootstrap bootstrap) {
        return new Population(populationSize, bootstrap);
    }

    /**
     * Calculate individual fitness (using FitnessCalculator)
     */
    public double calcIndividualFitness(Individual indiv, Bootstrap bootstrap) {
        FitnessCalculator calculator = new FitnessCalculator(bootstrap);
        return calculator.calcFitness(indiv);
    }

    /**
     * Iterate through population to calculate population fitness
     */
    public void evalPopulationFitness(Population pop, Bootstrap bootstrap) {
        double populationFitness = 0;
        FitnessCalculator calculator = new FitnessCalculator(bootstrap);

        for (Individual indiv : pop.getPopulation()) {
            double fitness = calculator.calcFitness(indiv);
            populationFitness += fitness;
        }
        pop.setPopulationFitness(populationFitness);
    }

    /**
     * Judge termination condition
     */
    public boolean isEnd(Population pop) {
        return pop.getFittest(0).getFitness() >= 100; // Hard constraints fully satisfied
    }

    public boolean isEnd(int count, int maxCount) {
        return count >= maxCount;
    }

    /**
     * Tournament selection
     */
    public Individual tournamentSelection(Population pop) {
        Population tourPop = new Population(tournamentSize);
        pop.shuffle();

        for (int i = 0; i < this.tournamentSize; i++) {
            Individual tourIndiv = pop.getIndividual(i);
            tourPop.setIndividual(i, tourIndiv);
        }
        return tourPop.getFittest(0);
    }

    /**
     * Roulette wheel selection
     */
    public Individual rouletteSelection(Population pop) {
        double totalFitness = pop.getPopulationFitness();
        if (totalFitness == 0) {
            return pop.getFittest(0);
        }

        double randomValue = random.nextDouble() * totalFitness;
        double cumulative = 0;

        for (Individual indiv : pop.getPopulation()) {
            cumulative += indiv.getFitness();
            if (cumulative >= randomValue) {
                return indiv;
            }
        }

        return pop.getFittest(0);
    }

    /**
     * Rank selection
     */
    public Individual rankSelection(Population pop) {
        int size = pop.size();
        // Index after sorting
        int randomRank = random.nextInt(size);
        return pop.getFittest(randomRank);
    }

    /**
     * Uniform crossover
     */
    public Individual uniformCrossover(Individual parent1, Individual parent2, Bootstrap bootstrap) {
        Individual child = new Individual(bootstrap);

        for (int i = 0; i < parent1.getChromsomeLength(); i++) {
            if (random.nextDouble() < 0.5) {
                child.setGene(i, parent1.getGene(i));
            } else {
                child.setGene(i, parent2.getGene(i));
            }
        }
        return child;
    }

    /**
     * Single-point crossover
     */
    public Individual singlePointCrossover(Individual parent1, Individual parent2, Bootstrap bootstrap) {
        int length = parent1.getChromsomeLength();
        int crossoverPoint = random.nextInt(length);

        Individual child = new Individual(bootstrap);

        for (int i = 0; i < length; i++) {
            if (i < crossoverPoint) {
                child.setGene(i, parent1.getGene(i));
            } else {
                child.setGene(i, parent2.getGene(i));
            }
        }
        return child;
    }

    /**
     * Two-point crossover
     */
    public Individual twoPointCrossover(Individual parent1, Individual parent2, Bootstrap bootstrap) {
        int length = parent1.getChromsomeLength();
        int point1 = random.nextInt(length);
        int point2 = random.nextInt(length - point1) + point1;

        Individual child = new Individual(bootstrap);

        for (int i = 0; i < length; i++) {
            if (i >= point1 && i < point2) {
                child.setGene(i, parent1.getGene(i));
            } else {
                child.setGene(i, parent2.getGene(i));
            }
        }
        return child;
    }

    /**
     * Uniform mutation
     */
    public void uniformMutation(Individual indiv, Bootstrap bootstrap) {
        for (int i = 0; i < indiv.getChromsomeLength(); i++) {
            if (random.nextDouble() < mutationRate) {
                // Even positions are rooms, odd positions are timeslots
                if (i % 2 == 0) {
                    // Timeslot mutation
                    indiv.setGene(i, bootstrap.getRandomTimeslot().getId());
                } else {
                    // Room mutation
                    indiv.setGene(i, bootstrap.getRandomRoom().getId());
                }
            }
        }
    }

    /**
     * Swap mutation
     */
    public void swapMutation(Individual indiv) {
        int length = indiv.getChromsomeLength();
        if (length < 2) return;

        int pos1 = random.nextInt(length);
        int pos2 = random.nextInt(length);

        int gene1 = indiv.getGene(pos1);
        int gene2 = indiv.getGene(pos2);

        indiv.setGene(pos1, gene2);
        indiv.setGene(pos2, gene1);
    }

    /**
     * Inversion mutation
     */
    public void inversionMutation(Individual indiv) {
        int length = indiv.getChromsomeLength();
        if (length < 2) return;

        int start = random.nextInt(length);
        int end = random.nextInt(length - start) + start;

        // Flip genes between start and end
        int left = start;
        int right = end;
        while (left < right) {
            int gene1 = indiv.getGene(left);
            int gene2 = indiv.getGene(right);
            indiv.setGene(left, gene2);
            indiv.setGene(right, gene1);
            left++;
            right--;
        }
    }

    /**
     * Adaptive crossover
     */
    public Population crossover(Population pop, Bootstrap bootstrap) {
        Population newPop = new Population(pop.size());

        // Retain elite individuals
        for (int i = 0; i < elitismCount; i++) {
            newPop.setIndividual(i, pop.getFittest(i));
        }

        // Crossover to generate new individuals
        for (int i = elitismCount; i < pop.size(); i++) {
            if (crossoverRate > random.nextDouble()) {
                Individual parent1 = tournamentSelection(pop);
                Individual parent2 = tournamentSelection(pop);

                // Randomly select crossover method
                int crossoverType = random.nextInt(3);
                switch (crossoverType) {
                    case 0:
                        newPop.setIndividual(i, uniformCrossover(parent1, parent2, bootstrap));
                        break;
                    case 1:
                        newPop.setIndividual(i, singlePointCrossover(parent1, parent2, bootstrap));
                        break;
                    default:
                        newPop.setIndividual(i, twoPointCrossover(parent1, parent2, bootstrap));
                }
            } else {
                newPop.setIndividual(i, tournamentSelection(pop));
            }
        }

        return newPop;
    }

    /**
     * Adaptive mutation
     */
    public Population mutate(Population pop, Bootstrap bootstrap) {
        Population newPop = new Population(populationSize);

        for (int i = 0; i < pop.size(); i++) {
            Individual indiv = pop.getFittest(i).getChromsome() != null
                ? pop.getFittest(i)
                : pop.getIndividual(i);

            // Elite individuals reduce mutation
            if (i > elitismCount) {
                // Randomly select mutation method
                int mutationType = random.nextInt(3);
                switch (mutationType) {
                    case 0:
                        uniformMutation(indiv, bootstrap);
                        break;
                    case 1:
                        swapMutation(indiv);
                        break;
                    default:
                        if (random.nextDouble() < mutationRate) {
                            inversionMutation(indiv);
                        }
                }
            }

            newPop.setIndividual(i, indiv);
        }

        return newPop;
    }

    /**
     * Calculate population diversity (fitness variance)
     */
    public double calcPopulationDiversity(Population pop) {
        double[] fitnessValues = new double[pop.size()];
        double sum = 0;

        for (int i = 0; i < pop.size(); i++) {
            fitnessValues[i] = pop.getIndividual(i).getFitness();
            sum += fitnessValues[i];
        }

        double mean = sum / pop.size();
        double variance = 0;

        for (double fitness : fitnessValues) {
            variance += Math.pow(fitness - mean, 2);
        }

        return variance / pop.size();
    }

    /**
     * Adaptive parameter adjustment
     */
    public void adaptParameters(Population pop) {
        double diversity = calcPopulationDiversity(pop);

        if (diversity < PREMATURE_THRESHOLD) {
            // Premature: increase mutation rate, decrease crossover rate
            mutationRate = Math.min(maxMutationRate, mutationRate * 1.5);
            crossoverRate = Math.max(0.5, crossoverRate * 0.9);
        } else {
            // Normal: decrease mutation rate
            mutationRate = Math.max(minMutationRate, mutationRate * 0.95);
        }
    }

    /**
     * Get current mutation rate
     */
    public double getMutationRate() {
        return mutationRate;
    }

    /**
     * Get current crossover rate
     */
    public double getCrossoverRate() {
        return crossoverRate;
    }

    /**
     * Print current parameters
     */
    public void printParameters() {
        System.out.printf("Current parameters - Mutation rate: %.4f, Crossover rate: %.4f, Population size: %d, Elite count: %d%n",
                mutationRate, crossoverRate, populationSize, elitismCount);
    }
}
