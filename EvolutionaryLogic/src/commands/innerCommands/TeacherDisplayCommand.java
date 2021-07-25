package commands.innerCommands;

import commands.Command;

import java.util.function.Consumer;

public class TeacherDisplayCommand implements Command {
    private Consumer<?> action;

    public TeacherDisplayCommand(Consumer<?> o) {
        this.action = o;
    }

    @Override
    public void execute() {
        action.accept(null);
    }

    @Override
    public String getCommandName() {
        return "Display Teacher";
    }
}
