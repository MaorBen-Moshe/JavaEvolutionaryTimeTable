package utils.infoModels;

public class MutationInfo {

    private String type;
    private double probability;
    private int tupples;
    private String component;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getTupples() {
        return tupples;
    }

    public void setTupples(int tupples) {
        this.tupples = tupples;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
