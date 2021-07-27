package models;

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

    public T getSolution() {
        return solution;
    }

    private void setSolution(T solution) {
        this.solution = solution;
    }

    private double fitness;
    private T solution;

    public BestSolutionItem(T table, double fitness){
        setSolution(table);
        setFitness(fitness);
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
}
