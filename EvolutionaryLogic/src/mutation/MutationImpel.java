package mutation;

import Interfaces.DataSupplier;

import java.io.Serializable;

public abstract class MutationImpel<T, S extends DataSupplier> implements Mutation<T, S>, Serializable {
    private final MutationTypes type;
    protected final double probability;

    protected MutationImpel(MutationTypes type, double probability){
        this.probability = probability;
        this.type = type;
    }

    public MutationTypes getType(){
        return type;
    }

    public double getProbability() { return probability; }
}
