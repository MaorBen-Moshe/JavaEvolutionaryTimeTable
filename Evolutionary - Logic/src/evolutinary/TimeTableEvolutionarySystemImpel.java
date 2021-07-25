package evolutinary;

import models.*;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class TimeTableEvolutionarySystemImpel extends EvolutionarySystemImpel<TimeTable> {
    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Set<SchoolClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<SchoolClass> classes) {
        this.classes = classes;
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

    //configuration of time table
    private Rules rules;
    private Set<Teacher> teachers;
    private Set<Subject> subjects;
    private Set<SchoolClass> classes;
    private int days;
    private int hours;
    private final Random random;

    public TimeTableEvolutionarySystemImpel(){
        random = new Random();
    }


    protected TimeTable createOptionalSolution(){
        int maxNumber = days * hours * teachers.size() * subjects.size() * classes.size();
        int minNumber = classes.stream().mapToInt(SchoolClass::getTotalNumberOfHours).sum();
        TimeTable timeTable = new TimeTable(maxNumber);
        int currentTableSize = random.ints(minNumber, maxNumber).findFirst().orElse(0);
        for(int i = 0; i < currentTableSize; i++){
            timeTable.add(createItem());
        }

        Collections.sort(timeTable);
        return timeTable;
    }

    private TimeTableItem createItem(){
        int daySelected, hourSelected;
        Teacher teacherSelected;
        Subject subjectSelected;
        SchoolClass classSelected;

        daySelected = random.ints(0, days).findFirst().orElse(-1);
        hourSelected = random.ints(0, hours).findFirst().orElse(-1);
        if(daySelected == -1 || hourSelected == -1){
            throw new IllegalArgumentException("Failed to random in create item");
        }

        teacherSelected = getRandItem(teachers);
        subjectSelected = getRandItem(subjects);
        classSelected = getRandItem(classes);
        return new TimeTableItem(daySelected, hourSelected, classSelected, teacherSelected, subjectSelected);
    }

    private <T  extends SerialItem> T getRandItem(Set<T> collection){
        int RandInt = random.nextInt(collection.size());
        return collection.stream().filter(item -> item.getId() == RandInt).findFirst().orElse(null);
    }

    @Override
    protected void createGeneration() {

    }

    @Override
    protected int evaluate(TimeTable optional) {
        return 0;
    }
}