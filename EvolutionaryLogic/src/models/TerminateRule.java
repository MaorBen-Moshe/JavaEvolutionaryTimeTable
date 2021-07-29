package models;

import evolutinary.EvolutionarySystem;

public abstract class TerminateRule {
    public EvolutionarySystem.TerminateRules getType() {
        return type;
    }

    private final EvolutionarySystem.TerminateRules type;

    public TerminateRule(EvolutionarySystem.TerminateRules type){
        this.type = type;
    }

    public abstract boolean isTerminate(Object... args);
}
