package commands;

import DTO.*;
import models.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StartSystemCommand extends CommandImpel{
    private final Supplier<StartSystemInfoDTO> whenRunningAlready;
    private final Supplier<StartSystemInfoDTO> whenNotRunning;
    private final Consumer<JumpInGenerationsResult> jumpInGenerationsListener;
    private final Consumer<CommandResult<?>> endRunning;
    private final Supplier<StartSystemInfoDTO> whenRunPastAlgo;
    private final Object lock;

    public StartSystemCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                              Object lock,
                              Supplier<StartSystemInfoDTO> whenRunPastAlgo,
                              Supplier<StartSystemInfoDTO> whenRunningAlready,
                              Supplier<StartSystemInfoDTO> whenNotRunning,
                              Consumer<JumpInGenerationsResult> jumpInGenerationsListener,
                              Consumer<CommandResult<?>> endRunning) {
        super(wrapper);
        this.lock = lock;
        this.whenRunPastAlgo = whenRunPastAlgo;
        this.whenNotRunning = whenNotRunning;
        this.whenRunningAlready = whenRunningAlready;
        this.endRunning = endRunning;
        this.jumpInGenerationsListener = jumpInGenerationsListener;
    }

    @Override
    public void execute() {
        CommandResult<?> result = new CommandResult<>();
        StartSystemInfoDTO rules;
        if(getEngineWrapper().isFileLoaded()){
            if(getEngineWrapper().getEngine().isRunningProcess()){
                rules = whenRunningAlready.get();
                if(rules == null){
                    return;
                }

                getEngineWrapper().getEngine().stopProcess();
            }
            else{
                // the algorithm is not running right now but it run in the past and has its information
                // so the user should be notify by that and decide if he wants to start the new algorithm.
                if(getEngineWrapper().getEngine().getGenerationFitnessHistory().size() != 0){
                    rules = whenRunPastAlgo.get();
                    if(rules == null){
                        return;
                    }
                }
                else{
                    rules = whenNotRunning.get();
                }
            }

            if(rules == null){
                result.setErrorMessage("Algorithm cannot start without terminate rules");
                endRunning.accept(result);
                return;
            }

            getEngineWrapper().getEngine().StartAlgorithm(lock, setByTerminate(rules.getTerminateRules()),
                    rules.getJumpInGenerations(),
                    jumpInGenerationsListener);
        }
        else{
            result.setErrorMessage("File should be loaded first");
        }
        endRunning.accept(result);
    }

    @Override
    public String getCommandName() {
        return "Start evolutionary system";
    }

    private Set<TerminateRule> setByTerminate(Set<TerminateRuleDTO> rules){
        Set<TerminateRule> toRet = new HashSet<>();
        rules.forEach(rule ->{
            switch(rule.getType()){
                case NumberOfGenerations:
                    GenerationsTerminateRuleDTO currentGen = (GenerationsTerminateRuleDTO) rule;
                    toRet.add(new GenerationTerminateRule(currentGen.getGenerations()));
                    break;
                case ByFitness:
                    FitnessTerminateRuleDTO currentFit = (FitnessTerminateRuleDTO) rule;
                    toRet.add(new FitnessTerminateRule(currentFit.getFitness()));
                    break;
                case ByTime:
                    TimeTerminateRuleDTO currentTime = (TimeTerminateRuleDTO) rule;
                    toRet.add(new TimeTerminateRule(currentTime.getTimeInMinutes()));
                    break;
            }
        });

        return toRet;
    }
}