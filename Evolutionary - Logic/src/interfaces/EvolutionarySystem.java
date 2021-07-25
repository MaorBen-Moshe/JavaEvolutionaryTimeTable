package interfaces;

import java.util.Map;

public interface EvolutionarySystem<T> {
    Map.Entry<T, Integer> StartAlgorithm();

    T getBestSolution();

    int getFitness(T item) throws ClassNotFoundException;

    boolean IsRunningProcess();
}
