package DTO;

import crossover.Crossover;
import mutation.Mutation;
import selection.Selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystemInfoDTO<T> {
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

    public Crossover<T> getCrossover() {
        return crossover;
    }

    public List<Mutation<T>> getMutations() {
        return new ArrayList<>(mutations);
    }

    private final Set<TeacherDTO> teachers;
    private final Set<SchoolClassDTO> classes;
    private final Set<SubjectDTO> subjects;
    private final RulesDTO rules;
    private final int days;
    private final int hours;

    private final int initialSize;
    private final Selection<T> selection;
    private final Crossover<T> crossover;
    private final List<Mutation<T>> mutations;

    public SystemInfoDTO(int days, int hours, Set<TeacherDTO> teachers, Set<SchoolClassDTO> classes, Set<SubjectDTO> subjects,
                         RulesDTO rules, int initialSize, Selection<T> selection, Crossover<T> crossover, List<Mutation<T>> mutations){
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
