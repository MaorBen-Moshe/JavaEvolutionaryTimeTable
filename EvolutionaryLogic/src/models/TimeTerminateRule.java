package models;

import evolutinary.EvolutionarySystem;

public class TimeTerminateRule extends TerminateRule{
    private final long timeInMinutes;

    public TimeTerminateRule(long timeInMinutes) {
        super(EvolutionarySystem.TerminateRules.ByTime);
        if(timeInMinutes < 0){
            throw new IllegalArgumentException("time in terminate rule must be positive");
        }

        this.timeInMinutes = timeInMinutes;
    }

    @Override
    public boolean isTerminate(Object... args) {
        if(args != null && args.length != 1 && !(args[0] instanceof Long)){
            throw new IllegalArgumentException("Time terminate rule accept only one value and the value must be an integer that represent the current time in minutes pass from the beginning of the algorithm");
        }

        long current = (long) (args != null ? args[0] : null);
        if(current < 0){
            throw new IllegalArgumentException("Time terminate rule accept only positive time in minutes");
        }

        return current >= this.timeInMinutes;
    }
}