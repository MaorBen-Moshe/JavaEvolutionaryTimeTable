package DTO;

import models.Rule;
import models.Rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RuleDTO {
    private final Rule.eStrength strength;
    private final Rules.eRules type;
    private final Map<String, Object> configurations;
    private Map<String,Object> unModifiedConfigurations;


    public RuleDTO(Rules.eRules ruleType, Rule.eStrength ruleStrength, Map<String, Object> configurations){
        this.strength = ruleStrength;
        this.type = ruleType;
        this.configurations = configurations == null ? new HashMap<>() : configurations;
        if(configurations != null){
            this.unModifiedConfigurations = Collections.unmodifiableMap(configurations);
        }
    }

    public Rule.eStrength getStrength() {
        return strength;
    }

    public Rules.eRules getType() {
        return type;
    }

    public Map<String, Object> getConfigurations() {
        return unModifiedConfigurations;
    }

    @Override
    public String toString() {
        String ret = "rule: " +
                "strength = " + strength +
                ", type = " + type;
        ret = ret + (configurations.size() != 0 ?  (", configurations: " + configurations) : "");
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleDTO ruleDTO = (RuleDTO) o;
        return strength == ruleDTO.strength && type == ruleDTO.type && configurations.equals(ruleDTO.configurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strength, type, configurations);
    }
}