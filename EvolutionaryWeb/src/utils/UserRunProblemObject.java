package utils;

public class UserRunProblemObject {
    private final String name;
    private final int generations;
    private final double fitness;

    public UserRunProblemObject(String name, int generations, double fitness) {
        this.name = name;
        this.generations = generations;
        this.fitness = fitness;
    }

    public String getName() {
        return name;
    }

    public int getGenerations() {
        return generations;
    }

    public double getFitness() {
        return fitness;
    }
}
