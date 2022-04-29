package EvolutionEngineDB.Selections;

import Algorithm.SolutionsPopulation;
import Algorithm.TimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tournament extends Selection{
    private float pte;


    public Tournament(String pte,int elitism) {
        super(elitism);
        int indexOfValue = pte.lastIndexOf("=") + 1;
        this.pte = Float.parseFloat(pte.substring(indexOfValue));
    }

    public Tournament(float pte, int elitism) {
        super(elitism);
        this.pte=pte;
    }

    public void setPte(float pte) {
        this.pte = pte;
    }


    @Override
    public SolutionsPopulation makeSelection(SolutionsPopulation solutionsPopulation) {
        int parent1Index;
        int parent2Index;
        float randomNumber;
        Random rnd=new Random();
        SolutionsPopulation selectedParents=new SolutionsPopulation();
        List<TimeTable> parents = new ArrayList<>();
        int solutionsPopulationSize=solutionsPopulation.getSolutions().size();

        for(int i=0; i<solutionsPopulationSize; i++){
            parent1Index=rnd.nextInt(solutionsPopulation.getSolutions().size());
            parent2Index=rnd.nextInt(solutionsPopulation.getSolutions().size());
            randomNumber=rnd.nextFloat();
            TimeTable parent1=(TimeTable) solutionsPopulation.getSolutions().get(parent1Index);
            TimeTable parent2=(TimeTable) solutionsPopulation.getSolutions().get(parent2Index);

            TimeTable parentWithSmallFitness;
            TimeTable parentWithBigFitness;

            if(parent1.getFitness()>=parent2.getFitness()){
                parentWithBigFitness=parent1;
                parentWithSmallFitness=parent2;
            }
            else{
                parentWithBigFitness=parent2;
                parentWithSmallFitness=parent1;
            }

            if(randomNumber>=pte){
               parents.add(parentWithBigFitness);
            }
            else{
                parents.add(parentWithSmallFitness);
            }
        }

        selectedParents.setSolutions(new ArrayList<>(parents));
        return selectedParents;
    }

    @Override
    public String toString() {
        String str=System.lineSeparator()+"Selection name: Tournament"+", configuration: "+"PTE= " + pte ;
        str+=System.lineSeparator()+"Elitism Size: "+getElitism();

        return str;
    }
}
