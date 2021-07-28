package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import models.BestSolutionItem;
import mutation.Mutation;
import selection.Selection;

import java.util.List;
import java.util.Set;

public interface EvolutionarySystem<T> {
    enum TerminateRules {
        NumberOfGenerations, ByFitness
    };

    BestSolutionItem<T> StartAlgorithm(Set<TerminateRules> terminateBy);

    BestSolutionItem<T> getBestSolution();

    double getFitness(T item) throws ClassNotFoundException;

    boolean IsRunningProcess();

    int getInitialPopulationSize();

    Selection<T> getSelection();

    Crossover<T> getCrossover();

    List<Mutation<T>> getMutations();

    List<Double> getGenerationFitnessHistory();

    void setAcceptedFitness(double acceptedFitness);

    void setAcceptedNumberOfGenerations(int acceptedNumberOfGenerations);

    int getCurrentNumberOfGenerations();

    void setJumpInGenerations(int jumpInGenerations);

    int getJumpInGenerations();
}
