package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

public class StopCommand extends CommandImpel{
    public StopCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        super(wrapper);
    }

    @Override
    public void execute() throws Exception {
        if(getEngineWrapper().isFileLoaded()){
           if(getEngineWrapper().getEngine().isRunningProcess() || getEngineWrapper().getEngine().isPauseOccurred()){
               getEngineWrapper().getEngine().stopProcess();
           }
        }
    }

    @Override
    public String getCommandName() {
        return "Stop algorithm";
    }
}