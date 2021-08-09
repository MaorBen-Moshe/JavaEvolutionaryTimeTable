package selection;

import models.TimeTable;

import java.io.Serializable;
import java.util.Map;

public class RouletteWheelSelection implements Selection<TimeTable>, Serializable {
    @Override
    public Map<TimeTable, Double> select(Map<TimeTable, Double> population, Map<TimeTable, Double> elita) {
        return null;
    }
}