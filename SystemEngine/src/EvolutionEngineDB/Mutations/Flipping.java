package EvolutionEngineDB.Mutations;

import Algorithm.TimeTable;

import java.util.Random;

public class Flipping extends Mutation {
    private int maxTupples;
    private char component;

    public Flipping(int maxTupples, char component,double probability) {
        super(probability);
        this.maxTupples = maxTupples;
        this.component = component;
    }

    public Flipping(String configuration) {
        String[] elements = configuration.split(",");
        for (String currentElement : elements) {
            String[] currStr = currentElement.split("=");
            if (currStr[0].equals("MaxTupples")) {
                maxTupples=Integer.parseInt(currStr[1]);
            }
            else if(currStr[0].equals("Component")){
                component=currStr[1].charAt(0);
            }
        }
    }

    public int getMaxTupples() {
        return maxTupples;
    }

    @Override
    public String toString() {
        return "Mutation name: Flipping"+ ", configurations: Max Tupples= " + maxTupples + ", Component= " + component+", "+super.toString();
    }

    public char getComponent() {
        return component;
    }

    public void setMaxTupples(int maxTupples) {
        this.maxTupples = maxTupples;
    }

    public void setComponent(char component) {
        this.component = component;
    }

    @Override
    public void makeMutation(TimeTable solutionForMutation) {
        Random rnd=new Random();
        if(getProbability()*100>= rnd.nextInt(100)) {
            int numberOfFifthsToChange = rnd.nextInt(maxTupples) + 1;
            for (int i = 0; i < numberOfFifthsToChange; i++) {
                int currFifthNumber = rnd.nextInt(solutionForMutation.getTimeTableCells().size());
                TimeTable.TimeTableCell fifthToChange = solutionForMutation.getTimeTableCells().get(currFifthNumber);
                switch (component) {
                    case 'D':
                        int mutatedDay = rnd.nextInt(solutionForMutation.getDays()) + 1;
                        fifthToChange.setDay(mutatedDay);
                        break;
                    case 'H':
                        int mutatedHour = rnd.nextInt(solutionForMutation.getHours()) + 1;
                        fifthToChange.setHour(mutatedHour);
                        break;
                    case 'T':
                        int mutatedTeacher = rnd.nextInt(solutionForMutation.getSchoolDB().getTeacherList().size()) + 1;
                        fifthToChange.setTeacher(mutatedTeacher);
                        break;
                    case 'C':
                        int mutatedClassRoom = rnd.nextInt(solutionForMutation.getSchoolDB().getClassRoomsList().size()) + 1;
                        fifthToChange.setClassRoom(mutatedClassRoom);
                        break;
                    case 'S':
                        int mutatedSubject = rnd.nextInt(solutionForMutation.getSchoolDB().getSubjectsList().size()) + 1;
                        fifthToChange.setSubject(mutatedSubject);
                        break;
                }
            }
        }
    }
}
