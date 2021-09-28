package utils.models;

public class ProcessingObject {
    private final boolean isRunning;
    private final boolean isPaused;
    private int userGenerations;
    private double userFitness;

    public ProcessingObject(boolean isRunning, boolean isPaused){
        this.isRunning = isRunning;
        this.isPaused = isPaused;
    }

    public ProcessingObject(boolean isRunning, boolean isPaused, int userGenerations, double userFitness){
        this(isRunning, isPaused);
        this.userGenerations = userGenerations;
        this.userFitness = userFitness;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getUserGenerations() {
        return userGenerations;
    }

    public double getUserFitness() {
        return userFitness;
    }
}
