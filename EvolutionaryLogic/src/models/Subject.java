package models;

import java.io.Serializable;

public class Subject extends SerialItem implements Comparable<Subject>, Serializable {

    public Subject(String name, int id) {
        super(name, id);
    }

    @Override
    public int compareTo(Subject o) {
        return super.compareTo(o);
    }
}