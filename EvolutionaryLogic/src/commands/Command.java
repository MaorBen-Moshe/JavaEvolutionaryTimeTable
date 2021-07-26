package commands;

public interface Command {
    void execute() throws Exception;

    String getCommandName();
}
