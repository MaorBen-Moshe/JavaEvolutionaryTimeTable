package mutation;

import Interfaces.DataSupplier;

public interface Mutation<T, S extends DataSupplier> {
    void mutate(T child, S supplier);
}
