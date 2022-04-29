package SchoolTimeTable;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private int id;
    private String name;
    private List<Subject> subjects;
    private int workingHoursPreference;



    public Teacher(int id, String name, int workingHoursPreference) {
        this.id = id;
        this.name = name;
        this.workingHoursPreference=workingHoursPreference;
        this.subjects =new ArrayList<Subject>();
    }

    public void setWorkingHoursPreference(int workingHoursPreference) {
        this.workingHoursPreference = workingHoursPreference;
    }

    public int getWorkingHoursPreference() {
        return workingHoursPreference;
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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void addSubjectToTeacher(Subject subjectToAdd){
        subjects.add(subjectToAdd);
    }

    @Override
    public String toString() {
        String teacherStr= "Teacher Id: "+  id +", the subjects of the teacher are: " ;
        for(Subject s:subjects )
        {
            teacherStr+=System.lineSeparator()+" "+s.toString();
        }
        return teacherStr;
    }
}
