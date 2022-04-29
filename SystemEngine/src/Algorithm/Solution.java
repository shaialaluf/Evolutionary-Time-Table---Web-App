package Algorithm;

abstract public class Solution {
    private float fitness;
    private float avgHardRules;
    private float avgSoftRules;
    private boolean isElitist=false;

    public boolean isElitist() {
        return isElitist;
    }

    public void setElitist(boolean elitist) {
        isElitist = elitist;
    }

    public void setAvgHardRules(float avgHardRules) {
        this.avgHardRules = avgHardRules;
    }

    public void setAvgSoftRules(float avgSoftRules) {
        this.avgSoftRules = avgSoftRules;
    }



    public float getFitness() {
        return fitness;
    }

    public float getAvgHardRules() {
        return avgHardRules;
    }

    public float getAvgSoftRules() {
        return avgSoftRules;
    }


    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
}
