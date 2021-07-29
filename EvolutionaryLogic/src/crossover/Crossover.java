package crossover;

import Interfaces.DataSupplier;

import java.util.Map;
import java.util.Set;

public interface Crossover<T, S extends DataSupplier> {
    Set<T> crossover(Map<T, Double> parents, S supplier);
}