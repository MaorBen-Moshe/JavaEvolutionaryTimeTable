package mutation;

import interfaces.Mutation;
import models.TimeTable;
import models.TimeTableItem;

import java.util.HashSet;
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

        itemsChosen.forEach(chosen -> {

            switch(component){
                case C: break;
                case D: break;
                case H: break;
                case S: break;
                case T: break;
            }
        });
    }
}
