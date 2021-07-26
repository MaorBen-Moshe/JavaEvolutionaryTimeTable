package crossover;

import interfaces.Crossover;
import models.TimeTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class AspectOrientedCrossover implements Crossover<TimeTable> {
    public enum Orientation{
        Class, Teacher
    }
    private final int cuttingPoints;
    private Orientation orientation;

    public AspectOrientedCrossover(int cuttingPoints, Orientation orientation){
        this.cuttingPoints = cuttingPoints;
        this.orientation = orientation;
    }

    @Override
    public Set<TimeTable> crossOver(Map<TimeTable, Double> parents) {
        return null;
    }
}
