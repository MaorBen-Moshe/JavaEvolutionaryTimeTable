package models;

import java.io.Serializable;
import java.util.Objects;

public class TimeTableItem implements Comparable<TimeTableItem>, Serializable {
    private int day;
    private int hour;
    private SchoolClass schoolClass;
    private Teacher teacher;
    private Subject subject;

    public TimeTableItem(int day, int hour, SchoolClass schoolClass, Teacher teacher, Subject subject) {
        this.day = day;
        this.hour = hour;
        this.schoolClass = schoolClass;
        this.teacher = teacher;
        this.subject = subject;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableItem that = (TimeTableItem) o;
        return day == that.day && hour == that.hour && schoolClass.equals(that.schoolClass) && teacher.equals(that.teacher) && subject.equals(that.subject);
    }

    public boolean equalsById(int day, int hour, int classId, int teacherId, int subjectId){
        return day == this.day &&
                hour == this.hour &&
                this.schoolClass.getId() == classId &&
                this.teacher.getId() == teacherId &&
                this.subject.getId() == subjectId;
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
        int ret = Integer.compare(day, o.day);
        if(ret == 0){
            ret = Integer.compare(hour, o.hour);
            if(ret == 0){
                ret = schoolClass.compareTo(o.schoolClass);
                if(ret == 0){
                    ret = teacher.compareTo(o.teacher);
                    if(ret == 0){
                        ret = subject.compareTo(o.subject);
                    }
                }
            }
        }

        return ret;
    }
}