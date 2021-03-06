package mutation;

import models.*;
import utils.ItemCreationUtil;
import utils.RandomUtils;

import java.io.Serializable;
import java.util.*;

public class FlippingMutation extends MutationImpel<TimeTable, TimeTableSystemDataSupplier> implements Serializable {

    public enum Component {
        S, T, C, H, D
    }

    private final int maxTupples;
    private final Component component;

    public FlippingMutation(double probability, int maxTupples, Component component){
        super(MutationTypes.Flipping, probability);
        if(maxTupples <= 0){
            throw new IllegalArgumentException("Flipping mutation accept only positive number for maxTupples");
        }

        this.maxTupples = maxTupples;
        this.component = component;
    }

    @Override
    public void mutate(TimeTable child, TimeTableSystemDataSupplier supplier) {
        double probToMutate = RandomUtils.nextDouble();
        if(probability == 0 || probability < probToMutate){
            return;
        }

        int random;
        int i = 1;

        int numberOfChosen = 0;
        int itemsToGenerateNumber = RandomUtils.nextIntInRange(1, maxTupples);
        Set<TimeTableItem> itemsChosen = new HashSet<>();
        for(TimeTableItem item : child.getSortedItems()){
            random = RandomUtils.nextIntInRange(0, child.size() + 1);
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
                    chosen.setSchoolClass(ItemCreationUtil.getRandItem(classes));
                    break;
                case D:
                    randSize = RandomUtils.nextIntInRange(1, supplier.getDays());
                    chosen.setDay(randSize);
                    break;
                case H:
                    randSize = RandomUtils.nextIntInRange(1, supplier.getHours());
                    chosen.setHour(randSize);
                    break;
                case S:
                    Map<Integer, Subject> subjects = supplier.getSubjects();
                    chosen.setSubject(ItemCreationUtil.getRandItem(subjects));
                    break;
                case T:
                    Map<Integer, Teacher> teachers = supplier.getTeachers();
                    chosen.setTeacher(ItemCreationUtil.getRandItem(teachers));
                    break;
            }
        });
    }

    @Override
    public String toString() {
        return "Flipping Mutation { " +
                "probability=" + probability +
                ", maxTupples=" + maxTupples +
                ", component=" + component +
                " }";
    }

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
}