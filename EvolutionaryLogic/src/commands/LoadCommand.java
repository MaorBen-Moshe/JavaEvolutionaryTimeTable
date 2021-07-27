package commands;

import utils.ETTXmlParser;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoadCommand extends CommandImpel{
    private Consumer<?> after;
    private Supplier<String> before;

    public LoadCommand(Supplier<String> before, Consumer<?> after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public void execute() throws Exception {
        String input = before.get();
        evolutionarySystem = ETTXmlParser.parse(input);
        isFileLoaded = true;
        after.accept(null);
    }

    @Override
    public String getCommandName() {
        return "load file";
    }
}
