package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.function.Supplier;

public class ExitCommand extends CommandImpel{
    private final Runnable beforeExit;
    private final Supplier<Boolean> ifRunning;

    public ExitCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                       Runnable beforeExit,
                       Supplier<Boolean> ifRunning){
        super(wrapper);
        this.ifRunning = ifRunning;
        this.beforeExit = beforeExit;
    }

    @Override
    public void execute() {
        if(engineWrapper.getEngine() != null && engineWrapper.getEngine().IsRunningProcess()){
            if(ifRunning.get()){
                engineWrapper.getEngine().stopProcess();
                doExit();
            }
        }
        else{
           doExit();
        }
    }

    @Override
    public String getCommandName() {
        return "Exit";
    }

    private void doExit(){
        beforeExit.run();
        System.exit(0);
    }
}