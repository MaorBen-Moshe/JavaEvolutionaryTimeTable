package DTO;


import evolutinary.EvolutionarySystem;

public class GenerationsTerminateRuleDTO extends TerminateRuleDTO {
    public int getGenerations() {
        return generations;
    }

    private final int generations;

    public GenerationsTerminateRuleDTO(int generations){
        super(EvolutionarySystem.TerminateRules.NumberOfGenerations);
        this.generations = generations;
    }
}
