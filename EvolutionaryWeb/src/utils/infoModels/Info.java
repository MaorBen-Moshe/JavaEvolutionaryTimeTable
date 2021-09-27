package utils.infoModels;

import java.util.List;

public class Info {
    private int population;
    private int elitism;
    private String selectionType;
    private String selectionInput;
    private String crossoverType;
    private int crossoverCutting;
    private String crossoverAspect;
    private List<MutationInfo> mutations;
    private int jumps;
    private boolean gensChecked;
    private int gensInput;
    private boolean fitnessChecked;
    private int fitnessInput;
    private boolean timeChecked;
    private long timeInput;

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getElitism() {
        return elitism;
    }

    public void setElitism(int elitism) {
        this.elitism = elitism;
    }

    public String getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public String getSelectionInput() {
        return selectionInput;
    }

    public void setSelectionInput(String selectionInput) {
        this.selectionInput = selectionInput;
    }

    public String getCrossoverType() {
        return crossoverType;
    }

    public void setCrossoverType(String crossoverType) {
        this.crossoverType = crossoverType;
    }

    public int getCrossoverCutting() {
        return crossoverCutting;
    }

    public void setCrossoverCutting(int crossoverCutting) {
        this.crossoverCutting = crossoverCutting;
    }

    public String getCrossoverAspect() {
        return crossoverAspect;
    }

    public void setCrossoverAspect(String crossoverAspect) {
        this.crossoverAspect = crossoverAspect;
    }

    public List<MutationInfo> getMutations() {
        return mutations;
    }

    public void setMutations(List<MutationInfo> mutations) {
        this.mutations = mutations;
    }

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

    public boolean getGensChecked() {
        return gensChecked;
    }

    public void setGensChecked(boolean gensChecked) {
        this.gensChecked = gensChecked;
    }

    public int getGensInput() {
        return gensInput;
    }

    public void setGensInput(int gensInput) {
        this.gensInput = gensInput;
    }

    public boolean getFitnessChecked() {
        return fitnessChecked;
    }

    public void setFitnessChecked(boolean fitnessChecked) {
        this.fitnessChecked = fitnessChecked;
    }

    public int getFitnessInput() {
        return fitnessInput;
    }

    public void setFitnessInput(int fitnessInput) {
        this.fitnessInput = fitnessInput;
    }

    public boolean getTimeChecked() {
        return timeChecked;
    }

    public void setTimeChecked(boolean timeChecked) {
        this.timeChecked = timeChecked;
    }

    public long getTimeInput() {
        return timeInput;
    }

    public void setTimeInput(long timeInput) {
        this.timeInput = timeInput;
    }
}
