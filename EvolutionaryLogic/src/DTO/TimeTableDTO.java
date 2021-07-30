package DTO;

import java.util.*;

public class TimeTableDTO {
    private final Set<TimeTableItemDTO> items;
    private final Set<TimeTableItemDTO> unModifiedItems;
    private final Map<RuleDTO, Double> rulesScore;
    private final Map<RuleDTO, Double> unModifiedRulesScore;
    private final double hardRulesAvg;
    private final double softRulesAvg;

    public TimeTableDTO(Set<TimeTableItemDTO> items, Map<RuleDTO, Double> rulesScore, double hardAvg, double softAvg){
        this.hardRulesAvg = hardAvg;
        this.softRulesAvg = softAvg;
        this.items = new TreeSet<>(items);
        this.unModifiedItems = Collections.unmodifiableSortedSet(new TreeSet<>(this.items));
        this.rulesScore = rulesScore;
        this.unModifiedRulesScore = Collections.unmodifiableMap(this.rulesScore);
    }

    public Set<TimeTableItemDTO> getItems() {
        return unModifiedItems;
    }

    public Map<RuleDTO, Double> getRulesScore() {
        return unModifiedRulesScore;
    }

    public double getHardRulesAvg() {
        return hardRulesAvg;
    }

    public double getSoftRulesAvg() {
        return softRulesAvg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableDTO that = (TimeTableDTO) o;
        return Double.compare(that.hardRulesAvg, hardRulesAvg) == 0 && Double.compare(that.softRulesAvg, softRulesAvg) == 0 && items.equals(that.items) && rulesScore.equals(that.rulesScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, rulesScore, hardRulesAvg, softRulesAvg);
    }
}
