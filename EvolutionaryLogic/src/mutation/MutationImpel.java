package mutation;

import Interfaces.DataSupplier;

import java.io.Serializable;

public abstract class MutationImpel<T, S extends DataSupplier> implements Mutation<T, S>, Serializable {
    private final MutationTypes type;
    protected double probability;

    protected MutationImpel(MutationTypes type, double probability){
        this.probability = probability;
        this.type = type;
    }

    public MutationTypes getType(){
        return type;
    }

    public double getProbability() { return probability; }

    public void setProbability(double probability){
        if(probability < 0 || probability > 1){
            throw new IllegalArgumentException("probability in " + type + " should be a double between 0 -1");
        }

        this.probability = probability;
    }
}