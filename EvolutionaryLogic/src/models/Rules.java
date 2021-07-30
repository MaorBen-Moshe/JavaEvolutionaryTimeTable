package models;

import java.util.*;

public class Rules{

    public enum eRules {
        DayOffClass, DayOffTeacher, Knowledgeable, Sequentiality, Singularity, TeacherIsHuman, WorkingHoursPreference,Satisfactory
    }

    private final int hardRulesWeight;
    private final Set<Rule> rules;
    private Set<Rule> unModifiedRules;

    public Rules(int hardRulesWeight){
        this.hardRulesWeight = hardRulesWeight >= 0 && hardRulesWeight <= 100 ? hardRulesWeight : 0;
        rules = new HashSet<>();
    }

    public int getHardRulesWeight(){
        return hardRulesWeight;
    }

    public Set<Rule> getRules() {
        return unModifiedRules;
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
        boolean ret = rules.add(rule);
        unModifiedRules = Collections.unmodifiableSet(rules);
        return ret;
    }
}
