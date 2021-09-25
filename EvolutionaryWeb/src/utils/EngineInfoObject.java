package utils;

import crossover.Crossover;
import models.TerminateRule;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import mutation.Mutation;
import selection.Selection;

import java.util.List;
import java.util.Set;

public class EngineInfoObject {
    private final Selection<TimeTable> selection;
    private final Crossover<TimeTable, TimeTableSystemDataSupplier> crossover;
    private final List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutations;
    private final int population;
    private final int elitism;
    private final Set<TerminateRule> terminateRules;
    private final int jumps;

    public Selection<TimeTable> getSelection() {
        return selection;
    }

    public Crossover<TimeTable, TimeTableSystemDataSupplier> getCrossover() {
        return crossover;
    }

    public List<Mutation<TimeTable, TimeTableSystemDataSupplier>> getMutations() {
        return mutations;
    }

    public int getPopulation() {
        return population;
    }

    public int getElitism() {
        return elitism;
    }

    public Set<TerminateRule> getTerminateRules() {
        return terminateRules;
    }

    public int getJumps() {
        return jumps;
    }


    public EngineInfoObject(int population, int elitism, int jumps, Set<TerminateRule> terminateRules, Selection<TimeTable> selection, Crossover<TimeTable, TimeTableSystemDataSupplier> crossover, List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutations){
        this.population = population;
        this.elitism = elitism;
        this.jumps = jumps;
        this.terminateRules = terminateRules;
        this.selection = selection;
        this.crossover = crossover;
        this.mutations = mutations;
    }
}
