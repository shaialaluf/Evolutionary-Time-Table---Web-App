package EvolutionEngineDB.Selections;

import Algorithm.Solution;
import Algorithm.SolutionsPopulation;
import Algorithm.TimeTable;

import java.util.*;

public class RouletteWheel extends Selection {
    public RouletteWheel(int elitism) {
        super(elitism);
    }

    @Override
    public SolutionsPopulation makeSelection(SolutionsPopulation solutionsPopulation) {
        SolutionsPopulation selectedParents = new SolutionsPopulation();
        Random rand = new Random();
        List<TimeTable> parents = new ArrayList<>();
        float currentSum = 0;
        int index = -1;
        float sumOfFitness = 0;
        for (TimeTable solution : (List<TimeTable>) solutionsPopulation.getSolutions()) {
            sumOfFitness += solution.getFitness();
        }

        sortSolutionsByFitness(solutionsPopulation);

//        ArrayList<Float> list=new ArrayList();
        //  float random=rand.nextInt((int) sumOfFitness)+((rand.nextInt((int)(sumOfFitness%((int) sumOfFitness)*1000)))/1000);
        while (parents.size() < solutionsPopulation.getSolutions().size()) {
            float random = rand.nextFloat();

            while (index<solutionsPopulation.getSolutions().size()-1 && random > currentSum) {
                index++;
                TimeTable t = (TimeTable) (solutionsPopulation.getSolutions().get(index));
                currentSum += t.getFitness() / sumOfFitness;
            }

            currentSum=0;
            if (random == 0f) {
                index = 0;
            }
            parents.add((TimeTable) solutionsPopulation.getSolutions().get(index));
        }

        selectedParents.setSolutions(new ArrayList<>(parents));
        return selectedParents;
    }

    @Override
    public String toString() {
        String str=System.lineSeparator()+"Selection name: Roulette Wheel";
        str+=System.lineSeparator()+"Elitism Size: "+getElitism();

        return str;
    }
}
