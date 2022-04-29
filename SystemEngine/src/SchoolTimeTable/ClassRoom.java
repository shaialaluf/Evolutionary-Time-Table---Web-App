package SchoolTimeTable;

import java.util.HashMap;
import java.util.Map;

public class ClassRoom {
    private int id;
    private String name;
    private Map<Subject,Integer> subject2WeeklyHours;

    public ClassRoom(int id, String name) {
        this.id = id;
        this.name = name;
        this.subject2WeeklyHours=new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Subject, Integer> getSubject2WeeklyHours() {
        return subject2WeeklyHours;
    }
    public void addSubjectToClass(Subject subjectId, int hours){
        subject2WeeklyHours.put(subjectId,hours);
    }
    @Override
    public String toString() {
        String classStr= "Class Id: "+  id +", the subjects and the hours that the class study are: " ;
        for(Map.Entry <Subject,Integer> subject2WeeklyHour:subject2WeeklyHours.entrySet() )
        {
            classStr+=System.lineSeparator()+" "+subject2WeeklyHour.getKey().toString()+" ,weekly hours :"+subject2WeeklyHour.getValue();
        }
        return classStr;
    }
    public void setSubject2WeeklyHours(Map<Subject, Integer> subject2WeeklyHours) {
        this.subject2WeeklyHours = subject2WeeklyHours;

    }
}
