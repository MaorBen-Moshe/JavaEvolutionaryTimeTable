package models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Rule {

    public enum eStrength{
        Soft, Hard
    }

    public eStrength getStrength() {
        return strength;
    }

    private final eStrength strength;

    public Rules.eRules getRuleType() {
        return ruleType;
    }

    private final Rules.eRules ruleType;

    public Rule(Rules.eRules rule, eStrength strength,  Map<String, String> configurations){
        ruleType = rule;
        this.strength = strength;
        this.configurations = configurations == null ? new HashMap<>() : configurations;
        unModifiedConfigurations = Collections.unmodifiableMap(this.configurations);
    }


    public Map<String, String> getConfigurations() {
        return unModifiedConfigurations;
    }

    private final Map<String, String> configurations;
    private Map<String, String> unModifiedConfigurations;


    @Override
    public String toString() {
        return "Rule{" +
                "ruleType=" + ruleType +
                ", strength=" + strength +
                (configurations.size() != 0 ? ", configurations= " + configurations : "") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return strength == rule.strength && ruleType == rule.ruleType && configurations.equals(rule.configurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strength, ruleType, configurations);
    }

    public void addConfiguration(String key, String val){
        if(!configurations.containsKey(key)){
            configurations.put(key, val);
            unModifiedConfigurations = Collections.unmodifiableMap(this.configurations);
        }
    }
}
