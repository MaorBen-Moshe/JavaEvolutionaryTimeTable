package DTO;

import models.Rule;
import models.Rules;

import java.util.Objects;

public class RuleDTO {
    public Rule.eStrength getStrength() {
        return strength;
    }

    public Rules.eRules getType() {
        return type;
    }


    public int getTotalHours() throws Exception {
        if(!type.equals(Rules.eRules.Sequentiality)){
            throw new Exception("To use this method you must be of type " + Rules.eRules.Sequentiality);
        }

        return totalHours;
    }

    private final Rule.eStrength strength;
    private final Rules.eRules type;
    private final int totalHours; // if(ruleType is sequentiality)

    public RuleDTO(Rules.eRules ruleType, Rule.eStrength ruleStrength, int totalHours){
        this.strength = ruleStrength;
        this.type = ruleType;
        this.totalHours = totalHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleDTO ruleDTO = (RuleDTO) o;
        return totalHours == ruleDTO.totalHours && strength == ruleDTO.strength && type == ruleDTO.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(strength, type, totalHours);
    }

    @Override
    public String toString() {
        String ret = "rule: " +
                "strength=" + strength +
                ", type=" + type;
        if(type.equals(Rules.eRules.Sequentiality)){
            ret = ret + ", " + "totalHours= " + totalHours;
        }

        return ret;
    }
}
