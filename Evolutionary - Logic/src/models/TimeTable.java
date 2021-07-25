package models;

import java.util.ArrayList;

public class TimeTable extends ArrayList<TimeTableItem> {
    public TimeTable(int maxSize) {
        super(maxSize);
    }
}