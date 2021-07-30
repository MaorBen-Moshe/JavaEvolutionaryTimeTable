package DTO;

import java.util.Collections;
import java.util.Set;

public class StartSystemInfoDTO {
    private final Set<TerminateRuleDTO> rules;
    private final Set<TerminateRuleDTO> unModifiedRules;
    private final int jumpInGenerations;

    public StartSystemInfoDTO(Set<TerminateRuleDTO> rules, int jumpInGenerations) {
        this.rules = rules;
        this.unModifiedRules = Collections.unmodifiableSet(this.rules);
        this.jumpInGenerations = jumpInGenerations;
    }

    public Set<TerminateRuleDTO> getRules() {
        return unModifiedRules;
    }

    public int getJumpInGenerations() {
        return jumpInGenerations;
    }
}
