package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import models.BestSolutionItem;
import mutation.Mutation;
import selection.Selection;

import java.util.List;
import java.util.Set;

public interface EvolutionarySystem<T, S extends DataSupplier> {
    enum TerminateRules {
        NumberOfGenerations, ByFitness
    };

    BestSolutionItem<T, S> StartAlgorithm(Set<TerminateRules> terminateBy);

    BestSolutionItem<T, S> getBestSolution();

    double getFitness(T item) throws ClassNotFoundException;

    boolean IsRunningProcess();

    int getInitialPopulationSize();

    Selection<T> getSelection();

    Crossover<T, S> getCrossover();

    List<Mutation<T, S>> getMutations();

    List<Double> getGenerationFitnessHistory();

    S getSystemInfo();

    void setAcceptedFitness(double acceptedFitness);

    void setAcceptedNumberOfGenerations(int acceptedNumberOfGenerations);

    int getCurrentNumberOfGenerations();

    int getAcceptedNumberOfGenerations();

    void setJumpInGenerations(int jumpInGenerations);

    int getJumpInGenerations();
}
