package exception;

public class TeacherWorkingHoursException extends RuntimeException  {

    public TeacherWorkingHoursException(int teacherId, int workingHours,int dh) {
        super("Teacher number "+teacherId +" has working hours preference that is bigger then DH. His working hours preference is: " +workingHours + ", but DH is: "+dh);
    }
}
