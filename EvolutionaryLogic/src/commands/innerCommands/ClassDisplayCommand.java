package commands.innerCommands;

import commands.Command;

import java.util.function.Consumer;

public class ClassDisplayCommand implements Command {
    private Consumer<?> action;

    public ClassDisplayCommand(Consumer<?> o) {
        this.action = o;
    }

    @Override
    public void execute() {
        action.accept(null);
    }

    @Override
    public String getCommandName() {
        return "Display Class";
    }
}
