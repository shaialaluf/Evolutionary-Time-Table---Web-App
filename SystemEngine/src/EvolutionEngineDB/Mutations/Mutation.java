package EvolutionEngineDB.Mutations;

import Algorithm.Solution;
import Algorithm.TimeTable;

public abstract class Mutation {
    private double probability;
/// maybe do evealuate abstract method - and the sons will take the config String and set it as they need. in is tribe.

    public Mutation() {
    }

    public Mutation(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
    @Override
    public String toString() {
        return "probability: "+ probability;

    }
    public void setProbability(double probability) {
        this.probability = probability;
    }
    public abstract void makeMutation(TimeTable solutionForMutation);
}
