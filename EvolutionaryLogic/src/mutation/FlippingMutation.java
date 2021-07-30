package mutation;

import models.*;

import java.util.*;

public class FlippingMutation implements Mutation<TimeTable, TimeTableSystemDataSupplier> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlippingMutation that = (FlippingMutation) o;
        return Double.compare(that.probability, probability) == 0 && maxTupples == that.maxTupples && component == that.component;
    }

    @Override
    public int hashCode() {
        return Objects.hash(probability, maxTupples, component);
    }

    public enum Component {
        S, T, C, H, D
    }

    private final double probability;
    private final int maxTupples;
    private final Component component;
    private final Random rand;

    public FlippingMutation(double probability, int maxTupples, Component component){
        this.maxTupples = maxTupples;
        this.component = component;
        this.probability = probability;
        rand = new Random();
    }

    @Override
    public void mutate(TimeTable child, TimeTableSystemDataSupplier supplier) {
        double probToMutate = rand.nextDouble();
        if(probability == 0 || probability < probToMutate){
            return;
        }

        int random;
        int i = 1;

        int numberOfChosen = 0;
        int itemsToGenerateNumber = rand.nextInt(maxTupples);
        Set<TimeTableItem> itemsChosen = new HashSet<>();
        for(TimeTableItem item : child.getSortedItems()){
            random = rand.nextInt(child.size() + 1);
            if(random % (i + 1) == 0){
                itemsChosen.add(item);
                numberOfChosen++;
            }

            if(numberOfChosen >= itemsToGenerateNumber){
                break;
            }

            i++;
        }

        itemsChosen.forEach(chosen -> {
            int randSize;
            switch(component){
                case C:
                    Map<Integer, SchoolClass> classes = supplier.getClasses();
                    chosen.setSchoolClass(getItem(classes));
                    break;
                case D:
                    randSize = rand.nextInt(supplier.getDays()) - 1;
                    chosen.setDay(randSize);
                    break;
                case H:
                    randSize = rand.nextInt(supplier.getHours()) - 1;
                    chosen.setHour(randSize);
                    break;
                case S:
                    Map<Integer, Subject> subjects = supplier.getSubjects();
                    chosen.setSubject(getItem(subjects));
                    break;
                case T:
                    Map<Integer, Teacher> teachers = supplier.getTeachers();
                    chosen.setTeacher(getItem(teachers));
                    break;
            }
        });
    }

    @Override
    public String toString() {
        return "FlippingMutation { " +
                "probability=" + probability +
                ", maxTupples=" + maxTupples +
                ", component=" + component +
                " }";
    }

    private <T extends SerialItem> T getItem(Map<Integer, T> collection){
        int randSize = rand.nextInt(collection.size());
        return collection.get(randSize);
    }
}