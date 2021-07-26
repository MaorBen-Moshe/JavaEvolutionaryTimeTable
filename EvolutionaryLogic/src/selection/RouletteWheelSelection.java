package selection;

import interfaces.Selection;
import models.TimeTable;

import java.util.List;
import java.util.Map;

public class RouletteWheelSelection implements Selection<TimeTable> {
    @Override
    public Map<TimeTable, Double> select(Map<TimeTable, Double> population) {
        return null;
    }
}
