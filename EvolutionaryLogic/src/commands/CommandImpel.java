package commands;

import evolutinary.EvolutionarySystem;
import models.TimeTable;

public abstract class CommandImpel implements Command{
    protected EvolutionarySystem<TimeTable> evolutionarySystem;
    protected boolean isFileLoaded = false;
}
