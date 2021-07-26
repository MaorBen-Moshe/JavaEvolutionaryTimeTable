package mutation;

import evolutinary.TimeTableEvolutionarySystemImpel;
import interfaces.Mutation;
import models.*;
import utils.SystemUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class FlippingMutation implements Mutation<TimeTable> {
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

        SystemUtils instance = SystemUtils.getInstance();
        itemsChosen.forEach(chosen -> {
            int randSize = 0;
            switch(component){
                case C:
                    Map<Integer, SchoolClass> classes = instance.getClasses();
                    chosen.setSchoolClass(getItem(classes));
                    break;
                case D:
                    randSize = rand.nextInt(instance.getDays()) - 1;
                    chosen.setDay(randSize);
                    break;
                case H:
                    randSize = rand.nextInt(instance.geHours()) - 1;
                    chosen.setHour(randSize);
                    break;
                case S:
                    Map<Integer, Subject> subjects = instance.getSubjects();
                    chosen.setSubject(getItem(subjects));
                    break;
                case T:
                    Map<Integer, Teacher> teachers = instance.getTeachers();
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