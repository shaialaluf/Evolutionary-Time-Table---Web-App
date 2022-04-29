package EvolutionEngineDB.Selections;

import Algorithm.Solution;
import Algorithm.SolutionsPopulation;
import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Truncation extends Selection {
    private int topPercent;

    public Truncation(String topPercent, int elitism) {
        super(elitism);
        int indexOfValue = topPercent.lastIndexOf("=") + 1;
        this.topPercent = Integer.parseInt(topPercent.substring(indexOfValue));
    }

    public Truncation(int topPercent, int elitism){
        super(elitism);
        this.topPercent=topPercent;
    }

    @Override
    public String toString() {
        String str=System.lineSeparator()+"Selection name: Truncation"+", configuration: "+"topPercent= " + topPercent ;
        str+=System.lineSeparator()+"Elitism Size: "+getElitism();

        return str;
    }

    @Override
    public SolutionsPopulation makeSelection(SolutionsPopulation solutionsPopulation) {
        SolutionsPopulation selectedParents=new SolutionsPopulation();
        sortSolutionsByFitness(solutionsPopulation);
        int numberOfParent= (int) Math.floor(topPercent*solutionsPopulation.getSolutions().size()/100);
        List<Solution> parents= new ArrayList<>(solutionsPopulation.getSolutions().subList(solutionsPopulation.getSolutions().size()-numberOfParent,solutionsPopulation.getSolutions().size()));
        selectedParents.setSolutions(parents);
        return  selectedParents;
    }


    public int getTopPercent() {
        return topPercent;
    }

    public void setTopPercent(int topPercent) {
        this.topPercent = topPercent;
    }

}
