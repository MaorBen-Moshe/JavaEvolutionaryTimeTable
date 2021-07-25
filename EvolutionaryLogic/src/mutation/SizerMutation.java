package mutation;

import interfaces.Mutation;
import models.TimeTable;

public class SizerMutation implements Mutation<TimeTable> {
    private final double probability;
    private final int totalTupples;

    public SizerMutation(double probability, int totalTupples){
        this.totalTupples = totalTupples;
        this.probability = probability;
    }

    @Override
    public void mutate(TimeTable child) {

    }
}
