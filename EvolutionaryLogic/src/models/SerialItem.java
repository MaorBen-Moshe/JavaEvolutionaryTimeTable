package models;

import java.io.Serializable;
import java.util.Objects;

public abstract class SerialItem implements Serializable {
    private final int id;
    private final String name;

    public SerialItem(String name, int id) {
        this.name = name;
        this.id = id;
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
        SerialItem that = (SerialItem) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "id = " + id +
                ", name = " + name;
    }

    protected int compareTo(SerialItem o) {
        int ret = Integer.compare(id, o.id);
        if(ret == 0){
            ret = name.compareTo(o.name);
        }

        return ret;
    }
}