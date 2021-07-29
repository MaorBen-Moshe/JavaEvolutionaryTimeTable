package crossover;

import models.*;

import java.util.*;

public class DayTimeOrientedCrossover implements Crossover<TimeTable, TimeTableSystemDataSupplier> {
    private final int cuttingPoints;

    public DayTimeOrientedCrossover(int cuttingPoints){
        this.cuttingPoints = cuttingPoints;
    }

    @Override
    public Set<TimeTable> crossover(Map<TimeTable, Double> parents, TimeTableSystemDataSupplier supplier) {
        if(parents.size() != 2){
            throw new IllegalArgumentException("DayTimeOrientedCrossover expect only 2 parents");
        }

        TimeTable parent1 = CrossoverUtils.getParent(parents);
        TimeTable parent2 = CrossoverUtils.getParent(parents);
        while(parent2.equals(parent1)){
            parent2 = CrossoverUtils.getParent(parents);
        }

        Set<TimeTable> children = new HashSet<>();
        List<Integer> cuttingPoints = CrossoverUtils.getCuttingPoints(parent1, parent2, this.cuttingPoints);
        children.add(createChild(parent1, parent2, cuttingPoints, supplier));
        children.add(createChild(parent1, parent2, cuttingPoints, supplier));
        return children;
    }

    private TimeTable createChild(TimeTable parent1, TimeTable parent2, List<Integer> cuttingPoints, TimeTableSystemDataSupplier supplier){
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

    @Override
    public String toString() {
        return "DayTimeOrientedCrossover{" +
                "cuttingPoints=" + cuttingPoints +
                '}';
    }
}