package models;

import java.util.*;

public class Rules extends HashSet<Rule> {

    public enum eRules {
        DayOffClass, DayOffTeacher, Knowledgeable, Sequentiality, Singularity, TeacherIsHuman, WorkingHoursPreference,Satisfactory
    }

    private final int hardRulesWeight;

    public Rules(int hardRulesWeight){
        super();
        this.hardRulesWeight = hardRulesWeight >= 0 && hardRulesWeight <= 100 ? hardRulesWeight : 0;
    }

    public Rule.eStrength getRuleStrength(String ruleId){
        Rule rule = super.stream()
                    .filter(current -> current.toString().equals(ruleId))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Rule " + ruleId + " not found"));

        return rule.getStrength();
    }

    public int getHardRulesWeight(){
        return hardRulesWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Rules rules = (Rules) o;
        return hardRulesWeight == rules.hardRulesWeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hardRulesWeight);
    }

    @Override
    public String toString() {
        return "Rules{" +
                "hardRulesWeight=" + hardRulesWeight +
                "} " + super.toString();
    }
}
