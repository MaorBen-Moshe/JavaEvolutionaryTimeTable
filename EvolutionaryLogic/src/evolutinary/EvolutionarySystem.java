package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import models.BestSolutionItem;
import models.TerminateRule;
import mutation.Mutation;
import selection.Selection;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EvolutionarySystem<T, S extends DataSupplier> {

    enum TerminateRules {
        ByFitness, NumberOfGenerations
    }

    BestSolutionItem<T, S> StartAlgorithm(Set<TerminateRule> terminateBy, int jumpInGenerations);

    boolean IsRunningProcess();

    int getInitialPopulationSize();

    Selection<T> getSelection();

    Crossover<T, S> getCrossover();

    List<Mutation<T, S>> getMutations();

    Map<Integer, Double> getGenerationFitnessHistory();

    S getSystemInfo();

    BestSolutionItem<T, S> getBestSolution();

    int getCurrentNumberOfGenerations();
}