package evolutinary;

import models.*;
import utils.ItemCreationUtil;
import utils.RandomUtils;

import java.util.*;

public class TimeTableEvolutionarySystemImpel extends EvolutionarySystemImpel<TimeTable, TimeTableSystemDataSupplier> {
    private Map<Integer, Teacher> teachers;
    private Map<Integer, Teacher> unModifiedTeachers;
    private Map<Integer, Subject> subjects;
    private Map<Integer, Subject> unModifiedSubjects;
    private Map<Integer, SchoolClass> classes;
    private Map<Integer, SchoolClass> unModifiedClasses;
    private Rules rules;
    private int days;
    private int hours;

    public TimeTableEvolutionarySystemImpel(){
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = createMapFromSet(teachers);
        this.unModifiedTeachers = Collections.unmodifiableMap(this.teachers);
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = createMapFromSet(subjects);
        this.unModifiedSubjects = Collections.unmodifiableMap(this.subjects);
    }

    public Map<Integer, Subject> getSubjects() {
        return unModifiedSubjects;
    }

    public void setClasses(Set<SchoolClass> classes) {
        this.classes = createMapFromSet(classes);
        this.unModifiedClasses = Collections.unmodifiableMap(this.classes);
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        if(days > 0){
            this.days = days;
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        if(hours > 0){
            this.hours = hours;
        }
    }

    public Rules getRules() {
        return rules;
    }

    public Map<Integer, Teacher> getTeachers() {
        return unModifiedTeachers;
    }

    public Map<Integer, SchoolClass> getClasses() {
        return unModifiedClasses;
    }

    @Override
    public TimeTableSystemDataSupplier getSystemInfo() {
        return new TimeTableSystemDataSupplier(days, hours, new HashMap<>(teachers), new HashMap<>(subjects), new HashMap<>(classes));
    }

    protected TimeTable createOptionalSolution(){
        int minNumber = classes.values().stream().mapToInt(SchoolClass::getTotalNumberOfHours).sum();
        TimeTable timeTable = new TimeTable();
        int currentTableSize = RandomUtils.nextIntInRange((int)Math.floor(0.7*minNumber), minNumber * 5);
        for(int i = 0; i < currentTableSize; i++){
            timeTable.add(createItem());
        }

        return timeTable;
    }

    @Override
    protected double evaluate(TimeTable optional) {
        List<Double> hardList = new ArrayList<>();
        List<Double> softList = new ArrayList<>();
        double answer = 0;
        for (Rule rule: rules.getRules()) {
            switch (rule.getRuleType()) {
                case Sequentiality: answer = evalSeq(optional); break;
                case DayOffClass: answer = evalDayOffClass(optional); break;
                case Singularity: answer = evalSingul(optional); break;
                case Satisfactory: answer = evalSatis(optional); break;
                case DayOffTeacher: answer = evalDayOffTeacher(optional); break;
                case Knowledgeable: answer = evalKnow(optional); break;
                case TeacherIsHuman: answer = evalHuman(optional); break;
                case WorkingHoursPreference: answer = evalWork(optional); break;
            }

            if(rule.getStrength().equals(Rule.eStrength.Hard)){
                hardList.add(answer);
            }else {
                softList.add(answer);
            }

            optional.addRuleScore(rule, answer);
        }

        return setRulesScoreAndGetResult(optional, hardList, softList);
    }

    private double setRulesScoreAndGetResult(TimeTable optional, List<Double> hardList, List<Double> softList) {
        double retVal;
        double hard = 0;
        double soft = 0;
        int hardWeight = rules.getHardRulesWeight();
        if(hardList.size() == 0 || softList.size() == 0){
            if(hardList.size() == 0){
                soft = getAvg(softList);
                hard = 100;
                optional.setHardRulesAvg(hard);
                optional.setSoftRulesAvg(soft);
            }
            else{
                hard = getAvg(hardList);
                soft = 100;
                optional.setHardRulesAvg(hard);
                optional.setSoftRulesAvg(soft);
            }
        }
        else{
            hard = getAvg(hardList);
            soft = getAvg(softList);
            optional.setHardRulesAvg(hard);
            optional.setSoftRulesAvg(soft);
        }

        retVal = ((hardWeight * hard) + ((100 - hardWeight) * soft)) / 100;
        return retVal;
    }

    private TimeTableItem createItem(){
        int daySelected, hourSelected;
        Teacher teacherSelected;
        Subject subjectSelected;
        SchoolClass classSelected;

        daySelected = RandomUtils.nextIntInRange(1, days);
        hourSelected = RandomUtils.nextIntInRange(1, hours);
        teacherSelected = ItemCreationUtil.getRandItem(teachers);
        subjectSelected = ItemCreationUtil.getRandItem(subjects);
        classSelected = ItemCreationUtil.getRandItem(classes);
        return new TimeTableItem(daySelected, hourSelected, classSelected, teacherSelected, subjectSelected);
    }

    private double getAvg(List<Double> list){
        double ret = 0;
        if(list.size() != 0){
            ret = list.stream().mapToDouble(x -> x).sum();
            ret /= list.size();
        }

        return ret;
    }

    private double evalSeq(TimeTable optional){
       return 0;
    }

    private double evalDayOffClass(TimeTable optional){
        return 0;
    }

    private double evalSingul(TimeTable optional){
        double ret = 100;
        Map<SchoolClass, Map<Integer, Set<Integer>>> classesDaysAndHours = new HashMap<>(classes.size());
        // initialize each teacher an map of days an optional hours of working
        for (SchoolClass klass : classes.values()) {
            classesDaysAndHours.put(klass, new HashMap<>());
            for(int i = 1; i <= days; i++){
                classesDaysAndHours.get(klass).put(i, new HashSet<>(hours));
            }
        }

        Set<SchoolClass> falseClasses = new HashSet<>();
        for (TimeTableItem item : optional.getSortedItems()) {
            Map<Integer, Set<Integer>> currentClass = classesDaysAndHours.get(item.getSchoolClass());
            Set<Integer> currentHours = currentClass.get(item.getDay());
            boolean added = currentHours.add(item.getHour());
            if(!added){
                // class already study at this time
                falseClasses.add(item.getSchoolClass());
                if(falseClasses.size() == classes.size()){
                    break;
                }
            }
        }

        ret -= ((double)(100 / classes.size()) * falseClasses.size());
        return ret;
    }

    private double evalSatis(TimeTable optional){
        double ret = 100;
        Map<SchoolClass, Map<Subject, Integer>> classesMap = new HashMap<>();
        classes.values().forEach(klass -> classesMap.put(klass,new HashMap<>(klass.getSubjectsNeeded())));

        Set<SchoolClass> falseClasses = new HashSet<>();
        for (TimeTableItem item : optional.getSortedItems()) {
            SchoolClass klass = item.getSchoolClass();
            Subject subject = item.getSubject();

            if(!classesMap.get(klass).containsKey(subject)){
                falseClasses.add(klass);
                if(falseClasses.size() == classes.size()){
                    break;
                }

                continue;
            }

            int oldVal = classesMap.get(klass).get(subject);
            classesMap.get(klass).replace(subject, oldVal - 1);
        }

        for (Map.Entry<SchoolClass, Map<Subject, Integer>> current : classesMap.entrySet()) {
            for (Map.Entry<Subject, Integer> entry : current.getValue().entrySet()) {
                if(entry.getValue() != 0){
                    falseClasses.add(current.getKey());
                    if(falseClasses.size() == classes.size()){
                        break;
                    }
                }
            }
        }

        ret -= ((double)(100 / classes.size()) * falseClasses.size());
        return ret;
    }

    private double evalDayOffTeacher(TimeTable optional){
        return 0;
    }

    private double evalKnow(TimeTable optional){
        double ret = 100;
        Teacher teacher;
        Subject subject;
        Set<Teacher> falseTeachers = new HashSet<>();
        for(TimeTableItem item : optional.getSortedItems()){
            teacher = item.getTeacher();
            subject = item.getSubject();
            if(!teacher.getSubjects().contains(subject)){
                falseTeachers.add(teacher);
                if(falseTeachers.size() == teachers.size()){
                    break;
                }
            }
        }

        ret -= ((double)(100 / teachers.size()) * falseTeachers.size());
        return ret;
    }

    private double evalHuman(TimeTable optional){
        double ret = 100;
        Map<Teacher, Map<Integer, Set<Integer>>> teachersDaysAndHours = new HashMap<>(teachers.size());
        // initialize each teacher an map of days an optional hours of working
        for (Teacher teacher : teachers.values()) {
            teachersDaysAndHours.put(teacher, new HashMap<>());
            for(int i = 1; i <= days; i++){
                teachersDaysAndHours.get(teacher).put(i, new HashSet<>(hours));
            }
        }

        Set<Teacher> falseTeachers = new HashSet<>();
        for (TimeTableItem item : optional.getSortedItems()) {
            Map<Integer, Set<Integer>> currentTeacher = teachersDaysAndHours.get(item.getTeacher());
            Set<Integer> currentHours = currentTeacher.get(item.getDay());
            boolean added = currentHours.add(item.getHour());
            if(!added){
                // teacher is teaching in different places at the same time
                falseTeachers.add(item.getTeacher());
                if(falseTeachers.size() == teachers.size()){
                    break;
                }
            }
        }

        ret -= ((double)(100 / teachers.size()) * falseTeachers.size());
        return ret;
    }

    private double evalWork(TimeTable optional){
        return 0;
    }

    private <T extends SerialItem> Map<Integer, T> createMapFromSet(Set<T> items){
        Map<Integer, T> ret = new HashMap<>();
        for (T item : items) {
            ret.put(item.getId(), item);
        }

        return ret;
    }
}