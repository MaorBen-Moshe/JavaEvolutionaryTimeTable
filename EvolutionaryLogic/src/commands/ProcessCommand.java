package commands;

import java.util.HashMap;
import java.util.List;
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
                result.setErrorMessage("Process is still running, number of generations remain: " +
                        (evolutionarySystem.getAcceptedNumberOfGenerations() - evolutionarySystem.getCurrentNumberOfGenerations()));
            }
            else{
                List<Double> process = evolutionarySystem.getGenerationFitnessHistory();
                if(process.size() > 0){
                    result.setResult(createMap(process));
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

    private Map<Integer, Double> createMap(List<Double> process) {
        int jump = evolutionarySystem.getJumpInGenerations();
        Map<Integer, Double> ret = new HashMap<>();
        int currentGeneration = 0;
        for(double current : process){
            ret.put(currentGeneration, current);
            currentGeneration += jump;
        }

        return ret;
    }

    @Override
    public String getCommandName() {
        return "System process details";
    }
}
