package commands;

import java.util.function.Consumer;

public class StartSystemCommand implements Command{

    private Consumer<?> action;

    public StartSystemCommand(Consumer<?> o) {
        this.action = o;
    }

    @Override
    public void execute() {
        action.accept(null);
    }

    @Override
    public String getCommandName() {
        return "Start evolutionary system";
    }
}
