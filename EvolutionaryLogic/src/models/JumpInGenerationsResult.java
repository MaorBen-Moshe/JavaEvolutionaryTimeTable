package models;

import java.util.Objects;

public class JumpInGenerationsResult {
    private final double fitness;
    private final int numberOfGeneration;
    private final double timePassed;

    public JumpInGenerationsResult(double fitness, int numberOfGeneration, double timePassed) {
        this.fitness = fitness;
        this.numberOfGeneration = numberOfGeneration;
        this.timePassed = timePassed;
    }

    public double getFitness() {
        return fitness;
    }

    public double getTimePassed() {
        return timePassed;
    }

    public int getNumberOfGeneration() {
        return numberOfGeneration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JumpInGenerationsResult that = (JumpInGenerationsResult) o;
        return Double.compare(that.fitness, fitness) == 0 && numberOfGeneration == that.numberOfGeneration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fitness, numberOfGeneration);
    }
}