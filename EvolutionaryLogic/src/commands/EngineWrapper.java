package commands;

import Interfaces.DataSupplier;
import evolutinary.EvolutionarySystem;

public final class EngineWrapper<T, S extends DataSupplier> {
    private EvolutionarySystem<T, S> engine;
    private boolean isFileLoaded;

    public EvolutionarySystem<T, S> getEngine() {
        return engine;
    }

    public void setEngine(EvolutionarySystem<T, S> engine) {
        if(engine != null){
            this.engine = engine;
            isFileLoaded = true;
        }
    }

    public boolean isFileLoaded() {
        return isFileLoaded;
    }
}