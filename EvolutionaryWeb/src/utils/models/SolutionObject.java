package utils.models;

import models.Rule;

import java.util.*;

public class SolutionObject {
    public static class CellItem{
        private final String objectName;
        private final int objectId;
        private final String subjectName;
        private final int subjectId;

        public CellItem(String objectName, int objectId, String subjectName, int subjectId){
            this.objectName = objectName;
            this.objectId = objectId;
            this.subjectId = subjectId;
            this.subjectName = subjectName;
        }
    }

    private static class RuleScore{
        private final Rule.eRules name;
        private final Rule.eStrength strength;
        private final double score;

        private RuleScore(Rule.eRules name, Rule.eStrength strength, double score){
            this.name = name;
            this.strength = strength;
            this.score = score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RuleScore ruleScore = (RuleScore) o;
            return name.equals(ruleScore.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    private final double softRulesAvg;
    private final double hardRulesAvg;
    private final int generationCreated;
    private final double fitnessScore;
    private final Set<RuleScore> rules;
    private final Map<Integer, Map<Integer, List<CellItem>>> tableInfo;

    public SolutionObject(double softAvg, double hardAvg, int genCreated, double fitness, Map<Rule, Double> rules, Map<Integer, Map<Integer, List<CellItem>>> info){
        this.softRulesAvg = softAvg;
        this.hardRulesAvg = hardAvg;
        this.generationCreated = genCreated;
        this.fitnessScore = fitness;
        this.rules = setRules(rules);
        this.tableInfo = info;
    }

    private Set<RuleScore> setRules(Map<Rule, Double> rules) {
        Set<RuleScore> scores = new HashSet<>();
        rules.forEach((rule, score) -> scores.add(new RuleScore(rule.getRuleType(), rule.getStrength(), score)));

        return scores;
    }
}