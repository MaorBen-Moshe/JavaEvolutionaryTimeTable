package models;
import java.util.*;
import java.util.function.Predicate;

public class TimeTable {
    public Set<TimeTableItem> getSortedItems() {
        return unModifiedSortedItems;
    }

    private final Set<TimeTableItem> sortedItems;
    private Set<TimeTableItem> unModifiedSortedItems;
    private final Map<Rule, Double> rulesScore;
    private Map<Rule, Double> unModifiedRulesScore;

    public double getHardRulesAvg() {
        return hardRulesAvg;
    }

    public void setHardRulesAvg(double hardRulesAvg) {
        this.hardRulesAvg = hardRulesAvg;
    }

    public double getSoftRulesAvg() {
        return softRulesAvg;
    }

    public void setSoftRulesAvg(double softRulesAvg) {
        this.softRulesAvg = softRulesAvg;
    }

    private double hardRulesAvg;
    private double softRulesAvg;

    public TimeTable() {
        sortedItems = new TreeSet<>();
        rulesScore = new HashMap<>();
    }

    public boolean add(TimeTableItem item){
        boolean ret = sortedItems.add(item);
        if(ret){
            this.unModifiedSortedItems = Collections.unmodifiableSortedSet(new TreeSet<>(sortedItems));
        }

        return ret;
    }

    public boolean remove(TimeTableItem item){
        boolean ret = sortedItems.remove(item);
        if(ret){
            this.unModifiedSortedItems = Collections.unmodifiableSortedSet(new TreeSet<>(sortedItems));
        }

        return ret;
    }

    public int size() {return sortedItems.size(); }

    public boolean contains(int day, int hour, int classId, int teacherId, int subjectId){
        boolean ret = false;
        for(TimeTableItem item : sortedItems){
            if(item.equalsById(day, hour, classId, teacherId, subjectId)){
                ret = true;
                break;
            }
        }

        return ret;
    }

    public boolean contains(int day, int hour, Predicate<TimeTableItem> predicate){
        boolean ret = false;
        for (TimeTableItem item : sortedItems){
           ret = item.getDay() == day && item.getHour() == hour && predicate.test(item);
           if(ret){
               break;
           }
        }

        return ret;
    }

    public void addRuleScore(Rule rule, double score){
        if(rulesScore.containsKey(rule)){
            rulesScore.replace(rule, score);
        }
        else{
            rulesScore.put(rule, score);
        }

        unModifiedRulesScore = Collections.unmodifiableMap(rulesScore);
    }

    public Map<Rule, Double> getRulesScore(){
        return unModifiedRulesScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTable timeTable = (TimeTable) o;
        return Double.compare(timeTable.hardRulesAvg, hardRulesAvg) == 0 && Double.compare(timeTable.softRulesAvg, softRulesAvg) == 0 && sortedItems.equals(timeTable.sortedItems) && rulesScore.equals(timeTable.rulesScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortedItems, rulesScore, hardRulesAvg, softRulesAvg);
    }
}