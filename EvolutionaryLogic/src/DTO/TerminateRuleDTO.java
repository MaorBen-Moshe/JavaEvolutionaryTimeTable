package DTO;

import evolutinary.EvolutionarySystem;

public class TerminateRuleDTO {
    public EvolutionarySystem.TerminateRules getRule() {
        return rule;
    }

    public int getVal() {
        return val;
    }

    private final EvolutionarySystem.TerminateRules rule;
    private final int val;

    public TerminateRuleDTO(EvolutionarySystem.TerminateRules rule, int val) {
        this.rule = rule;
        this.val = val;
    }
}
