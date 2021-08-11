package selection;

import models.TimeTable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RouletteWheelSelection implements Selection<TimeTable>, Serializable {
    @Override
    public List<TimeTable> select(Map<TimeTable, Double> population) {
        return null;
    }
}