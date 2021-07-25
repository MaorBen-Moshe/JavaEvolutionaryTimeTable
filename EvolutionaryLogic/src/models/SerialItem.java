package models;

import java.util.Objects;

public abstract class SerialItem {
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private final int id;
    private final String name;


    public SerialItem(String name, int id) {
        this.name = name;
        this.id = id;
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
        return "id=" + id +
                ", name='" + name;
    }
}
