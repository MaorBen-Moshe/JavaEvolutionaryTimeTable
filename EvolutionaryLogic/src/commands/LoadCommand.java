package commands;

import models.ResultParse;
import utils.ETTXmlParser;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoadCommand implements Command{
    private Consumer<ResultParse> after;
    private Supplier<String> before;

    public LoadCommand(Supplier<String> before, Consumer<ResultParse> after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public void execute() throws Exception {
        String input = before.get();
        ResultParse system = ETTXmlParser.parse(input);
        after.accept(system);
    }

    @Override
    public String getCommandName() {
        return "load file";
    }
}
