package crossover;

import models.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

public class AspectOrientedCrossover implements Crossover<TimeTable, TimeTableSystemDataSupplier>, Serializable {
    public enum Orientation{
        CLASS, TEACHER
    }
    private final int cuttingPoints;
    private final Orientation orientation;

    public AspectOrientedCrossover(int cuttingPoints, Orientation orientation){
        this.cuttingPoints = cuttingPoints;
        this.orientation = orientation;
    }

    @Override
    public Set<TimeTable> crossover(List<TimeTable> parents, TimeTableSystemDataSupplier supplier) {
        Set<TimeTable> children = new HashSet<>();
        Map<Integer, ? extends SerialItem> items = null;
        switch (orientation){
            case TEACHER: items = supplier.getTeachers(); break;
            case CLASS: items = supplier.getClasses(); break;
        }

        TimeTable parent1 = CrossoverUtils.getParent(parents);
        TimeTable parent2 = CrossoverUtils.getParent(parents);
        while(parent2.equals(parent1)){
            parent2 = CrossoverUtils.getParent(parents);
        }

        TimeTable child1 = createChild(parent1, parent2, items, supplier);
        TimeTable child2 = createChild(parent2, parent1,items, supplier);
        children.add(child1);
        children.add(child2);
        return children;
    }

    @Override
    public String toString() {
        return "Aspect Oriented Crossover { " +
                "cuttingPoints=" + cuttingPoints +
                ", orientation=" + orientation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AspectOrientedCrossover that = (AspectOrientedCrossover) o;
        return cuttingPoints == that.cuttingPoints && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cuttingPoints, orientation);
    }

    private <T extends SerialItem> TimeTable createChild(TimeTable parent1, TimeTable parent2, Map<Integer, T> byAspectMap, TimeTableSystemDataSupplier supplier){
        Map<T, TimeTable> descendantsOfEachT = new HashMap<>();
        final int maxCuttingPoints = supplier.getDays() * supplier.getHours() * supplier.getClasses().size() * supplier.getTeachers().size() * supplier.getSubjects().size();
        byAspectMap.forEach((key, val) -> {
            List<Integer> cutting = CrossoverUtils.getCuttingPoints(this.cuttingPoints, maxCuttingPoints);
            TimeTable current = null;
            switch (orientation){
                case TEACHER: current = createTeacherChildHelper(parent1, parent2, cutting, key, supplier); break;
                case CLASS: current = createClassChildHelper(parent1, parent2, cutting, key, supplier); break;
            }

            if(!descendantsOfEachT.containsKey(val)){
                descendantsOfEachT.put(val, current);
            }
        });

        TimeTable childCreated = new TimeTable();
        descendantsOfEachT.forEach((key, val) -> val.getSortedItems().forEach(item -> {
            boolean notContains = !checkContainsByAspect(childCreated,
                    item.getDay(),
                    item.getHour(),
                    key.getId());
            if(notContains){
                childCreated.add(item);
            }
        }));

        return childCreated;
    }

    private TimeTable createTeacherChildHelper(TimeTable parent1, TimeTable parent2, List<Integer> cuttingPoints, int currentAspectId, TimeTableSystemDataSupplier supplier){
        TimeTable currentParent;
        TimeTable child = new TimeTable();
        boolean isParent1 = true;
        int count = 0;
        int currentCuttingPointPlace = 0; // the cell in the list of cutting points;

        for(int d = 1; d <= supplier.getDays(); d++){
            for(int h = 1; h <= supplier.getHours(); h++){
                for(int c = 1; c <= supplier.getClasses().size(); c++){
                    for(int s = 1; s <= supplier.getSubjects().size(); s++){
                        if((currentCuttingPointPlace < cuttingPoints.size()) &&
                                (count > cuttingPoints.get(currentCuttingPointPlace))){
                            currentCuttingPointPlace++;
                            isParent1 = !isParent1;
                        }

                        currentParent = isParent1 ? parent1 : parent2;
                        if(currentParent.contains(d, h, c, currentAspectId, s)){
                            Teacher teacher = supplier.getTeachers().get(currentAspectId);
                            SchoolClass klass = supplier.getClasses().get(c);
                            Subject subject = supplier.getSubjects().get(s);
                            child.add(new TimeTableItem(d, h, klass, teacher, subject));
                        }

                        count++;
                    }
                }
            }
        }

        return child;
    }

    private TimeTable createClassChildHelper(TimeTable parent1, TimeTable parent2, List<Integer> cuttingPoints, int currentAspectId, TimeTableSystemDataSupplier supplier){
        TimeTable currentParent;
        TimeTable child = new TimeTable();
        boolean isParent1 = true;
        int count = 0;
        int currentCuttingPointPlace = 0; // the cell in the list of cutting points;

        for(int d = 1; d <= supplier.getDays(); d++){
            for(int h = 1; h <= supplier.getHours(); h++){
                for(int t = 1; t <= supplier.getTeachers().size(); t++){
                    for(int s = 1; s <= supplier.getSubjects().size(); s++){
                        if((currentCuttingPointPlace < cuttingPoints.size()) &&
                                (count > cuttingPoints.get(currentCuttingPointPlace))){
                            currentCuttingPointPlace++;
                            isParent1 = !isParent1;
                        }

                        currentParent = isParent1 ? parent1 : parent2;
                        if(currentParent.contains(d, h, currentAspectId, t, s)){
                            Teacher teacher = supplier.getTeachers().get(t);
                            SchoolClass klass = supplier.getClasses().get(currentAspectId);
                            Subject subject = supplier.getSubjects().get(s);
                            child.add(new TimeTableItem(d, h, klass, teacher, subject));
                        }

                        count++;
                    }
                }
            }
        }

        return child;
    }

    private boolean checkContainsByAspect(TimeTable check, int day, int hour, int id){
        Predicate<TimeTableItem> predicate = testItem ->
                orientation.equals(Orientation.TEACHER) ? testItem.getTeacher().getId() == id :
                                                          testItem.getSchoolClass().getId() == id;


        return check.contains(day, hour, predicate);
    }
}