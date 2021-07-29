package DTO;

import java.util.Collections;
import java.util.Set;

public class TerminateRulesDTO {
    public int getJumpInGenerations() {
        return jumpInGenerations;
    }

    private final Set<TerminateRuleDTO> rules;
    private final Set<TerminateRuleDTO> unModifiedRules;

    public Set<TerminateRuleDTO> getRules() {
        return unModifiedRules;
    }

    private final int jumpInGenerations;

    public TerminateRulesDTO(Set<TerminateRuleDTO> rules, int jumpInGenerations) {
        this.rules = rules;
        this.unModifiedRules = Collections.unmodifiableSet(this.rules);
        this.jumpInGenerations = jumpInGenerations;
    }
}
