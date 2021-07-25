package models;

import interfaces.EvolutionarySystem;

import java.util.*;

public class ResultParse {
    private EvolutionarySystem<?> system; // null if parseSucceeded = false;
    private final List<String> errors;

    public ResultParse(){
        system = null;
        errors = new ArrayList<>();
    }

    public boolean isSucceeded() { return errors.size() == 0; }

    public Optional<EvolutionarySystem<?>> getSystem() {
        return Optional.of(system);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setSystem(EvolutionarySystem<?> system) {
        this.system = system;
    }

    public boolean addError(String error){
        return errors.add(error);
    }
}
