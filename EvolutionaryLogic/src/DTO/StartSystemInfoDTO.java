package DTO;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class StartSystemInfoDTO {
    private final Set<TerminateRuleDTO> rules;
    private final Set<TerminateRuleDTO> unModifiedRules;
    private final int jumpInGenerations;

    public StartSystemInfoDTO(Set<TerminateRuleDTO> rules, int jumpInGenerations) {
        if(rules == null || rules.size() == 0){
            throw new IllegalArgumentException("at least one terminate rule should be provided to start the algorithm");
        }

        if(jumpInGenerations < 0){
            throw new IllegalArgumentException("Jump in generations should be positive number");
        }

        this.rules = rules;
        this.unModifiedRules = Collections.unmodifiableSet(this.rules);
        this.jumpInGenerations = jumpInGenerations;
    }

    public Set<TerminateRuleDTO> getTerminateRules() {
        return unModifiedRules;
    }

    public int getJumpInGenerations() {
        return jumpInGenerations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartSystemInfoDTO that = (StartSystemInfoDTO) o;
        return jumpInGenerations == that.jumpInGenerations && Objects.equals(rules, that.rules) && Objects.equals(unModifiedRules, that.unModifiedRules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules, unModifiedRules, jumpInGenerations);
    }
}