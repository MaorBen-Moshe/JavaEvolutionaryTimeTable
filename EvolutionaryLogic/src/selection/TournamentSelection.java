package selection;

import interfaces.Selection;
import models.TimeTable;

import java.util.List;

public class TournamentSelection implements Selection<TimeTable> {
    private final float pte;

    public TournamentSelection(float pte){
        this.pte = pte;
    }

    @Override
    public List<TimeTable> select(List<TimeTable> population) {
        return null;
    }

    public float getPte() {
        return pte;
    }
}
