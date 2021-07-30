package DTO;

import models.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelToDTOConverter {
    public Set<SubjectDTO> createSubjectsFromMap(Map<Integer, Subject> old){
        Set<SubjectDTO> ret = new HashSet<>();
        old.forEach((key, val) -> ret.add(createSubjectDTO(val)));
        return ret;
    }

    public Set<TeacherDTO> createTeachersFromMap(Map<Integer, Teacher> old){
        Set<TeacherDTO> ret = new HashSet<>();
        old.forEach((key, val) -> ret.add(createTeacherDTO(val)));
        return ret;
    }

    public Set<SchoolClassDTO> createClassesFromMap(Map<Integer, SchoolClass> old){
        Set<SchoolClassDTO> ret = new HashSet<>();
        old.forEach((key, val) -> ret.add(createClassDTO(val)));
        return ret;
    }

    public RulesDTO createRulesDTO(Rules rules){
        Set<RuleDTO> ret = new HashSet<>();
        rules.getRules().forEach(rule -> ret.add(new RuleDTO(rule.getRuleType(), rule.getStrength(), rule.getConfigurations())));
        return new RulesDTO(ret);
    }

    public Map<RuleDTO, Double> createRuleMapDTO(Map<Rule, Double> old){
        Map<RuleDTO, Double> ret = new HashMap<>();
        old.forEach((key, value) -> ret.put(new RuleDTO(key.getRuleType(), key.getStrength(), key.getConfigurations()), value));
        return ret;
    }

    public Set<SubjectDTO> createSubjects(Set<Subject> old){
        Set<SubjectDTO> ret = new HashSet<>();
        old.forEach(item -> ret.add(createSubjectDTO(item)));
        return ret;
    }

    public TeacherDTO createTeacherDTO(Teacher old){
        Set<SubjectDTO> subjects = createSubjects(old.getSubjects());
        return new TeacherDTO(old.getName(), old.getId(), subjects);
    }

    public SchoolClassDTO createClassDTO(SchoolClass old){
        return new SchoolClassDTO(old.getName(), old.getId(), createRequirementsDTO(old.getSubjectsNeeded()));
    }

    public Map<SubjectDTO, Integer> createRequirementsDTO(Map<Subject, Integer> old){
        Map<SubjectDTO, Integer> ret = new HashMap<>();
        old.forEach((key, val) -> ret.put(new SubjectDTO(key.getName(), key.getId()), val));
        return ret;
    }

    public SubjectDTO createSubjectDTO(Subject old){
        return new SubjectDTO(old.getName(), old.getId());
    }

    public TimeTableSystemDataSupplierDTO createDataSupplierDTO(TimeTableSystemDataSupplier old){
        return new TimeTableSystemDataSupplierDTO(old.getDays(), old.getHours(), createTeacherMap(old.getTeachers()),
                                                  createSubjectsMap(old.getSubjects()), createClassMap(old.getClasses()));
    }

    private Map<Integer, TeacherDTO> createTeacherMap(Map<Integer, Teacher> old){
        Map<Integer, TeacherDTO> ret = new HashMap<>();
        old.forEach((key, val) -> ret.put(key, new TeacherDTO(val.getName(), val.getId(), createSubjects(val.getSubjects()))));

        return ret;
    }

    private Map<Integer, SubjectDTO> createSubjectsMap(Map<Integer, Subject> old){
        Map<Integer, SubjectDTO> ret = new HashMap<>();
        old.forEach((key, val) -> ret.put(key, new SubjectDTO(val.getName(), val.getId())));

        return ret;
    }

    private Map<Integer, SchoolClassDTO> createClassMap(Map<Integer, SchoolClass> old){
        Map<Integer, SchoolClassDTO> ret = new HashMap<>();
        old.forEach((key, val) -> ret.put(key, new SchoolClassDTO(val.getName(), val.getId(), createRequirementsDTO(val.getSubjectsNeeded()))));

        return ret;
    }
}