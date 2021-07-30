package mutation;

import models.*;
import utils.ItemCreationUtil;
import utils.RandomUtils;

import java.util.*;

public class SizerMutation implements Mutation<TimeTable, TimeTableSystemDataSupplier> {
    private final double probability;
    private final int totalTupples;

    public SizerMutation(double probability, int totalTupples){
        this.totalTupples = totalTupples;
        this.probability = probability;
    }

    @Override
    public void mutate(TimeTable child, TimeTableSystemDataSupplier supplier) {
        double probToMutate = RandomUtils.nextDouble();
        if(probability == 0 || probability < probToMutate){
            return;
        }

        if(totalTupples < 0){
            int tupplesToChange = totalTupples * -1;
            tupplesToChange = RandomUtils.nextIntInRange(0, tupplesToChange);
            int minItemsToRemove = Math.max(tupplesToChange, supplier.getDays());
            int randomNumber;
            int i = 1;
            int numberOfRemoved = 0;
            List<TimeTableItem> temp = new ArrayList<>(child.getSortedItems());
            for(TimeTableItem item : temp){
                randomNumber = RandomUtils.nextIntInRange(0, child.size() + 1);
                if(randomNumber % (i + 1) == 0){
                    child.remove(item);
                    numberOfRemoved++;
                }

                if(numberOfRemoved >= minItemsToRemove){
                    break;
                }

                i++;
            }
        }
        else if(totalTupples > 0){
            int tupplesToChange = RandomUtils.nextIntInRange(0, totalTupples);
            int maxItemsToAdd = Math.min(supplier.getHours() * supplier.getDays(), tupplesToChange);
            for(int i = 0; i < maxItemsToAdd; i++){
                child.add(ItemCreationUtil.createItem(supplier));
            }
        }
    }

    @Override
    public String toString() {
        return "SizerMutation { " +
                "probability=" + probability +
                ", totalTupples=" + totalTupples +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SizerMutation that = (SizerMutation) o;
        return Double.compare(that.probability, probability) == 0 && totalTupples == that.totalTupples;
    }

    @Override
    public int hashCode() {
        return Objects.hash(probability, totalTupples);
    }
}