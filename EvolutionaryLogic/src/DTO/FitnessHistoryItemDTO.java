package DTO;
import java.util.Objects;

public class FitnessHistoryItemDTO<T> implements Comparable<FitnessHistoryItemDTO<T>>{
    private final int generationNumber;
    private final T solution;
    private final double currentGenerationFitness;
    private final double improvementFromLastGeneration; // zero if its generation number 0;

    public FitnessHistoryItemDTO(T solution, int genNumber, double currentFitness, double improvement){
        this.solution = solution;
        generationNumber = genNumber;
        currentGenerationFitness = currentFitness;
        improvementFromLastGeneration = improvement;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public double getCurrentGenerationFitness() {
        return currentGenerationFitness;
    }

    public double getImprovementFromLastGeneration() {
        return improvementFromLastGeneration;
    }

    public T getSolution() { return solution; }

    @Override
    public int compareTo(FitnessHistoryItemDTO o) {
        return Integer.compare(generationNumber, o.generationNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FitnessHistoryItemDTO<?> that = (FitnessHistoryItemDTO<?>) o;
        return generationNumber == that.generationNumber && Double.compare(that.currentGenerationFitness, currentGenerationFitness) == 0 && Double.compare(that.improvementFromLastGeneration, improvementFromLastGeneration) == 0 && solution.equals(that.solution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generationNumber, solution, currentGenerationFitness, improvementFromLastGeneration);
    }
}