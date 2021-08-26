package DTO;

import Interfaces.DataSupplier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TimeTableSystemDataSupplierDTO implements DataSupplier {
    private final Map<Integer, TeacherDTO> teachers;
    private final Map<Integer, TeacherDTO> unModifiedTeachers;
    private final Map<Integer, SubjectDTO> subjects;
    private final Map<Integer, SubjectDTO> unModifiedSubjects;
    private final Map<Integer, SchoolClassDTO> classes;
    private final Map<Integer, SchoolClassDTO> unModifiedClasses;
    private final int days;
    private final int hours;

    public TimeTableSystemDataSupplierDTO(int days,
                                          int hours,
                                          Map<Integer, TeacherDTO> teachers,
                                          Map<Integer, SubjectDTO> subjects,
                                          Map<Integer, SchoolClassDTO> classes){
        this.days = days;
        this.hours = hours;
        this.teachers = new HashMap<>(teachers);
        this.subjects = new HashMap<>(subjects);
        this.classes = new HashMap<>(classes);
        this.unModifiedTeachers = Collections.unmodifiableMap(this.teachers);
        this.unModifiedSubjects = Collections.unmodifiableMap(this.subjects);
        this.unModifiedClasses = Collections.unmodifiableMap(this.classes);
    }

    public Map<Integer, TeacherDTO> getTeachers() {
        return unModifiedTeachers;
    }

    public Map<Integer, SubjectDTO> getSubjects() {
        return unModifiedSubjects;
    }

    public Map<Integer, SchoolClassDTO> getClasses() {
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
        TimeTableSystemDataSupplierDTO that = (TimeTableSystemDataSupplierDTO) o;
        return days == that.days && hours == that.hours && teachers.equals(that.teachers) && subjects.equals(that.subjects) && classes.equals(that.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teachers, subjects, classes, days, hours);
    }
}