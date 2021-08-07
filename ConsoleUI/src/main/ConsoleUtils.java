package main;

import DTO.*;
import commands.*;
import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import java.util.*;
import java.util.stream.IntStream;

public class ConsoleUtils {
    private enum BestDisplay {
        Raw{
            @Override
            public String toString() {
                return "Raw display";
            }
        }, Class{
            @Override
            public String toString() {
                return "CLASS display";
            }
        }, Teacher{
            @Override
            public String toString() {
                return "TEACHER display";
            }
        }
    }
    private final static Command[] commands;
    private static boolean finished;


    static {
        finished = false;
        EngineWrapper<TimeTable, TimeTableSystemDataSupplier> engine = new EngineWrapper<>();
        commands = new Command[] {
          new LoadCommand(engine,
                  () -> {
              System.out.println("Please enter the full path of your file: (don't forget .xml at the end of the path)");
              return new Scanner(System.in).nextLine();
          }, (x) -> System.out.println("File loaded successfully."),
                  () -> {
                      System.out.println("There is a running process, do you want to terminate it and load the new file? (y/n)");
                      String answer = new Scanner(System.in).nextLine();
                      return answer.equalsIgnoreCase("y");
                  }),
          new SystemInfoCommand(engine, ConsoleUtils::displaySystemInfo),
          new StartSystemCommand(engine, () ->{
              System.out.println("There is an information from last run of the algorithm");
              System.out.println("Do you want to clean it and start a new one? (answer y/n)");
              String answer = new Scanner(System.in).nextLine();
              if(answer.equalsIgnoreCase("y")){
                  return getTerminate();
              }

              return null;
          } ,
          () -> {
              System.out.println("There is a running process");
              System.out.println("Do you want to stop it and run a new one? (answer y/n)");
              String answer = new Scanner(System.in).nextLine();
              if(answer.equalsIgnoreCase("y")){
                  return getTerminate();
              }

              return null;
          }, ConsoleUtils::getTerminate,
                  (result) -> System.out.println("Generation: " +
                                                  result.getNumberOfGeneration() +
                                                    ", Fitness: " + String.format("%.2f", result.getFitness()))
                  ,() -> System.out.println("Algorithm has started!") ,
                  (result) -> {
              if(result.isFailed()){
                  System.out.println(result.getErrorMessage());
                  return;
              }

              System.out.println("Process finished!");
          }),
          new BestSolutionCommand(engine,ConsoleUtils::displayBestSolution),
          new ProcessCommand(engine,ConsoleUtils::displayProcess),
          new ExitCommand(engine, () -> {
              System.out.println("Goodbye :)");
              finished = true;
          }, () ->{
              System.out.println("There is a running process, do you still want to leave? (y/n)");
              return new Scanner(System.in).nextLine().equalsIgnoreCase("y");
          })
        };
    }

    public static void RunApp(){
        final String choiceError = "Please choose a number of your choice between 1 -  " + commands.length;
        while(!finished){
            try{
                displayMenu();
                Scanner scan = new Scanner(System.in);
                String tempChoice = scan.nextLine();
                int choice = Integer.parseInt(tempChoice);
                if(choice >= 1 && choice <= commands.length){
                    commands[choice- 1].execute();
                    continue;
                }

                System.out.println(choiceError);
            }catch (NumberFormatException e){
                System.out.println(choiceError);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private static void displayMenu() {
        System.out.println("Menu:");
        for(int i = 1; i <= commands.length; i++){
            System.out.println(i + ". " + commands[i-1].getCommandName());
        }

        System.out.printf("please choose an option. (1 - %d)%n", commands.length);
    }

    private static StartSystemInfoDTO getTerminate(){
        Set<TerminateRuleDTO> retRules = new HashSet<>();
        int jump;
        System.out.println("please enter the way you want the algorithm would terminate: (possible multiple-choice, for example: 1,2 OR 2 etc)");
        EvolutionarySystem.TerminateRules[] rules = EvolutionarySystem.TerminateRules.values();
        for (int i = 1; i <= rules.length; i++) {
            System.out.println(i + ". " + rules[i - 1].toString());
        }

        String answer = new Scanner(System.in).nextLine();
        String[] splitted = answer.replace(" ", "").split(",");
        if (splitted.length > 0 && splitted.length <= rules.length) {
            for (String ruleId : splitted) {
                try {
                    int choice = Integer.parseInt(ruleId);
                    EvolutionarySystem.TerminateRules current = rules[choice - 1];
                    switch (current) {
                        case NumberOfGenerations:
                            System.out.println("please enter the number of generations (at least 100):");
                            int gen = new Scanner(System.in).nextInt();
                            if(gen < 100){
                                throw new IllegalArgumentException("number of generations should be at least 100");
                            }
                            else{
                                retRules.add(new GenerationsTerminateRuleDTO(gen));
                            }

                            break;
                        case ByFitness:
                            System.out.println("please enter the max fitness(positive number between 0-100):");
                            double fit = new Scanner(System.in).nextDouble();
                            if(fit >= 0 && fit <= 100){
                                retRules.add(new FitnessTerminateRuleDTO(fit));
                            }
                            else{
                                throw new IllegalArgumentException("fitness should be an integer between 0-100");
                            }

                            break;
                    }

                } catch (Exception e) {
                    throw new IllegalArgumentException("terminate rule inserted is illegal");
                }

            }
        } else {
            throw new IllegalArgumentException("terminate rules inserted is illegal");
        }

        try {
            System.out.println("please enter a number which you will see the algorithm process generation with jump of each generation:");
            jump = new Scanner(System.in).nextInt();
        } catch (Exception e) {
            throw new IllegalArgumentException("jump in generations number must be positive");
        }

        return new StartSystemInfoDTO(retRules, jump);
    }

    private static void displaySystemInfo(CommandResult<SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier>> result){
        if(result.isFailed()){
            System.out.println(result.getErrorMessage());
            return;
        }

        SystemInfoDTO<TimeTable, TimeTableSystemDataSupplier> info = result.getResult();
        System.out.println("Time table info: ");
        System.out.println("=================");
        System.out.println("Days in time table: " + info.getDays());
        System.out.println("Hours per day in time table: " + info.getHours());
        System.out.println("=================");
        System.out.println("Subjects: ");
        System.out.println("=================");
        printSerialItems(info.getSubjects());
        System.out.println("=================");
        System.out.println("Teachers: ");
        System.out.println("=================");
        printSerialItems(info.getTeachers());
        System.out.println("=================");
        System.out.println("School classes: ");
        System.out.println("=================");
        printSerialItems(info.getClasses());
        System.out.println("=================");
        System.out.println("Rules in system: ");
        System.out.println("=================");
        printRulesInfo(info.getRules());
        System.out.println("=====================================================");
        System.out.println("Engine info: ");
        System.out.println("=================");
        System.out.println("Population size: " + info.getInitialSize());
        System.out.println("Selection technique: " + info.getSelection());
        System.out.println("Crossover technique: " + info.getCrossover());
        System.out.println("Mutations: ");
        info.getMutations().forEach(System.out::println);
    }

    private static void printRulesInfo(RulesDTO rules) {
        StringBuilder output = new StringBuilder();
        rules.getRules().forEach(rule ->{
            output.setLength(0);
            output.append("Rule name: ").append(rule.getType().toString()).append(", ");
            output.append("Strength: ").append(rule.getStrength().toString());
            if(rule.getConfigurations().size() != 0){
                output.append(", configurations of rule: ").append(rule.getConfigurations());
            }

            System.out.println(output);
        });
    }

    private static <T extends SerialItemDTO> void printSerialItems(Set<T> items){
        items.stream()
                .sorted(Comparator.comparing(T::getId))
                .forEach(System.out::println);
    }

    private static void displayBestSolution(CommandResult<BestSolutionDTO> result){
        if(result.isFailed()){
            System.out.println(result.getErrorMessage());
            return;
        }

        BestDisplay[] displayOptions = BestDisplay.values();
        while(true){
            displayBestMenu(displayOptions);
            try{
                int choice = Integer.parseInt(new Scanner(System.in).nextLine());
                if(choice == displayOptions.length + 1){
                    break;
                }

                if(choice >= 1 && choice <= displayOptions.length){
                    switch (displayOptions[choice - 1]){
                        case Raw: rawDisplay(result.getResult()); break;
                        case Class: classDisplay(result.getResult()); break;
                        case Teacher: teacherDisplay(result.getResult()); break;
                    }
                }
                else{
                    System.out.println("Please choose a  number between 1 - " + (displayOptions.length + 1) + ".");
                }
            }catch (NumberFormatException e){
                System.out.println("Please choose a  number between 1 - " + (displayOptions.length + 1) + ".");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private static void displayBestMenu(BestDisplay[] displayOptions){
        System.out.println("Best solution display:");
        System.out.println("Please choose the way you want the solution would display:");
        IntStream.range(0, displayOptions.length).forEach(i -> System.out.println((i + 1) + ". " + displayOptions[i].toString()));
        System.out.println((displayOptions.length + 1) + ". Go back");
        System.out.println("Please choose a  number between 1 - " + (displayOptions.length + 1) + ".");
    }

    private static void rawDisplay(BestSolutionDTO solution) {
        TimeTableDTO timeTable = solution.getSolution();
        System.out.println("The generation this solution was created: " + solution.getGenerationCreated());
        System.out.printf("Fitness = %.2f%n", solution.getFitness());
        timeTable.getItems().forEach(item -> System.out.println("<" + item.getDay() + ", "
                           + item.getHour() + ", "
                           + item.getSchoolClass().getName() + ", "
                           + item.getTeacher().getName() + ", "
                           + item.getSubject().getName() + ">"));

        printRules(timeTable.getRulesScore(), timeTable.getSoftRulesAvg(), timeTable.getHardRulesAvg());
    }

    private static void classDisplay(BestSolutionDTO solution) {
        final Map<SchoolClassDTO, Map<Integer, Map<Integer, List<TimeTableItemDTO>>>> allDaysMap = new LinkedHashMap<>();
        TimeTableSystemDataSupplierDTO info = solution.getSupplier();
        System.out.println("The generation this solution was created: " + solution.getGenerationCreated());
        System.out.printf("Fitness = %.2f%n", solution.getFitness());
        TimeTableDTO table = solution.getSolution();
        info.getClasses().forEach((key, val) -> {
            Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = initializeTableView(info);
            table.getItems().forEach(item -> {
                if(item.getSchoolClass().equals(val)){
                    dayHourTable.get(item.getDay()).get(item.getHour()).add(item);
                }
            });

            allDaysMap.put(val, dayHourTable);
        });

        getMaxAndDisplayMaps(allDaysMap, BestDisplay.Class, info.getHours());
        printRules(table.getRulesScore(), table.getSoftRulesAvg(), table.getHardRulesAvg());
    }

    private static void teacherDisplay(BestSolutionDTO solution) {
        final Map<TeacherDTO, Map<Integer, Map<Integer, List<TimeTableItemDTO>>>> allDaysMap = new LinkedHashMap<>();
        System.out.println("Teachers display");
        System.out.println("======================================");
        TimeTableSystemDataSupplierDTO info = solution.getSupplier();
        System.out.println("The generation this solution was created: " + solution.getGenerationCreated());
        System.out.printf("Fitness = %.2f%n", solution.getFitness());
        TimeTableDTO table = solution.getSolution();
        info.getTeachers().forEach((key, val) -> {
            Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = initializeTableView(info);
            table.getItems().forEach(item -> {
                if(item.getTeacher().equals(val)){
                    dayHourTable.get(item.getDay()).get(item.getHour()).add(item);
                }
            });

            allDaysMap.put(val, dayHourTable);
        });

        getMaxAndDisplayMaps(allDaysMap, BestDisplay.Teacher, info.getHours());
        printRules(table.getRulesScore(), table.getSoftRulesAvg(), table.getHardRulesAvg());
    }

    private static Map<Integer, Map<Integer, List<TimeTableItemDTO>>> initializeTableView(TimeTableSystemDataSupplierDTO supplier){
        Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = new HashMap<>();
        IntStream.range(1, supplier.getDays() + 1).forEach(i -> {
            dayHourTable.put(i, new HashMap<>());
            IntStream.range(1, supplier.getHours() + 1).forEach(j -> dayHourTable.get(i).put(j, new ArrayList<>()));
        });

        return dayHourTable;
    }

    private static void getMaxAndDisplayMaps(Map<? extends SerialItemDTO, Map<Integer, Map<Integer, List<TimeTableItemDTO>>>> allDaysMap, BestDisplay howToDisplay, int numOfHoursInSystem){
        final int[] max = {0};
        allDaysMap.forEach((key, val) -> max[0] = Math.max(getMax(val), max[0]));
        allDaysMap.forEach((key, val) ->{
            System.out.println((howToDisplay.equals(BestDisplay.Teacher) ? "TEACHER: " : "CLASS: ") + key.getName() + ", id: " + key.getId());
            System.out.println("================");
            printMap(val, howToDisplay, max[0], numOfHoursInSystem);
            System.out.println("======================================");
        });
    }

    private static void printMap(Map<Integer, Map<Integer, List<TimeTableItemDTO>>> map , BestDisplay mode, int maxWidth,  int numOfHoursInSystem) {
        final String daysHours = "days\\hours";
        printInitialHours(daysHours, maxWidth, numOfHoursInSystem);
        map.forEach((days, hours) -> {
             int numOfSpaces = (daysHours.length() - String.valueOf(days).length() -1);
             System.out.print(days + ":" + String.format("%" + numOfSpaces + "s", " "));
             hours.forEach((hour, items) -> {
                            int spacesToAdd = (maxWidth - items.size()) * 10;
                            items.forEach(item -> {
                                    switch (mode) {
                                    case Teacher:
                                    System.out.print("  <" + item.getSchoolClass().getId() + ", " + item.getSubject().getId() + ">  ");
                                    break;
                                    case Class:
                                    System.out.print("  <" + item.getTeacher().getId() + ", " + item.getSubject().getId() + ">  ");
                                    break;
                                }
                            });

                            if(spacesToAdd != 0){
                                System.out.printf(("%" + spacesToAdd + "s"), " ");
                            }

                            System.out.print("|");
             });

            System.out.print(System.getProperty("line.separator"));
        });
    }

    private static void printInitialHours(final String daysToPrint , int maxWidth, int numOfHoursInSystem){
        System.out.print(daysToPrint);
        IntStream.range(1, numOfHoursInSystem + 1).forEach(i -> {
            double numOfSpaces = (maxWidth*10) / 2.0;
            System.out.printf("%" + numOfSpaces + "s", " ");
            System.out.print(i);
            System.out.printf("%" + (numOfSpaces - 1) + "s", " ");
            System.out.print("|");
        });

        System.out.print(System.getProperty("line.separator"));
    }

    private static int getMax(Map<Integer, Map<Integer, List<TimeTableItemDTO>>> map){
        final int[] max = {0};
        map.forEach((dayKey, dayValue) -> dayValue.forEach((hourKey, hourValue) -> max[0] = Math.max(max[0], hourValue.size())));
        return max[0];
    }

    private static void printRules(Map<RuleDTO, Double> rules, double softAvg, double hardAvg){
        rules.forEach((key, val) -> {
            System.out.println("Rule name: " + key.getType().toString());
            System.out.println("Rule strength: " + key.getStrength().toString());
            System.out.println("Rule Score: " + val);
            if(key.getConfigurations().size() != 0){
                System.out.println("configurations of rule: " + key.getConfigurations());
            }
        });

        System.out.println("Hard rules average: " + (hardAvg >= 0 ? hardAvg : "No hard rules were found."));
        System.out.println("Soft rules average: " + (softAvg >= 0 ? softAvg : "No soft rules were found."));
    }

    private static void displayProcess(CommandResult<List<FitnessHistoryItemDTO>> result){
        if(result.isFailed()){
            System.out.println(result.getErrorMessage());
            return;
        }

        List<FitnessHistoryItemDTO> generation = result.getResult();
        generation.forEach(current -> {
            String improvement = current.getGenerationNumber() != 0 ? ", improvement: " + String.format ("%.2f", current.getImprovementFromLastGeneration()) : "";
            System.out.println("Generation number: " + current.getGenerationNumber() + String.format(" with fitness: %.2f", current.getCurrentGenerationFitness()) + improvement);
        });
    }
}