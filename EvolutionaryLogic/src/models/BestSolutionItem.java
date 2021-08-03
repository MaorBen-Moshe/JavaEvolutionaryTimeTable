package models;

import Interfaces.DataSupplier;

import java.util.Objects;

public class BestSolutionItem<T, S extends DataSupplier> {
    private S supplier;
    private double fitness;
    private int generationCreated;
    private T solution;

    public BestSolutionItem(T table, double fitness, int generationCreated, S supplier){
        setSolution(table);
        setFitness(fitness);
        setSupplier(supplier);
    }

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

    private void setSupplier(S supplier) {
        this.supplier = supplier;
    }

    public int getGenerationCreated() {
        return generationCreated;
    }

    public void setGenerationCreated(int generationCreated) {
        this.generationCreated = generationCreated;
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
