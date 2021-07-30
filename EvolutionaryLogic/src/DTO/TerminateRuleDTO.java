package DTO;

import evolutinary.EvolutionarySystem;

import java.util.Objects;

public abstract class TerminateRuleDTO {
    public EvolutionarySystem.TerminateRules getType() {
        return type;
    }

    private final EvolutionarySystem.TerminateRules type;

    public TerminateRuleDTO(EvolutionarySystem.TerminateRules type){
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminateRuleDTO that = (TerminateRuleDTO) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}