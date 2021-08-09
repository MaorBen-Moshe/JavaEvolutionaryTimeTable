package selection;

import models.TimeTable;
import utils.RandomUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class TournamentSelection implements Selection<TimeTable>, Serializable {
    private final float pte;

    public TournamentSelection(float pte){
        if(!(pte >= 0 && pte <= 1)){
            throw new IllegalArgumentException("pte argument in tournament selection must be positive number between 0-1");
        }

        this.pte = pte;
    }

    @Override
    public Map<TimeTable, Double> select(Map<TimeTable, Double> population, Map<TimeTable, Double> elita) {
        Map<TimeTable, Double> retVal = new HashMap<>(elita);
        if(!(retVal.size() >= population.size())){
            IntStream.range(0, population.size()).forEach(i ->{
                Map.Entry<TimeTable, Double> parent1 = randomizeParent(population);
                Map.Entry<TimeTable, Double> parent2 = randomizeParent(population);
                double randomNum = RandomUtils.nextDouble();
                Map.Entry<TimeTable, Double> answer;
                if(randomNum > this.pte){
                    answer = getMaxParent(parent1, parent2);
                }
                else{
                    answer = getMinParent(parent1, parent2);
                }

                retVal.put(answer.getKey(), answer.getValue());
            });
        }

        return retVal;
    }

    private Map.Entry<TimeTable, Double> getMinParent(Map.Entry<TimeTable, Double> parent1, Map.Entry<TimeTable, Double> parent2) {
        return getParentHelper(parent1, parent2, () -> Math.min(parent1.getValue(), parent2.getValue()) == parent1.getValue());
    }

    private Map.Entry<TimeTable, Double> getMaxParent(Map.Entry<TimeTable, Double> parent1, Map.Entry<TimeTable, Double> parent2) {
        return getParentHelper(parent1, parent2, () -> Math.max(parent1.getValue(), parent2.getValue()) == parent1.getValue());
    }

    private Map.Entry<TimeTable, Double> getParentHelper(Map.Entry<TimeTable, Double> parent1, Map.Entry<TimeTable, Double> parent2, Supplier<Boolean> predicate){
        Map.Entry<TimeTable, Double> ret;
        if(predicate.get()){
            ret = parent1;
        }
        else{
            ret = parent2;
        }

        return ret;
    }

    private Map.Entry<TimeTable, Double> randomizeParent(Map<TimeTable, Double> population) {
        int rand = RandomUtils.nextIntInRange(0, population.size());
        int i = 0;
        Map.Entry<TimeTable, Double> ret = null;
        for(Map.Entry<TimeTable, Double> entry : population.entrySet()){
            if(i == rand){
                ret = entry;
                break;
            }
        }

        return ret;
    }
}