package models;

import java.io.Serializable;
import java.util.*;

public class Teacher extends SerialItem implements Comparable<Teacher>, Serializable {
    private final Set<Subject> subjects;
    private final Set<Subject> unModifiedSubjects;
    private final int workingHoursPref;

    public Teacher(String name, int id, Set<Subject> subjects, int workingHoursPref) {
        super(name, id);
        this.subjects = new HashSet<>(subjects);
        this.unModifiedSubjects = Collections.unmodifiableSet(this.subjects);
        this.workingHoursPref = workingHoursPref;
    }

    public Set<Subject> getSubjects() {
        return unModifiedSubjects;
    }


    @Override
    public int compareTo(Teacher o){
        int ret = super.compareTo(o);
        if(ret == 0){
            ret = subjects.equals(o.subjects) ? 0 : 1;
        }

        return ret;
    }

    public int getWorkingHoursPref() {
        return workingHoursPref;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "subjects=" + subjects +
                ", unModifiedSubjects=" + unModifiedSubjects +
                ", workingHoursPref=" + workingHoursPref +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return workingHoursPref == teacher.workingHoursPref && subjects.equals(teacher.subjects) && unModifiedSubjects.equals(teacher.unModifiedSubjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subjects, unModifiedSubjects, workingHoursPref);
    }
}