package mutation;

import models.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SizerMutation implements Mutation<TimeTable> {
    private final double probability;
    private final int totalTupples;
    private final Random rand;
    private final TimeTableSystemDataSupplier supplier;

    public SizerMutation(double probability, int totalTupples, TimeTableSystemDataSupplier supplier){
        this.totalTupples = totalTupples;
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

        if(totalTupples < 0){
            int tupplesToChange = totalTupples * -1;
            tupplesToChange = rand.nextInt(tupplesToChange);
            int minItemsToRemove = Math.max(tupplesToChange, supplier.getDays());
            int randomNumber;
            int i = 1;
            int numberOfRemoved = 0;
            Set<TimeTableItem> temp = new HashSet<>(child.getSortedItems());
            for(TimeTableItem item : temp){
                randomNumber = rand.nextInt(child.size() + 1);
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
            int tupplesToChange = rand.nextInt(totalTupples);
            int maxItemsToAdd = Math.min(supplier.getHours() * supplier.getDays(), tupplesToChange);
            int i = 0;
            while(i < maxItemsToAdd){
                if(child.add(createItem())){
                    i++;
                }
            }
        }
    }

    private TimeTableItem createItem(){
        int daySelected, hourSelected;
        Teacher teacherSelected;
        Subject subjectSelected;
        SchoolClass classSelected;

        daySelected = rand.nextInt(supplier.getDays());
        hourSelected = rand.nextInt(supplier.getHours());
        teacherSelected = getRandItem(supplier.getTeachers());
        subjectSelected = getRandItem(supplier.getSubjects());
        classSelected = getRandItem(supplier.getClasses());
        return new TimeTableItem(daySelected, hourSelected, classSelected, teacherSelected, subjectSelected);
    }

    private <T extends SerialItem> T getRandItem(Map<Integer, T> collection){
        int randInt = rand.nextInt(collection.size());
        return collection.get(randInt);
    }
}