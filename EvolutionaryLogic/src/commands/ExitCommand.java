package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.function.Supplier;

public class ExitCommand extends CommandImpel{
    private final Runnable exitRun;
    private final Supplier<Boolean> ifRunning;

    public ExitCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                       Runnable exitRun,
                       Supplier<Boolean> ifRunning){
        super(wrapper);
        this.ifRunning = ifRunning;
        this.exitRun = exitRun;
    }

    @Override
    public void execute() {
        if(getEngineWrapper().getEngine() != null && getEngineWrapper().getEngine().isRunningProcess()){
            if(ifRunning.get()){
                getEngineWrapper().getEngine().stopProcess();
                exitRun.run();
            }
        }
        else{
            exitRun.run();
        }
    }

    @Override
    public String getCommandName() {
        return "Exit";
    }
}