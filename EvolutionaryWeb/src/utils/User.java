package utils;

import com.sun.istack.internal.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class User {
    private final String name;
    private final Set<Problem> problemsRun;
    private int lastSeenProblem;


    public User(String name){
        this.name = name;
        this.problemsRun = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Set<Problem> getProblemsRun() {
        return problemsRun;
    }

    public boolean addProblem(@NotNull Problem prob){
        return this.problemsRun.add(prob);
    }

    public boolean removeProblem(@NotNull Problem prob){
        return this.problemsRun.remove(prob);
    }

    public Problem getProblemByID(int id){
        return this.problemsRun.stream()
                               .filter(x -> x.getProblemId() == id)
                               .findFirst()
                               .orElse(null);
    }

    public int getLastSeenProblem() {
        return lastSeenProblem;
    }

    public void setLastSeenProblem(int lastSeenProblem) {
        List<Integer> ids = problemsRun.stream().map(Problem::getProblemId).filter(x -> x == lastSeenProblem).collect(Collectors.toList());
        boolean contains = ids.size() == 1;
        if(!contains){
            throw new IllegalArgumentException("Problem id: " + lastSeenProblem + " is not run by " + name);
        }

        this.lastSeenProblem = lastSeenProblem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}