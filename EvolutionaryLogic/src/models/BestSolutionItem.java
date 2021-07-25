package models;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BestSolutionItem<T> {
    public double getFitness() {
        return fitness;
    }

    private void setFitness(double fitness) {
        if(fitness < 0 || fitness > 100){
            throw new IllegalArgumentException("fitness should be an integer between 0 - 100");
        }

        this.fitness = fitness;
    }

    private double fitness;

    public T getSolution() {
        return solution;
    }

    private void setSolution(@NotNull T solution) {
        this.solution = solution;
    }

    private T solution;

    private Map<Rules.eRules, Double> rulesScore;

    public BestSolutionItem(T table, double fitness){
        setSolution(table);
        setFitness(fitness);
        rulesScore = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestSolutionItem<?> that = (BestSolutionItem<?>) o;
        return fitness == that.fitness && solution.equals(that.solution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fitness, solution);
    }

    public void addRuleScore(Rules.eRules rule, double score){
        if(rulesScore.containsKey(rule)){
            rulesScore.replace(rule, score);
        }
        else{
            rulesScore.put(rule, score);
        }
    }
}
