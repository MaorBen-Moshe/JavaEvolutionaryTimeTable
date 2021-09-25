package utils;

import evolutinary.EvolutionarySystem;
import evolutinary.TimeTableEvolutionarySystemImpel;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.*;

public class Problem {
    private static int idCounter;
    private final int problemId;
    private final String creatorName;
    private final Map<User, ProblemConfigurations> usersSolveProblem;
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

    public Map<User, ProblemConfigurations> getUsersSolveProblem() {
        return Collections.unmodifiableMap(usersSolveProblem);
    }

    public double getCurrentBestFitnessOfProblem() {
        return usersSolveProblem.values().stream().map(ProblemConfigurations::getCurrentBestFitness).max(Comparator.naturalOrder()).orElse((double) 0);
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
        usersSolveProblem = new HashMap<>();
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

    public void setEngineInfoByUser(User user, EngineInfoObject info){
        if(usersSolveProblem.containsKey(user)){
            ProblemConfigurations config = usersSolveProblem.get(user);
            if(config.getSystem().isRunningProcess()){
                throw new IllegalArgumentException("Cannot change system engine info while the algorithm is running");
            }

            createEngine(user, info);
        }else{
            usersSolveProblem.put(user, createEngine(info));
        }
    }

    public void runProblem(User user){
        if(!usersSolveProblem.containsKey(user)){
            throw new IllegalArgumentException("user " + user.getName() + " does not exist");
        }

        usersSolveProblem.get(user).run();
    }

    public void pauseProblem(User user){
        if(!usersSolveProblem.containsKey(user)){
            throw new IllegalArgumentException("user " + user.getName() + " does not exist");
        }

        usersSolveProblem.get(user).pause();
    }

    public void resumeProblem(User user){
        if(!usersSolveProblem.containsKey(user)){
            throw new IllegalArgumentException("user " + user.getName() + " does not exist");
        }

        usersSolveProblem.get(user).resume();
    }

    public void stopProblem(User user){
        if(!usersSolveProblem.containsKey(user)){
            throw new IllegalArgumentException("user " + user.getName() + " does not exist");
        }

        usersSolveProblem.get(user).stop();
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

    private void createEngine(User user, EngineInfoObject info){
        ProblemConfigurations config = usersSolveProblem.get(user);
        TimeTableEvolutionarySystemImpel cloned = (TimeTableEvolutionarySystemImpel) config.getSystem();
        cloned.setElitism(info.getElitism());
        cloned.setInitialPopulationSize(info.getPopulation());
        cloned.setCrossover(info.getCrossover());
        cloned.setSelection(info.getSelection());
        cloned.setMutations(info.getMutations());

        config.setTerminateRules(info.getTerminateRules(), info.getJumps());
    }

    private ProblemConfigurations createEngine(EngineInfoObject info){
        TimeTableEvolutionarySystemImpel cloned = new TimeTableEvolutionarySystemImpel();
        cloned.setHours(system.getHours());
        cloned.setDays(system.getDays());
        cloned.setClasses(new HashSet<>(system.getClasses().values()));
        cloned.setRules(system.getRules());
        cloned.setSubjects(new HashSet<>(system.getSubjects().values()));
        cloned.setTeachers(new HashSet<>(system.getTeachers().values()));

        cloned.setElitism(info.getElitism());
        cloned.setInitialPopulationSize(info.getPopulation());
        cloned.setCrossover(info.getCrossover());
        cloned.setSelection(info.getSelection());
        cloned.setMutations(info.getMutations());

        ProblemConfigurations configurations = new ProblemConfigurations(cloned);
        configurations.setTerminateRules(info.getTerminateRules(), info.getJumps());
        return configurations;
    }
}