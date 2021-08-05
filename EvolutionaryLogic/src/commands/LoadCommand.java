package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import utils.ETTXmlParser;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoadCommand extends CommandImpel{
    private Consumer<?> after;
    private Supplier<String> before;
    private Supplier<Boolean> ifRunningAlready;

    public LoadCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                       Supplier<String> before,
                       Consumer<?> after,
                       Supplier<Boolean> ifRunningProcess) {
        super(wrapper);
        this.before = before;
        this.after = after;
        this.ifRunningAlready = ifRunningProcess;
    }

    @Override
    public void execute() throws Exception {
        if(engineWrapper.isFileLoaded() && engineWrapper.getEngine().IsRunningProcess()){
            if(ifRunningAlready.get()){
                engineWrapper.getEngine().stopProcess();
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
        engineWrapper.setEngine(ETTXmlParser.parse(input));
        after.accept(null);
    }
}
