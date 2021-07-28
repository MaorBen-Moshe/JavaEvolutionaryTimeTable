package commands;

import DTO.TerminateRuleDTO;
import DTO.TerminateRulesDTO;
import evolutinary.EvolutionarySystem;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StartSystemCommand extends CommandImpel{
    private final Supplier<TerminateRulesDTO> whenRunningAlready;
    private final Supplier<TerminateRulesDTO> whenNotRunning;
    private final Consumer<CommandResult<?>> afterStart;

    public StartSystemCommand(Supplier<TerminateRulesDTO> whenRunningAlready,
                              Supplier<TerminateRulesDTO> whenNotRunning,
                              Consumer<CommandResult<?>> after) {
        this.whenNotRunning = whenNotRunning;
        this.whenRunningAlready = whenRunningAlready;
        this.afterStart = after;
    }

    @Override
    public void execute() {
        CommandResult<?> result = new CommandResult<>();
        TerminateRulesDTO rules;
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

            evolutionarySystem.StartAlgorithm(setByTerminate(rules));
        }
        else{
            result.setErrorMessage("File should be loaded first");
        }

        afterStart.accept(result);
    }

    private Set<EvolutionarySystem.TerminateRules> setByTerminate(TerminateRulesDTO rules){
        Set<EvolutionarySystem.TerminateRules> toRet = new HashSet<>();
        Set<TerminateRuleDTO> toRun = rules.getRules();
        toRun.forEach(rule -> {
            switch(rule.getRule()){
                case ByFitness: evolutionarySystem.setAcceptedFitness(rule.getVal()); break;
                case NumberOfGenerations: evolutionarySystem.setAcceptedNumberOfGenerations(rule.getVal()); break;
            }

            toRet.add(rule.getRule());
        });

        evolutionarySystem.setJumpInGenerations(rules.getJumpInGenerations());
        return toRet;
    }

    @Override
    public String getCommandName() {
        return "Start evolutionary system";
    }
}
