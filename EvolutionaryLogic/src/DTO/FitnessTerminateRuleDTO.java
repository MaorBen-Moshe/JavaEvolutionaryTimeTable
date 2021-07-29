package DTO;


import evolutinary.EvolutionarySystem;

public class FitnessTerminateRuleDTO extends TerminateRuleDTO{
    public int getFitness() {
        return fitness;
    }

    private final int fitness;

    public FitnessTerminateRuleDTO(int fitness){
        super(EvolutionarySystem.TerminateRules.ByFitness);
        this.fitness = fitness;
    }
}
