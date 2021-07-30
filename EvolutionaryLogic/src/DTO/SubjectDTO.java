package DTO;

public class SubjectDTO extends SerialItemDTO implements Comparable<SubjectDTO>{
    public SubjectDTO(String name, int id) {
        super(name, id);
    }

    @Override
    public int compareTo(SubjectDTO o) {
        return super.compareTo(o);
    }
}
