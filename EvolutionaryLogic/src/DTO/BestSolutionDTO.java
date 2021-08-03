package DTO;

import models.BestSolutionItem;

public class BestSolutionDTO extends BestSolutionItem<TimeTableDTO, TimeTableSystemDataSupplierDTO> {

    public BestSolutionDTO(TimeTableDTO table, double fitness, int generationCreated, TimeTableSystemDataSupplierDTO supplier){
        super(table, fitness, generationCreated, supplier);
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
