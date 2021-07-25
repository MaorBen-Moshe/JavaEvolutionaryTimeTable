package commands;

import java.util.function.Consumer;

public class LoadCommand implements Command{
    private Consumer<?> action;

    public LoadCommand(Consumer<?> o) {
        this.action = o;
    }

    @Override
    public void execute() {
        action.accept(null);
    }

    @Override
    public String getCommandName() {
        return "load file";
    }
}
