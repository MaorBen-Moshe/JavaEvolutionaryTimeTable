package crossover;

import models.TimeTable;
import utils.RandomUtils;

import java.util.*;

public class CrossoverUtils {
    public static TimeTable getParent(Map<TimeTable, Double> parents){
        final int randomNumber = RandomUtils.nextIntInRange(1, parents.size());
        TimeTable retVal = null;
        int i = 1;
        for(TimeTable timeTable : parents.keySet()){
            if(i == randomNumber){
                retVal = timeTable;
                break;
            }

            i++;
        }

        return retVal;
    }

    public static List<Integer> getCuttingPoints(TimeTable parent1, TimeTable parent2, int cuttingPointsCount){
        List<Integer> cuttingPoints = new ArrayList<>();
        int maxCuttingOption = Math.max(parent1.size(), parent2.size());
        while(cuttingPoints.size() < cuttingPointsCount){
            int current = RandomUtils.nextIntInRange(0, maxCuttingOption);
            if(!cuttingPoints.contains(current)){
                cuttingPoints.add(current);
            }
        }

        cuttingPoints.sort(Comparator.naturalOrder());
        return cuttingPoints;
    }
}
