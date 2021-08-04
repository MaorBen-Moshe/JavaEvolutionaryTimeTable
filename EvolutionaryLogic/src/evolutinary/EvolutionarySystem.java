package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import models.BestSolutionItem;
import models.FitnessHistoryItem;
import models.JumpInGenerationsResult;
import models.TerminateRule;
import mutation.Mutation;
import selection.Selection;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface EvolutionarySystem<T, S extends DataSupplier> {

    enum TerminateRules {
        ByFitness, NumberOfGenerations
    }

    void StartAlgorithm(Set<TerminateRule> terminateBy, int jumpInGenerations, Consumer<JumpInGenerationsResult> listener);

    boolean IsRunningProcess();

    int getInitialPopulationSize();

    Selection<T> getSelection();

    Crossover<T, S> getCrossover();

    List<Mutation<T, S>> getMutations();

    List<FitnessHistoryItem> getGenerationFitnessHistory();

    S getSystemInfo();

    BestSolutionItem<T, S> getBestSolution();

    int getCurrentNumberOfGenerations();

    void stopProcess();
}