package commands;

import DTO.FitnessHistoryItemDTO;
import models.FitnessHistoryItem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.sql.Time;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProcessCommand extends CommandImpel{

    private final Consumer<CommandResult<List<FitnessHistoryItemDTO<TimeTable>>>> action;

    public ProcessCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                          Consumer<CommandResult<List<FitnessHistoryItemDTO<TimeTable>>>> o) {
        super(wrapper);
        this.action = o;
    }

    @Override
    public void execute() {
        CommandResult<List<FitnessHistoryItemDTO<TimeTable>>> result = new CommandResult<>();
        if(getEngineWrapper().isFileLoaded()){
            List<FitnessHistoryItem<TimeTable>> process = getEngineWrapper().getEngine().getGenerationFitnessHistory();
            if(getEngineWrapper().getEngine().isRunningProcess()){
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

    private List<FitnessHistoryItemDTO<TimeTable>> createAnswer(List<FitnessHistoryItem<TimeTable>> old){
        List<FitnessHistoryItemDTO<TimeTable>> ret = new ArrayList<>();
        old.forEach(item -> ret.add(new FitnessHistoryItemDTO<>(item.getSolution(), item.getGenerationNumber(), item.getCurrentGenerationFitness(), item.getImprovementFromLastGeneration())));
        ret.sort(FitnessHistoryItemDTO::compareTo);
        return ret;
    }
}