package main;

import commands.*;
import commands.bestDisplayCommands.ClassDisplayCommand;
import commands.bestDisplayCommands.RawDisplayCommand;
import commands.bestDisplayCommands.TeacherDisplayCommand;
import evolutinary.EvolutionarySystem;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ConsoleUtils {

    private static EvolutionarySystem<?> system;
    private static boolean isFileLoaded;
    private final static Command[] commands;
    private final static Command[] bestDisplayCommands;
    private final static Scanner scan;

    static {
        scan = new Scanner(System.in);
        isFileLoaded = false;
        system = null;
        commands = new Command[] {
          new LoadCommand(() -> {
              System.out.println("Please enter the full path of your file: (don't forget .xml at the end of the path)");
              return scan.nextLine();
          }, (result) -> System.out.println("File loaded successfully.")),
          new SystemInfoCommand(ConsoleUtils::displaySystemInfo),
          new StartSystemCommand(ConsoleUtils::startAlgorithm),
          new BestSolutionCommand(ConsoleUtils::displayBestSolution),
          new ProcessCommand(ConsoleUtils::displayProcess),
        };

        bestDisplayCommands = new Command[]{
                new RawDisplayCommand(ConsoleUtils::rawDisplay),
                new TeacherDisplayCommand(ConsoleUtils::teacherDisplay),
                new ClassDisplayCommand(ConsoleUtils::classDisplay)
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

    private static void startAlgorithm(Object... args){
        Set<EvolutionarySystem.TerminateRules> rules;
        if(isFileLoaded) {
            if(system.IsRunningProcess()){
                System.out.println("There is a running process");
                System.out.println("Do you want to stop it and run a new one? (answer y/n)");
                String answer = scan.nextLine();
                if(answer.equalsIgnoreCase("y")){
                    // need to reset algorithm first
                    rules = getTerminate();
                    system.StartAlgorithm(rules);
                    System.out.println("Process finished!");
                }
            }
            else{
                rules = getTerminate();
                system.StartAlgorithm(rules);
                System.out.println("Process finished!");
            }
        }
        else{
            System.out.println("Please load a file first.");
        }
    }

    private static Set<EvolutionarySystem.TerminateRules> getTerminate(){
        Set<EvolutionarySystem.TerminateRules> retRules = new HashSet<>();
        while(true){
            System.out.println("please enter the way you want the algorithm would terminate (write all the numbers of rules you want with coma between each one):");
            EvolutionarySystem.TerminateRules[] rules = EvolutionarySystem.TerminateRules.values();
            for(int i = 1; i <= rules.length; i++){
                System.out.println(i + ". " + rules[i - 1].toString());
            }

            String answer= scan.nextLine();
            String[] splitted = answer.replace(" ", "").split(",");
            if(splitted.length > 0  && splitted.length <= rules.length){
                for(String ruleId : splitted){
                    try{
                        int choice = Integer.parseInt(ruleId);
                        EvolutionarySystem.TerminateRules current = rules[choice -1];
                        switch (current){
                            case NumberOfGenerations:
                                System.out.println("please enter the number of generations (at least 100):");
                                system.setAcceptedNumberOfGenerations(scan.nextInt());
                                break;
                            case ByFitness:
                                System.out.println("please enter the max fitness(positive number between 0-100):");
                                system.setAcceptedFitness(scan.nextInt());
                                break;
                        }
                    }catch (Exception e){
                        throw new IllegalArgumentException("terminate rule inserted is illegal");
                    }

                    try{
                        System.out.println("please enter a number which you see the algorithm process generation with jump of each generation:");
                        system.setJumpInGenerations(scan.nextInt());
                    }catch (Exception e){
                        throw new IllegalArgumentException("jump in generations number must be positive");
                    }
                }

                break;
            }
            else{
                throw new IllegalArgumentException("terminate rules inserted is illegal");
            }
        }

        return retRules;
    }

    private static void displaySystemInfo(Object... args){

    }

    private static void displayBestSolution(Object... args){
        while(true){
            displayBestMenu();
            try{
                int choice = Integer.parseInt(scan.nextLine());
                if(choice == bestDisplayCommands.length + 1){
                    break;
                }

                if(choice >= 1 && choice <= bestDisplayCommands.length){
                    bestDisplayCommands[choice - 1].execute();
                }
                else{
                    System.out.println("Please choose a  number between 1 - " + (bestDisplayCommands.length + 1) + ".");
                }
            }catch (NumberFormatException e){
                System.out.println("Please choose a  number between 1 - " + (bestDisplayCommands.length + 1) + ".");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private static void displayBestMenu(){
        System.out.println("Best solution display:");
        System.out.println("Please choose the way you want the solution would display:");
        for(int i = 0; i < bestDisplayCommands.length; i++){
            System.out.println((i + 1) + ". " + bestDisplayCommands[i].getCommandName());
        }

        System.out.println((bestDisplayCommands.length + 1) + ". Go back");
        System.out.println("Please choose a  number between 1 - " + (bestDisplayCommands.length + 1) + ".");
    }

    private static void rawDisplay(Object... args) {

    }

    private static void classDisplay(Object... args) {

    }

    private static void teacherDisplay(Object... args) {

    }

    private static void displayProcess(Object... args){

    }
}