package DTO;

import Interfaces.DataSupplier;
import crossover.Crossover;
import mutation.Mutation;
import selection.Selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystemInfoDTO<T, S extends DataSupplier> {
    public Set<TeacherDTO> getTeachers() {
        return new HashSet<>(teachers);
    }

    public Set<SchoolClassDTO> getClasses() {
        return new HashSet<>(classes);
    }

    public Set<SubjectDTO> getSubjects() {
        return new HashSet<>(subjects);
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
    private final Set<SchoolClassDTO> classes;
    private final Set<SubjectDTO> subjects;
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
        this.classes = new HashSet<>(classes);
        this.subjects = new HashSet<>(subjects);
        this.rules = rules;

        this.initialSize = initialSize;
        this.selection = selection;
        this.crossover = crossover;
        this.mutations = new ArrayList<>(mutations);
    }
}
