package Problems;

import Algorithm.TimeTable;
import Algorithm.TimeTableCellByNames;
import SchoolTimeTable.Rule;
import SchoolTimeTable.RuleId;
import SchoolTimeTable.SchoolDB;
import SchoolTimeTable.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestSolution {
    private Map<Rule, Float> ruleGrades;
    private Map<String, List<TimeTableCellByNames>[][]> allTeachersTimeTables;
    private Map<String, List<TimeTableCellByNames>[][]> allClassesTimeTables;
    private List<TimeTableCellByNames> rawList;
    private int totalHourConfiguration=-1;
    private transient SchoolDB schoolDB;

    public BestSolution(SchoolDB schoolDB) {
        this.schoolDB = schoolDB;
        allTeachersTimeTables = new HashMap<>();
        allClassesTimeTables = new HashMap<>();
        rawList = new ArrayList<>();
    }

    public void setSchoolDB(SchoolDB schoolDB) {
        this.schoolDB = schoolDB;
    }

    public void setRuleGrades(Map<Rule, Float> ruleGrades) {
        this.ruleGrades = ruleGrades;
     for(Map.Entry<Rule,Float> entry: ruleGrades.entrySet()){
         if(entry.getKey().getRuleId().equals(Enum.valueOf(RuleId.class,"Sequentiality"))){
             totalHourConfiguration=  Integer.parseInt(entry.getKey().getRuleConfiguration());
         }



     }
    }

    public void convertTeacherMatrix(List<TimeTable.TimeTableCell>[][] teacherMatrixWithNumbers, String teacherName) {
        int days = schoolDB.getNumberOfDays();
        int hours = schoolDB.getNumberOfHours();
        List<TimeTableCellByNames>[][] teacherMatrixWithNames = new List[days][hours];
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < hours; j++) {
                teacherMatrixWithNames[i][j]=new ArrayList<>();
                for (TimeTable.TimeTableCell fifthNumbers : teacherMatrixWithNumbers[i][j]) {
                    TimeTableCellByNames fifthNames = new TimeTableCellByNames(fifthNumbers.getDay(), fifthNumbers.getHour(), fifthNumbers.getClassRoom(), fifthNumbers.getTeacher(), fifthNumbers.getSubject());
                    fifthNames.setClassRoom((schoolDB.getClassRoomsCollection().getClassRoomById(fifthNumbers.getClassRoom())).getName());
                    fifthNames.setSubject((schoolDB.getSubjectsCollection().getSubjectById(fifthNumbers.getSubject())).getName());
                    fifthNames.setTeacher((schoolDB.getTeacherCollection().getTeacherById(fifthNumbers.getTeacher())).getName());
                    teacherMatrixWithNames[i][j].add(fifthNames);
                }
            }
        }
        allTeachersTimeTables.put(teacherName, teacherMatrixWithNames);
    }


    public void convertClassMatrix(List<TimeTable.TimeTableCell>[][] classMatrixWithNumbers, String className) {
        int days = schoolDB.getNumberOfDays();
        int hours = schoolDB.getNumberOfHours();
        List<TimeTableCellByNames>[][] classMatrixWithNames = new List[days][hours];
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < hours; j++) {
                classMatrixWithNames[i][j]=new ArrayList<>();
                for (TimeTable.TimeTableCell fifthNumbers : classMatrixWithNumbers[i][j]) {
                    TimeTableCellByNames fifthNames = new TimeTableCellByNames(fifthNumbers.getDay(), fifthNumbers.getHour(), fifthNumbers.getClassRoom(), fifthNumbers.getTeacher(), fifthNumbers.getSubject());
                    fifthNames.setClassRoom((schoolDB.getClassRoomsCollection().getClassRoomById(fifthNumbers.getClassRoom())).getName());
                    fifthNames.setSubject((schoolDB.getSubjectsCollection().getSubjectById(fifthNumbers.getSubject())).getName());
                    fifthNames.setTeacher((schoolDB.getTeacherCollection().getTeacherById(fifthNumbers.getTeacher())).getName());
                    classMatrixWithNames[i][j].add(fifthNames);
                }
            }
        }
        allClassesTimeTables.put(className, classMatrixWithNames);
    }


    public void convertToRawListNames(List<TimeTable.TimeTableCell> timeTableCells) {
        for(TimeTable.TimeTableCell fifthNumbers: timeTableCells){
            TimeTableCellByNames fifthNames=new TimeTableCellByNames(fifthNumbers.getDay(),fifthNumbers.getHour(),fifthNumbers.getClassRoom(),fifthNumbers.getTeacher(),fifthNumbers.getSubject());
            fifthNames.setClassRoom(schoolDB.getClassRoomsCollection().getClassRoomById(fifthNumbers.getClassRoom()).getName());
            fifthNames.setTeacher(schoolDB.getTeacherCollection().getTeacherById(fifthNumbers.getTeacher()).getName());
            fifthNames.setSubject(schoolDB.getSubjectsCollection().getSubjectById(fifthNumbers.getSubject()).getName());
            rawList.add(fifthNames);
        }


    }
}
