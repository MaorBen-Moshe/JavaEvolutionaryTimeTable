package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

public class StopCommand extends CommandImpel{
    public StopCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        super(wrapper);
    }

    @Override
    public void execute() throws Exception {
        getEngineWrapper().getEngine().pauseProcess();
    }

    @Override
    public String getCommandName() {
        return "Stop algorithm";
    }
}