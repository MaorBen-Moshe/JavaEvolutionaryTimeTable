package crossover;

import models.TimeTable;

import java.util.*;

public class CrossoverUtils {
    static Random rand;

    static {
        rand = new Random();
    }

    static TimeTable getParent(Map<TimeTable, Double> parents){
        final int randomNumber = rand.nextInt(parents.size());
        TimeTable retVal = null;
        int i = 0;
        for(TimeTable timeTable : parents.keySet()){
            if(i == randomNumber){
                retVal = timeTable;
                break;
            }

            i++;
        }

        return retVal;
    }

    static List<Integer> getCuttingPoints(TimeTable parent1, TimeTable parent2, int cuttingPointsCount){
        List<Integer> cuttingPoints = new ArrayList<>();
        int maxCuttingOption = Math.max(parent1.size(), parent2.size());
        while(cuttingPoints.size() < cuttingPointsCount){
            int current = rand.nextInt(maxCuttingOption);
            if(!cuttingPoints.contains(current)){
                cuttingPoints.add(current);
            }
        }

        cuttingPoints.sort(Comparator.naturalOrder());
        return cuttingPoints;
    }
}
