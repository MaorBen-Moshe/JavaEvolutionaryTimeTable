package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.function.Supplier;

public class SaveSystemCommand extends CommandImpel{

    private final Supplier<String> getPath;
    private final Supplier<Boolean> whenSavedDataAlready;
    private final Runnable whenNullSystem;
    private final Runnable whenRunningProcess;
    private final Runnable afterSave;

    public SaveSystemCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                             Runnable afterSave,
                             Supplier<String> getPath,
                             Supplier<Boolean> whenSavedDataAlready,
                             Runnable whenNullSystem,
                             Runnable whenRunningProcess) {
        super(wrapper);
        this.afterSave = afterSave;
        this.getPath = getPath;
        this.whenSavedDataAlready = whenSavedDataAlready;
        this.whenNullSystem = whenNullSystem;
        this.whenRunningProcess = whenRunningProcess;
    }

    @Override
    public void execute() throws Exception {
        if(!getEngineWrapper().isFileLoaded()){
            whenNullSystem.run();
        }
        else if(getEngineWrapper().getEngine().isRunningProcess()){
            whenRunningProcess.run();
        }
        else{
            String path = getPath.get();
            File file = new File(path + "\\evoInfo");
            boolean res = file.createNewFile();
            if(!res){
                if(!whenSavedDataAlready.get()){
                    return;
                }

                file.deleteOnExit();
                file.createNewFile();
            }

            path = file.getAbsolutePath();
            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
                out.writeObject(getEngineWrapper().getEngine());
                afterSave.run();
            }
        }
    }

    @Override
    public String getCommandName() {
        return "Save System info to file";
    }
}