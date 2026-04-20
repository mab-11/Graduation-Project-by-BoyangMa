package com.pxw.pojo.GA;

// Genetic algorithm operations required methods and variables
public class GA {
    // Population size
    private final int populationSize;
    // Mutation rate
    private final double mutationRate;
    // Crossover rate
    private final double crossoverRate;
    // Elite member count
    private final int elitismCount;
    // Tournament size
        protected int tournamentSize;

    public GA(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    // Initialize population
    public Population initPopulation(Bootstrap bootstrap) {
        return new Population(populationSize, bootstrap);
    }

    // Calculate individual fitness
    public double calcIndividualFitness(Individual indiv, Bootstrap bootstrap) {
        // Create new bootstrap object to use -- copy from existing bootstrap
        Bootstrap threadBootstrap = new Bootstrap(bootstrap);
        threadBootstrap.createTable(indiv);
        // Calculate fitness
        int clashes = threadBootstrap.calcClashes();
        double fitness = 1 / (double) (clashes + 1);
        indiv.setFitness(fitness);
        return fitness;
    }

    // Iterate through population to calculate population fitness, evaluate population
    public void evalPopulationFitness(Population pop, Bootstrap bootstrap) {
        double populationFitness = 0;

        // Loop to calculate individual fitness and accumulate
        for (Individual indiv : pop.getPopulation()) {
            populationFitness += this.calcIndividualFitness(indiv, bootstrap);
        }
        pop.setPopulationFitness(populationFitness);
    }

    // Termination condition: stop after reaching maximum generation
    public boolean isEnd(Population pop) {
        return pop.getFittest(0).getFitness() == 1.0;
    }

    public boolean isEnd(int count, int maxCount) {
        return (count > maxCount);
    }

    // Tournament selection
    public Individual getParent(Population pop) {
        // Create tournament
        Population tourPop = new Population(this.tournamentSize);

        // Add random individuals to tournament
        pop.shuffle();
        for (int i = 0; i < this.tournamentSize; i++) {
            Individual tourIndiv = pop.getIndividual(i);
            tourPop.setIndividual(i, tourIndiv);
        }

        // Return best individual
        return tourPop.getFittest(0);
    }

    // Uniform crossover
    public Population crossover(Population pop, Bootstrap bootstrap) {
        // Create new population
        Population newPop = new Population(pop.size());

        // Loop through current population based on fitness
        for (int index = 0; index < pop.size(); index++) {
            Individual firstParent = pop.getFittest(index);

            // Whether to apply crossover to individual?
            if (this.crossoverRate > Math.random() && index > this.elitismCount) {
                // Initialize offspring
                Individual child = new Individual(bootstrap);

                // Find second parent
                Individual SecondParent = getParent(pop);

                // Loop through genome for crossover
                for (int geneIndex = 0; geneIndex < firstParent.getChromsomeLength(); geneIndex++) {
                    // Uniform crossover
                    if (0.5 > Math.random()) {
                        child.setGene(geneIndex, firstParent.getGene(geneIndex));
                    } else {
                        child.setGene(geneIndex, SecondParent.getGene(geneIndex));
                    }
                }

                // Add offspring to new population
                newPop.setIndividual(index, child);

            } else {
                // If no crossover, add directly to new population
                newPop.setIndividual(index, firstParent);
            }

        }
        // Return new population
        return newPop;

    }

    // Uniform mutation
    public Population mutate(Population pop, Bootstrap bootstrap) {
        // Initialize new population
        Population newPop = new Population(this.populationSize);

        // Loop through current population based on fitness
        for (int index = 0; index < pop.size(); index++) {
            Individual indiv = pop.getFittest(index);
            // Elite individuals skip population mutation
            if (index > this.elitismCount) {
                // Create random individual for gene exchange
                Individual randomIndiv = new Individual(bootstrap);
                // Loop through current individual's genes
                for (int geneIndex = 0; geneIndex < indiv.getChromsomeLength(); geneIndex++) {
                    // Does single gene need mutation (exchange)?
                    if (this.mutationRate > Math.random()) {
                        // Exchange gene
                        indiv.setGene(geneIndex, randomIndiv.getGene(geneIndex));
                    }

                }
            }
            // Add new individual to population
            newPop.setIndividual(index, indiv);

        }
        // Return mutated population
        return newPop;
    }


}
