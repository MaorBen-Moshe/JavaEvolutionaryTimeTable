package commands;

import java.util.Map;
import java.util.function.Consumer;

public class ProcessCommand extends CommandImpel{

    private Consumer<CommandResult<Map<Integer, Double>>> action;

    public ProcessCommand(Consumer<CommandResult<Map<Integer, Double>>> o) {
        this.action = o;
    }
    @Override
    public void execute() {
        CommandResult<Map<Integer, Double>> result = new CommandResult<>();
        if(isFileLoaded){
            if(evolutionarySystem.IsRunningProcess()){
                result.setErrorMessage("Process is still running, current number of generation: " + evolutionarySystem.getCurrentNumberOfGenerations());
            }
            else{
                Map<Integer, Double> process = evolutionarySystem.getGenerationFitnessHistory();
                if(process.size() > 0){
                    result.setResult(process);
                }
                else{
                    result.setErrorMessage("Algorithm should start first at least one time");
                }
            }
        }
        else{
            result.setErrorMessage("File should be loaded first");
        }

        action.accept(result);
    }

    @Override
    public String getCommandName() {
        return "System process details";
    }
}
