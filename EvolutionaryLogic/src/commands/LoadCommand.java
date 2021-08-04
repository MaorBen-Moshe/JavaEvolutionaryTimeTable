package commands;

import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import utils.ETTXmlParser;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoadCommand extends CommandImpel{
    private Consumer<?> after;
    private Supplier<String> before;

    public LoadCommand(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper,
                       Supplier<String> before,
                       Consumer<?> after) {
        super(wrapper);
        this.before = before;
        this.after = after;
    }

    @Override
    public void execute() throws Exception {
        String input = before.get();
        engineWrapper.setEngine(ETTXmlParser.parse(input));
        engineWrapper.setFileLoaded(true);
        after.accept(null);
    }

    @Override
    public String getCommandName() {
        return "load file";
    }
}
