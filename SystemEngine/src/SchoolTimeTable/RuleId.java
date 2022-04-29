package SchoolTimeTable;

import Algorithm.TimeTable;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RuleId {
    TeacherIsHuman {
        @Override
        public float calculateGrade(TimeTable solution, SchoolDB school) {
            int[] teachersFlags;
            teachersFlags = new int[school.getTeacherList().size()];
            float pointsToDecrease = calcPointsToDecrease(solution);
            for (int i = 0; i < teachersFlags.length; i++) {
                teachersFlags[i] = 0;
            }

            List<TimeTable.TimeTableCell>[][] TimeTableMatrix = solution.getTimeTableCellsMatrix();
            for (int i = 0; i < solution.getDays(); i++) {
                for (int j = 0; j < solution.getHours(); j++) {
                    for (TimeTable.TimeTableCell fifth : TimeTableMatrix[i][j]) {
                        teachersFlags[fifth.getTeacher() - 1]++;
                    }

                    for (int k = 0; k < teachersFlags.length; k++) {
                        if (teachersFlags[k] > 1) {
                            ruleGrade -= (teachersFlags[k] - 1) * pointsToDecrease;
                        }
                    }

                    for (int k = 0; k < teachersFlags.length; k++) {
                        teachersFlags[k] = 0;
                    }

                }
            }
            return ruleGrade;
        }

        @Override
        public void evaluateConfiguration(String config) {

        }
    },
    Singularity {
        @Override
        public float calculateGrade(TimeTable solution, SchoolDB school) {
            int[] classesFlags = new int[school.getTeacherList().size()];
            float pointsToDecrease = calcPointsToDecrease(solution);
            for (int i = 0; i < classesFlags.length; i++) {
                classesFlags[i] = 0;
            }

            List<TimeTable.TimeTableCell>[][] TimeTableMatrix = solution.getTimeTableCellsMatrix();
            for (int i = 0; i < solution.getDays(); i++) {
                for (int j = 0; j < solution.getHours(); j++) {
                    for (TimeTable.TimeTableCell fifth : TimeTableMatrix[i][j]) {
                        classesFlags[fifth.getClassRoom() - 1]++;
                    }

                    for (int k = 0; k < classesFlags.length; k++) {
                        if (classesFlags[k] > 1) {
                            ruleGrade -= (classesFlags[k] - 1) * pointsToDecrease;
                        }
                    }

                    for (int k = 0; k < classesFlags.length; k++) {
                        classesFlags[k] = 0;
                    }
                }
            }
            return ruleGrade;
        }

        @Override
        public void evaluateConfiguration(String config) {

        }
    },
    Knowledgeable {
        @Override
        public float calculateGrade(TimeTable solution, SchoolDB school) {
            Teacher currentTeacher = null;
            boolean isKnowledgeable = false;
            float pointToDecrease = calcPointsToDecrease(solution);
            List<TimeTable.TimeTableCell> timeTableList = solution.getTimeTableCells();
            for (TimeTable.TimeTableCell cell : timeTableList) {
                currentTeacher = school.getTeacherCollection().getTeacherById(cell.getTeacher());
                for (Subject subject : currentTeacher.getSubjects()) {
                    if (subject.getId() == cell.getSubject()) {
                        isKnowledgeable = true;
                        break;
                    }
                }
                if (isKnowledgeable == false) {
                    ruleGrade -= pointToDecrease;
                } else {
                    isKnowledgeable = false;
                }
            }
//                if (!currentTeacher.getSubjects().contains(cell.getSubject())) {
//                    ruleGrade -= pointToDecrease;
//                }
//            }
            return ruleGrade;
        }

        public void evaluateConfiguration(String config) {

        }
    },
    Satisfactory {
        @Override
        public float calculateGrade(TimeTable solution, SchoolDB school) {
            double[] classesGrade = new double[school.getClassRoomsList().size()];
            Arrays.fill(classesGrade, 0);
            int index;
            int numerator;
            int denominator;
            int amountOfHour;
            Map<Integer, Integer>[] currSubject2Hours = new HashMap[school.getClassRoomsList().size()];
            for (ClassRoom currClass : school.getClassRoomsList()) {
                currSubject2Hours[currClass.getId() - 1] = new HashMap<>();
            }
            for (TimeTable.TimeTableCell cell : solution.getTimeTableCells()) {
                int classId = cell.getClassRoom();
                int subjectId = cell.getSubject();
                if (currSubject2Hours[classId - 1].containsKey(subjectId)) {
                    amountOfHour = currSubject2Hours[classId - 1].get(subjectId);
                    currSubject2Hours[classId - 1].put(subjectId, amountOfHour + 1);
                } else {
                    currSubject2Hours[classId - 1].put(subjectId, 1);
                }
            }
            for (ClassRoom currClass : school.getClassRoomsList()) {
                int classId = currClass.getId();
                index = 0;
                double[] reqGrades = new double[currClass.getSubject2WeeklyHours().size()];
                for (Map.Entry<Subject, Integer> currReq : currClass.getSubject2WeeklyHours().entrySet()) {//changed to Subject from Integer
                    if (currSubject2Hours[currClass.getId() - 1].containsKey(currReq.getKey().getId())) {
                        numerator = Math.min(currReq.getValue(), currSubject2Hours[currClass.getId() - 1].get(currReq.getKey().getId()));
                        denominator = Math.max(currReq.getValue(), currSubject2Hours[currClass.getId() - 1].get(currReq.getKey().getId()));
                        reqGrades[index] = ((double) numerator * 100) / denominator;

                    } else {
                        reqGrades[index] = 0;
                    }
                    index++;
                }
                classesGrade[classId - 1] = Arrays.stream(reqGrades).average().getAsDouble();
            }
            ruleGrade = (float) Arrays.stream(classesGrade).average().getAsDouble();
            return ruleGrade;
        }

        public void evaluateConfiguration(String config) {

        }
    },

    DayOffTeacher {
        @Override
        public float calculateGrade(TimeTable solution, SchoolDB school) {
            int max = 0;
            int min = 0;
            int workingDayCount = 0;
            float sumOfPointToDecreaseForAllTeacher = 0;
            float pointToDecrease;
            boolean teachersDaysOff[][] = new boolean[school.getTeacherList().size()][school.getNumberOfDays()];
            for (int i = 0; i < school.getTeacherList().size(); i++) {
                Arrays.fill(teachersDaysOff[i], false);
            }
            for (TimeTable.TimeTableCell fifth : solution.getTimeTableCells()) {
                teachersDaysOff[fifth.getTeacher() - 1][fifth.getDay() - 1] = true;
            }
            for (int i = 0; i < school.getTeacherList().size(); i++) {
                for (int j = 0; j < school.getNumberOfDays(); j++) {
                    if (teachersDaysOff[i][j] == true) {
                        workingDayCount++;
                    }
                }

                int daysOff=school.getNumberOfDays() - workingDayCount;
                if(daysOff>0) {
                    pointToDecrease=0;
                }
                else{
                    pointToDecrease=100;
                }

                sumOfPointToDecreaseForAllTeacher +=pointToDecrease;
                workingDayCount=0;

            }
            pointToDecrease=sumOfPointToDecreaseForAllTeacher/(float) school.getTeacherList().size();
            return 100-pointToDecrease;
        }

        @Override
        public void evaluateConfiguration(String config) {

        }
    },


    Sequentiality {
        @Override
            public float calculateGrade(TimeTable solution, SchoolDB school) {
            num++;
            int totalHours = Integer.parseInt(configuration);
            int days=school.getNumberOfDays();
            int hours=school.getNumberOfHours();
            int classes=school.getClassRoomsList().size();
            int subjects=school.getSubjectsList().size();
            int[] maxHoursOfSubjectsPerDay=new int[subjects];
            int[] countCurrentSequenceSubjects=new int[subjects];
            boolean[] isSubjectExistInLastCell=new boolean[subjects];
            boolean[] isSubjectExistInCurrCell=new boolean[subjects];
            float[][] gradesOfSubjectsPerDay=new float[days][subjects];
            float[] classesGrades=new float[classes];
            List<List<TimeTable.TimeTableCell>[][]> classesTimeTables=solution.getTimeTableOfAllClasses();

            for(int c=0; c<classes; c++){
                List<TimeTable.TimeTableCell>[][] classMatrix=classesTimeTables.get(c);
                for (int d = 0; d < days; d++) {
                    Arrays.fill(maxHoursOfSubjectsPerDay, 0);
                    Arrays.fill(isSubjectExistInLastCell, false);
                    Arrays.fill(countCurrentSequenceSubjects, 0);
                    for (int h = 0; h < hours; h++) {
                        Arrays.fill(isSubjectExistInCurrCell, false);
                        for (TimeTable.TimeTableCell currCell : classMatrix[d][h]) {
                            if (isSubjectExistInLastCell[currCell.getSubject() - 1] == true
                                    && isSubjectExistInCurrCell[currCell.getSubject() - 1] == false) {
                                countCurrentSequenceSubjects[currCell.getSubject() - 1]++;
                                isSubjectExistInCurrCell[currCell.getSubject() - 1] = true;
                            } else if (isSubjectExistInLastCell[currCell.getSubject() - 1] == false
                                    && isSubjectExistInCurrCell[currCell.getSubject() - 1] == false) {
                                if (countCurrentSequenceSubjects[currCell.getSubject() - 1] > maxHoursOfSubjectsPerDay[currCell.getSubject() - 1]) {
                                    maxHoursOfSubjectsPerDay[currCell.getSubject() - 1] = countCurrentSequenceSubjects[currCell.getSubject() - 1];
                                }
                                countCurrentSequenceSubjects[currCell.getSubject() - 1] = 1;
                                isSubjectExistInCurrCell[currCell.getSubject() - 1] = true;
                            }
                        }
                        isSubjectExistInLastCell = Arrays.copyOf(isSubjectExistInCurrCell, isSubjectExistInCurrCell.length);////////////////////////////////////

                    }
                    for (int s = 0; s < subjects; s++) {
                        if (countCurrentSequenceSubjects[s] > maxHoursOfSubjectsPerDay[s]) {
                            maxHoursOfSubjectsPerDay[s] = countCurrentSequenceSubjects[s];
                        }
                    }

                    for (int s = 0; s < subjects; s++) {
                        if (maxHoursOfSubjectsPerDay[s] > totalHours) {
                            gradesOfSubjectsPerDay[d][s] = 100 - ((((float) (maxHoursOfSubjectsPerDay[s] - totalHours)) / ((float) (hours - totalHours))) * 100);
                        } else {
                            gradesOfSubjectsPerDay[d][s] = 100;
                        }
                    }
                }

                float sumOfGrades=0;
                for(int d=0; d<days; d++){
                    for(int s=0; s<subjects; s++){
                        sumOfGrades+=gradesOfSubjectsPerDay[d][s];
                    }
                }

                classesGrades[c]=sumOfGrades/((float)(days*subjects));

            }


            float sumOfGrades=0;
            for(int c=0; c<classes; c++){
                sumOfGrades+= classesGrades[c];
            }

            return sumOfGrades/((float)(classes));




//            num++;
//            int totalHours = Integer.parseInt(configuration);
//            int days=school.getNumberOfDays();
//            int hours=school.getNumberOfHours();
//            int classes=school.getClassRoomsList().size();
//            int subjects=school.getSubjectsList().size();
//            int[] maxHoursOfSubjectsPerDay=new int[subjects];
//            int[] countCurrentSequenceSubjects=new int[subjects];
//            boolean[] isSubjectExistInLastCell=new boolean[subjects];
//            boolean[] isSubjectExistInCurrCell=new boolean[subjects];
//            float[][] gradesOfSubjectsPerDay=new float[days][subjects];
//            float[] classesGrades=new float[classes];
//
//
//           for(int c=0; c<classes; c++){
//               List<TimeTable.TimeTableCell>[][] classMatrix=solution.getTimeTableByClass(c);
//                for (int d = 0; d < days; d++) {
//                    Arrays.fill(maxHoursOfSubjectsPerDay, 0);
//                    Arrays.fill(isSubjectExistInLastCell, false);
//                    Arrays.fill(countCurrentSequenceSubjects, 0);
//                    for (int h = 0; h < hours; h++) {
//                        Arrays.fill(isSubjectExistInCurrCell, false);
//                        for (TimeTable.TimeTableCell currCell : classMatrix[d][h]) {
//                            if (isSubjectExistInLastCell[currCell.getSubject() - 1] == true
//                                    && isSubjectExistInCurrCell[currCell.getSubject() - 1] == false) {
//                                countCurrentSequenceSubjects[currCell.getSubject() - 1]++;
//                                isSubjectExistInCurrCell[currCell.getSubject() - 1] = true;
//                            } else if (isSubjectExistInLastCell[currCell.getSubject() - 1] == false
//                                    && isSubjectExistInCurrCell[currCell.getSubject() - 1] == false) {
//                                if (countCurrentSequenceSubjects[currCell.getSubject() - 1] > maxHoursOfSubjectsPerDay[currCell.getSubject() - 1]) {
//                                    maxHoursOfSubjectsPerDay[currCell.getSubject() - 1] = countCurrentSequenceSubjects[currCell.getSubject() - 1];
//                                }
//                                countCurrentSequenceSubjects[currCell.getSubject() - 1] = 1;
//                                isSubjectExistInCurrCell[currCell.getSubject() - 1] = true;
//                            }
//                        }
//                        isSubjectExistInLastCell = Arrays.copyOf(isSubjectExistInCurrCell, isSubjectExistInCurrCell.length);////////////////////////////////////
//
//                    }
//                    for (int s = 0; s < subjects; s++) {
//                        if (countCurrentSequenceSubjects[s] > maxHoursOfSubjectsPerDay[s]) {
//                            maxHoursOfSubjectsPerDay[s] = countCurrentSequenceSubjects[s];
//                        }
//                    }
//
//                    for (int s = 0; s < subjects; s++) {
//                        if (maxHoursOfSubjectsPerDay[s] > totalHours) {
//                            gradesOfSubjectsPerDay[d][s] = 100 - ((((float) (maxHoursOfSubjectsPerDay[s] - totalHours)) / ((float) (hours - totalHours))) * 100);
//                        } else {
//                            gradesOfSubjectsPerDay[d][s] = 100;
//                        }
//                    }
//                }
//
//               float sumOfGrades=0;
//               for(int d=0; d<days; d++){
//                   for(int s=0; s<subjects; s++){
//                       sumOfGrades+=gradesOfSubjectsPerDay[d][s];
//                   }
//               }
//
//               classesGrades[c]=sumOfGrades/((float)(days*subjects));
//
//            }
//
//
//            float sumOfGrades=0;
//            for(int c=0; c<classes; c++){
//                    sumOfGrades+= classesGrades[c];
//                }
//
//            return sumOfGrades/((float)(classes));
        }

        @Override
        public void evaluateConfiguration(String config) {
            configuration = config;

        }
    },


    DayOffClass{
        @Override
        public float calculateGrade(TimeTable solution, SchoolDB school) {
            int max = 0;
            int min = 0;
            int workingDayCount = 0;
            float sumOfPointToDecreaseForAllClass = 0;
            float pointToDecrease;
            boolean classesDaysOff[][] = new boolean[school.getClassRoomsList().size()][school.getNumberOfDays()];
            for (int i = 0; i < school.getClassRoomsList().size(); i++) {
                Arrays.fill(classesDaysOff[i], false);
            }
            for (TimeTable.TimeTableCell fifth : solution.getTimeTableCells()) {
                classesDaysOff[fifth.getClassRoom() - 1][fifth.getDay() - 1] = true;
            }
            for (int i = 0; i < school.getClassRoomsList().size(); i++) {
                for (int j = 0; j < school.getNumberOfDays(); j++) {
                    if (classesDaysOff[i][j] == true) {
                        workingDayCount++;
                    }
                }

                int daysOff=school.getNumberOfDays() - workingDayCount;
                if(daysOff>0) {
                    pointToDecrease=0;
                }
                else{
                    pointToDecrease=100;
                }

                sumOfPointToDecreaseForAllClass +=pointToDecrease;
                workingDayCount=0;

            }
            pointToDecrease=sumOfPointToDecreaseForAllClass/(float) school.getClassRoomsList().size();
            return 100-pointToDecrease;
        }

        @Override
        public void evaluateConfiguration(String config) {

        }
    },


    WorkingHoursPreference{
        @Override
        public float calculateGrade(TimeTable solution, SchoolDB school) {
            int numerator;
            int denominator;
            float oneTeacherGrade;
            int workingHoursDeFacto;
            float sumGradesOfAllTeachers=0;

            for(Teacher teacher: school.getTeacherList()){
                workingHoursDeFacto=0;
                List<TimeTable.TimeTableCell>[][] teacherMatrix=solution.getTimeTableByTeacher(teacher.getId());
                for(int i=0; i<solution.getDays(); i++){
                    for(int j=0; j<solution.getHours(); j++){
                        if(teacherMatrix[i][j].size()!=0){
                            workingHoursDeFacto++;
                        }
                    }
                }
                numerator=Math.min(workingHoursDeFacto,teacher.getWorkingHoursPreference());
                denominator=Math.max(workingHoursDeFacto,teacher.getWorkingHoursPreference());
                sumGradesOfAllTeachers+=((double) numerator * 100) / denominator;
            }

            return (sumGradesOfAllTeachers/school.getTeacherList().size());
        }

        @Override
        public void evaluateConfiguration(String config) {

        }
    };


    // for rule Sequentiality:
    // a. set configuration member in evaluateConfiguration method


    public abstract float calculateGrade(TimeTable solution, SchoolDB school);

    public abstract void evaluateConfiguration(String config);

    protected float ruleGrade;
    protected String configuration=null;
    protected static int num=0;

    public float calcPointsToDecrease(TimeTable solution) {
        int numberOfFifths = solution.getTimeTableCells().size();
        float pointsToDecrease = (float) (100.0) / numberOfFifths;
        ruleGrade = 100;
        return pointsToDecrease;
    }
//    public float calcPointsToDecreaseBySubject(SchoolDB school){
//        float numberOfSubjects=school.getSubjectsList().size();
//        float pointsToDecrease= 100/numberOfSubjects;
//        pointsToDecrease=(float)Math.floor(pointsToDecrease);
//        ruleGrade=100;
//        return pointsToDecrease;
//    }

}





