package selection;

import java.util.Map;

public interface Selection<T> {
    Map<T, Double> select(Map<T, Double> population);
}