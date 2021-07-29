package models;

import Interfaces.DataSupplier;

import java.util.Objects;

public class BestSolutionItem<T, S extends DataSupplier> {
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

    public S getSupplier() {
        return supplier;
    }

    private void setSolution(T solution) {
        this.solution = solution;
    }

    private double fitness;
    private T solution;

    private void setSupplier(S supplier) {
        this.supplier = supplier;
    }

    private S supplier;

    public BestSolutionItem(T table, double fitness, S supplier){
        setSolution(table);
        setFitness(fitness);
        setSupplier(supplier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BestSolutionItem<?, ?> that = (BestSolutionItem<?, ?>) o;
        return Double.compare(that.fitness, fitness) == 0 && solution.equals(that.solution) && supplier.equals(that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fitness, solution, supplier);
    }
}
