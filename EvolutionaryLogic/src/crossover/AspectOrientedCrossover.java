package crossover;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.Map;
import java.util.Set;

public class AspectOrientedCrossover implements Crossover<TimeTable> {
    public enum Orientation{
        Class, Teacher
    }
    private final int cuttingPoints;
    private Orientation orientation;
    private final TimeTableSystemDataSupplier supplier;

    public AspectOrientedCrossover(int cuttingPoints, Orientation orientation, TimeTableSystemDataSupplier supplier){
        this.cuttingPoints = cuttingPoints;
        this.orientation = orientation;
        this.supplier = supplier;
    }

    @Override
    public Set<TimeTable> crossover(Map<TimeTable, Double> parents) {
        return null;
    }
}
