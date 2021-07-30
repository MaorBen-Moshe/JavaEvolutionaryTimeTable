package DTO;

import models.SerialItem;

import java.util.Objects;

public class SerialItemDTO {
    private final int id;
    private final String name;

    public SerialItemDTO(String name, int id){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerialItemDTO that = (SerialItemDTO) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'';
    }

    protected int compareTo(SerialItemDTO o) {
        int ret = Integer.compare(id, o.id);
        if(ret == 0){
            ret = name.compareTo(o.name);
        }

        return ret;
    }
}
