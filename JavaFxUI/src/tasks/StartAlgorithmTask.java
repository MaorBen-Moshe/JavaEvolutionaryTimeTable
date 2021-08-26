package tasks;

import DTO.*;
import commands.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import models.JumpInGenerationsResult;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.function.Consumer;

public class StartAlgorithmTask extends Task<Void> {
    private final StartSystemCommand startCommand;
    private final PauseCommand pauseCommand;
    private final StopCommand stopCommand;
    private final ResumeCommand resumeCommad;
    private int totalGenerations;
    private double totalFitness;
    private long totalTime;
    private final Object lock;
    private final DoubleProperty currentFitnessProperty = new SimpleDoubleProperty(0);
    private final IntegerProperty currentGenerationProperty = new SimpleIntegerProperty(0);
    private final DoubleProperty fitnessProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty generationsProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty timeProperty = new SimpleDoubleProperty(0);

    public StartAlgorithmTask(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper, StartSystemInfoDTO info, Consumer<CommandResult<?>> whenFinished) {
        setInitialValues(info);
        lock = new Object();
        startCommand = new StartSystemCommand(wrapper, lock,
                () -> info, () -> info, () -> info, this::taskProcess,(result) -> Platform.runLater(() -> whenFinished.accept(result)));
        pauseCommand = new PauseCommand(wrapper);
        stopCommand = new StopCommand(wrapper);
        resumeCommad = new ResumeCommand(wrapper);
    }

    public DoubleProperty getFitnessProperty() {
        return fitnessProperty;
    }

    public DoubleProperty getCurrentFitnessProperty() {
        return currentFitnessProperty;
    }

    public IntegerProperty getCurrentGenerationsProperty() {
        return currentGenerationProperty;
    }

    public DoubleProperty getGenerationsProperty() {
        return generationsProperty;
    }

    public DoubleProperty getTimeProperty() {
        return timeProperty;
    }

    public void resume() throws Exception{
        synchronized (lock){
            resumeCommad.execute();
            lock.notifyAll();
        }
    }

    public void pause() throws Exception {
        pauseCommand.execute();
    }

    public void stop() throws Exception {
        synchronized (lock){
            stopCommand.execute();
            lock.notifyAll();
        }
    }

    @Override
    protected Void call() {
        startCommand.execute();
        return null;
    }

    private void taskProcess(JumpInGenerationsResult result){
        Platform.runLater(() -> {
            currentGenerationProperty.setValue(result.getNumberOfGeneration());
            currentFitnessProperty.setValue(result.getFitness());
            generationsProperty.setValue((double)result.getNumberOfGeneration() / totalGenerations);
            fitnessProperty.setValue(result.getFitness() / totalFitness);
            timeProperty.setValue(result.getTimePassed() / totalTime);
        });
    }

    private void setInitialValues(StartSystemInfoDTO info){
        for(TerminateRuleDTO rule : info.getTerminateRules()){
            switch(rule.getType()){
                case NumberOfGenerations: totalGenerations = ((GenerationsTerminateRuleDTO)rule).getGenerations(); break;
                case ByFitness: totalFitness = ((FitnessTerminateRuleDTO)rule).getFitness(); break;
                case ByTime: totalTime = ((TimeTerminateRuleDTO)rule).getTimeInMinutes(); break;
            }
        }
    }
}