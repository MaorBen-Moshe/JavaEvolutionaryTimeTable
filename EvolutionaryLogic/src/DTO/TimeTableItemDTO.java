package DTO;

import java.util.Objects;

public class TimeTableItemDTO implements Comparable<TimeTableItemDTO> {
    private final int day;
    private final int hour;
    private final SchoolClassDTO schoolClass;
    private final SubjectDTO subject;
    private final TeacherDTO teacher;

    public TimeTableItemDTO(int day, int hour, SchoolClassDTO klass, SubjectDTO subject, TeacherDTO teacher){
        this.day = day;
        this.hour = hour;
        this.schoolClass = klass;
        this.subject = subject;
        this.teacher = teacher;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public SchoolClassDTO getSchoolClass() {
        return schoolClass;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    @Override
    public int compareTo(TimeTableItemDTO o) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableItemDTO that = (TimeTableItemDTO) o;
        return day == that.day && hour == that.hour && schoolClass.equals(that.schoolClass) && subject.equals(that.subject) && teacher.equals(that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, schoolClass, subject, teacher);
    }
}