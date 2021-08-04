package commands;

import evolutinary.EvolutionarySystem;
import evolutinary.TimeTableEvolutionarySystemImpel;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

public abstract class CommandImpel implements Command{
    protected EngineWrapper<TimeTable, TimeTableSystemDataSupplier> engineWrapper;
    protected CommandImpel(EngineWrapper<TimeTable, TimeTableSystemDataSupplier> wrapper){
        this.engineWrapper = wrapper;
    }
}
