package selection;

import models.TimeTable;

import java.io.Serializable;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

public class TruncationSelection extends SelectionImpel<TimeTable> implements Serializable {
    private int topPercent;

    public TruncationSelection(int topPercent){
        super(SelectionTypes.Truncation);
        setTopPercent(topPercent);
    }

    public int getTopPercent() {
        return topPercent;
    }

    public void setTopPercent(int topPercent) {
        if(topPercent <= 0 || topPercent > 100){
            throw new IllegalArgumentException("TopPercent in Truncation selection must be an integer between 1-100");
        }

        this.topPercent = topPercent;
    }

    @Override
    public List<TimeTable> select(Map<TimeTable, Double> population) {
        int percent = (topPercent * population.size()) / 100;
        Map<TimeTable, Double> selected =  population.entrySet()
                         .stream()
                         .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                         .limit(percent)
                         .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1));

        return new ArrayList<>(selected.keySet());
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