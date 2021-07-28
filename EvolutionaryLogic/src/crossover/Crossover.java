package crossover;

import Interfaces.DataSupplier;

import java.util.Map;
import java.util.Set;

public interface Crossover<T> {
    Set<T> crossOver(Map<T, Double> parents);
}