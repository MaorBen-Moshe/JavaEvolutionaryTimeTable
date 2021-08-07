package utils;

import crossover.CrossoverTypes;
import generated.*;
import models.SerialItem;
import mutation.FlippingMutation;
import mutation.MutationTypes;
import selection.SelectionTypes;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValidateUtils {

    public static <T extends Enum<T>> boolean enumValid(T[] enumTypes,Class<T> enumTypeClass, String stringType){
        boolean isTypeValid = false;
        for(T type : enumTypes){
            try{
                if(type.equals(T.valueOf(enumTypeClass, stringType))){
                    isTypeValid = true;
                    break;
                }
            }catch (Exception e){
                throw new IllegalArgumentException(stringType + " is not part of valid " + enumTypeClass.getSimpleName());
            }
        }

        return isTypeValid;
    }

    public static boolean daysHoursValid(int days, int hours){
        return days > 0 && hours > 0;
    }

    public static boolean serialItemsNotValid(Set<? extends SerialItem> items) {
        final boolean[] isValid = {false};
        List<? extends SerialItem> sorted = items.stream()
                .sorted(Comparator.comparingInt((ToIntFunction<SerialItem>) SerialItem::getId))
                .collect(Collectors.toList());

        if((sorted.get(0).getId() == 1)){
            IntStream.range(1, items.size()).forEach(i -> isValid[0] = (sorted.get(i).getId() == i + 1) && (sorted.get(i - 1).getId() + 1 == i + 1));
        }

        return !isValid[0];
    }

    public static boolean initialPopulationPositive(int initialPopulation){
        return initialPopulation > 0;
    }

    public static boolean selectionValid(ETTSelection selection){
        String stringType = selection.getType();
        String configurations;
        if(stringType.equals(SelectionTypes.Truncation.toString())){
            configurations = selection.getConfiguration();
            if(configurations != null && !configurations.trim().isEmpty()){
                String[] split = configurations.trim().split("=");
                if(!(split.length == 2) || !split[0].equalsIgnoreCase("TopPercent")){
                    throw new IllegalArgumentException("selection configuration of type " + split[0] + " is not valid");
                }
                else{
                    try{
                        int top = Integer.parseInt(split[1]);
                        if(!(top >= 1 && top <= 100)){
                            throw new IllegalArgumentException("selection configuration: + " + split[0] + " with value " + split[1] + " is not valid");
                        }
                    }catch (NumberFormatException e){
                        throw new IllegalArgumentException("Selection configuration Top percent should be an integer between 1 - 100");
                    }
                }
            }
            else{
                throw new IllegalArgumentException("you must supply parameter of TopPercent in order to use Truncation selection");
            }
        }
        else if(stringType.equals(SelectionTypes.Tournament.toString())){
            configurations = selection.getConfiguration();
            if(!configurations.trim().isEmpty()){
                String[] split = configurations.replace(" ", "").split("=");
                if(!(split.length == 2) || !split[0].equalsIgnoreCase("PTE")){
                    throw new IllegalArgumentException("selection configuration of type " + split[0] + " is not valid");
                }
                else{
                    try{
                        float top = Float.parseFloat(split[1]);
                        if(!(top >= 0 && top <= 1)){
                            throw new IllegalArgumentException("selection configuration: + " + split[0] + " with value " + split[1] + " is not valid");
                        }
                    }catch (NumberFormatException e){
                        throw new IllegalArgumentException("Selection configuration Top percent should be an integer between 1 - 100");
                    }
                }
            }
            else{
                throw new IllegalArgumentException("you must supply parameter of PTE in order to use tournament selection");
            }
        }

        return enumValid(SelectionTypes.values(), SelectionTypes.class, stringType);
    }

    public static boolean crossoverValid(ETTCrossover crossover){
        String stringType = crossover.getName();
        if(CrossoverTypes.AspectOriented.toString().equals(stringType)){
            String configurations = crossover.getConfiguration();
            if(configurations != null &&!configurations.trim().isEmpty()){
                String[] split = configurations.replace(" ", "").split("=");
                if(!(split.length == 2) || !split[0].equalsIgnoreCase("Orientation")){
                    throw new IllegalArgumentException("crossover configuration of type " + split[0] + " is not valid");
                }
                else{
                    if(!(split[1].equalsIgnoreCase("class") || split[1].equalsIgnoreCase("teacher"))){
                        throw new IllegalArgumentException("crossover configuration: + " + split[0] + " with value " + split[1] + " is not valid");
                    }
                }
            }
            else{
                throw new IllegalArgumentException("you must supply parameter of Orientation in order to use Aspect oriented crossover");
            }
        }

        return enumValid(CrossoverTypes.values(), CrossoverTypes.class, stringType) &&
                crossover.getCuttingPoints() > 0;
    }

    public static boolean mutationsValid(ETTMutations mutations){
        boolean isValid = false;
        List<ETTMutation> ettMutations = mutations.getETTMutation();
        for(ETTMutation mutation : ettMutations){
            String stringType = mutation.getName();
            double probability = mutation.getProbability();
            if(!(probability >= 0 && probability <= 1)){
                throw new IllegalArgumentException(stringType + "'s probability is not in the range of 0-1");
            }

            if(enumValid(MutationTypes.values(), MutationTypes.class, stringType)){
                isValid = true;
            }
            else{
                isValid = false;
                break;
            }

            if(stringType.equals(MutationTypes.Flipping.toString())){
                checkFlipping(mutation);
            }
            else if(stringType.equals(MutationTypes.Sizer.toString())){
                checkSizer(mutation);
            }
        }

        return isValid;
    }

    private static void checkFlipping(ETTMutation flipping){
        String configurations = flipping.getConfiguration();
        String[] splitArray = configurations.split(",");
        if(!configurations.trim().isEmpty()){
            if(splitArray.length != 2){
                throw new IllegalArgumentException("configurations of flipping mutation accept only MaxTupples and Component");
            }

            if(!(checkMaxTupples(splitArray[0]) || checkMaxTupples(splitArray[1]))){
                throw new IllegalArgumentException("Configurations of flipping mutation require MaxTupples configuration with integer value");
            }

            if(!(checkComponent(splitArray[0]) || checkComponent(splitArray[1]))){
                throw new IllegalArgumentException("Configurations of flipping mutation require Component configuration with one of the values: " + Arrays.toString(FlippingMutation.Component.values()));
            }
        }
        else{
            throw new IllegalArgumentException("Flipping mutation require maxTupples and Component configurations");
        }
    }

    private static boolean checkMaxTupples(String maxTupple){
        String[] splitArray = maxTupple.split("=");
        boolean retValue = false;
        if(!maxTupple.trim().isEmpty()){
            if(!(splitArray.length == 2) || !splitArray[0].equalsIgnoreCase("MaxTupples")){
                retValue = false;
            }
            else{
                try{
                    Integer.parseInt(splitArray[1]);
                    retValue = true;
                } catch (NumberFormatException e){
                    retValue = false;
                }
            }
        }

        return retValue;
    }

    private static boolean checkComponent(String componentString){
        String[] splitArray = componentString.split("=");
        boolean retValue = false;
        if(!componentString.trim().isEmpty()){
            if(!(splitArray.length == 2) || !splitArray[0].equalsIgnoreCase("Component")){
                retValue = false;
            }
            else{
                try{
                    FlippingMutation.Component.valueOf(splitArray[1].toUpperCase(Locale.ROOT));
                    retValue = true;
                } catch (IllegalArgumentException e){
                    retValue = false;
                }
            }
        }

        return retValue;
    }

    private static void checkSizer(ETTMutation sizer){
        String configurations = sizer.getConfiguration();
        if(configurations != null &&!configurations.trim().isEmpty()){
            String[] split = configurations.replace(" ", "").split("=");
            if(!(split.length == 2) || !split[0].equalsIgnoreCase("TotalTupples")){
                throw new IllegalArgumentException("mutation configuration of type " + split[0] + " is not valid");
            }
            else{
                try{
                    Integer.parseInt(split[1]);
                } catch (NumberFormatException e){
                    throw new IllegalArgumentException("TotalTupples of sizer mutation require an integer instead of: " + split[1]);
                }
            }
        }
        else{
            throw new IllegalArgumentException("you must supply parameter of TotalTupples in order to use sizer mutation");
        }
    }
}