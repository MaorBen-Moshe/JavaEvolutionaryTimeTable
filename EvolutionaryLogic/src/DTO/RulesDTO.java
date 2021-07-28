package DTO;

import java.util.HashSet;
import java.util.Set;

public class RulesDTO {
    private final Set<RuleDTO> rules;

    public Set<RuleDTO> getRules() {
        return new HashSet<>(rules);
    }

    public RulesDTO(Set<RuleDTO> rules) {
        this.rules = new HashSet<>(rules);
    }
}
