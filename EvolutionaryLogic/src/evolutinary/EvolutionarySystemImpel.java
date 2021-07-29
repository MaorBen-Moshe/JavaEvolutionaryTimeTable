package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import mutation.Mutation;
import selection.Selection;
import models.BestSolutionItem;

import java.util.*;

// T is a population item
public abstract class EvolutionarySystemImpel<T, S extends DataSupplier> implements EvolutionarySystem<T, S> {
    private Map<T, Double> population;
    private final Map<T, Double> childPopulation;

    @Override
    public List<Double> getGenerationFitnessHistory() {
        return new ArrayList<>(generationFitnessHistory);
    }

    private final List<Double> generationFitnessHistory; // cell 0: best fitness of generation 1 and so on... (by jump in generations)
    private BestSolutionItem<T, S> bestItem;
    private boolean isRunning = false;

    public int getJumpInGenerations() {
        return jumpInGenerations;
    }

    public void setJumpInGenerations(int jumpInGenerations) {
        if(jumpInGenerations <= 0){
            throw new IllegalArgumentException("Jump in generations must be positive number");
        }
        this.jumpInGenerations = jumpInGenerations;
    }

    public int getCurrentNumberOfGenerations() {
        return currentNumberOfGenerations;
    }

    private int currentNumberOfGenerations;

    public void setAcceptedFitness(double acceptedFitness) {
        if(acceptedFitness < 0 || acceptedFitness > 100){
            throw new IllegalArgumentException("Fitness must be a positive number between 0-100");
        }

        this.acceptedFitness = acceptedFitness;
    }

    public int getAcceptedNumberOfGenerations() {
        return acceptedNumberOfGenerations;
    }


    public void setAcceptedNumberOfGenerations(int acceptedNumberOfGenerations) {
        if(acceptedNumberOfGenerations < 100){
            throw new IllegalArgumentException("accepted number of generations must be at least 100");
        }

        this.acceptedNumberOfGenerations = acceptedNumberOfGenerations;
    }

    public void setCrossover(Crossover<T, S> crossover) {
        this.crossover = crossover;
    }

    public void setMutations(List<Mutation<T, S>> mutations) {
        this.mutations = mutations;
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
        return mutations;
    }

    public void setSelection(Selection<T> selection) {
        this.selection = selection;
    }

    public void setInitialPopulationSize(int initialPopulationSize) {
        this.initialPopulationSize = initialPopulationSize;
    }

    public Map<T, Double> getPopulation() {
        return population;
    }

    private Crossover<T, S> crossover;
    private Selection<T> selection;
    private List<Mutation<T, S>> mutations;
    private int initialPopulationSize;
    private int acceptedNumberOfGenerations;
    private int jumpInGenerations;
    private double acceptedFitness;

    protected EvolutionarySystemImpel(){
        jumpInGenerations = 1;
        population = new HashMap<>();
        childPopulation = new HashMap<>();
        generationFitnessHistory = new ArrayList<>();
    }

    @Override
    public BestSolutionItem<T, S> StartAlgorithm(Set<TerminateRules> terminateBy) {
        BestSolutionItem<T, S> currentBestFitness;
        isRunning = true;
        generationFitnessHistory.clear();
        /* initial*/
        initialPopulation();
        currentBestFitness = evaluateGeneration();
        generationFitnessHistory.add(currentBestFitness.getFitness());
        /* iterative*/
        while(!isTerminate(terminateBy)){
            createGeneration();
            currentBestFitness = evaluateGeneration();
            currentNumberOfGenerations++;
            if(currentNumberOfGenerations % jumpInGenerations == 0){
                generationFitnessHistory.add(currentBestFitness.getFitness());
            }
        }

        isRunning = false;
        bestItem = currentBestFitness;
        return bestItem;
    }

    @Override
    public BestSolutionItem<T, S> getBestSolution() {
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

    @Override
    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    private void initialPopulation(){
        for(int i =0; i < initialPopulationSize; i++){
            T option = createOptionalSolution();
            population.put(option, evaluate(option));
        }
    }

    private BestSolutionItem<T, S> evaluateGeneration() {
        BestSolutionItem<T, S> retVal = null;
        population.keySet().forEach(item -> population.replace(item, population.get(item), evaluate(item)));
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

    private boolean isTerminate(Set<TerminateRules> terminateBy) {
        boolean answer = false;
        for (TerminateRules terminate : terminateBy) {
            switch (terminate){
                case NumberOfGenerations: answer = currentNumberOfGenerations >= acceptedNumberOfGenerations;  break;
                case ByFitness: answer = bestItem.getFitness() >= acceptedFitness; break;
                default: answer = false; break;
            }

            if(answer){
                break;
            }
        }

        return answer;
    }
}