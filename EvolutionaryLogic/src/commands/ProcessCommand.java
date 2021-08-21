package commands;

import DTO.*;
import models.FitnessHistoryItem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.sql.Time;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProcessCommand extends CommandImpel{

    private final Consumer<CommandResult<List<FitnessHistoryItemDTO<TimeTableDTO>>>> action;

    public ProcessCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                          Consumer<CommandResult<List<FitnessHistoryItemDTO<TimeTableDTO>>>> o) {
        super(wrapper);
        this.action = o;
    }

    @Override
    public void execute() {
        CommandResult<List<FitnessHistoryItemDTO<TimeTableDTO>>> result = new CommandResult<>();
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

    private List<FitnessHistoryItemDTO<TimeTableDTO>> createAnswer(List<FitnessHistoryItem<TimeTable>> old){
        List<FitnessHistoryItemDTO<TimeTableDTO>> ret = new ArrayList<>();
        old.forEach(item -> ret.add(new FitnessHistoryItemDTO<>(createDTOTable(item.getSolution()), item.getGenerationNumber(), item.getCurrentGenerationFitness(), item.getImprovementFromLastGeneration())));
        ret.sort(FitnessHistoryItemDTO::compareTo);
        return ret;
    }

    private TimeTableDTO createDTOTable(TimeTable old){
        ModelToDTOConverter converter = new ModelToDTOConverter();
        Set<TimeTableItemDTO> items = new HashSet<>();
        old.getSortedItems().forEach(item -> items.add(new TimeTableItemDTO(item.getDay(), item.getHour(), converter.createClassDTO(item.getSchoolClass()), converter.createSubjectDTO(item.getSubject()), converter.createTeacherDTO(item.getTeacher()))));
        Map<RuleDTO, Double> rules = converter.createRuleMapDTO(old.getRulesScore());
        return new TimeTableDTO(items, rules, old.getHardRulesAvg(), old.getSoftRulesAvg());
    }
}