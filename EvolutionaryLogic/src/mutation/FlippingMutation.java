package mutation;

import models.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class FlippingMutation implements Mutation<TimeTable, TimeTableSystemDataSupplier> {
    public enum Component {
        S, T, C, H, D
    }

    private final double probability;
    private final int maxTupples;
    private final Component component;
    private final Random rand;
    private TimeTableSystemDataSupplier supplier;

    public FlippingMutation(double probability, int maxTupples, Component component, TimeTableSystemDataSupplier supplier){
        this.maxTupples = maxTupples;
        this.component = component;
        this.probability = probability;
        rand = new Random();
        this.supplier = supplier;
    }

    @Override
    public void mutate(TimeTable child) {
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

            if(numberOfChosen >= maxTupples){
                break;
            }

            i++;
        }

        itemsChosen.forEach(chosen -> {
            int randSize = 0;
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

    private <T extends SerialItem> T getItem(Map<Integer, T> collection){
        int randSize = rand.nextInt(collection.size());
        return collection.get(randSize);
    }
}