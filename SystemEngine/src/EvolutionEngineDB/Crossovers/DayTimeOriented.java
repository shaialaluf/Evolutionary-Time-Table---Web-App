package EvolutionEngineDB.Crossovers;

import Algorithm.TimeTable;
import Algorithm.TimeTable.TimeTableCell;

import java.util.*;

public class DayTimeOriented extends Crossover {

    @Override
    public String toString() {
        return System.lineSeparator() + "Crossover name: DayTimeOriented" + ", " + super.toString();
    }
    public DayTimeOriented(){};

    public DayTimeOriented(int cuttingPoints){
        setCuttingPointsNumber(cuttingPoints);
    }


    @Override
    public List<TimeTable> makeCrossover(TimeTable parent1, TimeTable parent2, int currentGeneration) {//// no need to create new fifths for children!!!
        List<TimeTableCell> parent1List = parent1.createSortedListByDaysHours();
        List<TimeTableCell> parent2List = parent2.createSortedListByDaysHours();
        TimeTable offSpring1 = new TimeTable(parent1.getSchoolDB());
        TimeTable offSpring2 = new TimeTable(parent2.getSchoolDB());
        int subjectsAmount = parent1.getSchoolDB().getSubjectsList().size();
        int classesAmount = parent1.getSchoolDB().getClassRoomsList().size();
        int teachersAmount = parent1.getSchoolDB().getTeacherList().size();
        int hoursAmount = parent1.getHours();
        int daysAmount = parent1.getDays();
        createOffspringsByCuttingPoints(parent1, parent1List, parent2List, offSpring1, offSpring2, subjectsAmount, classesAmount, teachersAmount, hoursAmount, daysAmount);
        while (offSpring1.getTimeTableCells().size() == 0 || offSpring2.getTimeTableCells().size() == 0) {
            createOffspringsByCuttingPoints(parent1, parent1List, parent2List, offSpring1, offSpring2, subjectsAmount, classesAmount, teachersAmount, hoursAmount, daysAmount);
        }
        offSpring1.createTimeTableMatrixFromList();/// maybe no need
        offSpring2.createTimeTableMatrixFromList();/// maybe no need
        List<TimeTable> twoOffsprings = new ArrayList<>();
        twoOffsprings.add(offSpring1);
        twoOffsprings.add(offSpring2);
        return twoOffsprings; // return the two offsprings in list

    }

    private void createOffspringsByCuttingPoints(TimeTable parent1, List<TimeTableCell> parent1List, List<TimeTableCell> parent2List, TimeTable offSpring1, TimeTable offSpring2, int subjectsAmount, int classesAmount, int teachersAmount, int hoursAmount, int daysAmount) {
        int[] cuttingPointsLocations = new int[this.getCuttingPointsNumber() + 1];// extra place for cutting point that is the end of the list (d*h*t*c*s)
        Random rndPoints = new Random();
        int maxNumberOfFifths = parent1.getMaxNumberOfFifths();
        for (int i = 0; i < cuttingPointsLocations.length - 1; i++) {
            cuttingPointsLocations[i] = rndPoints.nextInt(maxNumberOfFifths) + 1;
        }
        cuttingPointsLocations[cuttingPointsLocations.length - 1] = subjectsAmount * classesAmount * teachersAmount * hoursAmount * daysAmount;// extra place for cutting point that is the end of the list (d*h*t*c*s)

        Arrays.sort(cuttingPointsLocations);

        int j1 = 0;
        int j2 = 0;
        for (int i = 0; i < cuttingPointsLocations.length; i++) {
            while (j1 < parent1List.size() && parent1List.get(j1).convertCellToInt(subjectsAmount, classesAmount, teachersAmount, hoursAmount, daysAmount) + 1 <= cuttingPointsLocations[i]) {
                if (i % 2 == 0) {
                    offSpring1.addFifthToList(parent1List.get(j1));
                    j1++;
                } else {
                    offSpring2.addFifthToList(parent1List.get(j1));
                    j1++;
                }
            }


            while (j2 < parent2List.size() && parent2List.get(j2).convertCellToInt(subjectsAmount, classesAmount, teachersAmount, hoursAmount, daysAmount) + 1 <= cuttingPointsLocations[i]) {
                if (i % 2 == 0) {
                    offSpring2.addFifthToList(parent2List.get(j2));
                    j2++;
                } else {
                    offSpring1.addFifthToList(parent2List.get(j2));
                    j2++;
                }
            }
        }
    }
}
