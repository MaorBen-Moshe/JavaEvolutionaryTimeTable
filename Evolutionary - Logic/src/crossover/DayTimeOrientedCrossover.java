package crossover;

import interfaces.Crossover;
import models.TimeTable;

import java.util.List;

public class DayTimeOrientedCrossover implements Crossover<TimeTable> {
    private final int cuttingPoints;

    public DayTimeOrientedCrossover(int cuttingPoints){
        this.cuttingPoints = cuttingPoints;
    }

    @Override
    public List<TimeTable> crossOver(List<TimeTable> parents) {
        return null;
    }
}
