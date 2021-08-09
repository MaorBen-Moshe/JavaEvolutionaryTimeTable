package crossover;

import Interfaces.DataSupplier;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface Crossover<T, S extends DataSupplier> extends Serializable {
    Set<T> crossover(Map<T, Double> parents, S supplier);
}