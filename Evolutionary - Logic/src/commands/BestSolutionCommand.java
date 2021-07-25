package commands;

import java.util.function.Consumer;

public class BestSolutionCommand implements Command{
    private Consumer<?> action;

    public BestSolutionCommand(Consumer<?> o) {
        this.action = o;
    }

    @Override
    public void execute() {
        action.accept(null);
    }

    @Override
    public String getCommandName() {
        return "Best Solution";
    }
}
