package exception;

public class ClassWithIllegalSubjectException extends RuntimeException {
    private int classId;
    private String ClassName;
    private int subjectId;
    private int numberOfSubjects;

    public ClassWithIllegalSubjectException(int classId, String ClassName, int subjectId, int numberOfSubjects) {
        super("The Class with id: " + classId + " ,and name: " + ClassName + " studies unrecognized subject (" + subjectId + ")" + System.lineSeparator() +
                "The range of the subjects is between 1 - " + numberOfSubjects);
        this.classId = classId;
        this.ClassName = ClassName;
        this.subjectId = subjectId;
        this.numberOfSubjects = numberOfSubjects;
    }
}
