package selection;

import interfaces.Selection;
import models.TimeTable;

import java.util.List;

public class TruncationSelection implements Selection<TimeTable> {
    private final int topPercent;

    public TruncationSelection(int topPercent){
        this.topPercent = topPercent;
    }

    @Override
    public List<TimeTable> select(List<TimeTable> population) {
        return null;
    }

    public int getTopPercent() {
        return topPercent;
    }
}
