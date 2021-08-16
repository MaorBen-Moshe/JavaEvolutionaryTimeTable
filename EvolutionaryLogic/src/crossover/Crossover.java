package crossover;

import Interfaces.DataSupplier;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Crossover<T, S extends DataSupplier> extends Serializable {
    Set<T> crossover(List<T> parents, S supplier);

    int getCuttingPoints();

    CrossoverTypes getType();
}