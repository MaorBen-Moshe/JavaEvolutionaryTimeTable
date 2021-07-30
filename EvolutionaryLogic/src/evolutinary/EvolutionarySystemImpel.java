package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import models.GenerationTerminateRule;
import models.JumpInGenerationsResult;
import models.TerminateRule;
import mutation.Mutation;
import selection.Selection;
import models.BestSolutionItem;

import java.util.*;
import java.util.function.Consumer;

// T is a population item
public abstract class EvolutionarySystemImpel<T, S extends DataSupplier> implements EvolutionarySystem<T, S> {
    private Map<T, Double> population;
    private final Map<T, Double> childPopulation;

    @Override
    public Map<Integer, Double> getGenerationFitnessHistory() {
        return new HashMap<>(generationFitnessHistory);
    }

    private final Map<Integer, Double> generationFitnessHistory; // cell 0: best fitness of generation 1 and so on... (by jump in generations)
    private BestSolutionItem<T, S> bestItem;
    private boolean isRunning = false;

    public int getCurrentNumberOfGenerations() {
        return currentNumberOfGenerations;
    }

    private int currentNumberOfGenerations;

    public void setCrossover(Crossover<T, S> crossover) {
        this.crossover = crossover;
    }

    public void setMutations(List<Mutation<T, S>> mutations) {
        this.mutations = mutations;
        this.unModifiedMutations = Collections.unmodifiableList(this.mutations);
    }

    @Override
    public Selection<T> getSelection() {
        return selection;
    }

    @Override
    public Crossover<T, S> getCrossover() {
        return crossover;
    }

    @Override
    public List<Mutation<T, S>> getMutations() {
        return unModifiedMutations;
    }

    public void setSelection(Selection<T> selection) {
        this.selection = selection;
    }

    public void setInitialPopulationSize(int initialPopulationSize) {
        this.initialPopulationSize = initialPopulationSize;
    }

    private Crossover<T, S> crossover;
    private Selection<T> selection;
    private List<Mutation<T, S>> mutations;
    private List<Mutation<T, S>> unModifiedMutations;
    private int initialPopulationSize;

    protected EvolutionarySystemImpel(){
        population = new HashMap<>();
        childPopulation = new HashMap<>();
        generationFitnessHistory = new HashMap<>();
    }

    @Override
    public BestSolutionItem<T, S> StartAlgorithm(Set<TerminateRule> terminateBy, int jumpInGenerations, Consumer<JumpInGenerationsResult> listener) {
        jumpInGenerations = jumpInGenerations <= 0 ? 1 : jumpInGenerations;
        isRunning = true;
        generationFitnessHistory.clear();
        try{
            /* initial*/
            initialPopulation();
            bestItem = evaluateGeneration();
            generationFitnessHistory.put(0, bestItem.getFitness());
            /* iterative*/
            while(!isTerminate(terminateBy)){
                createGeneration();
                bestItem = evaluateGeneration();
                currentNumberOfGenerations++;
                if(currentNumberOfGenerations % jumpInGenerations == 0){
                    generationFitnessHistory.put(currentNumberOfGenerations, bestItem.getFitness());
                    listener.accept(new JumpInGenerationsResult(bestItem.getFitness(), currentNumberOfGenerations));
                }
            }
        } catch (Exception e){
            isRunning = false;
            throw e;
        }

        isRunning = false;
        return bestItem;
    }

    @Override
    public BestSolutionItem<T, S> getBestSolution() {
        return bestItem;
    }

    @Override
    public boolean IsRunningProcess() {
        return isRunning;
    }

    @Override
    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    private void initialPopulation(){
        for(int i =0; i < initialPopulationSize; i++){
            T option = createOptionalSolution();
            population.put(option, 0.);
        }
    }

    private BestSolutionItem<T, S> evaluateGeneration() {
        BestSolutionItem<T, S> retVal = null;
        population.keySet().forEach(item -> population.replace(item, evaluate(item)));
        Optional<Map.Entry<T, Double>> optional = population.entrySet()
                                                            .stream()
                                                            .max(Comparator.comparingDouble(Map.Entry::getValue));

        Map.Entry<T, Double> temp = optional.orElse(null);
        if(temp != null){
            retVal = new BestSolutionItem<>(temp.getKey(), temp.getValue(), getSystemInfo());
        }

        return retVal;
    }

    protected void createGeneration(){
        S supplier = getSystemInfo();
        Map<T, Double> selected = selection.select(population);
        while(childPopulation.size() < population.size()){
            Set<T> children = crossover.crossover(selected, supplier);
            children.forEach(child ->{
                mutations.forEach(mutation -> mutation.mutate(child, supplier));
                childPopulation.put(child, 0.);
            });
        }

        population = new HashMap<>(childPopulation);
        childPopulation.clear();
    }

    protected abstract double evaluate(T optional);

    protected abstract T createOptionalSolution();

    private boolean isTerminate(Set<TerminateRule> terminateBy) {
        boolean answer = false;
        for (TerminateRule terminate : terminateBy) {
            switch (terminate.getType()){
                case NumberOfGenerations:
                    answer = terminate.isTerminate(currentNumberOfGenerations);  break;
                case ByFitness: answer =  terminate.isTerminate(bestItem.getFitness()); break;
                default: answer = false; break;
            }

            if(answer){
                break;
            }
        }

        return answer;
    }
}