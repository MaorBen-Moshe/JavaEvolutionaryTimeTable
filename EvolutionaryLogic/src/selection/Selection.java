package selection;

import java.io.Serializable;
import java.util.Map;

public interface Selection<T> extends Serializable {
    Map<T, Double> select(Map<T, Double> population, Map<T, Double> elita);
}