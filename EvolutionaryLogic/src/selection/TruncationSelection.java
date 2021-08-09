package selection;

import models.TimeTable;

import java.io.Serializable;
import java.sql.Time;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TruncationSelection implements Selection<TimeTable>, Serializable {
    private final int topPercent;

    public TruncationSelection(int topPercent){
        if(topPercent <= 0 || topPercent > 100){
            throw new IllegalArgumentException("TopPercent in Truncation selection must be an integer between 1-100");
        }

        this.topPercent = topPercent;
    }

    @Override
    public Map<TimeTable, Double> select(Map<TimeTable, Double> population, Map<TimeTable, Double> elita) {
        int percent = (topPercent * population.size()) / 100;
        if(percent - elita.size() <= 0){
            return elita;
        }

        Map<TimeTable, Double> selected =  population.entrySet()
                         .stream()
                         .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                         .limit(percent - elita.size())
                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1));

        selected.putAll(elita);
        return selected;
    }

    @Override
    public String toString() {
        return "Truncation Selection { " +
                "topPercent=" + topPercent +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruncationSelection that = (TruncationSelection) o;
        return topPercent == that.topPercent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(topPercent);
    }
}