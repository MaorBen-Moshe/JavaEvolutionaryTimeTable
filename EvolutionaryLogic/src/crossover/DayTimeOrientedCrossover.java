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
        TimeTable parent1 = CrossoverUtils.getParent(parents);
        TimeTable parent2 = CrossoverUtils.getParent(parents);
        while(parent2 == null || parent2.equals(parent1)){
            parent2 = CrossoverUtils.getParent(parents);
        }

        Set<TimeTable> children = new HashSet<>();
        TimeTable child1 = createChild(parent1, parent2, supplier);
        TimeTable child2 = createChild(parent1, parent2, supplier);
        /*while(child2.equals(child1)){
            child2 = createChild(parent1, parent2, supplier);
        }*/

        children.add(child1);
        children.add(child2);
        return children;
    }

    @Override
    public String toString() {
        return "DayTimeOrientedCrossover { " +
                "cuttingPoints=" + cuttingPoints +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayTimeOrientedCrossover that = (DayTimeOrientedCrossover) o;
        return cuttingPoints == that.cuttingPoints;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuttingPoints);
    }

    private TimeTable createChild(TimeTable parent1, TimeTable parent2, TimeTableSystemDataSupplier supplier){
        TimeTable currentParent;
        TimeTable child = new TimeTable();
        List<Integer> cuttingPoints = CrossoverUtils.getCuttingPoints(parent1, parent2, this.cuttingPoints);
        boolean isParent1 = true;
        int count = 0;
        int currentCuttingPointPlace = 0; // the cell in the list of cutting points;

        for(int d = 1; d <= supplier.getDays(); d++){
            for(int h = 1; h <= supplier.getHours(); h++){
                for(int c = 1; c <= supplier.getClasses().size(); c++){
                    for(int t = 1; t <= supplier.getTeachers().size(); t++){
                        for(int s = 1; s <= supplier.getSubjects().size(); s++){
                            if(currentCuttingPointPlace < cuttingPoints.size() &&
                                    count > cuttingPoints.get(currentCuttingPointPlace)){
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
}