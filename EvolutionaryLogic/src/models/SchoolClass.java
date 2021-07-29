package models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SchoolClass extends SerialItem {
    private final Map<Subject, Integer> subjectsNeeded; // subject and the hours the class needs in a week
    private final Map<Subject, Integer> unModifiedSubjectsNeeded;

    public Map<Subject, Integer> getSubjectsNeeded() {
        return unModifiedSubjectsNeeded;
    }

    public int getTotalNumberOfHours() {
        return totalNumberOfHours;
    }

    private final int totalNumberOfHours;

    public SchoolClass(String name, int id, Map<Subject, Integer> subjects) {
        super(name, id);
        subjectsNeeded = new HashMap<>(subjects);
        unModifiedSubjectsNeeded = Collections.unmodifiableMap(subjectsNeeded);
        totalNumberOfHours = subjectsNeeded.values().stream().mapToInt(integer -> integer).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SchoolClass that = (SchoolClass) o;
        return subjectsNeeded.equals(that.subjectsNeeded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subjectsNeeded);
    }

    @Override
    public String toString() {
        return  super.toString() + ", subjectsNeeded=" + subjectsNeeded;
    }
}
