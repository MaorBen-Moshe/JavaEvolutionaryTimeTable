package evolutinary;

import models.*;

import java.util.*;

public class TimeTableEvolutionarySystemImpel extends EvolutionarySystemImpel<TimeTable> {
    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = createMapFromSet(teachers);
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = createMapFromSet(subjects);
    }

    public Collection<Subject> getSubjects() {
        return subjects.values();
    }

    public void setClasses(Set<SchoolClass> classes) {
        this.classes = createMapFromSet(classes);
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
    private Map<Integer, Teacher> teachers;

    private Map<Integer, Subject> subjects;
    private Map<Integer, SchoolClass> classes;
    private int days;
    private int hours;
    private final Random random;

    public TimeTableEvolutionarySystemImpel(){
        random = new Random();
    }


    protected TimeTable createOptionalSolution(){
        int maxNumber = days * hours * teachers.size() * subjects.size() * classes.size();
        int minNumber = classes.values().stream().mapToInt(SchoolClass::getTotalNumberOfHours).sum();
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

        daySelected = random.nextInt(days);
        hourSelected = random.nextInt(hours);
        teacherSelected = getRandItem(teachers);
        subjectSelected = getRandItem(subjects);
        classSelected = getRandItem(classes);
        return new TimeTableItem(daySelected, hourSelected, classSelected, teacherSelected, subjectSelected);
    }

    private <T extends SerialItem> T getRandItem(Map<Integer, T> collection){
        int randInt = random.nextInt(collection.size());
        return collection.get(randInt);
    }

    @Override
    protected void createGeneration() {

    }

    @Override
    protected double evaluate(TimeTable optional) {
        List<Double> hardList = new ArrayList<>();
        List<Double> softList = new ArrayList<>();
        double answer = 0;
        for (Rule rule: rules.getRules()) {
            switch (rule.getRuleType()) {
                case Sequentiality: answer = evalSeq(optional); break;
                case DayOffClass: answer = evalDayOffClass(optional); break;
                case Singularity: answer = evalSingul(optional); break;
                case Satisfactory: answer = evalSatis(optional); break;
                case DayOffTeacher: answer = evalDayOffTeacher(optional); break;
                case Knowledgeable: answer = evalKnow(optional); break;
                case TeacherIsHuman: answer = evalHuman(optional); break;
                case WorkingHoursPreference: answer = evalWork(optional); break;
            }

            if(rule.getStrength().equals(Rule.eStrength.Hard)){
                hardList.add(answer);
            }else {
                softList.add(answer);
            }

            optional.addRuleScore(rule, answer);
        }

        double hard = getAvg(hardList);
        double soft = getAvg(softList);
        optional.setHardRulesAvg(hard);
        optional.setSoftRulesAvg(soft);
        int hardWeight = rules.getHardRulesWeight();
        return ((hardWeight * hard) + ((100 - hardWeight) * soft)) / 100;
    }

    private double getAvg(List<Double> list){
        double ret = 0;
        if(list.size() != 0){
            ret = list.stream().mapToDouble(x -> x).sum();
            ret /= list.size();
        }

        return ret;
    }

    private double evalSeq(TimeTable optional){
       return 0;
    }

    private double evalDayOffClass(TimeTable optional){
        return 0;
    }

    private double evalSingul(TimeTable optional){
        double ret = 100;
        Map<SchoolClass, Map<Integer, Set<Integer>>> classesDaysAndHours = new HashMap<>(classes.size());
        // initialize each teacher an map of days an optional hours of working
        for (SchoolClass klass : classes.values()) {
            classesDaysAndHours.put(klass, new HashMap<>());
            for(int i = 0; i < days; i++){
                classesDaysAndHours.get(klass).put(i, new HashSet<>(hours));
            }
        }

        Set<SchoolClass> falseClasses = new HashSet<>();
        for (TimeTableItem item : optional.getSortedItems()) {
            Map<Integer, Set<Integer>> currentClass = classesDaysAndHours.get(item.getSchoolClass());
            Set<Integer> currentHours = currentClass.get(item.getDay());
            boolean added = currentHours.add(item.getHour());
            if(!added){
                // class already study at this time
                falseClasses.add(item.getSchoolClass());
            }
        }

        ret -= ((double)(100 / classes.size()) * falseClasses.size());
        return ret;
    }

    private double evalSatis(TimeTable optional){
        double ret = 100;
        Map<SchoolClass, Map<Subject, Integer>> classesMap = new HashMap<>();
        classes.values().forEach(klass -> classesMap.put(klass,new HashMap<>(klass.getSubjectsNeeded())));

        Set<SchoolClass> falseClasses = new HashSet<>();
        for (TimeTableItem item : optional.getSortedItems()) {
            SchoolClass klass = item.getSchoolClass();
            Subject subject = item.getSubject();

            if(!classesMap.get(klass).containsKey(subject)){
                falseClasses.add(klass);
                continue;
            }

            int oldVal = classesMap.get(klass).get(subject);
            classesMap.get(klass).replace(subject, oldVal - 1);
        }

        for (Map.Entry<SchoolClass, Map<Subject, Integer>> current : classesMap.entrySet()) {
            for (Map.Entry<Subject, Integer> entry : current.getValue().entrySet()) {
                if(entry.getValue() != 0){
                    falseClasses.add(current.getKey());
                }
            }
        }

        ret -= ((double)(100 / classes.size()) * falseClasses.size());
        return ret;
    }

    private double evalDayOffTeacher(TimeTable optional){
        return 0;
    }

    private double evalKnow(TimeTable optional){
        double ret = 100;
        Teacher teacher;
        Subject subject;
        Set<Teacher> falseTeachers = new HashSet<>();
        for(TimeTableItem item : optional.getSortedItems()){
            teacher = item.getTeacher();
            subject = item.getSubject();
            if(!teacher.getSubjects().contains(subject)){
                falseTeachers.add(teacher);
            }
        }

        ret -= ((double)(100 / teachers.size()) * falseTeachers.size());
        return ret;
    }

    private double evalHuman(TimeTable optional){
        double ret = 0;
        Map<Teacher, Map<Integer, Set<Integer>>> teachersDaysAndHours = new HashMap<>(teachers.size());
        // initialize each teacher an map of days an optional hours of working
        for (Teacher teacher : teachers.values()) {
            teachersDaysAndHours.put(teacher, new HashMap<>());
            for(int i = 0; i < days; i++){
                teachersDaysAndHours.get(teacher).put(i, new HashSet<>(hours));
            }
        }

        Set<Teacher> falseTeachers = new HashSet<>();
        for (TimeTableItem item : optional.getSortedItems()) {
            Map<Integer, Set<Integer>> currentTeacher = teachersDaysAndHours.get(item.getTeacher());
            Set<Integer> currentHours = currentTeacher.get(item.getDay());
            boolean added = currentHours.add(item.getHour());
            if(!added){
                // teacher is teaching in different places at the same time
                falseTeachers.add(item.getTeacher());
            }
        }

        ret -= ((double)(100 / teachers.size()) * falseTeachers.size());
        return ret;
    }

    private double evalWork(TimeTable optional){
        return 0;
    }

    private <T extends SerialItem> Map<Integer, T> createMapFromSet(Set<T> items){
        Map<Integer, T> ret = new HashMap<>();
        for (T item : items) {
            ret.put(item.getId(), item);
        }

        return ret;
    }
}