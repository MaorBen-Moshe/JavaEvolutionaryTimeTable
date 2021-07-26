package evolutinary;

import interfaces.Crossover;
import interfaces.Mutation;
import interfaces.Selection;
import models.BestSolutionItem;

import java.util.*;

// T is a population item
public abstract class EvolutionarySystemImpel<T> implements interfaces.EvolutionarySystem<T> {
    private Map<T, Double> population;
    private Map<T, Double> childPopulation;
    private List<Double> generationFitnessHistory; // cell 0: best fitness of generation 1 and so on...
    private BestSolutionItem<T> bestItem;
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

    private int jumpInGenerations;

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

    private double acceptedFitness;

    public void setAcceptedNumberOfGenerations(int acceptedNumberOfGenerations) {
        if(acceptedNumberOfGenerations < 100){
            throw new IllegalArgumentException("accepted number of generations must be at least 100");
        }

        this.acceptedNumberOfGenerations = acceptedNumberOfGenerations;
    }

    private int acceptedNumberOfGenerations;

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
        jumpInGenerations = 1;
        population = new HashMap<>();
        childPopulation = new HashMap<>();
    }

    @Override
    public BestSolutionItem<T> StartAlgorithm(Set<TerminateRules> terminateBy) {
        BestSolutionItem<T> currentBestFitness;
        isRunning = true;
        /* initial*/
        initialPopulation();
        currentBestFitness = evaluateGeneration();
        /* iterative*/
        while(!isTerminate(terminateBy)){
            createGeneration();
            currentBestFitness = evaluateGeneration();
            currentNumberOfGenerations++;
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
        Map<T, Double> selected = selection.select(population);
        while(childPopulation.size() <= population.size()){
            Set<T> children = crossOver.crossOver(selected);
            children.forEach(child ->{
                mutations.forEach(mutation -> mutation.mutate(child));
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