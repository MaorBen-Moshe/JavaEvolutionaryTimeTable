package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import utils.ETTXmlParser;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoadCommand extends CommandImpel{
    private final Consumer<String> after;
    private final Supplier<String> before;
    private final Supplier<Boolean> ifRunningAlready;

    public LoadCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                       Supplier<String> before,
                       Consumer<String> after,
                       Supplier<Boolean> ifRunningProcess) {
        super(wrapper);
        this.before = before;
        this.after = after;
        this.ifRunningAlready = ifRunningProcess;
    }

    @Override
    public void execute() throws Exception {
        if(getEngineWrapper().isFileLoaded() && getEngineWrapper().getEngine().isRunningProcess()){
            if(ifRunningAlready.get()){
                getEngineWrapper().getEngine().stopProcess();
                loadFile();
            }
        }
        else{
            loadFile();
        }
    }

    @Override
    public String getCommandName() {
        return "load file";
    }

    private void loadFile() throws Exception{
        String input = before.get();
        getEngineWrapper().setEngine(ETTXmlParser.parse(input));
        after.accept(input);
    }
}