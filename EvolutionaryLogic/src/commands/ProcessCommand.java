package commands;

import java.util.function.Consumer;

public class ProcessCommand implements Command{

    private Consumer<?> action;

    public ProcessCommand(Consumer<?> o) {
        this.action = o;
    }
    @Override
    public void execute() {
        action.accept(null);
    }

    @Override
    public String getCommandName() {
        return "System process details";
    }
}
