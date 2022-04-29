package SchoolTimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TeachersCollection {
    private List<Teacher> teachers;

    public TeachersCollection() {
        teachers = new ArrayList<>();
    }

    public void addToTeacherCollection(Teacher teacher) {
        teachers.add(teacher);
    }

    public Teacher getTeacherById(int id) {
        Teacher t=null;
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                t=teacher;
            }
        }
        return t;
    }

    @Override
    public String toString() {
        String teachersStr="List of all teachers: ";
        for(Teacher currTeacher: teachers){
            String currMsg=System.lineSeparator()+currTeacher.toString();
            teachersStr+=currMsg;
        }
        return teachersStr;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void sortTeachersById(){
        Collections.sort(teachers,new Comparator<Teacher>() {
            public int compare(Teacher t1, Teacher t2){
                  return t1.getId()-t2.getId();
            }
        });

    }
}
