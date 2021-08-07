package models;

import java.util.*;

public class Rules{
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

    public double evaluateRules(TimeTable optional, TimeTableSystemDataSupplier supplier){
        List<Double> hardList = new ArrayList<>();
        List<Double> softList = new ArrayList<>();
        final double[] answer = {0};
        rules.forEach(rule -> {
            answer[0] = rule.evaluateRule(optional, supplier);
            if(rule.getStrength().equals(Rule.eStrength.Hard)){
                hardList.add(answer[0]);
            }else {
                softList.add(answer[0]);
            }

            optional.addRuleScore(rule, answer[0]);
        });

        return setRulesScoreAndGetResult(optional, hardList, softList);
    }

    private double setRulesScoreAndGetResult(TimeTable optional, List<Double> hardList, List<Double> softList) {
        double retVal;
        double hard;
        double soft;
        int hardWeight = getHardRulesWeight();
        if(hardList.size() == 0 || softList.size() == 0){
            if(hardList.size() == 0){
                soft = getAvg(softList);
                hard = -1;
                retVal = soft;
            }
            else{
                hard = getAvg(hardList);
                soft = -1;
                retVal = hard;
            }
        }
        else{
            hard = getAvg(hardList);
            soft = getAvg(softList);
            retVal = ((hardWeight * hard) + ((100 - hardWeight) * soft)) / 100;
        }

        optional.setHardRulesAvg(hard);
        optional.setSoftRulesAvg(soft);
        return retVal;
    }


    private double getAvg(List<Double> list){
        if(list.size() == 0){
            return 0;
        }

        return list.stream().mapToDouble(x -> x).average().orElse(0.0);
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
        return "Rules { " +
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