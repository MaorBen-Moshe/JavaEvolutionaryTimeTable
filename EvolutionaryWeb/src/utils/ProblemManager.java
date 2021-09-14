package utils;

import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ProblemManager {
    private final Set<EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier>> problemsList;

    public ProblemManager() {
        problemsList = new HashSet<>();
    }

    public synchronized void addProblem(EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> evoSystem) {
        problemsList.add(evoSystem);
    }

    public synchronized void removeProblem(EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> evoSystem) {
        problemsList.remove(evoSystem);
    }

    public synchronized Set<EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier>> getProblems() {
        return Collections.unmodifiableSet(problemsList);
    }

    public boolean isProblemExists(EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> evoSystem) {
        return problemsList.contains(evoSystem);
    }
}