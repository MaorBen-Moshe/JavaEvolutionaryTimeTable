package mutation;

import Interfaces.DataSupplier;

import java.io.Serializable;

public interface Mutation<T, S extends DataSupplier> extends Serializable {
    void mutate(T child, S supplier);

    MutationTypes getType();

    double getProbability();

    void setProbability(double probability);
}