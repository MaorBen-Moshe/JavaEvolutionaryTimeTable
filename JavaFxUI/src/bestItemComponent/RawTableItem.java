package bestItemComponent;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RawTableItem{
    private final SimpleIntegerProperty day;
    private final  SimpleIntegerProperty hour;
    private final SimpleStringProperty teacher;
    private final SimpleStringProperty klass;
    private final SimpleStringProperty subject;


    public RawTableItem(int day, int hour, String teacher, String klass, String subject) {
        this.day = new SimpleIntegerProperty(day);
        this.hour = new SimpleIntegerProperty(hour);
        this.teacher = new SimpleStringProperty(teacher);
        this.klass = new SimpleStringProperty(klass);
        this.subject = new SimpleStringProperty(subject);
    }

    public int getDay() {
        return day.get();
    }

    public int getHour() {
        return hour.get();
    }

    public String getTeacher() {
        return teacher.get();
    }

    public String getKlass() {
        return klass.get();
    }

    public String getSubject() {
        return subject.get();
    }
}