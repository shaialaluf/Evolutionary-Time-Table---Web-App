package Algorithm.FinishConditions;

public class FinishByGenerationsNumber implements FinishCondition {
    private int maxGenerations;
    private int currentGenerationNumber;

    public FinishByGenerationsNumber(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public int getCurrentGenerationNumber() {
        return currentGenerationNumber;
    }

    @Override
    public boolean isFinish(float currentGenerationNumber) {
        int currGeneration=(int)currentGenerationNumber;
        this.currentGenerationNumber=currGeneration;
        return maxGenerations == currGeneration;
    }

    @Override
    public String toString() {
        return "FinishByGenerationsNumber{" +
                "maxGenerations=" + maxGenerations +
                '}';
    }
}
