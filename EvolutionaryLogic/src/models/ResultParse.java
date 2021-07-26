package models;

import interfaces.EvolutionarySystem;

import java.util.*;

public class ResultParse {
    private EvolutionarySystem<?> system; // null if parseSucceeded = false;

    private boolean isSucceeded;

    public ResultParse(){
        system = null;
        isSucceeded = false;
    }

    public boolean isSucceeded() { return isSucceeded; }

    public void setSucceeded(boolean succeeded) {
        isSucceeded = succeeded;
    }

    public EvolutionarySystem<?> getSystem() {
        return system;
    }

    public void setSystem(EvolutionarySystem<?> system) {
        this.system = system;
    }
}
