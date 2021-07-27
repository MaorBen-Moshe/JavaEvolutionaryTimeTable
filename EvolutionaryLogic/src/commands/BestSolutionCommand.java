package commands;

import DTO.*;
import models.*;

import java.util.*;
import java.util.function.Consumer;

public class BestSolutionCommand extends CommandImpel{
    private Consumer<CommandResult<BestSolutionDTO>> after;

    public BestSolutionCommand(Consumer<CommandResult<BestSolutionDTO>> after) {
        this.after = after;
    }

    @Override
    public void execute() {
        CommandResult<BestSolutionDTO> result = new CommandResult<>();
        if(isFileLoaded){
          if(!evolutionarySystem.IsRunningProcess()){
              result.setResult(createAnswer());
          }
          else{
              result.setErrorMessage("There is running process already");
          }
        }
        else{
            result.setErrorMessage("There is a loaded file already");
        }

        after.accept(result);
    }

    private BestSolutionDTO createAnswer(){
        BestSolutionItem<TimeTable> solution = evolutionarySystem.getBestSolution();
        TimeTable table = solution.getSolution();
        Set<TimeTableItemDTO> set = new TreeSet<>();
        table.getSortedItems().forEach(item -> set.add(new TimeTableItemDTO(item.getDay(),
                                                                            item.getHour(),
                                                                            createClassDTO(item.getSchoolClass()),
                                                                            createSubjectDTO(item.getSubject()),
                                                                            createTeacherDTO(item.getTeacher()))));

        TimeTableDTO newTable = new TimeTableDTO(set, createRuleMapDTO(table.getRulesScore()),
                                                 table.getHardRulesAvg(), table.getSoftRulesAvg());
        return new BestSolutionDTO(newTable, solution.getFitness());
    }

    private Map<RuleDTO, Double> createRuleMapDTO(Map<Rule, Double> old){
        Map<RuleDTO, Double> ret = new HashMap<>();
        old.forEach((key, value) -> ret.put(new RuleDTO(key.getRuleType(), key.getStrength(), key.getTotalHours()), value));
        return ret;
    }

    private Set<SubjectDTO> createSubjects(Set<Subject> old){
        Set<SubjectDTO> ret = new HashSet<>();
        old.forEach(item -> ret.add(createSubjectDTO(item)));
        return ret;
    }

    private TeacherDTO createTeacherDTO(Teacher old){
        Set<SubjectDTO> subjects = createSubjects(old.getSubjects());
        return new TeacherDTO(old.getName(), old.getId(), subjects);
    }

    private SchoolClassDTO createClassDTO(SchoolClass old){
        return new SchoolClassDTO(old.getName(), old.getId(), createRequirementsDTO(old.getSubjectsNeeded()));
    }

    private Map<SubjectDTO, Integer> createRequirementsDTO(Map<Subject, Integer> old){
        Map<SubjectDTO, Integer> ret = new HashMap<>();
        old.forEach((key, val) -> ret.put(new SubjectDTO(key.getName(), key.getId()), val));
        return ret;
    }

    private SubjectDTO createSubjectDTO(Subject old){
        return new SubjectDTO(old.getName(), old.getId());
    }

    @Override
    public String getCommandName() {
        return "Best Solution";
    }
}
