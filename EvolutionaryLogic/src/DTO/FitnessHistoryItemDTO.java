package DTO;
import java.util.Objects;

public class FitnessHistoryItemDTO implements Comparable<FitnessHistoryItemDTO>{
    private final int generationNumber;
    private final double currentGenerationFitness;
    private final double improvementFromLastGeneration; // zero if its generation number 0;

    public FitnessHistoryItemDTO(int genNumber, double currentFitness, double improvement){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FitnessHistoryItemDTO that = (FitnessHistoryItemDTO) o;
        return generationNumber == that.generationNumber && Double.compare(that.currentGenerationFitness, currentGenerationFitness) == 0 && Double.compare(that.improvementFromLastGeneration, improvementFromLastGeneration) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(generationNumber, currentGenerationFitness, improvementFromLastGeneration);
    }

    @Override
    public int compareTo(FitnessHistoryItemDTO o) {
        return Integer.compare(generationNumber, o.generationNumber);
    }
}