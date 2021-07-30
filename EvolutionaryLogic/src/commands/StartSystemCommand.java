package commands;

import DTO.FitnessTerminateRuleDTO;
import DTO.GenerationsTerminateRuleDTO;
import DTO.TerminateRuleDTO;
import DTO.StartSystemInfoDTO;
import models.FitnessTerminateRule;
import models.GenerationTerminateRule;
import models.JumpInGenerationsResult;
import models.TerminateRule;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StartSystemCommand extends CommandImpel{
    private final Supplier<StartSystemInfoDTO> whenRunningAlready;
    private final Supplier<StartSystemInfoDTO> whenNotRunning;
    private final Consumer<JumpInGenerationsResult> jumpInGenerationsListener;
    private final Consumer<CommandResult<?>> afterStart;

    public StartSystemCommand(Supplier<StartSystemInfoDTO> whenRunningAlready,
                              Supplier<StartSystemInfoDTO> whenNotRunning,
                              Consumer<JumpInGenerationsResult> jumpInGenerationsListener,
                              Consumer<CommandResult<?>> after) {
        this.whenNotRunning = whenNotRunning;
        this.whenRunningAlready = whenRunningAlready;
        this.afterStart = after;
        this.jumpInGenerationsListener = jumpInGenerationsListener;
    }

    @Override
    public void execute() {
        CommandResult<?> result = new CommandResult<>();
        StartSystemInfoDTO rules;
        if(isFileLoaded){
            if(evolutionarySystem.IsRunningProcess()){
                rules = whenRunningAlready.get();
            }
            else{
                rules = whenNotRunning.get();
            }

            if(rules == null){
                result.setErrorMessage("Algorithm cannot start without terminate rules");
                return;
            }

            evolutionarySystem.StartAlgorithm(setByTerminate(rules.getRules()), rules.getJumpInGenerations(), jumpInGenerationsListener);
        }
        else{
            result.setErrorMessage("File should be loaded first");
        }

        afterStart.accept(result);
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
            }
        });

        return toRet;
    }

    @Override
    public String getCommandName() {
        return "Start evolutionary system";
    }
}
