package models;

import java.util.Objects;

public class TimeTableItem implements Comparable<TimeTableItem> {
    private final int day;
    private final int hour;
    private final SchoolClass schoolClass;
    private final Teacher teacher;
    private final Subject subject;

    public TimeTableItem(int day, int hour, SchoolClass schoolClass, Teacher teacher, Subject subject) {
        this.day = day;
        this.hour = hour;
        this.schoolClass = schoolClass;
        this.teacher = teacher;
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableItem that = (TimeTableItem) o;
        return day == that.day && hour == that.hour && schoolClass.equals(that.schoolClass) && teacher.equals(that.teacher) && subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, schoolClass, teacher, subject);
    }

    @Override
    public String toString() {
        return "day=" + day +
                ", hour=" + hour +
                ", schoolClass=" + schoolClass +
                ", teacher=" + teacher +
                ", subject=" + subject;
    }

    @Override
    public int compareTo(TimeTableItem o) {
        int daysCompare = Integer.compare(day, o.day);
        if(daysCompare != 0){
            return daysCompare;
        }

        return Integer.compare(hour, o.hour);
    }
}
