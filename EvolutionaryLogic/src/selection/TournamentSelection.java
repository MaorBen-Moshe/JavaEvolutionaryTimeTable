package selection;

import models.TimeTable;

import java.util.Map;

public class TournamentSelection implements Selection<TimeTable> {
    private final float pte;

    public TournamentSelection(float pte){
        this.pte = pte;
    }

    @Override
    public Map<TimeTable, Double> select(Map<TimeTable, Double> population) {
        return null;
    }
}