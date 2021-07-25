package evolutinary;

import models.*;

import java.util.*;

public class TimeTableEvolutionarySystemImpel extends EvolutionarySystemImpel<TimeTable> {
    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Set<SchoolClass> getClasses() {
        return classes;
    }

    public void setClasses(Set<SchoolClass> classes) {
        this.classes = classes;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        if(days > 0){
            this.days = days;
        }
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        if(hours > 0){
            this.hours = hours;
        }
    }

    //configuration of time table
    private Rules rules;
    private Set<Teacher> teachers;
    private Set<Subject> subjects;
    private Set<SchoolClass> classes;
    private int days;
    private int hours;
    private final Random random;

    public TimeTableEvolutionarySystemImpel(){
        random = new Random();
    }


    protected TimeTable createOptionalSolution(){
        int maxNumber = days * hours * teachers.size() * subjects.size() * classes.size();
        int minNumber = classes.stream().mapToInt(SchoolClass::getTotalNumberOfHours).sum();
        TimeTable timeTable = new TimeTable();
        int currentTableSize = random.ints(minNumber, maxNumber).findFirst().orElse(0);
        for(int i = 0; i < currentTableSize; i++){
            timeTable.add(createItem());
        }

        //Collections.sort(timeTable);
        return timeTable;
    }

    private TimeTableItem createItem(){
        int daySelected, hourSelected;
        Teacher teacherSelected;
        Subject subjectSelected;
        SchoolClass classSelected;

        daySelected = random.ints(0, days).findFirst().orElse(-1);
        hourSelected = random.ints(0, hours).findFirst().orElse(-1);
        if(daySelected == -1 || hourSelected == -1){
            throw new IllegalArgumentException("Failed to random in create item");
        }

        teacherSelected = getRandItem(teachers);
        subjectSelected = getRandItem(subjects);
        classSelected = getRandItem(classes);
        return new TimeTableItem(daySelected, hourSelected, classSelected, teacherSelected, subjectSelected);
    }

    private <T  extends SerialItem> T getRandItem(Set<T> collection){
        int RandInt = random.nextInt(collection.size());
        return collection.stream().filter(item -> item.getId() == RandInt).findFirst().orElse(null);
    }

    @Override
    protected void createGeneration() {

    }

    @Override
    protected double evaluate(TimeTable optional) {
        double hard = 100;
        double soft = 100;
        for (Rule rule: rules) {
            double answer = switch (rule.getRuleType()) {
                case Sequentiality -> evalSeq(optional);
                case DayOffClass -> evalDayOffClass(optional);
                case Singularity -> evalSingul(optional);
                case Satisfactory -> evalSatis(optional);
                case DayOffTeacher -> evalDayOffTeacher(optional);
                case Knowledgeable -> evalKnow(optional);
                case TeacherIsHuman -> evalHuman(optional);
                case WorkingHoursPreference -> evalWork(optional);
            };

            if(rule.getStrength().equals(Rule.eStrength.Hard)){
                hard -= answer;
            }else {
                soft -= answer;
            }
        }

        hard = Math.max(hard, 0);
        soft = Math.max(soft, 0);

        int hardWeight = rules.getHardRulesWeight();
        return ((hardWeight * hard) + ((100 - hardWeight) * soft)) / 100;
    }

    private double evalSeq(TimeTable optional){
       return 0;
    }

    private double evalDayOffClass(TimeTable optional){
        return 0;
    }

    private double evalSingul(TimeTable optional){
        double ret = 0;
        Map<SchoolClass, Map<Integer, Set<Integer>>> classesDaysAndHours = new HashMap<>(classes.size());
        // initialize each teacher an map of days an optional hours of working
        for (SchoolClass klass : classes) {
            classesDaysAndHours.put(klass, new HashMap<>());
            for(int i = 0; i < days; i++){
                classesDaysAndHours.get(klass).put(i, new HashSet<>(hours));
            }
        }

        for (TimeTableItem item : optional) {
            Map<Integer, Set<Integer>> currentClass = classesDaysAndHours.get(item.getSchoolClass());
            Set<Integer> currentHours = currentClass.get(item.getDay());
            boolean added = currentHours.add(item.getHour());
            if(!added){
                // class already study at this time
                ret += 0.5;
            }
        }

        return ret;
    }

    private double evalSatis(TimeTable optional){
        double ret = 0;
        Map<SchoolClass, Map<Subject, Integer>> classesMap = new HashMap<>();
        classes.forEach(klass -> {
            classesMap.put(klass,new HashMap<>(klass.getSubjectsNeeded()));
        });

        for (TimeTableItem item : optional) {
            SchoolClass klass = item.getSchoolClass();
            Subject subject = item.getSubject();

            if(!classesMap.get(klass).containsKey(subject)){
                ret += 0.5;
                continue;
            }

            int oldVal = classesMap.get(klass).get(subject);
            classesMap.get(klass).replace(subject, oldVal - 1);
        }

        for (Map.Entry<SchoolClass, Map<Subject, Integer>> current : classesMap.entrySet()) {
            for (Map.Entry<Subject, Integer> entry : current.getValue().entrySet()) {
                if(entry.getValue() != 0){
                    ret += 0.5;
                }
            }
        }

        return ret;
    }

    private double evalDayOffTeacher(TimeTable optional){
        return 0;
    }

    private double evalKnow(TimeTable optional){
        double ret = 0;
        Teacher teacher;
        Subject subject;
        for(TimeTableItem item : optional){
            teacher = item.getTeacher();
            subject = item.getSubject();
            if(!teacher.getSubjects().contains(subject)){
                ret += 0.5;
            }
        }

        return ret;
    }

    private double evalHuman(TimeTable optional){
        double ret = 0;
        Map<Teacher, Map<Integer, Set<Integer>>> teachersDaysAndHours = new HashMap<>(teachers.size());
        // initialize each teacher an map of days an optional hours of working
        for (Teacher teacher : teachers) {
            teachersDaysAndHours.put(teacher, new HashMap<>());
            for(int i = 0; i < days; i++){
                teachersDaysAndHours.get(teacher).put(i, new HashSet<>(hours));
            }
        }

        for (TimeTableItem item : optional) {
            Map<Integer, Set<Integer>> currentTeacher = teachersDaysAndHours.get(item.getTeacher());
            Set<Integer> currentHours = currentTeacher.get(item.getDay());
            boolean added = currentHours.add(item.getHour());
            if(!added){
                // teacher is teaching in different places at the same time
                ret += 0.5;
            }
        }

        return ret;
    }

    private double evalWork(TimeTable optional){
        return 0;
    }
}