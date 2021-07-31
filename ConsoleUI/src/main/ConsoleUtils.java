package main;

import DTO.*;
import com.sun.deploy.util.StringUtils;
import commands.*;
import evolutinary.EvolutionarySystem;
import evolutinary.TimeTableEvolutionarySystemImpel;
import models.*;
import utils.ETTXmlParser;
import utils.ItemCreationUtil;
import utils.RandomUtils;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
                return "Class display";
            }
        }, Teacher{
            @Override
            public String toString() {
                return "Teacher display";
            }
        }
    }
    private final static Command[] commands;
    private final static Scanner scan;

    static {
        scan = new Scanner(System.in);
        commands = new Command[] {
          new LoadCommand(() -> {
              System.out.println("Please enter the full path of your file: (don't forget .xml at the end of the path)");
              return scan.nextLine();
          }, (x) -> System.out.println("File loaded successfully.")),
          new SystemInfoCommand(ConsoleUtils::displaySystemInfo),
          new StartSystemCommand(() -> {
              System.out.println("There is a running process");
              System.out.println("Do you want to stop it and run a new one? (answer y/n)");
              String answer = scan.nextLine();
              if(answer.equalsIgnoreCase("y")){
                  // need to reset algorithm first
                  return getTerminate();
              }

              return null;
          }, ConsoleUtils::getTerminate,
                  (result) -> System.out.println("Generation: " +
                                                  result.getNumberOfGeneration() +
                                                    ", Fitness: " + String.format("%.1f", result.getFitness()) + " .")
                  , (result) -> {
              if(result.isFailed()){
                  System.out.println(result.getErrorMessage());
                  return;
              }

              System.out.println("Process finished!");
          }),
          new BestSolutionCommand(ConsoleUtils::displayBestSolution),
          new ProcessCommand(ConsoleUtils::displayProcess),
        };
    }

    public static void RunApp(){
        final String choiceError = "Please choose a number of your choice between 1 -  " + (commands.length + 1);
        while(true){
            try{
                displayMenu();
                String tempChoice = scan.nextLine();
                int choice = Integer.parseInt(tempChoice);
                if(choice >= 1 && choice <= (commands.length + 1)){
                    if(choice == commands.length + 1){
                        System.out.println("goodbye :)");
                        break;
                    }

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

        System.out.println((commands.length + 1) + ". Exit");
        System.out.println("please choose an option. (1 - " + (commands.length + 1) + ")");
    }

    private static StartSystemInfoDTO getTerminate(){
        Set<TerminateRuleDTO> retRules = new HashSet<>();
        int jump;
        System.out.println("please enter the way you want the algorithm would terminate (possible multiple-choice, for example: 1,2 OR 2 ):");
        EvolutionarySystem.TerminateRules[] rules = EvolutionarySystem.TerminateRules.values();
        for (int i = 1; i <= rules.length; i++) {
            System.out.println(i + ". " + rules[i - 1].toString());
        }

        String answer = scan.nextLine();
        String[] splitted = answer.replace(" ", "").split(",");
        if (splitted.length > 0 && splitted.length <= rules.length) {
            for (String ruleId : splitted) {
                try {
                    int choice = Integer.parseInt(ruleId);
                    EvolutionarySystem.TerminateRules current = rules[choice - 1];
                    switch (current) {
                        case NumberOfGenerations:
                            System.out.println("please enter the number of generations (at least 100):");
                            int gen = scan.nextInt();
                            if(gen < 100){
                                throw new IllegalArgumentException("number of generations should be at least 100");
                            }
                            else{
                                retRules.add(new GenerationsTerminateRuleDTO(gen));
                            }
                            break;
                        case ByFitness:
                            System.out.println("please enter the max fitness(positive number between 0-100):");
                            double fit = scan.nextDouble();
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
            jump = scan.nextInt();
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
        System.out.println("Subjects: ");
        System.out.println("=================");
        printSerialItems(info.getSubjects());
        System.out.println("Teachers: ");
        System.out.println("=================");
        printSerialItems(info.getTeachers());
        System.out.println("School classes: ");
        System.out.println("=================");
        printSerialItems(info.getClasses());
        System.out.println("=====================================================");

        System.out.println("Engine info: ");
        System.out.println("=================");
        System.out.println("Population size: " + info.getInitialSize());
        System.out.println("Selection technique: " + info.getSelection());
        System.out.println("Crossover technique: " + info.getCrossover());
        System.out.println("Mutations: ");
        info.getMutations().forEach(System.out::println);
    }

    private static <T extends SerialItemDTO> void printSerialItems(Set<T> items){
        List<T> listItems = new ArrayList<>(items);
        listItems.sort(Comparator.comparing(T::getId));
        listItems.forEach(item -> System.out.println(item.toString()));
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
                int choice = Integer.parseInt(scan.nextLine());
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
        for(int i = 0; i < displayOptions.length; i++){
            System.out.println((i + 1) + ". " + displayOptions[i].toString());
        }

        System.out.println((displayOptions.length + 1) + ". Go back");
        System.out.println("Please choose a  number between 1 - " + (displayOptions.length + 1) + ".");
    }

    private static void rawDisplay(BestSolutionDTO solution) {
        TimeTableDTO timeTable = solution.getSolution();
        System.out.println("Fitness = " + solution.getFitness());
        timeTable.getItems().forEach(item -> System.out.println("< " + item.getDay() + ", "
                           + item.getHour() + ", "
                           + item.getSchoolClass().getName() + ", "
                           + item.getTeacher().getName() + ", "
                           + item.getSubject().getName() + ">"));

        printRules(timeTable.getRulesScore(), timeTable.getSoftRulesAvg(), timeTable.getHardRulesAvg());
    }

    private static void classDisplay(BestSolutionDTO solution) {
        TimeTableSystemDataSupplierDTO info = solution.getSupplier();
        System.out.println("Fitness = " + solution.getFitness());
        TimeTableDTO table = solution.getSolution();
        info.getClasses().forEach((key, val) -> {
            System.out.println("Class: " + val.getName() + ", id: " + val.getId());
            System.out.println("================");
            Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = initializeTableView(info);
            table.getItems().forEach(item -> {
                if(item.getSchoolClass().equals(val)){
                    dayHourTable.get(item.getDay()).get(item.getHour()).add(item);
                }
            });

            printMap(dayHourTable, BestDisplay.Class);
            System.out.println("======================================");
        });

        printRules(table.getRulesScore(), table.getSoftRulesAvg(), table.getHardRulesAvg());
    }

    private static void teacherDisplay(BestSolutionDTO solution) {
        System.out.println("Teachers display");
        System.out.println("======================================");
        TimeTableSystemDataSupplierDTO info = solution.getSupplier();
        System.out.println("Fitness = " + solution.getFitness());
        TimeTableDTO table = solution.getSolution();
        info.getTeachers().forEach((key, val) -> {
            System.out.println("Teacher: " + val.getName() + ", id: " + val.getId());
            System.out.println("================");
            Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = initializeTableView(info);
            table.getItems().forEach(item -> {
                if(item.getTeacher().equals(val)){
                    dayHourTable.get(item.getDay()).get(item.getHour()).add(item);
                }
            });

            printMap(dayHourTable, BestDisplay.Teacher);
            System.out.println("======================================");
        });

        printRules(table.getRulesScore(), table.getSoftRulesAvg(), table.getHardRulesAvg());
    }

    private static Map<Integer, Map<Integer, List<TimeTableItemDTO>>> initializeTableView(TimeTableSystemDataSupplierDTO supplier){
        Map<Integer, Map<Integer, List<TimeTableItemDTO>>> dayHourTable = new HashMap<>();
        for(int i = 1; i <= supplier.getDays(); i++){
            dayHourTable.put(i, new HashMap<>());
            for(int j = 1; j <= supplier.getHours(); j++){
                dayHourTable.get(i).put(j, new ArrayList<>());
            }
        }

        return dayHourTable;
    }

    private static void printMap(Map<Integer, Map<Integer, List<TimeTableItemDTO>>> map , BestDisplay mode) {
        getMaxOfListAndPrintHours(map);
        map.forEach((days, hours) -> {
            System.out.print(days + ":  ");
            hours.forEach((hour, items) -> items.forEach(item -> {
                switch (mode){
                    case Teacher:
                        System.out.print("  <" + item.getSchoolClass().getId() +", " +item.getSubject().getId()  + ">  ");
                        break;
                    case Class:
                        System.out.print("  <" + item.getTeacher().getId() +", " +item.getSubject().getId()  + ">  ");
                        break;
                }
            }));

            System.out.print(System.getProperty("line.separator"));
        });
    }

    private static void getMaxOfListAndPrintHours(Map<Integer, Map<Integer, List<TimeTableItemDTO>>> map){
        Map<Integer, List<TimeTableItemDTO>> maxHour = map.entrySet().stream().max((key, val) -> val.getValue().size()).get().getValue();
        int max = maxHour.entrySet().stream().max((key, val) -> val.getValue().size()).get().getValue().size();
        int hours = map.values().size();
        System.out.print("days\\hours ");
        IntStream.range(1, hours).forEach(i -> {
            Stream.generate(() -> " ").limit(max/2).forEach(System.out::print);
            System.out.print(i);
            Stream.generate(() -> " ").limit(max/2).forEach(System.out::print);
        });

        System.out.print(System.getProperty("line.separator"));
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

        System.out.println("Hard rules average: " + hardAvg);
        System.out.println("Soft rules average: " + softAvg);
    }

    private static void displayProcess(CommandResult<Map<Integer, Double>> result){
        if(result.isFailed()){
            System.out.println(result.getErrorMessage());
            return;
        }

        Map<Integer, Double> generation = result.getResult();
        double lastFitness = -1;
        for(Map.Entry<Integer, Double> keyVal : generation.entrySet()){
            String improvement = lastFitness != -1 ? "improvement: " + (keyVal.getValue() - lastFitness) : "";
            System.out.println("Generation number: " + keyVal.getKey() + " with fitness: " + keyVal.getValue() + improvement);
            lastFitness = keyVal.getValue();
        }
    }
}