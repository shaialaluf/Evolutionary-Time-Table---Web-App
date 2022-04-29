package EvolutionEngineDB.Mutations;

import Algorithm.TimeTable;

import java.util.Random;

public class Sizer extends Mutation{
    private int totalTupples;
    private Random rndTupples;


    public Sizer(String configuration) {
        rndTupples=new Random();
        String[] currStr = configuration.split("=");
        totalTupples=Integer.parseInt(currStr[1]);
    }
    public Sizer(int totalTupples,double probability) {
        super(probability);
        rndTupples=new Random();
        this.totalTupples=totalTupples;
    }


    @Override
    public void makeMutation(TimeTable solutionForMutation) {
        if(getProbability()*100>= rndTupples.nextInt(100)) {
            randomTupplesChange(solutionForMutation);
        }
    }

    public void randomTupplesChange(TimeTable solutionForMutation){
        int numberOfTupplesToChange;
        int minForRandom;
        if(totalTupples>0){
             minForRandom=Math.min(solutionForMutation.getDays()*solutionForMutation.getHours(),totalTupples);
             numberOfTupplesToChange=rndTupples.nextInt(minForRandom)+1;
             for(int i=0; i<numberOfTupplesToChange; i++){
                 solutionForMutation.addFifthToList();
             }
        }
        else{
            if(solutionForMutation.getTimeTableCells().size()>solutionForMutation.getDays()){
                int totalTupplesAbs=Math.abs(totalTupples);
                minForRandom=Math.min(solutionForMutation.getTimeTableCells().size()-solutionForMutation.getDays(),totalTupplesAbs);
                numberOfTupplesToChange=rndTupples.nextInt(minForRandom)+1;
                for(int i=0; i<numberOfTupplesToChange; i++){
                    solutionForMutation.removeFifthFromBeginning();
                }

            }

        }
    }

    @Override
    public String toString() {
        return "Mutation name: Sizer"+", configurations: Total Tupples= " + totalTupples + ", "+super.toString();
    }

    public void setTotalTupples(int totalTupples) {
        this.totalTupples = totalTupples;
    }

    // D=6
    // 90 - Fifths
    // 40 - totaltupples

}
