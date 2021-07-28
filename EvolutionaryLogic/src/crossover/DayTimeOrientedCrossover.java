package crossover;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.*;

public class DayTimeOrientedCrossover implements Crossover<TimeTable> {
    private final int cuttingPoints;
    private final Random rand;
    private final TimeTableSystemDataSupplier supplier;

    public DayTimeOrientedCrossover(int cuttingPoints, TimeTableSystemDataSupplier supplier){
        rand = new Random();
        this.cuttingPoints = cuttingPoints;
        this.supplier = supplier;
    }

    @Override
    public Set<TimeTable> crossover(Map<TimeTable, Double> parents) {
        TimeTable parent1 = getParent(parents);
        TimeTable parent2 = getParent(parents);
        Set<TimeTable> children = new HashSet<>();
        Set<Integer> cuttingPoints = new TreeSet<>();
        int maxCuttingOption = Math.max(parent1.size(), parent2.size());
        while(cuttingPoints.size() >= this.cuttingPoints){
            cuttingPoints.add(rand.nextInt(maxCuttingOption));
        }

        // arrange the two parents in a table of size DH
        // create two children by cutting points
        // return children
        return children;
    }

    private TimeTable getParent(Map<TimeTable, Double> parents){
        int randNumber = rand.nextInt(parents.size());
        TimeTable ret = null;
        int i = 0;
        for(TimeTable timeTable : parents.keySet()){
            if(i == randNumber){
                ret = timeTable;
                break;
            }

            i++;
        }

        return ret;
    }
}
