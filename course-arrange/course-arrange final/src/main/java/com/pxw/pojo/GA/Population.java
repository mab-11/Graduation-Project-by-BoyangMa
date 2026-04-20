package com.pxw.pojo.GA;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


// Store array composed of individuals - population's overall fitness
public class Population {
    private Individual population[];
    private double populationFitness = -1;

    public Population(int populationSize) {
        this.population = new Individual[populationSize];
    }


    // Population initialization
    public Population(int populationSize, Bootstrap bootstrap) {
        // Initialize population
        this.population = new Individual[populationSize];

        // Loop through population size
        for (int individualCount = 0;individualCount<populationSize;individualCount++){
            // Create chromosome
            Individual individual = new Individual(bootstrap);
            // Add to population
            population[individualCount] = individual;

        }
    }

    // Get population
    public Individual[] getPopulation() {
        return this.population;
    }

    // Fitness sorting
    public Individual getFittest(int offset) {
        Arrays.sort(this.population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if (o1.getFitness() > o2.getFitness()) {
                    return -1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                }

                return 0;
            }
        });
        return this.population[offset];
    }


    public void setPopulationFitness(double fitness) {
        this.populationFitness = fitness;
    }

    public double getPopulationFitness() {
        return this.populationFitness;
    }

    public int size() {
        return this.population.length;
    }

    public Individual setIndividual(int offset, Individual individual) {
        return population[offset] = individual;
    }

    public Individual getIndividual(int offset) {
        return population[offset];
    }


    // Shuffle
    public void shuffle() {
        Random r = new Random();
        for (int i = population.length -1; i > 0; i--) {
            int index = r.nextInt(i+1);
            Individual individual = population[index];
            population[index] = population[i];
            population[i] = individual;
        }
    }

}
