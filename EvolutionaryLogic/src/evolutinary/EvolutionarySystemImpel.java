package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import models.FitnessHistoryItem;
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
    private Crossover<T, S> crossover;
    private Selection<T> selection;
    private List<Mutation<T, S>> mutations;
    private List<Mutation<T, S>> unModifiedMutations;
    private int initialPopulationSize;
    private final List<FitnessHistoryItem> generationFitnessHistory; // cell 0: best fitness of generation 1 and so on... (by jump in generations)
    private BestSolutionItem<T, S> bestItem;
    private boolean isRunning = false;
    private boolean stopOccurred = false;
    private int currentNumberOfGenerations;
    private final Object generationsLock;
    private final Object bestItemLock;

    protected EvolutionarySystemImpel(){
        population = new HashMap<>();
        childPopulation = new HashMap<>();
        generationFitnessHistory = new ArrayList<>();
        generationsLock = new Object();
        bestItemLock = new Object();
    }

    @Override
    public List<FitnessHistoryItem> getGenerationFitnessHistory() {
        return new ArrayList<>(generationFitnessHistory);
    }

    public int getCurrentNumberOfGenerations() {
        synchronized (generationsLock){
            return currentNumberOfGenerations;
        }
    }

    @Override
    public void stopProcess() {
        if(isRunning){
            stopOccurred = true;
        }
    }

    private void incCurrentNumberOfGenerations(){
        synchronized (generationsLock){
            currentNumberOfGenerations++;
        }
    }


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

    @Override
    public void StartAlgorithm(Set<TerminateRule> terminateBy, int jumpInGenerations, Consumer<JumpInGenerationsResult> listener) {
        jumpInGenerations = jumpInGenerations <= 0 ? 1 : jumpInGenerations;
        isRunning = true;
        currentNumberOfGenerations = 0;
        generationFitnessHistory.clear();
        try{
            /* initial*/
            initialAndEvaluatePopulation();
            synchronized (bestItemLock){
                bestItem = getCurrentBestOption();
            }

            generationFitnessHistory.add(new FitnessHistoryItem(getCurrentNumberOfGenerations(),
                                         bestItem.getFitness(),
                                         0));
            /* iterative*/
            while(!isTerminate(terminateBy) && !stopOccurred){
                incCurrentNumberOfGenerations();
                createAndEvaluateGeneration();
                synchronized (bestItemLock){
                    bestItem = getCurrentBestOption();
                }

                if(getCurrentNumberOfGenerations() % jumpInGenerations == 0){
                    double improvement = bestItem.getFitness() - generationFitnessHistory.get(generationFitnessHistory.size() - 1).getCurrentGenerationFitness();
                    FitnessHistoryItem item = new FitnessHistoryItem(getCurrentNumberOfGenerations(),
                                                                     bestItem.getFitness(),
                                                                     improvement);
                    generationFitnessHistory.add(item);
                }
            }
        } catch (Exception e){
            isRunning = false;
            throw e;
        }

        isRunning = false;
        stopOccurred = false;
        population.clear();
    }

    @Override
    public BestSolutionItem<T, S> getBestSolution() {
        synchronized (bestItemLock){
            return bestItem;
        }
    }

    @Override
    public boolean IsRunningProcess() {
        return isRunning;
    }

    @Override
    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    protected void createAndEvaluateGeneration(){
        S supplier = getSystemInfo();
        Map<T, Double> selected = selection.select(population);
        //childPopulation.putAll(selected);
        while(childPopulation.size() < population.size()){
            Set<T> children = crossover.crossover(selected, supplier);
            children.forEach(child ->{
                mutations.forEach(mutation -> mutation.mutate(child, supplier));
                childPopulation.put(child, evaluate(child));
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

    private BestSolutionItem<T, S> getCurrentBestOption() {
        BestSolutionItem<T, S> retVal = null;
        Optional<Map.Entry<T, Double>> optional = population.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue));

        Map.Entry<T, Double> temp = optional.orElseThrow(() -> new NullPointerException("Failed to get current best item in generation: " + currentNumberOfGenerations));
        if(temp != null){
            retVal = new BestSolutionItem<>(temp.getKey(), temp.getValue(), getCurrentNumberOfGenerations(), getSystemInfo());
        }

        return retVal;
    }

    private void initialAndEvaluatePopulation(){
        for(int i = 0; i < initialPopulationSize; i++){
            T option = createOptionalSolution();
            population.put(option, evaluate(option));
        }
    }
}