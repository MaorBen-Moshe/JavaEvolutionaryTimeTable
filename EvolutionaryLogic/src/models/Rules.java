package models;

import java.util.*;

public class Rules{

    public enum eRules {
        DayOffClass, DayOffTeacher, Knowledgeable, Sequentiality, Singularity, TeacherIsHuman, WorkingHoursPreference,Satisfactory
    }

    private final int hardRulesWeight;
    private final HashSet<Rule> rules;

    public Rules(int hardRulesWeight){
        this.hardRulesWeight = hardRulesWeight >= 0 && hardRulesWeight <= 100 ? hardRulesWeight : 0;
        rules = new HashSet<>();
    }

    public Rule.eStrength getRuleStrength(String ruleId){
        Rule rule = rules.stream()
                    .filter(current -> current.toString().equals(ruleId))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Rule " + ruleId + " not found"));

        return rule.getStrength();
    }

    public int getHardRulesWeight(){
        return hardRulesWeight;
    }

    public HashSet<Rule> getRules() {
        return new HashSet<>(rules);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rules rules1 = (Rules) o;
        return hardRulesWeight == rules1.hardRulesWeight && rules.equals(rules1.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hardRulesWeight, rules);
    }

    @Override
    public String toString() {
        return "Rules{" +
                "hardRulesWeight=" + hardRulesWeight +
                ", rules=" + rules +
                '}';
    }

    public boolean contains(Rule rule){
        return rules.contains(rule);
    }

    public boolean add(Rule rule){
        return rules.add(rule);
    }
}
