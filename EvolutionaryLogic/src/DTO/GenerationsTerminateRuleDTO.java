package DTO;


import evolutinary.EvolutionarySystem;

import java.util.Objects;

public class GenerationsTerminateRuleDTO extends TerminateRuleDTO {
    private final int generations;

    public GenerationsTerminateRuleDTO(int generations){
        super(EvolutionarySystem.TerminateRules.NumberOfGenerations);
        this.generations = generations;
    }

    public int getGenerations() {
        return generations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenerationsTerminateRuleDTO that = (GenerationsTerminateRuleDTO) o;
        return generations == that.generations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), generations);
    }
}
