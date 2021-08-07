package commands;

import DTO.ModelToDTOConverter;
import DTO.SystemInfoDTO;
import evolutinary.TimeTableEvolutionarySystemImpel;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.function.Consumer;

public class SystemInfoCommand extends CommandImpel{

    private final Consumer<CommandResult<SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier>>> action;

    public SystemInfoCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                             Consumer<CommandResult<SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier>>> o) {
        super(wrapper);
        this.action = o;
    }

    @Override
    public void execute() {
        CommandResult<SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier>> result = new CommandResult<>();
        if(getEngineWrapper().isFileLoaded()){
            result.setResult(createAnswer());
        }
        else{
            result.setErrorMessage("Please load a file first");
        }

        action.accept(result);
    }

    @Override
    public String getCommandName() {
        return "System Information";
    }

    private SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier> createAnswer(){
        ModelToDTOConverter converter = new ModelToDTOConverter();
        TimeTableEvolutionarySystemImpel system = (TimeTableEvolutionarySystemImpel) getEngineWrapper().getEngine();
        return new SystemInfoDTO<>(system.getDays(),
                system.getHours(),
                converter.createTeachersFromMap(system.getTeachers()),
                converter.createClassesFromMap(system.getClasses()),
                converter.createSubjectsFromMap(system.getSubjects()),
                converter.createRulesObjectDTO(system.getRules()),
                system.getInitialPopulationSize(),
                system.getSelection(),
                system.getCrossover(),
                system.getMutations());
    }
}