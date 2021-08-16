package DTO;

import evolutinary.EvolutionarySystem;
import java.util.Objects;

public class FitnessTerminateRuleDTO extends TerminateRuleDTO{
    private final double fitness;

    public FitnessTerminateRuleDTO(double fitness){
        super(EvolutionarySystem.TerminateRules.ByFitness);
        if(fitness < 0 || fitness > 100){
            throw new IllegalArgumentException("Fitness should be a float between 0-100");
        }

        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FitnessTerminateRuleDTO that = (FitnessTerminateRuleDTO) o;
        return fitness == that.fitness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fitness);
    }
}