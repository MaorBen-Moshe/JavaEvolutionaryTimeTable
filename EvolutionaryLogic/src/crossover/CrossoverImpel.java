package crossover;

import Interfaces.DataSupplier;

import java.io.Serializable;

public abstract class CrossoverImpel<T, S extends DataSupplier> implements Crossover<T, S>, Serializable {
    protected int cuttingPoints;
    private final CrossoverTypes type;

    protected CrossoverImpel(CrossoverTypes type, int cuttingPoints){
        this.type = type;
        this.cuttingPoints = cuttingPoints;
    }

    public int getCuttingPoints() { return cuttingPoints; }

    public CrossoverTypes getType() { return type; }
}
