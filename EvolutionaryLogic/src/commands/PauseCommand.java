package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

public class PauseCommand extends CommandImpel{
    public PauseCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        super(wrapper);
    }

    @Override
    public void execute() throws Exception {
        getEngineWrapper().getEngine().pauseProcess();
    }

    @Override
    public String getCommandName() {
        return "Pause algorithm";
    }
}
