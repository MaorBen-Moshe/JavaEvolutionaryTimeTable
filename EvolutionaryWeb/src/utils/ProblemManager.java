package utils;

import java.util.*;
import java.util.stream.Collectors;

public final class ProblemManager {
    private final Map<Integer, Problem> problemsList;

    public ProblemManager() {
        problemsList = new HashMap<>();
    }

    public synchronized void addProblem(Problem evoSystem) {
        if(!problemsList.containsKey(evoSystem.getProblemId())){
            problemsList.put(evoSystem.getProblemId(), evoSystem);
        }
    }

    public synchronized void removeProblem(Problem evoSystem) {
        problemsList.remove(evoSystem.getProblemId());
    }

    public synchronized Set<Problem> getProblems() {
        return Collections.unmodifiableSet(new HashSet<>(problemsList.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toSet())));
    }

    public boolean isProblemExists(Problem evoSystem) {
        return problemsList.containsKey(evoSystem.getProblemId());
    }
}