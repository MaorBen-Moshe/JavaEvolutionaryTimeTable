package commands;

import java.util.List;
import java.util.function.Consumer;

public class ProcessCommand extends CommandImpel{

    private Consumer<CommandResult<List<Double>>> action;

    public ProcessCommand(Consumer<CommandResult<List<Double>>> o) {
        this.action = o;
    }
    @Override
    public void execute() {
        CommandResult<List<Double>> result = new CommandResult<>();
        if(isFileLoaded){
            List<Double> process = evolutionarySystem.getGenerationFitnessHistory();
            if(process.size() > 0){
                result.setResult(process);
            }
            else{
                result.setErrorMessage("Algorithm should start first at least one time");
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
