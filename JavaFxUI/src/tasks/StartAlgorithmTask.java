package tasks;

import DTO.*;
import commands.CommandResult;
import commands.EngineWrapper;
import commands.StartSystemCommand;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import models.JumpInGenerationsResult;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import java.util.Map;
import java.util.function.Consumer;

public class StartAlgorithmTask extends Task<Void> {
    private StartSystemCommand startCommand;
    private Map<String, ProgressBar> progressBars;
    private int totalGenerations;
    private double totalFitness;
    private long totalTime;

    public StartAlgorithmTask(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper, StartSystemInfoDTO info, Consumer<CommandResult<?>> whenFinished,
                              Map<String, ProgressBar> progressBars) {
        this.progressBars = progressBars;
        setInitialValues(info);
        startCommand = new StartSystemCommand(wrapper,
                () -> info, () -> info, () -> info, this::taskProcess, () -> System.out.println("algorithm has started"), whenFinished);
    }

    @Override
    protected Void call() throws Exception {
        return null;
    }

    private void taskProcess(JumpInGenerationsResult result){
        Platform.runLater(() ->{
            if(progressBars.containsKey("Generations")){
                progressBars.get("Generations").setProgress((double)result.getNumberOfGeneration() / totalGenerations);
            }

            if(progressBars.containsKey("Fitness")){
                progressBars.get("Fitness").setProgress((double)result.getFitness() / totalFitness);
            }

            if(progressBars.containsKey("Time")){
                progressBars.get("Time").setProgress((double)result.getTimePassed() / totalTime);
            }
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
