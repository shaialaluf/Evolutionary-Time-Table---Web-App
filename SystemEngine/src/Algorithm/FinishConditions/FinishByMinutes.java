package Algorithm.FinishConditions;

public class FinishByMinutes  implements FinishCondition {
    private float minutesToStop;
    private float currentTime;

    @Override
    public String toString() {
        return "FinishByMinutes{" +
                "minutesToStop=" + minutesToStop +
                '}';
    }

    public FinishByMinutes(float minutesToStop) {
        this.minutesToStop = minutesToStop;
    }

    public float getMinutesToStop() {
        return minutesToStop;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    @Override
    public boolean isFinish(float currentMinutes) {
        this.currentTime=currentMinutes;
        return (currentMinutes>=minutesToStop);
    }

}
