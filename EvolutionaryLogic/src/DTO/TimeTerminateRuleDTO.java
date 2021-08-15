package DTO;

import evolutinary.EvolutionarySystem;

import java.util.Objects;

public class TimeTerminateRuleDTO extends TerminateRuleDTO{
    private final long timeInMinutes;

    public TimeTerminateRuleDTO(long timeInMinutes) {
        super(EvolutionarySystem.TerminateRules.ByTime);
        if(timeInMinutes < 0){
            throw new IllegalArgumentException("time in terminate rule must be positive");
        }

        this.timeInMinutes = timeInMinutes;
    }

    public long getTimeInMinutes() {
        return timeInMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TimeTerminateRuleDTO that = (TimeTerminateRuleDTO) o;
        return timeInMinutes == that.timeInMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeInMinutes);
    }
}
