package SchoolTimeTable;

import CoreEvolution.Systemic;

import java.util.List;

public class SchoolDB {
    private TeachersCollection teachers;
    private ClassRoomCollection classRooms;
    private SubjectsCollection subjects;
    private RulesCollection rules;
    private int numberOfDays;
    private int numberOfHours;

    public SchoolDB() {
        teachers = new TeachersCollection();
        classRooms = new ClassRoomCollection();
        subjects = new SubjectsCollection();
        rules=new RulesCollection();
    }



    public int getNumberOfDays() {
        return numberOfDays;
    }


    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public TeachersCollection getTeacherCollection() {
        return teachers;
    }

    public List<Teacher> getTeacherList() {
        return teachers.getTeachers();
    }

    public List<ClassRoom> getClassRoomsList() {
        return classRooms.getClassRooms();
    }

    public ClassRoomCollection getClassRoomsCollection() {
        return classRooms;
    }

    public List<Subject> getSubjectsList() {
        return subjects.getSubjects();
    }

    public SubjectsCollection getSubjectsCollection() {
        return subjects;
    }

    public List<Rule> getRulesList(){return rules.getRules();}

    public RulesCollection getRulesCollection() {
        return rules;
    }

    @Override
    public String toString() {
       String schoolStr="School details:"+ System.lineSeparator() +subjects.toString()+ System.lineSeparator()
               +teachers.toString()+ System.lineSeparator() +classRooms.toString()+ System.lineSeparator() +rules.toString();
       return schoolStr;
    }
}

