package models;

import evolutinary.EvolutionarySystem;

public class FitnessTerminateRule extends TerminateRule {
    private final double fitness;

    public FitnessTerminateRule(double fitness){
        super(EvolutionarySystem.TerminateRules.ByFitness);
        this.fitness = fitness;
    }

    @Override
    public boolean isTerminate(Object... args) {
        if(args.length != 1 && !(args[0] instanceof Double)){
            throw new IllegalArgumentException("Fitness terminate rule accept only the fitness");
        }

        Double currentFitness = (Double) args[0];
        if(currentFitness < 0 || currentFitness > 100){
            throw new IllegalArgumentException("Fitness terminate rule accept only a fitness between 0-100");
        }

        return currentFitness > fitness;
    }
}