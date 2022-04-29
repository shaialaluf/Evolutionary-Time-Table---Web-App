package exception;

public class TeacherWithIllegalSubjectException extends RuntimeException {
    private int teacherId;
    private String teacherName;
    private int subjectId;
    private int numberOfSubjects;

    public TeacherWithIllegalSubjectException(int teacherId, String teacherName, int subjectId, int numberOfSubjects) {
        super("The Teacher with id: "+teacherId+" ,and name: "+teacherName+" teaches unrecognized subject ("+subjectId+"). "+
                "The range of the subjects is between 1 - "+numberOfSubjects);
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.subjectId = subjectId;
        this.numberOfSubjects = numberOfSubjects;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getNumberOfSubjects() {
        return numberOfSubjects;
    }


}
