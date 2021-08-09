package models;

import evolutinary.EvolutionarySystem;

public class GenerationTerminateRule extends TerminateRule {
    private final int numberOfGenerations;

    public GenerationTerminateRule(int generations){
        super(EvolutionarySystem.TerminateRules.NumberOfGenerations);
        this.numberOfGenerations = generations;
    }

    @Override
    public boolean isTerminate(Object... args) {
        if(args != null && args.length != 1 && !(args[0] instanceof Integer)){
            throw new IllegalArgumentException("Generations terminate rule accept only the number of generations");
        }

        Integer currentNumberOfGenerations = (Integer) (args != null ? args[0] : null);
        if(currentNumberOfGenerations < 0){
            throw new IllegalArgumentException("Generations terminate rule accept only a positive number of generations");
        }

        return currentNumberOfGenerations >= numberOfGenerations;
    }
}