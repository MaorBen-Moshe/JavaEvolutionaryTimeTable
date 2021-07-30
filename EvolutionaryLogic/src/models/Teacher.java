package models;

import java.util.*;

public class Teacher extends SerialItem implements Comparable<Teacher> {
    private final Set<Subject> subjects;
    private final Set<Subject> unModifiedSubjects;

    public Teacher(String name, int id, Set<Subject> subjects) {
        super(name, id);
        this.subjects = new HashSet<>(subjects);
        this.unModifiedSubjects = Collections.unmodifiableSet(this.subjects);
    }

    public Set<Subject> getSubjects() {
        return unModifiedSubjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return subjects.equals(teacher.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subjects);
    }

    @Override
    public String toString() {
        return  super.toString() + ", " +
                "subjects =" + subjects;
    }

    @Override
    public int compareTo(Teacher o){
        int ret = super.compareTo(o);
        if(ret == 0){
            ret = subjects.equals(o.subjects) ? 0 : 1;
        }

        return ret;
    }
}