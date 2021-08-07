package commands;

import DTO.*;
import models.*;

import java.util.*;
import java.util.function.Consumer;

public class BestSolutionCommand extends CommandImpel{
    private final Consumer<CommandResult<BestSolutionDTO>> after;

    public BestSolutionCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                               Consumer<CommandResult<BestSolutionDTO>> after) {
        super(wrapper);
        this.after = after;
    }

    @Override
    public void execute() {
        CommandResult<BestSolutionDTO> result = new CommandResult<>();
        if(getEngineWrapper().isFileLoaded()){
          if(getEngineWrapper().getEngine().getBestSolution() != null){
              result.setResult(createAnswer());
          }
          else{
              result.setErrorMessage("Algorithm should start first at least one time.");
          }
        }
        else{
            result.setErrorMessage("There is not a loaded file");
        }

        after.accept(result);
    }

    @Override
    public String getCommandName() {
        return "Best Solution";
    }

    private BestSolutionDTO createAnswer(){
        ModelToDTOConverter converter = new ModelToDTOConverter();
        BestSolutionItem<TimeTable, TimeTableSystemDataSupplier> solution = getEngineWrapper().getEngine().getBestSolution();
        TimeTable table = solution.getSolution();
        Set<TimeTableItemDTO> set = new TreeSet<>();
        table.getSortedItems().forEach(item -> set.add(new TimeTableItemDTO(item.getDay(),
                item.getHour(),
                converter.createClassDTO(item.getSchoolClass()),
                converter.createSubjectDTO(item.getSubject()),
                converter.createTeacherDTO(item.getTeacher()))));

        TimeTableDTO newTable = new TimeTableDTO(set, converter.createRuleMapDTO(table.getRulesScore()),
                table.getHardRulesAvg(), table.getSoftRulesAvg());
        TimeTableSystemDataSupplierDTO supplier = new ModelToDTOConverter().createDataSupplierDTO(getEngineWrapper().getEngine().getSystemInfo());
        return new BestSolutionDTO(newTable, solution.getFitness(), solution.getGenerationCreated(), supplier);
    }
}