package evolutinary;

import models.BestSolutionItem;
import java.util.Set;

public interface EvolutionarySystem<T> {
    enum TerminateRules {
        NumberOfGenerations, ByFitness
    };

    BestSolutionItem<T> StartAlgorithm(Set<TerminateRules> terminateBy);

    BestSolutionItem<T> getBestSolution();

    double getFitness(T item) throws ClassNotFoundException;

    boolean IsRunningProcess();

    void setAcceptedFitness(double acceptedFitness);

    void setAcceptedNumberOfGenerations(int acceptedNumberOfGenerations);

    int getCurrentNumberOfGenerations();

    void setJumpInGenerations(int jumpInGenerations);

    int getJumpInGenerations();
}
