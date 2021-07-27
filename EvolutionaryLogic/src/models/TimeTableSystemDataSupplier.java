package models;

import Interfaces.DataSupplier;

import java.util.HashMap;
import java.util.Map;

public class TimeTableSystemDataSupplier implements DataSupplier {
    public Map<Integer, Teacher> getTeachers() {
        return new HashMap<>(teachers);
    }

    public Map<Integer, Subject> getSubjects() {
        return new HashMap<>(subjects);
    }

    public Map<Integer, SchoolClass> getClasses() {
        return new HashMap<>(classes);
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    private final Map<Integer, Teacher> teachers;
    private final Map<Integer, Subject> subjects;
    private final Map<Integer, SchoolClass> classes;
    private final int days;
    private final int hours;

    public TimeTableSystemDataSupplier(int days,
                                       int hours,
                                       Map<Integer, Teacher> teachers,
                                       Map<Integer, Subject> subjects, Map<Integer, SchoolClass> classes){
        this.days = days;
        this.hours = hours;
        this.teachers = new HashMap<>(teachers);
        this.subjects = new HashMap<>(subjects);
        this.classes = new HashMap<>(classes);
    }
}
