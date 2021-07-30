package DTO;


import evolutinary.EvolutionarySystem;

import java.util.Objects;

public class FitnessTerminateRuleDTO extends TerminateRuleDTO{
    public double getFitness() {
        return fitness;
    }

    private final double fitness;

    public FitnessTerminateRuleDTO(double fitness){
        super(EvolutionarySystem.TerminateRules.ByFitness);
        this.fitness = fitness;
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
