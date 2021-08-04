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
        return rules.evaluateRules(optional, getSystemInfo());
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

    private <T extends SerialItem> Map<Integer, T> createMapFromSet(Set<T> items){
        Map<Integer, T> ret = new HashMap<>();
        for (T item : items) {
            ret.put(item.getId(), item);
        }

        return ret;
    }
}