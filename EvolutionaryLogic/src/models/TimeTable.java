package models;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class TimeTable {
    public Set<TimeTableItem> getSortedItems() {
        return new TreeSet<>(sortedItems);
    }

    private final Set<TimeTableItem> sortedItems;

    public TimeTable() {
        sortedItems = new TreeSet<>();
    }

    public boolean add(TimeTableItem item){
        return sortedItems.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTable timeTable = (TimeTable) o;
        return sortedItems.equals(timeTable.sortedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortedItems);
    }
}