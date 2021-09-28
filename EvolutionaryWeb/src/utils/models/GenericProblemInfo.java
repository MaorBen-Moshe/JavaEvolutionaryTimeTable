package utils.models;

public class GenericProblemInfo {
    private final int days;
    private final int hours;

    public GenericProblemInfo(int days, int hours){
        this.days = days;
        this.hours = hours;
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }
}
