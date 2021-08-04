package commands;

import DTO.FitnessHistoryItemDTO;
import models.FitnessHistoryItem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProcessCommand extends CommandImpel{

    private Consumer<CommandResult<List<FitnessHistoryItemDTO>>> action;

    public ProcessCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                          Consumer<CommandResult<List<FitnessHistoryItemDTO>>> o) {
        super(wrapper);
        this.action = o;
    }

    @Override
    public void execute() {
        CommandResult<List<FitnessHistoryItemDTO>> result = new CommandResult<>();
        if(engineWrapper.isFileLoaded()){
            List<FitnessHistoryItem> process = engineWrapper.getEngine().getGenerationFitnessHistory();
            if(engineWrapper.getEngine().IsRunningProcess()){
                process = process.stream()
                        .sorted(Collections.reverseOrder())
                        .limit(10)
                        .collect(Collectors.toList());

                result.setResult(createAnswer(process));
            }
            else{
                if(process.size() > 0){
                    result.setResult(createAnswer(process));
                }
                else {
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

    private List<FitnessHistoryItemDTO> createAnswer(List<FitnessHistoryItem> old){
        List<FitnessHistoryItemDTO> ret = new ArrayList<>();
        old.forEach(item -> ret.add(new FitnessHistoryItemDTO(item.getGenerationNumber(), item.getCurrentGenerationFitness(), item.getImprovementFromLastGeneration())));

        ret.sort(FitnessHistoryItemDTO::compareTo);
        return ret;
    }
}