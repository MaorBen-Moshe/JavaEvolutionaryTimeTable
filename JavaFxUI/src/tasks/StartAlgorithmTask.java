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
    private int totalGenerations;
    private double totalFitness;
    private long totalTime;
    private final Object lock;
    private final DoubleProperty fitnessProperty = new SimpleDoubleProperty();
    private final IntegerProperty generationsProperty = new SimpleIntegerProperty();
    private final LongProperty timeProperty = new SimpleLongProperty();

    public StartAlgorithmTask(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper, StartSystemInfoDTO info, Consumer<CommandResult<?>> whenFinished) {
        setInitialValues(info);
        lock = new Object();
        startCommand = new StartSystemCommand(wrapper, lock,
                () -> info, () -> info, () -> info, this::taskProcess, whenFinished);
        pauseCommand = new PauseCommand(wrapper);
        stopCommand = new StopCommand(wrapper);
    }

    public DoubleProperty getFitnessProperty() {
        return fitnessProperty;
    }

    public IntegerProperty getGenerationsProperty() {
        return generationsProperty;
    }

    public LongProperty getTimeProperty() {
        return timeProperty;
    }

    public void resume(){
        synchronized (lock){
            notifyAll();
        }
    }

    public void pause() throws Exception {
        pauseCommand.execute();
    }

    public void stop() throws Exception {
        stopCommand.execute();
    }

    @Override
    protected Void call() throws Exception {
        startCommand.execute();
        return null;
    }

    private void taskProcess(JumpInGenerationsResult result){
        Platform.runLater(() ->{
            generationsProperty.setValue((double)result.getNumberOfGeneration() / totalGenerations);
            fitnessProperty.setValue(result.getFitness() / totalFitness);
            timeProperty.setValue(result.getTimePassed() / totalTime);
        });
    }

    private void setInitialValues(StartSystemInfoDTO info){
        for(TerminateRuleDTO rule : info.getTerminateRules()){
            switch(rule.getType()){
                case NumberOfGenerations: totalGenerations = ((GenerationsTerminateRuleDTO)rule).getGenerations();
                case ByFitness: totalFitness = ((FitnessTerminateRuleDTO)rule).getFitness();
                case ByTime: totalTime = ((TimeTerminateRuleDTO)rule).getTimeInMinutes();
            }
        }
    }
}
