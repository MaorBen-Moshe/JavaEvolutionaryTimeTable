package commands;

import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.function.Supplier;

public class LoadSystemCommand extends CommandImpel{
    private final Supplier<String> getPath;
    private final Runnable afterLoad;
    private final Runnable whenRunning;

    public LoadSystemCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                             Runnable afterLoad,
                             Runnable whenRunning,
                             Supplier<String> getPath) {
        super(wrapper);
        this.afterLoad = afterLoad;
        this.whenRunning = whenRunning;
        this.getPath = getPath;
    }

    @Override
    public void execute() throws Exception {
        if(getEngineWrapper().getEngine() != null && getEngineWrapper().getEngine().isRunningProcess()){
            whenRunning.run();
            return;
        }

        String path = getPath.get();
        if(path != null){
            try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
                getEngineWrapper().setEngine((EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier>) in.readObject());
                afterLoad.run();
            }
        }
    }

    @Override
    public String getCommandName() {
        return "Load system info from file";
    }
}