package Algorithm;

import SchoolTimeTable.ClassRoom;
import SchoolTimeTable.Teacher;

import java.text.DecimalFormat;
import java.util.List;

public enum BestSolutionOrientaion {

    RAW {
        @Override
        public StringBuilder stringOrientation(TimeTable bestSolution, int bestSolutionGenNumber) {
            StringBuilder answer = new StringBuilder();

            answer.append("Raw representation of best solution: "+System.lineSeparator());
            for (TimeTable.TimeTableCell currCell : bestSolution.getTimeTableCells()) {
                answer.append(String.format("< Day: %d, ", currCell.getDay()) );
                answer.append(String.format("Hour: %d, ", currCell.getHour()) );
                answer.append(String.format("Class Id: %d, ", currCell.getClassRoom()) );
                answer.append(String.format("Teacher Id: %d, ", currCell.getTeacher()) );
                answer.append(String.format("Subject Id: %d > ", currCell.getSubject()) );
                answer.append(System.lineSeparator());
            }


            //"<1 ,12,1,2>"
            //answer.append('<');
//            answer.append("Day:     ");
//            for (TimeTable.TimeTableCell currCell : bestSolution.getTimeTableCells()) {
//                answer.append(String.format("%1$2s", currCell.getDay()) + " |");
//            }
//            answer.deleteCharAt(answer.length() - 1);
//            answer.append(System.lineSeparator());
//
//            answer.append("Hour:    ");
//            for (TimeTable.TimeTableCell currCell : bestSolution.getTimeTableCells()) {
//                answer.append(String.format("%1$2s", currCell.getHour()) + " |");
//            }
//            answer.deleteCharAt(answer.length() - 1);
//            answer.append(System.lineSeparator());
//
//            answer.append("Class:   ");
//            for (TimeTable.TimeTableCell currCell : bestSolution.getTimeTableCells()) {
//                answer.append(String.format("%1$2s", currCell.getClassRoom()) + " |");
//            }
//            answer.deleteCharAt(answer.length() - 1);
//            answer.append(System.lineSeparator());
//
//            answer.append("Teacher: ");
//            for (TimeTable.TimeTableCell currCell : bestSolution.getTimeTableCells()) {
//                answer.append(String.format("%1$2s", currCell.getTeacher()) + " |");
//            }
//            answer.deleteCharAt(answer.length() - 1);
//            answer.append(System.lineSeparator());
//
//            answer.append("Subject: ");
//            for (TimeTable.TimeTableCell currCell : bestSolution.getTimeTableCells()) {
//                answer.append(String.format("%1$2s", currCell.getSubject()) + " |");
//            }
//            answer.deleteCharAt(answer.length() - 1);
            RulesReviewOfBestSolution(answer, bestSolution,bestSolutionGenNumber);
            return answer;

            //answer.setCharAt(answer.length()-1,'>');

        }
    },

    TEACHERORIENTED {
        @Override
        public StringBuilder stringOrientation(TimeTable bestSolution, int bestSolutionGenNumber){
            int rows = bestSolution.getHours();
            int cols = bestSolution.getDays();
            List<TimeTable.TimeTableCell>[][] TimeTableOfTeacher;
            StringBuilder answer = new StringBuilder();

            answer.append(System.lineSeparator());
            for (Teacher currentTeacher : bestSolution.getSchoolDB().getTeacherList()) {
                    answer.append("Teacher with ID:"+currentTeacher.getId()+" and name: "+currentTeacher.getName()+"."+System.lineSeparator());
                TimeTableOfTeacher = bestSolution.getTimeTableByTeacher(currentTeacher.getId());
                int maxStudy = bestSolution.getMaxStudyPerHour(TimeTableOfTeacher);
                if(maxStudy>1){
                    answer.append("illegal time table - more than one study in one hour."+System.lineSeparator());
                }
                answer.append("time table - for teacher: "+System.lineSeparator());
                answer.append("(x,y)->(classId,subjectId)"+System.lineSeparator());
                answer.append("H"+"/"+"D|");
                for (int k = 1; k <= cols; k++) {
                    for (int t = 0; t < maxStudy; t++) {
                        answer.append(String.format("   "));
                    }
                    answer.append(String.format("%d", k));
                    for (int t = 0; t < maxStudy; t++) {
                        answer.append(String.format("   "));
                    }
                    answer.append("|");
                }

                for (int i = 0; i < rows; i++) {
                    answer.append(System.lineSeparator());
                    answer.append(String.format("----"));
                    for (int k = 1; k <= cols ; k++) {
                        for(int m=1;m<=9+(6*(maxStudy-1))-1;m++){
                            answer.append(String.format("-"));
                        }
                    }
                    answer.append(System.lineSeparator() + String.format("%d  |", i + 1));
                    for (int j = 0; j < cols; j++) {

                        if (TimeTableOfTeacher[j][i].size() != 0) {
                            answer.append(" ");
                            int dist = maxStudy - TimeTableOfTeacher[j][i].size();
                            if (dist > 0) {
                                for (int d = 0; d < dist; d++) {
                                    answer.append(String.format("      "));
                                }
                            }
                            for (TimeTable.TimeTableCell currentFifth : TimeTableOfTeacher[j][i]) {
                                answer.append(String.format("(%d,%d)", currentFifth.getClassRoom(), currentFifth.getSubject()));
                                answer.append(" ");
                            }
                            answer.append("|");
                        } else {
                            for (int t = 0; t < maxStudy; t++) {
                                answer.append(String.format("   "));
                            }
                            answer.append(String.format("X"));
                            for (int t = 0; t < maxStudy; t++) {
                                answer.append(String.format("   "));
                            }
                            answer.append("|");
                        }

                    }
                }
                answer.append(System.lineSeparator());
                answer.append(System.lineSeparator());
                answer.append(System.lineSeparator());
            }
            RulesReviewOfBestSolution(answer, bestSolution,bestSolutionGenNumber);
            return answer;

        }
    },


    CLASSORIENTED {
        @Override
        public StringBuilder stringOrientation(TimeTable bestSolution, int bestSolutionGenNumber) {
            int rows = bestSolution.getHours();
            int cols = bestSolution.getDays();
            List<TimeTable.TimeTableCell>[][] TimeTableOfClass;
            StringBuilder answer = new StringBuilder();

            answer.append(System.lineSeparator());
            for (ClassRoom currentClass : bestSolution.getSchoolDB().getClassRoomsList()) {
                answer.append("Class with ID:"+currentClass.getId()+" and name: "+currentClass.getName()+"."+System.lineSeparator());
                TimeTableOfClass = bestSolution.getTimeTableByClass(currentClass.getId());
                int maxStudy = bestSolution.getMaxStudyPerHour(TimeTableOfClass);
                if(maxStudy>1){
                    answer.append("illegal time table - more than one study in one hour."+System.lineSeparator());
                }
                answer.append("time table - for class: "+System.lineSeparator());
                answer.append("(x,y)->(teacherId,subjectId)"+System.lineSeparator());
                answer.append("H"+"/"+"D|");
                for (int k = 1; k <= cols; k++) {
                    for (int t = 0; t < maxStudy; t++) {
                        answer.append(String.format("   "));
                    }
                    answer.append(String.format("%d", k));
                    for (int t = 0; t < maxStudy; t++) {
                        answer.append(String.format("   "));
                    }
                    answer.append("|");
                }

                for (int i = 0; i < rows; i++) {
                    answer.append(System.lineSeparator());
                    answer.append(String.format("----"));
                    for (int k = 1; k <= cols ; k++) {
                        for(int m=1;m<=9+(6*(maxStudy-1))-1;m++){
                            answer.append(String.format("-"));
                        }
                    }
                    answer.append(System.lineSeparator() + String.format("%d  |", i + 1));
                    for (int j = 0; j < cols; j++) {

                        if (TimeTableOfClass[j][i].size() != 0) {
                            answer.append(" ");
                            int dist = maxStudy - TimeTableOfClass[j][i].size();
                            if (dist > 0) {
                                for (int d = 0; d < dist; d++) {
                                    answer.append(String.format("      "));
                                }
                            }
                            for (TimeTable.TimeTableCell currentFifth : TimeTableOfClass[j][i]) {
                                answer.append(String.format("(%d,%d)", currentFifth.getTeacher(), currentFifth.getSubject()));
                                answer.append(" ");
                            }
                            answer.append("|");
                        } else {
                            for (int t = 0; t < maxStudy; t++) {
                                answer.append(String.format("   "));
                            }
                            answer.append(String.format("X"));
                            for (int t = 0; t < maxStudy; t++) {
                                answer.append(String.format("   "));
                            }
                            answer.append("|");
                        }

                    }
                }
                answer.append(System.lineSeparator());
                answer.append(System.lineSeparator());
                answer.append(System.lineSeparator());
            }
            RulesReviewOfBestSolution(answer, bestSolution, bestSolutionGenNumber);
            return answer;

        }
    };

    public abstract StringBuilder stringOrientation(TimeTable bestSolution, int bestSolutionGenNumber) ;

    public void RulesReviewOfBestSolution(StringBuilder orientation, TimeTable bestSolution, int bestSolutionGenNumber) {
        orientation.append(System.lineSeparator() + System.lineSeparator());
        orientation.append("Fitness of best solution: " + bestSolution.getFitness() + System.lineSeparator() + System.lineSeparator());
        orientation.append("All rules grades:" + System.lineSeparator());
        bestSolution.getRuleGrades().entrySet().stream().forEach(t -> orientation.append("Rule Name: " + t.getKey().getRuleId() + System.lineSeparator()
                + "Rule Type: " + t.getKey().getType() + System.lineSeparator() + "Rule Grade: " + t.getValue() + System.lineSeparator() + System.lineSeparator()));

        orientation.append("Hard Rules average: " + String.format("%.01f",bestSolution.getAvgHardRules()) + System.lineSeparator());
        orientation.append("Soft Rules average: " +String.format("%.01f", bestSolution.getAvgSoftRules()) + System.lineSeparator()+ System.lineSeparator());
        orientation.append("Generation number of best solution: "+bestSolutionGenNumber);
    }
}
