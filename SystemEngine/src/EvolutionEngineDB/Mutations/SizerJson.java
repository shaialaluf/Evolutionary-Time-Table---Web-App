package EvolutionEngineDB.Mutations;

public class SizerJson {
    private int totalTupples;
    private float probability;

    @Override
    public String toString() {
        return "SizerJson{" +
                "totalTupples=" + totalTupples +
                ", probability=" + probability +
                '}';
    }

    public SizerJson(int totalTupples, float probability) {
        this.totalTupples = totalTupples;
        this.probability = probability;
    }

    public int getTotalTupples() {
        return totalTupples;
    }

    public void setTotalTupples(int totalTupples) {
        this.totalTupples = totalTupples;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }
}
