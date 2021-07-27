package commands.bestDisplayCommands;

import commands.Command;
import commands.CommandImpel;

import java.util.function.Consumer;

public class ClassDisplayCommand extends CommandImpel {
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
