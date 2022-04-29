package exception;

public class ClassPassedHoursLimitException extends RuntimeException{
    private int classId;
    private String ClassName;
    private int classSumOfHours;
    private int maxSumOfHours;

    public ClassPassedHoursLimitException(int classId, String className, int classSumOfHours, int maxSumOfHours) {
        super("The Class with id: "+classId+" and name: "+className+" passed the limit of studying hours. The class has: " +classSumOfHours
                +" hours"+" but maximum hours number is: " + maxSumOfHours);
        this.classId = classId;
        ClassName = className;
        this.classSumOfHours = classSumOfHours;
        this.maxSumOfHours = maxSumOfHours;
    }
}
