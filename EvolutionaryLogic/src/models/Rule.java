package models;

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

    public int getTotalHours(){ return totalHours; }

    private int totalHours; // if(ruleType is sequentiality)

    public void setTotalHours(int totalHours) throws Exception {
        if(!ruleType.equals(Rules.eRules.Sequentiality)){
            throw new Exception("To use this method you must be of type " + Rules.eRules.Sequentiality);
        }

        this.totalHours = totalHours;
    }

    public Rule(Rules.eRules rule, eStrength strength){
        ruleType = rule;
        this.strength = strength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return totalHours == rule.totalHours && ruleType == rule.ruleType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleType, totalHours);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "ruleType=" + ruleType +
                ", strength=" + strength +
                '}';
    }
}
