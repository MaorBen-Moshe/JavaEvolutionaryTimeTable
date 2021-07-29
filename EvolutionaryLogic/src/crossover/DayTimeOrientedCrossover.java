package crossover;

import models.*;

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
        if(parents.size() != 2){
            throw new IllegalArgumentException("DayTimeOrientedCrossover expect only 2 parents");
        }

        TimeTable parent1 = getParent(parents);
        TimeTable parent2 = getParent(parents);
        Set<TimeTable> children = new HashSet<>();
        List<Integer> cuttingPoints = getCuttingPoints(parent1, parent2);
        children.add(createChild(parent1, parent2, cuttingPoints));
        children.add(createChild(parent1, parent2, cuttingPoints));
        return children;
    }

    private TimeTable createChild(TimeTable parent1, TimeTable parent2, List<Integer> cuttingPoints){
        TimeTable currentParent;
        TimeTable child = new TimeTable();
        boolean isParent1 = true;
        int count = 0;
        int currentCuttingPointPlace = 0; // the cell in the list of cutting points;

        for(int d = 0; d < supplier.getDays(); d++){
            for(int h = 0; h < supplier.getHours(); h++){
                for(int c = 1; c <= supplier.getClasses().size(); c++){
                    for(int t = 1; t <= supplier.getTeachers().size(); t++){
                        for(int s = 1; s <= supplier.getSubjects().size(); s++){
                            if(count > cuttingPoints.get(currentCuttingPointPlace)){
                                currentCuttingPointPlace++;
                                isParent1 = !isParent1;
                            }

                            currentParent = isParent1 ? parent1 : parent2;
                            if(currentParent.contains(d, h, c, t, s)){
                                Teacher teacher = supplier.getTeachers().get(t);
                                SchoolClass klass = supplier.getClasses().get(c);
                                Subject subject = supplier.getSubjects().get(s);
                                child.add(new TimeTableItem(d, h, klass, teacher, subject));
                            }

                            count++;
                        }
                    }
                }
            }
        }

        return child;
    }

    private List<Integer> getCuttingPoints(TimeTable parent1, TimeTable parent2){
        List<Integer> cuttingPoints = new ArrayList<>();
        int maxCuttingOption = Math.max(parent1.size(), parent2.size());
        while(cuttingPoints.size() < this.cuttingPoints){
            int current = rand.nextInt(maxCuttingOption);
            if(!cuttingPoints.contains(current)){
                cuttingPoints.add(current);
            }
        }

        cuttingPoints.sort(Comparator.naturalOrder());
        return cuttingPoints;
    }

    private TimeTable getParent(Map<TimeTable, Double> parents){
        final int randNumber = rand.nextInt(parents.size());
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