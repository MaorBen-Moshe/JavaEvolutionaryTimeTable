package interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Crossover<T> {
    Set<T> crossOver(Map<T, Double> parents);
}
