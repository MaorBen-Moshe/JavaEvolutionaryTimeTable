package commands;

import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

public abstract class CommandImpel implements Command{
    protected static EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> evolutionarySystem;
    protected static boolean isFileLoaded = false;
}
