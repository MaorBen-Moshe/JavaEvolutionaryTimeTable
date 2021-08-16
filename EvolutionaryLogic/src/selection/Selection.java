package selection;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface Selection<T> extends Serializable {
    List<T> select(Map<T, Double> population);

    SelectionTypes getType();
}