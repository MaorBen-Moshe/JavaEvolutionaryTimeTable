package DTO;

import Interfaces.DataSupplier;
import crossover.Crossover;
import mutation.Mutation;
import selection.Selection;

import java.util.*;

public class SystemInfoDTO<T, S extends DataSupplier> {
    public Set<TeacherDTO> getTeachers() {
        return unModifiedTeachers;
    }

    public Set<SchoolClassDTO> getClasses() {
        return unModifiedClasses;
    }

    public Set<SubjectDTO> getSubjects() {
        return unModifiedSubjects;
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public Selection<T> getSelection() {
        return selection;
    }

    public Crossover<T, S> getCrossover() {
        return crossover;
    }

    public List<Mutation<T, S>> getMutations() {
        return new ArrayList<>(mutations);
    }

    public RulesDTO getRules() {
        return rules;
    }

    public int getInitialSize() {
        return initialSize;
    }

    private final Set<TeacherDTO> teachers;
    private final Set<TeacherDTO> unModifiedTeachers;
    private final Set<SchoolClassDTO> classes;
    private final Set<SchoolClassDTO> unModifiedClasses;
    private final Set<SubjectDTO> subjects;
    private final Set<SubjectDTO> unModifiedSubjects;
    private final RulesDTO rules;
    private final int days;
    private final int hours;

    private final int initialSize;
    private final Selection<T> selection;
    private final Crossover<T, S> crossover;
    private final List<Mutation<T, S>> mutations;

    public SystemInfoDTO(int days, int hours, Set<TeacherDTO> teachers, Set<SchoolClassDTO> classes, Set<SubjectDTO> subjects,
                         RulesDTO rules, int initialSize, Selection<T> selection, Crossover<T, S> crossover, List<Mutation<T, S>> mutations){
        this.days = days;
        this.hours = hours;
        this.teachers = new HashSet<>(teachers);
        this.unModifiedTeachers = Collections.unmodifiableSet(this.teachers);
        this.classes = new HashSet<>(classes);
        this.unModifiedClasses = Collections.unmodifiableSet(this.classes);
        this.subjects = new HashSet<>(subjects);
        this.unModifiedSubjects = Collections.unmodifiableSet(this.subjects);
        this.rules = rules;

        this.initialSize = initialSize;
        this.selection = selection;
        this.crossover = crossover;
        this.mutations = new ArrayList<>(mutations);
    }
}
