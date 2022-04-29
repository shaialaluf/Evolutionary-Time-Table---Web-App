package Algorithm.FinishConditions;

public class FinishByFitness implements FinishCondition {
    private float fitnessToStop;
    private float currentFitness;

    public FinishByFitness(float fitnessToStop) {
        this.fitnessToStop = fitnessToStop;
    }

    public float getFitnessToStop() {
        return fitnessToStop;
    }

    public float getCurrentFitness() {
        return currentFitness;
    }

    @Override
    public String toString() {
        return "FinishByFitness{" +
                "fitnessToStop=" + fitnessToStop ;
    }

    @Override
    public boolean isFinish(float currentFitness) {
        this.currentFitness=currentFitness;
        return (currentFitness>=fitnessToStop);
    }
}
