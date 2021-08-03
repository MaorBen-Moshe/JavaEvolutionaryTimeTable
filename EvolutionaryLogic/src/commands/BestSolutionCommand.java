package commands;

import DTO.*;
import models.*;

import java.util.*;
import java.util.function.Consumer;

public class BestSolutionCommand extends CommandImpel{
    private Consumer<CommandResult<BestSolutionDTO>> after;

    public BestSolutionCommand(Consumer<CommandResult<BestSolutionDTO>> after) {
        this.after = after;
    }

    @Override
    public void execute() {
        CommandResult<BestSolutionDTO> result = new CommandResult<>();
        if(isFileLoaded){
          if(evolutionarySystem.getBestSolution() != null){
              result.setResult(createAnswer());
          }
          else{
              result.setErrorMessage("You should start the algorithm first at least one time.");
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
        BestSolutionItem<TimeTable, TimeTableSystemDataSupplier> solution = evolutionarySystem.getBestSolution();
        TimeTable table = solution.getSolution();
        Set<TimeTableItemDTO> set = new TreeSet<>();
        table.getSortedItems().forEach(item -> set.add(new TimeTableItemDTO(item.getDay(),
                item.getHour(),
                converter.createClassDTO(item.getSchoolClass()),
                converter.createSubjectDTO(item.getSubject()),
                converter.createTeacherDTO(item.getTeacher()))));

        TimeTableDTO newTable = new TimeTableDTO(set, converter.createRuleMapDTO(table.getRulesScore()),
                table.getHardRulesAvg(), table.getSoftRulesAvg());
        TimeTableSystemDataSupplierDTO supplier = new ModelToDTOConverter().createDataSupplierDTO(evolutionarySystem.getSystemInfo());
        return new BestSolutionDTO(newTable, solution.getFitness(), solution.getGenerationCreated(), supplier);
    }
}
