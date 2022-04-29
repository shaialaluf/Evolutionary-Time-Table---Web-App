package EvolutionEngineDB.Crossovers;

import Algorithm.TimeTable;

import java.util.List;

public abstract class Crossover {
    private int cuttingPointsNumber;

    @Override
    public String toString() {
        return "cuttingPointsNumber= " + cuttingPointsNumber;
    }

    public int getCuttingPointsNumber() {
        return cuttingPointsNumber;
    }

    public void setCuttingPointsNumber(int cuttingPointsNumber) {
        this.cuttingPointsNumber = cuttingPointsNumber;
    }

    public abstract List<TimeTable> makeCrossover(TimeTable parent1, TimeTable parent2, int currentGeneration);
}
