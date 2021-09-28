package utils.models;

import evolutinary.EvolutionarySystem;
import evolutinary.TimeTableEvolutionarySystemImpel;
import models.JumpInGenerationsResult;
import models.TerminateRule;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.Set;
import java.util.function.Consumer;

public class ProblemConfigurations {
    private Set<TerminateRule> terminateRules;
    private final TimeTableEvolutionarySystemImpel system;
    private final Object lock;
    private int jumps;

    public ProblemConfigurations(TimeTableEvolutionarySystemImpel system){
        lock = new Object();
        this.system = system;
    }

    public int getGenerationNumber(){
        return (system == null || system.getGenerationFitnessHistory().size() <= 0) ? 0 : system.getCurrentNumberOfGenerations();
    }

    public double getCurrentBestFitness(){
        if(system == null){
            throw new IllegalArgumentException("Algorithm is Not running yet");
        }

        return system.getBestSolution() != null ? system.getBestSolution().getFitness() : 0;
    }

    public EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> getSystem(){return system;}

    public void setTerminateRules(Set<TerminateRule> terminateRules, int jumps) {
        this.terminateRules = terminateRules;
        this.jumps = jumps;

        if(system != null){
            system.clearTerminateRules();
            this.terminateRules.forEach(system::addTerminateRule);
        }
    }

    public void run(Consumer<JumpInGenerationsResult> onRun){
        if(system == null){
            throw new IllegalArgumentException("Please enter system info first");
        }

        if(jumps == 0 || terminateRules == null || terminateRules.size() <= 0){
            throw new IllegalArgumentException("Please enter system terminate rules first");
        }

        if(system.isRunningProcess() || system.isPauseOccurred()){
            throw new IllegalArgumentException("Algorithm still processing");
        }

        new Thread(() -> system.StartAlgorithm(lock, terminateRules, jumps, onRun)).start();
    }

    public void pause(){
        if(system == null){
            throw new IllegalArgumentException("Please enter system info first");
        }

        if(!system.isRunningProcess()){
            throw new IllegalArgumentException("Algorithm should be running first");
        }

        system.pauseProcess();
    }

    public void resume(){
        if(system == null){
            throw new IllegalArgumentException("Please enter system info first");
        }

        if(system.isRunningProcess()){
            throw new IllegalArgumentException("Algorithm already running");
        }

        if(!system.isPauseOccurred()){
            throw new IllegalArgumentException("Algorithm should be paused first");
        }

        synchronized (lock){
            system.resumeProcess();
            lock.notifyAll();
        }
    }

    public void stop(){
        if(system == null){
            throw new IllegalArgumentException("Please enter system info first");
        }

        if(system.isPauseOccurred()){
            system.stopProcess();
            lock.notifyAll();
        }

        if(system.isRunningProcess()){
            system.stopProcess();
        }
    }

}