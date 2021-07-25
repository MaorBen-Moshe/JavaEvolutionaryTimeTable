package mutation;

import interfaces.Mutation;
import models.TimeTable;

public class FlippingMutation implements Mutation<TimeTable> {
    public enum Component {
        S, T, C, H, D
    }

    private final double probability;
    private final int maxTupples;
    private final Component component;

    public FlippingMutation(double probability, int maxTupples, Component component){
        this.maxTupples = maxTupples;
        this.component = component;
        this.probability = probability;
    }

    @Override
    public void mutate(TimeTable child) {

    }
}
