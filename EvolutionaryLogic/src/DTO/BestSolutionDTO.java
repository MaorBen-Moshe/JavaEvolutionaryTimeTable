package DTO;

import models.BestSolutionItem;
import models.TimeTableSystemDataSupplier;

public class BestSolutionDTO extends BestSolutionItem<TimeTableDTO, TimeTableSystemDataSupplier> {

    public BestSolutionDTO(TimeTableDTO table, double fitness, TimeTableSystemDataSupplier supplier){
        super(table, fitness, supplier);
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
