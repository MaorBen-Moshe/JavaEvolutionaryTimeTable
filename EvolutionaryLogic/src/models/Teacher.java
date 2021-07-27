package models;

import java.util.*;

public class Teacher extends SerialItem {
    public Set<Subject> getSubjects() {
        return new HashSet<>(subjects);
    }

    private final Set<Subject> subjects;

    public Teacher(String name, int id, Set<Subject> subjects) {
        super(name, id);
        this.subjects = new HashSet<>(subjects);
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
                "subjects=" + subjects;
    }
}
