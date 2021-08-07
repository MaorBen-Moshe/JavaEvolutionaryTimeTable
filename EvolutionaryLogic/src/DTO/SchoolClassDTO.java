package DTO;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class SchoolClassDTO extends SerialItemDTO implements Comparable<SchoolClassDTO>{
    private final int totalNumberOfHours;
    private final Map<SubjectDTO, Integer> subjectsNeeded;
    private final Map<SubjectDTO, Integer> unModifiedSubjectsNeeded;

    public SchoolClassDTO(String name, int id, Map<SubjectDTO, Integer> subjects) {
        super(name, id);
        this.subjectsNeeded = subjects;
        unModifiedSubjectsNeeded = Collections.unmodifiableMap(this.subjectsNeeded);
        totalNumberOfHours = subjectsNeeded.values().stream().mapToInt(integer -> integer).sum();
    }

    public Map<SubjectDTO, Integer> getSubjectsNeeded() {
        return unModifiedSubjectsNeeded;
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

    @Override
    public String toString() {
        return super.toString() +
                ", subjectsNeeded:{ " + subjectToString() +
                "}, total number of hours = " + totalNumberOfHours;
    }

    @Override
    public int compareTo(SchoolClassDTO o) {
        int ret = super.compareTo(o);
        if(ret == 0){
            ret = subjectsNeeded.equals(o.subjectsNeeded) ? 0 : 1;
        }

        return ret;
    }

    private String subjectToString(){
        StringBuilder builder = new StringBuilder();
        subjectsNeeded.forEach((key, val) -> {
            builder.append("[ subject = ").append(key).append(", hours = ").append(val).append(" ]");
            builder.append(", ");
        });

        builder.replace(builder.length() - 2, builder.length(), "");
        return builder.toString();
    }
}