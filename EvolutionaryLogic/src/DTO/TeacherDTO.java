package DTO;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class TeacherDTO extends SerialItemDTO implements Comparable<TeacherDTO>{
    private final Set<SubjectDTO> subjects;
    private final Set<SubjectDTO> unModifiedSubjects;

    public TeacherDTO(String name, int id, Set<SubjectDTO> subjects) {
        super(name, id);
        this.subjects = subjects;
        this.unModifiedSubjects = Collections.unmodifiableSet(subjects);
    }

    public Set<SubjectDTO> getSubjects() {
        return unModifiedSubjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TeacherDTO that = (TeacherDTO) o;
        return subjects.equals(that.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subjects);
    }

    @Override
    public String toString() {
        return super.toString() + "[subjects=" + subjects + ']';
    }

    @Override
    public int compareTo(TeacherDTO o) {
        int ret = super.compareTo(o);
        if(ret == 0){
            ret = subjects.equals(o.subjects) ? 0 : 1;
        }

        return ret;
    }
}