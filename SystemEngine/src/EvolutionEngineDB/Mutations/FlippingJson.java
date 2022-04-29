package EvolutionEngineDB.Mutations;

public class FlippingJson {
    private float probability;
    private int maxTupples;
    private char component;

    @Override
    public String toString() {
     return "FlippingJson{" +
                "probability=" + probability +
                ", maxTupples=" + maxTupples +
                ", component=" + component +
                '}';
    }

    public FlippingJson(float probability, int maxTupples, char component) {
        this.probability = probability;
        this.maxTupples = maxTupples;
        this.component = component;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    public int getMaxTupples() {
        return maxTupples;
    }

    public void setMaxTupples(int maxTupples) {
        this.maxTupples = maxTupples;
    }

    public char getComponent() {
        return component;
    }

    public void setComponent(char component) {
        this.component = component;
    }
}
