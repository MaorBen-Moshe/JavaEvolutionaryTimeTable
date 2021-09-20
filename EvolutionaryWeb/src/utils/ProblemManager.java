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

    public synchronized List<Problem> getProblems() {
        List<Problem> x = problemsList.values().stream().sorted(Comparator.comparing(Problem::getProblemId)).collect(Collectors.toList());
        System.out.println(x);
        return x;
    }

    public boolean isProblemExists(Problem evoSystem) {
        return problemsList.containsKey(evoSystem.getProblemId());
    }
}