package utils.models;

public class ProcessingObject {
    private final boolean isRunning;
    private final boolean isPaused;

    public ProcessingObject(boolean isRunning, boolean isPaused){
        this.isRunning = isRunning;
        this.isPaused = isPaused;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }
}
