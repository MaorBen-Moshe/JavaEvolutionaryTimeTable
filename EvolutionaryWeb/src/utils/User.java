package utils;

import com.sun.istack.internal.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private final String name;
    private final Set<Problem> problemsRun;

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