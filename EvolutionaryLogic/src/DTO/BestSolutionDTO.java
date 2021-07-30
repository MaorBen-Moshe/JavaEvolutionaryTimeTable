package DTO;

import models.BestSolutionItem;

public class BestSolutionDTO extends BestSolutionItem<TimeTableDTO, TimeTableSystemDataSupplierDTO> {

    public BestSolutionDTO(TimeTableDTO table, double fitness, TimeTableSystemDataSupplierDTO supplier){
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
