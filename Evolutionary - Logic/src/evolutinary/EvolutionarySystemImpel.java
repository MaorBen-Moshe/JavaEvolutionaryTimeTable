package evolutinary;

import interfaces.Crossover;
import interfaces.Mutation;
import interfaces.Selection;
import models.BestSolutionItem;

import java.util.*;

// T is a population item
public abstract class EvolutionarySystemImpel<T> implements interfaces.EvolutionarySystem<T> {

    private Map<T, Double> population;
    private List<Integer> generationFitnessHistory; // cell 0: best fitness of generation 1 and so on...
    private BestSolutionItem<T> bestItem;
    private boolean isRunning = false;

    public Crossover<T> getCrossOver() {
        return crossOver;
    }

    public void setCrossOver(Crossover<T> crossOver) {
        this.crossOver = crossOver;
    }

    public List<Mutation<T>> getMutations() {
        return mutations;
    }

    public void setMutations(List<Mutation<T>> mutations) {
        this.mutations = mutations;
    }

    public Selection<T> getSelection() {
        return selection;
    }

    public void setSelection(Selection<T> selection) {
        this.selection = selection;
    }

    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    public void setInitialPopulationSize(int initialPopulationSize) {
        this.initialPopulationSize = initialPopulationSize;
    }

    public Map<T, Double> getPopulation() {
        return population;
    }

    private Crossover<T> crossOver;
    private Selection<T> selection;
    private List<Mutation<T>> mutations;
    private int initialPopulationSize;

    protected EvolutionarySystemImpel(){
        population = new HashMap<>();
    }

    @Override
    public BestSolutionItem<T> StartAlgorithm() {
        BestSolutionItem<T> currentBestFitness;
        isRunning = true;
        /* initial*/
        initialPopulation();
        currentBestFitness = evaluateGeneration();
        /* iterative*/
        while(!isTerminate()){
            createGeneration();
            currentBestFitness = evaluateGeneration();
        }

        isRunning = false;
        bestItem = currentBestFitness;
        return bestItem;
    }

    @Override
    public BestSolutionItem<T> getBestSolution() {
        return bestItem;
    }

    @Override
    public double getFitness(T item) throws ClassNotFoundException {
        if(population.containsKey(item)){
            return population.get(item);
        }

        throw new ClassNotFoundException(item + " is not found in the population");
    }

    @Override
    public boolean IsRunningProcess() {
        return isRunning;
    }

    private void initialPopulation(){
        for(int i =0; i < initialPopulationSize; i++){
            T option = createOptionalSolution();
            population.put(option, evaluate(option));
        }
    }

    private BestSolutionItem<T> evaluateGeneration() {
        BestSolutionItem<T> retVal = null;
        population.keySet().forEach(item -> population.replace(item, population.get(item), evaluate(item)));
        Optional<Map.Entry<T, Double>> optional = population.entrySet()
                                                            .stream()
                                                            .max(Comparator.comparingDouble(Map.Entry::getValue));

        Map.Entry<T, Double> temp = optional.orElse(null);
        if(temp != null){
            retVal = new BestSolutionItem<>(temp.getKey(), temp.getValue());
        }

        return retVal;
    }

    protected void createGeneration(){
        List<T> selected = selection.select(new ArrayList<>(population.keySet()));
        List<T> children = crossOver.crossOver(selected);
        children.forEach(child -> mutations.forEach(mutation -> mutation.mutate(child)));
        // save children
    }

    protected abstract double evaluate(T optional);

    protected abstract T createOptionalSolution();

    private boolean isTerminate() {
        return false;
    }
}