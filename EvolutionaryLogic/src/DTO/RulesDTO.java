package DTO;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RulesDTO {
    private final Set<RuleDTO> rules;
    private final Set<RuleDTO> unModifiedRules;

    public RulesDTO(Set<RuleDTO> rules) {
        this.rules = new HashSet<>(rules);
        this.unModifiedRules = Collections.unmodifiableSet(this.rules);
    }

    public Set<RuleDTO> getRules() {
        return unModifiedRules;
    }

    @Override
    public String toString() {
        return "rules=" + rules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RulesDTO rulesDTO = (RulesDTO) o;
        return rules.equals(rulesDTO.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules);
    }
}
