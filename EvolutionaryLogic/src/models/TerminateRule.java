package models;

import evolutinary.EvolutionarySystem;
import java.util.Objects;

public abstract class TerminateRule {
    private final EvolutionarySystem.TerminateRules type;

    public TerminateRule(EvolutionarySystem.TerminateRules type){
        this.type = type;
    }

    public abstract boolean isTerminate(Object... args);

    public EvolutionarySystem.TerminateRules getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerminateRule that = (TerminateRule) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "TerminateRule { " +
                "type = " + type +
                " }";
    }
}