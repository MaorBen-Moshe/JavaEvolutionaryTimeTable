package commands;

import Interfaces.DataSupplier;
import evolutinary.EvolutionarySystem;
import models.TimeTable;

public abstract class CommandImpel implements Command{
    protected static EvolutionarySystem<TimeTable> evolutionarySystem;
    protected static boolean isFileLoaded = false;
}
