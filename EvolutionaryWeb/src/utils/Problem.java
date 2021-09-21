package utils;

import evolutinary.EvolutionarySystem;
import evolutinary.TimeTableEvolutionarySystemImpel;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Problem {
    private static int idCounter;
    private final int problemId;
    private final String creatorName;
    private final Set<String> usersSolveProblem;
    private float currentBestFitnessOfProblem;
    private int days;
    private int hours;
    private int teachers;
    private int classes;
    private int subjects;
    private int numberOfSoftRules;
    private int numberOfHardRules;
    private TimeTableEvolutionarySystemImpel system;

    public int getProblemId() {
        return problemId;
    }

    public Set<String> getUsersSolveProblem() {
        return Collections.unmodifiableSet(usersSolveProblem);
    }

    public float getCurrentBestFitnessOfProblem() {
        return currentBestFitnessOfProblem;
    }

    public void setCurrentBestFitnessOfProblem(float currentBestFitnessOfProblem) {
        this.currentBestFitnessOfProblem = currentBestFitnessOfProblem;
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public int getTeachers() {
        return teachers;
    }

    public int getClasses() {
        return classes;
    }

    public int getSubjects() {
        return subjects;
    }

    public int getNumberOfSoftRules() {
        return numberOfSoftRules;
    }

    public int getNumberOfHardRules() {
        return numberOfHardRules;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> getSystem() {
        return system;
    }

    static {
        idCounter = 1;
    }

    public Problem(String creatorName){
        usersSolveProblem = new HashSet<>();
        problemId = idCounter++;
        this.creatorName = creatorName;
    }

    public void setSystem(TimeTableEvolutionarySystemImpel system){
        if(system != null){
            this.system = system;
            TimeTableSystemDataSupplier supplier = this.system.getSystemInfo();
            this.days = supplier.getDays();
            this.hours = supplier.getHours();
            this.teachers = supplier.getTeachers().size();
            this.subjects = supplier.getSubjects().size();
            this.classes = supplier.getClasses().size();
            this.numberOfSoftRules = this.system.getNumberOfSoftRules();
            this.numberOfHardRules = this.system.getNumberOfHardRules();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return problemId == problem.problemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(problemId);
    }
}