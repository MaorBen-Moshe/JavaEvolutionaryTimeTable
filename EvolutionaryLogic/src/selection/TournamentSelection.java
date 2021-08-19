package selection;

import models.TimeTable;
import utils.RandomUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class TournamentSelection extends SelectionImpel<TimeTable> implements Serializable {
    private float pte;

    public TournamentSelection(float pte){
        super(SelectionTypes.Tournament);
        setPte(pte);
    }

    public float getPte() {
        return pte;
    }

    public void setPte(float pte){
        if(!(pte >= 0 && pte <= 1)){
            throw new IllegalArgumentException("pte argument in tournament selection must be positive number between 0-1");
        }

        this.pte = pte;
    }

    @Override
    public List<TimeTable> select(Map<TimeTable, Double> population) {
        List<TimeTable> retVal = new ArrayList<>();
        IntStream.range(0, population.size()).forEach(i ->{
            Map.Entry<TimeTable, Double> parent1 = randomizeParent(population);
            Map.Entry<TimeTable, Double> parent2 = randomizeParent(population);
            double randomNum = RandomUtils.nextDouble();
            TimeTable answer;
            if(randomNum > this.pte){
                answer = getMaxParent(parent1, parent2);
            }
            else{
                answer = getMinParent(parent1, parent2);
            }

            retVal.add(answer);
        });

        return retVal;
    }

    private TimeTable getMinParent(Map.Entry<TimeTable, Double> parent1, Map.Entry<TimeTable, Double> parent2) {
        return getParentHelper(parent1, parent2, () -> Math.min(parent1.getValue(), parent2.getValue()) == parent1.getValue());
    }

    private TimeTable getMaxParent(Map.Entry<TimeTable, Double> parent1, Map.Entry<TimeTable, Double> parent2) {
        return getParentHelper(parent1, parent2, () -> Math.max(parent1.getValue(), parent2.getValue()) == parent1.getValue());
    }

    private TimeTable getParentHelper(Map.Entry<TimeTable, Double> parent1, Map.Entry<TimeTable, Double> parent2, Supplier<Boolean> predicate){
        TimeTable ret;
        if(predicate.get()){
            ret = parent1.getKey();
        }
        else{
            ret = parent2.getKey();
        }

        return ret;
    }

    private Map.Entry<TimeTable, Double> randomizeParent(Map<TimeTable, Double> population) {
        int rand = RandomUtils.nextIntInRange(1, population.size());
        int i = 1;
        Map.Entry<TimeTable, Double> ret = null;
        for(Map.Entry<TimeTable, Double> entry : population.entrySet()){
            if(i == rand){
                ret = entry;
                break;
            }

            i++;
        }

        return ret;
    }

    @Override
    public String toString() {
        return "Tournament Selection { " +
                "pte=" + pte +
                " } ";
    }
}