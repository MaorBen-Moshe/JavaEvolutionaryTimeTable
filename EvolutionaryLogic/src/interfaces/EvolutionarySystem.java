package interfaces;

import models.BestSolutionItem;

import java.util.Map;

public interface EvolutionarySystem<T> {
    BestSolutionItem<T> StartAlgorithm();

    BestSolutionItem<T> getBestSolution();

    double getFitness(T item) throws ClassNotFoundException;

    boolean IsRunningProcess();
}
