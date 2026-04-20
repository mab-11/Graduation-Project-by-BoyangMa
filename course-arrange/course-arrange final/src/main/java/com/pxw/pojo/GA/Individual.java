package com.pxw.pojo.GA;


import com.pxw.pojo.Task;

// Individual class - candidate solution
// Responsible for storing and operating on one chromosome
// Two constructors: 1. Accept length, randomly initialize a chromosome 2. Accept array as chromosome
public class Individual {
    // Chromosome
    private int[] chromsome;
    // Fitness tracking
    private double fitness = -1;
    // Hard constraint penalty value
    private double hardPenalty = -1;
    // Soft constraint score
    private double softScore = -1;
    // Resource utilization
    private double resourceUtil = -1;


    public Individual(int[] chromsome) {
        // Create individual chromosome
        this.chromsome = chromsome;
    }

    // Initialize: use timetable object to determine the number of classes that must be scheduled (chromosome length)
    // Chromosome itself is randomly generated from timetable with room, timeslot, and professor
    public Individual(Bootstrap bootstrap){
        // Number of classes
        int numTables = bootstrap.getNumTables();

        // 1 gene for room, 1 gene for timeslot
        int chromosomeLength = numTables*2;
        // Create random chromosome
        int[] newChromosome = new int[chromosomeLength];
        int chromosomeIndex = 0;
        // Teaching task loop
        for (Task task : bootstrap.getTasksAsArray()){
                // Add teaching task

                // Add random time
                int timeslotId = bootstrap.getRandomTimeslot().getId();
                newChromosome[chromosomeIndex] = timeslotId;
                chromosomeIndex++;

                // Add random room
                int roomId = bootstrap.getRandomRoom().getId();
                newChromosome[chromosomeIndex] = roomId;
                chromosomeIndex++;

        }
        this.chromsome = newChromosome;

    }


    public int[] getChromsome() {
        return chromsome;
    }

    public int getChromsomeLength() {
        return this.chromsome.length;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getHardPenalty() {
        return hardPenalty;
    }

    public void setHardPenalty(double hardPenalty) {
        this.hardPenalty = hardPenalty;
    }

    public double getSoftScore() {
        return softScore;
    }

    public void setSoftScore(double softScore) {
        this.softScore = softScore;
    }

    public double getResourceUtil() {
        return resourceUtil;
    }

    public void setResourceUtil(double resourceUtil) {
        this.resourceUtil = resourceUtil;
    }


    // Gene
    public int getGene(int offset) {
        return this.chromsome[offset];
    }

    public void setGene(int offset, int gene) {
        this.chromsome[offset] = gene;
    }

    // Output gene string
    @Override
    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromsome.length; gene++) {
            output += this.chromsome[gene];
        }

        return output;
    }
}
