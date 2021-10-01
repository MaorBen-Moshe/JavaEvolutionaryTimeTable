package utils;

import utils.models.Problem;

import java.util.*;
import java.util.stream.Collectors;

public final class ProblemManager {
    private final Map<Integer, Problem> problemsList;

    public ProblemManager() {
        problemsList = new HashMap<>();
    }

    public void addProblem(Problem evoSystem) {
        if(!problemsList.containsKey(evoSystem.getProblemId())){
            problemsList.put(evoSystem.getProblemId(), evoSystem);
        }
    }

    public List<Problem> getProblems() {
        List<Problem> x = problemsList.values().stream().sorted(Comparator.comparing(Problem::getProblemId)).collect(Collectors.toList());
        System.out.println(x);
        return x;
    }

    public Problem getProblemById(int id){
        return problemsList.get(id);
    }

    public boolean isProblemExists(Problem evoSystem) {
        return problemsList.containsKey(evoSystem.getProblemId());
    }
}