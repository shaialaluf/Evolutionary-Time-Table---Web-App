package EvolutionEngineDB.Selections;

import Algorithm.Solution;
import Algorithm.SolutionsPopulation;
import Algorithm.TimeTable;

import java.util.*;
import java.util.stream.Collector;



public abstract class Selection {

    private int elitism;
    private transient List<TimeTable> elitaSolutions;


    public Selection(int elitism) {
        this.elitism = elitism;
        elitaSolutions=new ArrayList<>();
    }

    public void sortSolutionsByFitness(SolutionsPopulation solutionsPopulation){
        Collections.sort(solutionsPopulation.getSolutions(), (Comparator<TimeTable>) (s1, s2) -> {
            if (s1.getFitness() < s2.getFitness()) {
                return -1;
            } else if (s1.getFitness() > s2.getFitness()) {
                return 1;
            } else {
                return 0;
            }
        });

    }


    public void createElitism(SolutionsPopulation solutionsPopulation){
        sortSolutionsByFitness(solutionsPopulation);
        int populationSize=solutionsPopulation.getSolutions().size();
        elitaSolutions=new ArrayList<>(solutionsPopulation.getSolutions().subList(populationSize-elitism, populationSize));
        for(TimeTable solution: elitaSolutions){
            solution.setElitist(true);
        }
    }

    public List<TimeTable> getElitaSolutions() {
        return elitaSolutions;
    }

    public int getElitism() {
        return elitism;
    }


    public abstract SolutionsPopulation makeSelection(SolutionsPopulation solutionsPopulation);

    public void setElitism(int elitism) {
        this.elitism = elitism;
    }
}
