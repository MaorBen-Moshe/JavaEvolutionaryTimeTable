package evolutinary;

import Interfaces.DataSupplier;
import crossover.Crossover;
import models.*;
import mutation.Mutation;
import selection.Selection;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// T is a population item
public abstract class EvolutionarySystemImpel<T, S extends DataSupplier> implements EvolutionarySystem<T, S>, Serializable {
    private Map<T, Double> population;
    private final Map<T, Double> childPopulation;
    private Crossover<T, S> crossover;
    private Selection<T> selection;
    private List<Mutation<T, S>> mutations;
    private List<Mutation<T, S>> unModifiedMutations;
    private int initialPopulationSize;
    private final List<FitnessHistoryItem<T>> generationFitnessHistory; // cell 0: best fitness of generation 1 and so on... (by jump in generations)
    private BestSolutionItem<T, S> bestItem;
    private volatile boolean isRunning = false;
    private volatile boolean stopOccurred = false;
    private volatile boolean pauseOccurred = false;
    private volatile int currentNumberOfGenerations;
    private volatile int elitism;
    private Instant startTime;
    private Instant blockTime;
    private final CustomLock generationsLock;
    private final CustomLock bestItemLock;
    private final CustomLock fitnessHistoryLock;

    protected EvolutionarySystemImpel(){
        population = new HashMap<>();
        childPopulation = new HashMap<>();
        generationFitnessHistory = new ArrayList<>();
        generationsLock = new CustomLock();
        bestItemLock = new CustomLock();
        fitnessHistoryLock = new CustomLock();
    }

    @Override
    public List<FitnessHistoryItem<T>> getGenerationFitnessHistory() {
        synchronized (fitnessHistoryLock){
            return new ArrayList<>(generationFitnessHistory);
        }
    }

    public int getCurrentNumberOfGenerations() {
        synchronized (generationsLock){
            return currentNumberOfGenerations;
        }
    }

    @Override
    public void stopProcess() {
        if(isRunningProcess() || isPauseOccurred()){
            setStopOccurred(true);
            setRunning(false);
            setPauseOccurred(false);
        }
    }

    @Override
    public void pauseProcess() {
        if(isRunningProcess()){
            setPauseOccurred(true);
            setRunning(false);
        }
    }

    @Override
    public void resumeProcess() {
        if(!isRunningProcess()){
            setPauseOccurred(false);
            setRunning(true);
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
    public void StartAlgorithm(Object pauseLock, Set<TerminateRule> terminateBy, int jumpInGenerations, Consumer<JumpInGenerationsResult> listener){
        jumpInGenerations = jumpInGenerations <= 0 ? 1 : jumpInGenerations;
        initialAlgoData();
        try{
            /* initial*/
            initialAndEvaluatePopulation();
            /* iterative*/
            while(!isTerminate(terminateBy) && !isStopOccurred()){
                synchronized (pauseLock){
                    while(isPauseOccurred()){
                        // stop algorithm for while
                        try{
                            blockTime = Instant.now();
                            pauseLock.wait();
                        }catch (InterruptedException ignored){
                        }

                        if(isStopOccurred()) break;
                        long offset = Duration.between(blockTime, Instant.now()).getSeconds();
                        startTime = startTime.plusSeconds(offset);
                    }
                }
                incCurrentNumberOfGenerations();
                createAndEvaluateGeneration();
                if(getCurrentNumberOfGenerations() % jumpInGenerations == 0){
                    addFitnessItemToHistory();
                    listener.accept(new JumpInGenerationsResult(getBestSolution().getFitness(), getCurrentNumberOfGenerations(), Duration.between(startTime, Instant.now()).toMinutes()));
                }
            }
        } catch (Exception e){
            clearAlgoData();
            throw e;
        }

        clearAlgoData();
    }

    @Override
    public BestSolutionItem<T, S> getBestSolution() {
        synchronized (bestItemLock){
            return bestItem;
        }
    }

    @Override
    public int getElitism() {
        return elitism;
    }

    @Override
    public void setElitism(int elitism) {
        if(elitism < 0 || elitism > initialPopulationSize){
            throw new IllegalArgumentException("elitism argument must be a positive integer and at most the size of the population");
        }

        this.elitism = elitism;
    }

    @Override
    public boolean isRunningProcess() { return isRunning; }

    @Override
    public boolean isPauseOccurred() { return pauseOccurred; }

    @Override
    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    protected void createAndEvaluateGeneration(){
        S supplier = getSystemInfo();
        Map<T, Double> elita = getElitaPopulation();
        childPopulation.putAll(elita);
        List<T> selected = selection.select(population);
        while(childPopulation.size() < population.size()){
            Set<T> children = crossover.crossover(selected, supplier);
            children.forEach(child ->{
                mutations.forEach(mutation -> mutation.mutate(child, supplier));
                childPopulation.put(child, evaluate(child));
            });
        }

        population = new HashMap<>(childPopulation);
        childPopulation.clear();
        setBestItem(getCurrentBestOption());
    }

    protected abstract double evaluate(T optional);

    protected abstract T createOptionalSolution();

    private Map<T, Double> getElitaPopulation(){
        return population.entrySet()
                  .stream()
                  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                  .limit(getElitism())
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1));
    }

    private boolean isTerminate(Set<TerminateRule> terminateBy) {
        boolean answer = false;
        for (TerminateRule terminate : terminateBy) {
            switch (terminate.getType()){
                case NumberOfGenerations:
                    answer = terminate.isTerminate(currentNumberOfGenerations);  break;
                case ByFitness: answer =  terminate.isTerminate(bestItem.getFitness()); break;
                case ByTime: answer = terminate.isTerminate(Duration.between(startTime, Instant.now()).toMinutes()); break;
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
        IntStream.range(0, initialPopulationSize).forEach(i ->{
            T option = createOptionalSolution();
            population.put(option, evaluate(option));
        });

        setBestItem(getCurrentBestOption());
        addFitnessItemToHistory();
    }

    private void setBestItem(BestSolutionItem<T, S> bestItem) {
        synchronized (bestItemLock){
            this.bestItem = bestItem;
        }
    }

    private void setCurrentNumberOfGenerations(int currentNumberOfGenerations) {
        synchronized (generationsLock){
            this.currentNumberOfGenerations = currentNumberOfGenerations;
        }
    }

    private boolean isStopOccurred() { return stopOccurred; }

    private void setRunning(boolean running) { isRunning = running; }

    private void setStopOccurred(boolean stopOccurred) { this.stopOccurred = stopOccurred; }

    private void setPauseOccurred(boolean pauseOccurred) { this.pauseOccurred = pauseOccurred; }

    private void initialAlgoData(){
        setRunning(true);
        setCurrentNumberOfGenerations(0);
        generationFitnessHistory.clear();
        startTime = Instant.now();
    }

    private void clearAlgoData(){
        setRunning(false);
        setStopOccurred(false);
        setPauseOccurred(false);
        population.clear();
        startTime = null;
    }

    private void addFitnessItemToHistory(){
        synchronized (fitnessHistoryLock){
            double improvement = generationFitnessHistory.size() == 0 ? 0 : bestItem.getFitness() - generationFitnessHistory.get(generationFitnessHistory.size() - 1).getCurrentGenerationFitness();
            FitnessHistoryItem<T> item = new FitnessHistoryItem<>(getBestSolution().getSolution(), getCurrentNumberOfGenerations(),
                    getBestSolution().getFitness(),
                    improvement);
            generationFitnessHistory.add(item);
        }
    }
}