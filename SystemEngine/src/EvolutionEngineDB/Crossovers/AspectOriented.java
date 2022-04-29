package EvolutionEngineDB.Crossovers;

import Algorithm.TimeTable;
import SchoolTimeTable.SchoolDB;

import java.util.*;

public class AspectOriented extends Crossover {
    public enum orientation {
        TEACHER,
        CLASS
    }

    private orientation type;

    public AspectOriented(String configuration) {

        String[] currStr = configuration.split("=");
        type = Enum.valueOf(orientation.class, currStr[1].toUpperCase());
    }

    public void setType(String type) {
        this.type = Enum.valueOf(orientation.class,type.toUpperCase());
    }

    public orientation getType() {
        return type;
    }

    public AspectOriented(int cuttingPoints, String orientationStr){
        setCuttingPointsNumber(cuttingPoints);
        type=Enum.valueOf(orientation.class,orientationStr.toUpperCase());
    }

    @Override
    public List<TimeTable> makeCrossover(TimeTable parent1, TimeTable parent2, int currentGeneration) {
        int teacherSize = parent1.getSchoolDB().getTeacherList().size();
        int classSize = parent1.getSchoolDB().getClassRoomsList().size();
        int subjectsAmount = parent1.getSchoolDB().getSubjectsList().size();
        int classesAmount = parent1.getSchoolDB().getClassRoomsList().size();
        int teachersAmount = parent1.getSchoolDB().getTeacherList().size();
        int hoursAmount = parent1.getHours();
        int daysAmount = parent1.getDays();
        TimeTable offSpring1=new TimeTable(parent1.getSchoolDB());
        TimeTable offSpring2=new TimeTable(parent1.getSchoolDB());
        crossoverByOrientation(parent1, parent2, teacherSize,classSize, subjectsAmount, classesAmount, teachersAmount, hoursAmount, daysAmount, offSpring1, offSpring2);
        while (offSpring1.getTimeTableCells().size() == 0 || offSpring2.getTimeTableCells().size() == 0) {
            crossoverByOrientation(parent1, parent2, teacherSize,classSize, subjectsAmount, classesAmount, teachersAmount, hoursAmount, daysAmount, offSpring1, offSpring2);
        }
        offSpring1.createTimeTableMatrixFromListWithoutSort();
        offSpring2.createTimeTableMatrixFromListWithoutSort();
        List<TimeTable> twoOffsprings = new ArrayList<>();
        twoOffsprings.add(offSpring1);
        twoOffsprings.add(offSpring2);
        return twoOffsprings;
    }

    @Override
    public String toString() {
        return System.lineSeparator() + "Crossover name: AspectOriented" + ", " + "Configuration: " + type.toString() + ", " + super.toString();
    }


    private void crossoverByOrientation(TimeTable parent1, TimeTable parent2, int teacherSize,int classSize, int subjectsAmount, int classesAmount, int teachersAmount, int hoursAmount, int daysAmount, TimeTable offSpring1, TimeTable offSpring2) {

        if(type==orientation.TEACHER) {
            for (int i = 1; i <= teacherSize; i++) {
                List<TimeTable.TimeTableCell> teacherSegmentP1 = createListFromMatrix(parent1.getTimeTableByTeacher(i), parent1.getSchoolDB());
                List<TimeTable.TimeTableCell> teacherSegmentP2 = createListFromMatrix(parent2.getTimeTableByTeacher(i), parent2.getSchoolDB());
                createOffspringsByCuttingPoints(parent1, teacherSegmentP1, teacherSegmentP2, offSpring1, offSpring2, subjectsAmount, classesAmount, teachersAmount, hoursAmount, daysAmount);
            }
        }
        else{
            for (int i = 1; i <= classSize; i++) {
                List<TimeTable.TimeTableCell> classSegmentP1 = createListFromMatrix(parent1.getTimeTableByClass(i), parent1.getSchoolDB());
                List<TimeTable.TimeTableCell> classSegmentP2 = createListFromMatrix(parent2.getTimeTableByClass(i), parent2.getSchoolDB());
                createOffspringsByCuttingPoints(parent1, classSegmentP1, classSegmentP2, offSpring1, offSpring2, subjectsAmount, classesAmount, teachersAmount, hoursAmount, daysAmount);
            }
        }

    }

    public List<TimeTable.TimeTableCell> createListFromMatrix(List<TimeTable.TimeTableCell>[][] timeTableCellsMatrix, SchoolDB schoolDB) {
        List<TimeTable.TimeTableCell> sortedList = new ArrayList<>();
        for (int i = 0; i < schoolDB.getNumberOfDays(); i++) {
            for (int j = 0; j < schoolDB.getNumberOfHours(); j++) {
                for (TimeTable.TimeTableCell cell : timeTableCellsMatrix[i][j]) {
                    // TimeTable.TimeTableCell copiedCell = new TimeTable.Tim/ur(), cell.getClassRoom(), cell.getTeacher(), cell.getSubject());
                    // sortedList.add(copiedCell);
                    sortedList.add(cell);
                }
            }
        }

        return sortedList;
    }

    private void createOffspringsByCuttingPoints(TimeTable parent1, List<TimeTable.TimeTableCell> parent1List, List<TimeTable.TimeTableCell> parent2List, TimeTable offSpring1, TimeTable offSpring2, int subjectsAmount, int classesAmount, int teachersAmount, int hoursAmount, int daysAmount) {
        int maxNumberOfFifths;
        int maxParentSize = Math.max(parent1List.size(), parent2List.size());
        if (type == orientation.CLASS) {
            maxNumberOfFifths = Math.max(maxParentSize, subjectsAmount * teachersAmount * hoursAmount * daysAmount);// extra place for cutting point that is the end of the list (d*h*t*c*s)
        } else {
            maxNumberOfFifths = Math.max(maxParentSize, subjectsAmount * classesAmount * hoursAmount * daysAmount);// extra place for cutting point that is the end of the list (d*h*t*c*s)
        }
        int[] cuttingPointsLocations = new int[this.getCuttingPointsNumber() + 1];// extra place for cutting point that is the end of the list (d*h*t*c*s)
        Random rndPoints = new Random();
        // int maxNumberOfFifths = parent1.getMaxNumberOfFifths();
        for (int i = 0; i < cuttingPointsLocations.length - 1; i++) {
            cuttingPointsLocations[i] = rndPoints.nextInt(maxNumberOfFifths) + 1;
        }
//        if(type==orientation.CLASS){
        cuttingPointsLocations[cuttingPointsLocations.length - 1] = maxNumberOfFifths;// extra place for cutting point that is the end of the list (d*h*t*c*s)
//        }
//       else{
//            cuttingPointsLocations[cuttingPointsLocations.length - 1] = subjectsAmount * classesAmount * hoursAmount * daysAmount;// extra place for cutting point that is the end of the list (d*h*t*c*s)
//        }

        Arrays.sort(cuttingPointsLocations);

        int j1 = 0;
        int j2 = 0;
        if (type == orientation.CLASS) {
            for (int i = 0; i < cuttingPointsLocations.length; i++) {
                while (j1 < parent1List.size() && parent1List.get(j1).convertCellToIntByClass(subjectsAmount, teachersAmount, hoursAmount, daysAmount) + 1 <= cuttingPointsLocations[i]) {
                    if (i % 2 == 0) {
                        offSpring1.addFifthToList(parent1List.get(j1));
                        j1++;
                    } else {
                        offSpring2.addFifthToList(parent1List.get(j1));
                        j1++;
                    }
                }


                while (j2 < parent2List.size() && parent2List.get(j2).convertCellToIntByClass(subjectsAmount, teachersAmount, hoursAmount, daysAmount) + 1 <= cuttingPointsLocations[i]) {
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
        else{
                for (int i = 0; i < cuttingPointsLocations.length; i++) {

                    while (j1 < parent1List.size() && parent1List.get(j1).convertCellToIntByTeacher(subjectsAmount, classesAmount, hoursAmount, daysAmount) + 1 <= cuttingPointsLocations[i]) {
                        if (i % 2 == 0) {
                            offSpring1.addFifthToList(parent1List.get(j1));
                            j1++;
                        } else {
                            offSpring2.addFifthToList(parent1List.get(j1));
                            j1++;
                        }
                    }

                    while (j2 < parent2List.size() && parent2List.get(j2).convertCellToIntByTeacher(subjectsAmount, classesAmount, hoursAmount, daysAmount) + 1 <= cuttingPointsLocations[i]) {
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
    }

