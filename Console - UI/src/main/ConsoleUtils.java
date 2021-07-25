package main;

import commands.*;
import commands.innerCommands.ClassDisplayCommand;
import commands.innerCommands.RawDisplayCommand;
import commands.innerCommands.TeacherDisplayCommand;
import interfaces.EvolutionarySystem;
import utils.ETTXmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
          new LoadCommand(ConsoleUtils::load),
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

    private static void load(Object... args){
        System.out.println("Please enter the full path of your file:");
        String answer = scan.nextLine();
        try {
            system = ETTXmlParser.parse(answer);
            isFileLoaded = true;
            System.out.println("File loaded successfully.");
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void startAlgorithm(Object... args){
        if(isFileLoaded) {
            if(system.IsRunningProcess()){
                System.out.println("There is a running process");
                System.out.println("Do you want to stop it and run a new one? (answer y/n)");
                String answer = scan.nextLine();
                if(answer.equalsIgnoreCase("y")){
                    // need to reset algorithm first
                    system.StartAlgorithm();
                    System.out.println("Process finished!");
                }
            }
            else{
                system.StartAlgorithm();
                System.out.println("Process finished!");
            }
        }
        else{
            System.out.println("Please load a file first.");
        }
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