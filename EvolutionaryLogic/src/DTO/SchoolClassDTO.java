package DTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SchoolClassDTO extends SerialItemDTO{
    public int getTotalNumberOfHours() {
        return totalNumberOfHours;
    }

    public Map<SubjectDTO, Integer> getSubjectsNeeded() {
        return new HashMap<>(subjectsNeeded);
    }

    private final int totalNumberOfHours;
    private final Map<SubjectDTO, Integer> subjectsNeeded;

    public SchoolClassDTO(String name, int id, Map<SubjectDTO, Integer> subjects) {
        super(name, id);
        this.subjectsNeeded = subjects;
        totalNumberOfHours = subjectsNeeded.values().stream().mapToInt(integer -> integer).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolClassDTO that = (SchoolClassDTO) o;
        return totalNumberOfHours == that.totalNumberOfHours && subjectsNeeded.equals(that.subjectsNeeded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalNumberOfHours, subjectsNeeded);
    }
}
