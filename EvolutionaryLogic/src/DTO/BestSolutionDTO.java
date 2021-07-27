package DTO;

import models.BestSolutionItem;

public class BestSolutionDTO extends BestSolutionItem<TimeTableDTO> {
    public BestSolutionDTO(TimeTableDTO table, double fitness){
        super(table, fitness);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
