package models;
import java.util.*;

public class TimeTable {
    public Set<TimeTableItem> getSortedItems() {
        return new TreeSet<>(sortedItems);
    }

    private final Set<TimeTableItem> sortedItems;
    private final Map<Rule, Double> rulesScore;

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
        return sortedItems.add(item);
    }

    public boolean remove(TimeTableItem item){
        return sortedItems.remove(item);
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

    public void addRuleScore(Rule rule, double score){
        if(rulesScore.containsKey(rule)){
            rulesScore.replace(rule, score);
        }
        else{
            rulesScore.put(rule, score);
        }
    }

    public Map<Rule, Double> getRulesScore(){
        return new HashMap<>(rulesScore);
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