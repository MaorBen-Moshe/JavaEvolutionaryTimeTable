package selection;

import models.TimeTable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TruncationSelection implements Selection<TimeTable> {
    private final int topPercent;

    public TruncationSelection(int topPercent){
        if(topPercent <= 0 || topPercent > 100){
            throw new IllegalArgumentException("TopPercent in Truncation selection must be an integer between 1-100");
        }

        this.topPercent = topPercent;
    }

    @Override
    public Map<TimeTable, Double> select(Map<TimeTable, Double> population) {
        int percent = (topPercent * population.size()) / 100;
        return population.entrySet()
                         .stream()
                         .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                         .limit(percent)
                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

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
