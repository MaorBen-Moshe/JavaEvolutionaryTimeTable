package crossover;

import Interfaces.DataSupplier;

import java.util.Map;
import java.util.Set;

public interface Crossover<T> {
    Set<T> crossover(Map<T, Double> parents);
}