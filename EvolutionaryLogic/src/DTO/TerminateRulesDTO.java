package DTO;

import java.util.HashSet;
import java.util.Set;

public class TerminateRulesDTO {
    public Set<TerminateRuleDTO> getRules() {
        return new HashSet<>(rules);
    }

    public int getJumpInGenerations() {
        return jumpInGenerations;
    }

    private final Set<TerminateRuleDTO> rules;
    private final int jumpInGenerations;

    public TerminateRulesDTO(Set<TerminateRuleDTO> rules, int jumpInGenerations) {
        this.rules = rules;
        this.jumpInGenerations = jumpInGenerations;
    }
}