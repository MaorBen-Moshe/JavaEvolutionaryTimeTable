package commands;

public class ExitCommand extends CommandImpel{
    private final Runnable beforeExit;

    public ExitCommand(Runnable beforeExit){ this.beforeExit = beforeExit; }

    @Override
    public void execute() {
        beforeExit.run();
        if(evolutionarySystem != null && evolutionarySystem.IsRunningProcess()){
            evolutionarySystem.stopProcess();
        }

        System.exit(0);
    }

    @Override
    public String getCommandName() {
        return "Exit";
    }
}