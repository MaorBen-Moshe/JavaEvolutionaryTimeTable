package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

public class ResumeCommand extends CommandImpel{

    public ResumeCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        super(wrapper);
    }

    @Override
    public void execute() throws Exception {
        getEngineWrapper().getEngine().resumeProcess();
    }

    @Override
    public String getCommandName() {
        return "Resume algorithm";
    }
}