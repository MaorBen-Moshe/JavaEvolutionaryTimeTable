package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import models.*;
import mutation.Mutation;
import selection.Selection;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface EvolutionarySystem<T, S extends DataSupplier> extends Serializable {

    enum TerminateRules {
        ByFitness, NumberOfGenerations, ByTime
    }

    void StartAlgorithm(Object lock, Set<TerminateRule> terminateBy, int jumpInGenerations, Consumer<JumpInGenerationsResult> listener);

    boolean isRunningProcess();

    boolean isPauseOccurred();

    int getInitialPopulationSize();

    Selection<T> getSelection();

    void setSelection(Selection<T> selection);

    void setCrossover(Crossover<T, S> crossover);

    void addTerminateRule(TerminateRule terminate);

    void clearTerminateRules();

    Crossover<T, S> getCrossover();

    List<Mutation<T, S>> getMutations();

    int getElitism();

    void setElitism(int elitism);

    List<FitnessHistoryItem<T>> getGenerationFitnessHistory();

    S getSystemInfo();

    BestSolutionItem<T, S> getBestSolution();

    int getCurrentNumberOfGenerations();

    void stopProcess();

    void pauseProcess();

    void resumeProcess();
}