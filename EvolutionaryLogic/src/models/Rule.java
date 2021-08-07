package models;

import java.util.*;
import java.util.stream.IntStream;

public class Rule {

    public enum eStrength{
        Soft, Hard
    }

    public enum eRules {
        DayOffClass{
            @Override
            public double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations) {
                return 0;
            }
        },
        DayOffTeacher {
            @Override
            public double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations) {
                return 0;
            }
        },
        Knowledgeable {
            @Override
            public double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations) {
                double ret = 100;
                Map<Integer, Teacher> teachers = supplier.getTeachers();
                Teacher teacher;
                Subject subject;
                Set<Teacher> falseTeachers = new HashSet<>();
                for(TimeTableItem item : optional.getSortedItems()){
                    teacher = item.getTeacher();
                    subject = item.getSubject();
                    if(!teacher.getSubjects().contains(subject)){
                        falseTeachers.add(teacher);
                        if(falseTeachers.size() == teachers.size()){
                            break;
                        }
                    }
                }

                ret -= ((double)(100 / teachers.size()) * falseTeachers.size());
                return ret;
            }
        },
        Sequentiality {
            @Override
            public double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations) {
                return 0;
            }
        },
        Singularity {
            @Override
            public double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations) {
                double ret = 100;
                Map<Integer, SchoolClass> classes = supplier.getClasses();
                Map<SchoolClass, Map<Integer, Set<Integer>>> classesDaysAndHours = new HashMap<>(classes.size());
                // initialize each teacher an map of days an optional hours of working
                classes.values().forEach(klass ->{
                    classesDaysAndHours.put(klass, new HashMap<>());
                    IntStream.range(1, supplier.getDays() + 1).forEach(i -> classesDaysAndHours.get(klass).put(i, new HashSet<>(supplier.getHours())));
                });

                Set<SchoolClass> falseClasses = new HashSet<>();
                for (TimeTableItem item : optional.getSortedItems()) {
                    Map<Integer, Set<Integer>> currentClass = classesDaysAndHours.get(item.getSchoolClass());
                    Set<Integer> currentHours = currentClass.get(item.getDay());
                    boolean added = currentHours.add(item.getHour());
                    if(!added){
                        // class already study at this time
                        falseClasses.add(item.getSchoolClass());
                        if(falseClasses.size() == supplier.getClasses().size()){
                            break;
                        }
                    }
                }

                ret -= ((double)(100 / classes.size()) * falseClasses.size());
                return ret;
            }
        },
        TeacherIsHuman {
            @Override
            public double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations) {
                double ret = 100;
                Map<Integer, Teacher> teachers = supplier.getTeachers();
                Map<Teacher, Map<Integer, Set<Integer>>> teachersDaysAndHours = new HashMap<>(teachers.size());
                // initialize each teacher an map of days an optional hours of working
                teachers.values().forEach(teacher ->{
                    teachersDaysAndHours.put(teacher, new HashMap<>());
                    IntStream.range(1, supplier.getDays() + 1).forEach(i -> teachersDaysAndHours.get(teacher).put(i, new HashSet<>(supplier.getHours())));
                });

                Set<Teacher> falseTeachers = new HashSet<>();
                for (TimeTableItem item : optional.getSortedItems()) {
                    Map<Integer, Set<Integer>> currentTeacher = teachersDaysAndHours.get(item.getTeacher());
                    Set<Integer> currentHours = currentTeacher.get(item.getDay());
                    boolean added = currentHours.add(item.getHour());
                    if(!added){
                        // teacher is teaching in different places at the same time
                        falseTeachers.add(item.getTeacher());
                        if(falseTeachers.size() == teachers.size()){
                            break;
                        }
                    }
                }

                ret -= ((double)(100 / teachers.size()) * falseTeachers.size());
                return ret;
            }
        },
        WorkingHoursPreference {
            @Override
            public double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations) {
                return 0;
            }
        },
        Satisfactory {
            @Override
            public double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations) {
                double ret = 100;
                Map<Integer, SchoolClass> classes = supplier.getClasses();
                Map<SchoolClass, Map<Subject, Integer>> classesMap = new HashMap<>();
                classes.values().forEach(klass -> classesMap.put(klass,new HashMap<>(klass.getSubjectsNeeded())));

                Set<SchoolClass> falseClasses = new HashSet<>();
                for (TimeTableItem item : optional.getSortedItems()) {
                    SchoolClass klass = item.getSchoolClass();
                    Subject subject = item.getSubject();

                    if(!classesMap.get(klass).containsKey(subject)){
                        falseClasses.add(klass);
                        if(falseClasses.size() == classes.size()){
                            break;
                        }

                        continue;
                    }

                    int oldVal = classesMap.get(klass).get(subject);
                    classesMap.get(klass).replace(subject, oldVal - 1);
                }

                classesMap.forEach((key, val) -> {
                    for (Map.Entry<Subject, Integer> entry : val.entrySet()) {
                        if(entry.getValue() != 0){
                            falseClasses.add(key);
                            if(falseClasses.size() == classes.size()){
                                break;
                            }
                        }
                    }
                });

                ret -= ((double)(100 / classes.size()) * falseClasses.size());
                return ret;
            }
        };

        public abstract double evaluate(TimeTable optional, TimeTableSystemDataSupplier supplier, Map<String, Object> configurations);
    }

    private final eRules ruleType;
    private final eStrength strength;
    private final Map<String, Object> configurations;
    private Map<String, Object> unModifiedConfigurations;

    public Rule(eRules rule, eStrength strength,  Map<String, Object> configurations){
        ruleType = rule;
        this.strength = strength;
        this.configurations = configurations == null ? new HashMap<>() : configurations;
        unModifiedConfigurations = Collections.unmodifiableMap(this.configurations);
    }

    public double evaluateRule(TimeTable optional, TimeTableSystemDataSupplier supplier){
        return ruleType.evaluate(optional, supplier, configurations);
    }

    public eRules getRuleType() {
        return ruleType;
    }

    public eStrength getStrength() {
        return strength;
    }

    public Map<String, Object> getConfigurations() {
        return unModifiedConfigurations;
    }

    @Override
    public String toString() {
        return "Rule { " +
                "ruleType=" + ruleType +
                ", strength=" + strength +
                (configurations.size() != 0 ? ", configurations= " + configurations : "") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return strength == rule.strength && ruleType == rule.ruleType && configurations.equals(rule.configurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strength, ruleType, configurations);
    }

    public void addConfiguration(String key, Object val){
        if(!configurations.containsKey(key)){
            configurations.put(key, val);
            unModifiedConfigurations = Collections.unmodifiableMap(this.configurations);
        }
    }
}