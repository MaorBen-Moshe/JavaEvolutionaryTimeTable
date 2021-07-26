package crossover;

import interfaces.Crossover;
import models.TimeTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DayTimeOrientedCrossover implements Crossover<TimeTable> {
    private final int cuttingPoints;

    public DayTimeOrientedCrossover(int cuttingPoints){
        this.cuttingPoints = cuttingPoints;
    }

    @Override
    public Set<TimeTable> crossOver(Map<TimeTable, Double> parents) {
        return null;
    }
}
