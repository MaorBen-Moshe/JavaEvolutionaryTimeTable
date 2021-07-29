package DTO;

import evolutinary.EvolutionarySystem;

public abstract class TerminateRuleDTO {
    public EvolutionarySystem.TerminateRules getType() {
        return type;
    }

    private final EvolutionarySystem.TerminateRules type;

    public TerminateRuleDTO(EvolutionarySystem.TerminateRules type){
        this.type = type;
    }
}