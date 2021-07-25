package commands;

import java.util.function.Consumer;

public class SystemInfoCommand implements Command{

    private Consumer<?> action;

    public SystemInfoCommand(Consumer<?> o) {
        this.action = o;
    }

    @Override
    public void execute() {
        action.accept(null);
    }

    @Override
    public String getCommandName() {
        return "System Information";
    }
}
