package Problems;

public class CurrentResult {
    private int generation;
    private float fitness;
    private boolean isLast=false;

    public CurrentResult(int generation, float fitness) {
        this.generation = generation;
        this.fitness = fitness;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isLast() {
        return isLast;
    }

    public CurrentResult() {
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }


}
