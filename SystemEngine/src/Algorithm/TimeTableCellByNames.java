package Algorithm;

public class TimeTableCellByNames {
    private int day;
    private int hour;

    private String classRoom;
    private int classId;

    private String teacher;
    private int teacherId;

    private String subject;
    private int subjectId;

    public TimeTableCellByNames(int day, int hour, String classRoom, String teacher, String subject)  {
        this.day = day;
        this.hour = hour;
        this.classRoom = classRoom;
        this.teacher = teacher;
        this.subject = subject;
    }


    public TimeTableCellByNames(int day, int hour,int classId, int teacherId,int subjectId)  {
        this.day = day;
        this.hour = hour;
        this.classId = classId;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }


    public TimeTableCellByNames(){

    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }


}
