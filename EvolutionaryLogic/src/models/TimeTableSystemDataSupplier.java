package models;

import Interfaces.DataSupplier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TimeTableSystemDataSupplier implements DataSupplier {
    private final Map<Integer, Teacher> teachers;
    private final Map<Integer, Teacher> unModifiedTeachers;
    private final Map<Integer, Subject> subjects;
    private final Map<Integer, Subject> unModifiedSubjects;
    private final Map<Integer, SchoolClass> classes;
    private final Map<Integer, SchoolClass> unModifiedClasses;
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
        this.unModifiedTeachers = Collections.unmodifiableMap(this.teachers);
        this.unModifiedSubjects = Collections.unmodifiableMap(this.subjects);
        this.unModifiedClasses = Collections.unmodifiableMap(this.classes);
    }

    public Map<Integer, Teacher> getTeachers() {
        return unModifiedTeachers;
    }

    public Map<Integer, Subject> getSubjects() {
        return unModifiedSubjects;
    }

    public Map<Integer, SchoolClass> getClasses() {
        return unModifiedClasses;
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableSystemDataSupplier supplier = (TimeTableSystemDataSupplier) o;
        return days == supplier.days && hours == supplier.hours && teachers.equals(supplier.teachers) && subjects.equals(supplier.subjects) && classes.equals(supplier.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teachers, subjects, classes, days, hours);
    }
}