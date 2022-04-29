package SchoolTimeTable;

public class StudyDemandUtility {
    private int weeklyHour;
    private int subjectId;
    private String subjectName;

    public StudyDemandUtility(int weeklyHour, int subjectId, String subjectName) {
        this.weeklyHour = weeklyHour;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public int getWeeklyHour() {
        return weeklyHour;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    @Override
    public String toString() {
        return "StudyDemandUtility{" +
                "weeklyHour=" + weeklyHour +
                ", subjectId=" + subjectId +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
