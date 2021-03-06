package utils;

import crossover.AspectOrientedCrossover;
import crossover.CrossoverTypes;
import crossover.DayTimeOrientedCrossover;
import evolutinary.TimeTableEvolutionarySystemImpel;
import generated.*;
import crossover.Crossover;
import evolutinary.EvolutionarySystem;
import mutation.Mutation;
import selection.Selection;
import models.*;
import mutation.FlippingMutation;
import mutation.MutationTypes;
import mutation.SizerMutation;
import selection.RouletteWheelSelection;
import selection.SelectionTypes;
import selection.TournamentSelection;
import selection.TruncationSelection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class ETTXmlParser {

    public static EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> parse(InputStream stream) throws Exception{
        if(stream == null){
            throw new IllegalArgumentException("Input stream should have content");
        }

        ETTDescriptor descriptor = deserializeFrom(stream);
        // for ex1 and ex2 schema
        //return createTimeTableImpel(descriptor.getETTTimeTable(), descriptor.getETTEvolutionEngine());
        //for ex3 schema
        return createTimeTableImpel(descriptor.getETTTimeTable(), null);
    }

    // parse region
    public static EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> parse(String filePath) throws Exception {
        if(filePath == null){
            throw new IllegalArgumentException("File path should be provided");
        }

        File file = new File(filePath.trim());
        InputStream inputStream = new FileInputStream(file);
        ETTDescriptor descriptor = deserializeFrom(inputStream);
        // for ex1 and ex2 schema
        //return createTimeTableImpel(descriptor.getETTTimeTable(), descriptor.getETTEvolutionEngine());
        //for ex3 schema
        return createTimeTableImpel(descriptor.getETTTimeTable(), null);
    }

    public static Selection<TimeTable> createSelection(String type, String input) throws Exception {
        ETTSelection selection = new ETTSelection();
        String prefix = type.equals("Truncation") ? "TopPercent=" : "PTE=";
        selection.setConfiguration(prefix + input);
        selection.setType(type);
        return createSelection(selection);
    }

    public static Crossover<TimeTable, TimeTableSystemDataSupplier> createCrossover(String type, int cutting, String aspect) throws Exception {
        ETTCrossover crossover = new ETTCrossover();
        crossover.setName(type);
        crossover.setCuttingPoints(cutting);
        crossover.setConfiguration("Orientation=" + aspect);
        return createCrossover(crossover);
    }

    private static ETTDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("generated");
        Unmarshaller unMarshaller = jc.createUnmarshaller();
        return (ETTDescriptor) unMarshaller.unmarshal(in);
    }

    private static EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> createTimeTableImpel(ETTTimeTable timeTable, ETTEvolutionEngine engine) throws Exception {
        TimeTableEvolutionarySystemImpel ret = new TimeTableEvolutionarySystemImpel();
        // each method validate first if the details are ok by itself or using ValidationUtils class
        // create the time table evolutionary part
        setDaysHours(ret, timeTable.getDays(), timeTable.getHours());
        ret.setRules(createRules(timeTable.getETTRules()));
        ret.setSubjects(new HashSet<>(createSubjects(timeTable.getETTSubjects())));
        ret.setTeachers(new HashSet<>(createTeachers(timeTable.getETTTeachers(), ret.getSubjects().values(), timeTable.getDays(), timeTable.getHours())));
        ret.setClasses(new HashSet<>(createClasses(timeTable.getETTClasses(), ret.getSubjects().values(), ret.getHours() * ret.getDays())));

        if(engine != null){
            // create the engine part
            int initialPopulation = engine.getETTInitialPopulation().getSize();
            setInitialPopulation(ret, initialPopulation);
            ret.setSelection(createSelection(engine.getETTSelection()));
            Integer optionalElitism = engine.getETTSelection().getETTElitism();
            ret.setElitism(optionalElitism != null ? optionalElitism : 0);
            ret.setCrossover(createCrossover(engine.getETTCrossover()));
            ret.setMutations(createMutations(engine.getETTMutations()));
        }

        return ret;
    }

    // end region

    // engine region
    private static void setInitialPopulation(TimeTableEvolutionarySystemImpel system, int initialPopulation){
        if(ValidateUtils.initialPopulationPositive(initialPopulation)){
            system.setInitialPopulationSize(initialPopulation);
        }else{
            throw new IllegalArgumentException("initial population must be positive number. you insert: " + initialPopulation);
        }
    }

    private static List<Mutation<TimeTable, TimeTableSystemDataSupplier>> createMutations(ETTMutations ettMutations) throws Exception {
        List<Mutation<TimeTable, TimeTableSystemDataSupplier>> retMutations = new ArrayList<>();
        MutationTypes type;
        List<ETTMutation> ettMutationsList = ettMutations.getETTMutation();

        try{
            if(ValidateUtils.mutationsValid(ettMutations)){
                for(ETTMutation mutation : ettMutationsList){
                    type = MutationTypes.valueOf(mutation.getName());
                    // check for configurations
                    double probability = mutation.getProbability();
                    switch (type){
                        case Flipping:
                            retMutations.add(new FlippingMutation(probability,
                                                                  getFlippingMaxTupples(mutation.getConfiguration()),
                                                                  getFlippingComponent(mutation.getConfiguration())));
                            break;
                        case Sizer:
                            int totalTupples = Integer.parseInt(mutation.getConfiguration().split("=")[1]);
                            retMutations.add(new SizerMutation(probability, totalTupples));
                    }
                }
            }
        }catch (Exception e){
            throw new Exception(e.getMessage() + ". creation of mutations failed");
        }

        return retMutations;
    }

    private static int getFlippingMaxTupples(String configurations){
        String[] split = configurations.split(",");
        String[] first = split[0].split("=");
        String[] second = split[1].split("=");
        int retVal;

        if(first[0].equalsIgnoreCase("MaxTupples")){
            retVal = Integer.parseInt(first[1]);
        }
        else if(second[0].equalsIgnoreCase("MaxTupples")){
            retVal = Integer.parseInt(second[1]);
        }
        else{
            throw new IllegalArgumentException("Failed to find MaxTupples in flipping mutation");
        }

        return retVal;
    }

    private static FlippingMutation.Component getFlippingComponent(String configurations){
        String[] split = configurations.split(",");
        String[] first = split[0].split("=");
        String[] second = split[1].split("=");
        FlippingMutation.Component retVal;

        if(first[0].equalsIgnoreCase("Component")){
            retVal = FlippingMutation.Component.valueOf(first[1].toUpperCase(Locale.ROOT));
        }
        else if(second[0].equalsIgnoreCase("Component")){
            retVal = FlippingMutation.Component.valueOf(second[1].toUpperCase(Locale.ROOT));
        }
        else{
            throw new IllegalArgumentException("Failed to find Component in flipping mutation");
        }

        return retVal;
    }

    private static Crossover<TimeTable, TimeTableSystemDataSupplier> createCrossover(ETTCrossover ettCrossover) throws Exception {
        Crossover<TimeTable, TimeTableSystemDataSupplier> retCrossover = null;
        CrossoverTypes type = CrossoverTypes.valueOf(ettCrossover.getName());
        int cuttingPoints = ettCrossover.getCuttingPoints();
        if(cuttingPoints <= 0){
            throw new IllegalArgumentException("Crossover accept only positive values for cutting points");
        }

        try{
            if(ValidateUtils.crossoverValid(ettCrossover)){
                switch (type){
                    case AspectOriented:
                        AspectOrientedCrossover.Orientation orientation = AspectOrientedCrossover.Orientation.valueOf(ettCrossover.getConfiguration().replace(" ", "").split("=")[1].toUpperCase(Locale.ROOT));
                        retCrossover = new AspectOrientedCrossover(cuttingPoints, orientation);
                        break;
                    case DayTimeOriented:
                        retCrossover = new DayTimeOrientedCrossover(cuttingPoints);
                        break;
                }
            }
        }catch (Exception e){
            throw new Exception(e.getMessage() + ". creation of crossover failed");
        }

        return retCrossover;
    }

    private static Selection<TimeTable> createSelection(ETTSelection ettSelection) throws Exception {
        Selection<TimeTable> retSelection = null;
        SelectionTypes type = SelectionTypes.valueOf(ettSelection.getType());
        try{
            if(ValidateUtils.selectionValid(ettSelection)){
                switch (type){
                    case Tournament:
                        float pte = Float.parseFloat(ettSelection.getConfiguration().split("=")[1]);
                        retSelection = new TournamentSelection(pte);
                        break;
                    case Truncation:
                        int top = Integer.parseInt(ettSelection.getConfiguration().split("=")[1]);
                        retSelection = new TruncationSelection(top);
                        break;
                    case RouletteWheel:
                        retSelection = new RouletteWheelSelection();
                        break;
                }
            }
        }catch (Exception e){
            throw new Exception(e.getMessage() + ". creation of selection failed");
        }

        return retSelection;
    }

    // end region

    // time table region
    private static Set<SchoolClass> createClasses(ETTClasses ettClasses, Collection<Subject> allSubjects, int totalNumberOfHoursInSystem) throws Exception {
        List<ETTClass> ettClassList = ettClasses.getETTClass();
        List<SchoolClass> tempClasses = new ArrayList<>(ettClassList.size());
        for(ETTClass klass : ettClassList){
            Map<Subject, Integer> subjects = createSubjectMapByRequirements(klass, klass.getETTRequirements(), allSubjects, totalNumberOfHoursInSystem);
            tempClasses.add(new SchoolClass(klass.getETTName(), klass.getId(), subjects));
        }

        Set<SchoolClass> retClasses = new HashSet<>(tempClasses);
        if(retClasses.size() != tempClasses.size()){
            throw new IllegalArgumentException("Classes cannot have same id");
        }

        if(ValidateUtils.serialItemsNotValid(retClasses)){
            throw new IllegalArgumentException("Classes ids should start from 1 and grow by 1 for each item");
        }

        return retClasses;
    }

    private static Map<Subject, Integer> createSubjectMapByRequirements(ETTClass klass, ETTRequirements ettRequirements, Collection<Subject> allSubjects, int totalNumberOfHoursInSystem) throws Exception {
        List<ETTStudy> ettStudies = ettRequirements.getETTStudy();
        List<Integer> allIds = new ArrayList<>(ettStudies.size());
        Map<Subject, Integer> retSubjectsMap = new HashMap<>();
        int totalHoursCounter;
        //get all the subjects ids given by the user
        ettStudies.forEach(study -> allIds.add(study.getSubjectId()));
        // checks if all the subjects in the requirements are exist in the system
        Set<Subject> subjects;
        try{
            subjects = getSubjectsById(allSubjects, allIds);
        }catch (Exception e){
            throw new Exception("In classes part: " + e.getMessage());
        }
        subjects.forEach(subject -> ettStudies.forEach(study -> {
            if(subject.getId() == study.getSubjectId()){
                retSubjectsMap.put(subject, study.getHours());
            }
        }));

        totalHoursCounter = retSubjectsMap.values().stream().mapToInt(i -> i).sum();
        if(totalHoursCounter > totalNumberOfHoursInSystem){
            throw new IllegalArgumentException("class " + klass.getETTName() + ", id: " + klass.getId() + " total hours must be at most number of days in system times number of hours in system");
        }

        return retSubjectsMap;
    }

    private static Set<Teacher> createTeachers(ETTTeachers ettTeachers, Collection<Subject> allSubjectsInSystem, int days, int hours) throws Exception {
        List<ETTTeacher> ettTeachersList = ettTeachers.getETTTeacher();
        List<Teacher> tempTeachers = new ArrayList<>(ettTeachersList.size());
        for(ETTTeacher teacher : ettTeachersList){
            List<Integer> ids = new ArrayList<>();
            teacher.getETTTeaching().getETTTeaches().forEach(teach -> ids.add(teach.getSubjectId()));
            int workingHours = teacher.getETTWorkingHours();
            if(workingHours < 0 || workingHours > days * hours){
                throw new IllegalArgumentException("Teacher: " + teacher.getETTName() + " with id: "
                                                   + teacher.getId() + " cannot work negative number or number bigger than " + (days * hours));
            }

            try{
                tempTeachers.add(new Teacher(teacher.getETTName(),
                                             teacher.getId(),
                                             getSubjectsById(allSubjectsInSystem, ids), workingHours));
            }catch (Exception e){
                throw new Exception("In teachers: " + teacher.getETTName() + ", id: " + teacher.getId() + " " + e.getMessage());
            }
        }

        Set<Teacher> retTeachers = new HashSet<>(tempTeachers);
        if(retTeachers.size() != tempTeachers.size()){
            throw new IllegalArgumentException("Teachers cannot have same id");
        }

        if(ValidateUtils.serialItemsNotValid(retTeachers)){
            throw new IllegalArgumentException("Teachers ids should start from 1 and grow by 1 for each item");
        }

        return retTeachers;
    }

    private static Set<Subject> getSubjectsById(Collection<Subject> allSubjects, List<Integer> ids){
        List<Subject> tempSubjects = new ArrayList<>(ids.size());

        ids.forEach(id -> {
            int oldSize = tempSubjects.size();
            allSubjects.forEach(subject -> {
                if(subject.getId() == id){
                    tempSubjects.add(subject);
                }
            });

            if(oldSize == tempSubjects.size()){
                throw new IllegalArgumentException("subject id -> " + id + " that you provided is not one of the subjects ids in the system");
            }
        });

        // if there was an id that repeated itself we throw exception
        Set<Subject> retSubjects = new HashSet<>(tempSubjects);
        if(tempSubjects.size() != retSubjects.size()){
            throw new IllegalArgumentException("In Subjects, ids cannot repeat themselves!");
        }

        return retSubjects;
    }

    private static Set<Subject> createSubjects(ETTSubjects ettSubjects) {
        List<ETTSubject> ettSubjectsList = ettSubjects.getETTSubject();
        List<Subject> tempSubjects = new ArrayList<>(ettSubjectsList.size());
        ettSubjectsList.forEach(subject -> tempSubjects.add(new Subject(subject.getName(), subject.getId())));
        Set<Subject> retSubjects = new HashSet<>(tempSubjects);
        if(retSubjects.size() != tempSubjects.size()){
            throw new IllegalArgumentException("Subjects cannot have duplication ids");
        }

        if(ValidateUtils.serialItemsNotValid(retSubjects)){
            throw new IllegalArgumentException("Subjects ids should start from 1 and grow by 1 for each item");
        }

        return retSubjects;
    }

    private static Rules createRules(ETTRules ettRules) {
        List<ETTRule> ettRulesList = ettRules.getETTRule();
        int rulesWeight = ettRules.getHardRulesWeight();
        if(!(rulesWeight >= 0 && rulesWeight <= 100)){
            throw new IllegalArgumentException("Hard rules weight must be an integer between 0-100. you insert: " + rulesWeight);
        }

        Rule current = null;
        Rules rules = new Rules(rulesWeight);
        for(ETTRule rule : ettRulesList){
            if(ValidateUtils.enumValid(Rule.eStrength.values(), Rule.eStrength.class, rule.getType()) &&
               ValidateUtils.enumValid(Rule.eRules.values(), Rule.eRules.class, rule.getETTRuleId())){
                Rule.eStrength strength = Rule.eStrength.valueOf(rule.getType());
                Map<String, Object> configurations = null;
                if(rule.getETTRuleId().equals(Rule.eRules.Sequentiality.toString())){
                    int total = setSequentiality(rule.getETTConfiguration());
                    configurations = new HashMap<>();
                    configurations.put("Total hours", total);
                }

                current = new Rule(Rule.eRules.valueOf(rule.getETTRuleId()), strength, configurations);
            }

            if(rules.contains(current)){
                throw new IllegalArgumentException(current + " cannot be duplicated in rules. each rule must be shown at most one time!");
            }

            rules.add(current);
        }

        return rules;
    }

    private static int setSequentiality(String configurations){
        if(configurations == null || configurations.replace(" ", "").isEmpty()){
            throw new IllegalArgumentException("Rule Sequentiality must supply Total hours only!");
        }

        String[] configurationSplit = configurations.split("=");
        if(!(configurationSplit[0].equalsIgnoreCase("TotalHours"))){
            throw new IllegalArgumentException("Rule Sequentiality must supply Total hours!");
        }
        else{
            try{
                int total = Integer.parseInt(configurationSplit[1]);
                if(total > 0){
                    return total;
                }
                else{
                    throw new IllegalArgumentException("Total hours of rule Sequentiality must be positive integer you insert: totalHours = " + total);
                }
            }catch (NumberFormatException e){
                throw new IllegalArgumentException("Total hours of rule Sequentiality must be a positive integer!");
            }
        }
    }

    private static void setDaysHours(TimeTableEvolutionarySystemImpel system, int days, int hours){
        if(ValidateUtils.daysHoursValid(days, hours)){
            system.setDays(days);
            system.setHours(hours);
        }
        else{
            throw new IllegalArgumentException(days + ", " + hours + " in file must be positive integers");
        }
    }

    // end region
}