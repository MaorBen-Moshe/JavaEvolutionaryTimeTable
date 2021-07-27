package selection;

import models.TimeTable;

import java.util.Comparator;
import java.util.Map;
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
}
