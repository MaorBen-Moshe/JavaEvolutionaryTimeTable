package mutation;

import models.*;

import java.util.*;

public class SizerMutation implements Mutation<TimeTable, TimeTableSystemDataSupplier> {
    private final double probability;
    private final int totalTupples;
    private final Random rand;

    public SizerMutation(double probability, int totalTupples){
        this.totalTupples = totalTupples;
        this.probability = probability;
        rand = new Random();
    }

    @Override
    public void mutate(TimeTable child, TimeTableSystemDataSupplier supplier) {
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
                if(child.add(createItem(supplier))){
                    i++;
                }
            }
        }
    }

    private TimeTableItem createItem(TimeTableSystemDataSupplier supplier){
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