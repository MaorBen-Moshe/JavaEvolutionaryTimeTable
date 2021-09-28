package utils;

import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import mutation.Mutation;
import utils.infoModels.Info;
import utils.infoModels.PropertyInfoObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EngineInfoCreator {

    public static List<PropertyInfoObject> createEngineInfo(EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> system, Info info){
        List<PropertyInfoObject> ret = new ArrayList<>();
        ret.add(new PropertyInfoObject("Population", String.valueOf(system.getInitialPopulationSize())));
        ret.add(new PropertyInfoObject("Elitism", String.valueOf(system.getElitism())));
        ret.add(new PropertyInfoObject("Selection", system.getSelection().toString()));
        ret.add(new PropertyInfoObject("Crossover", system.getCrossover().toString()));
        ret.add(new PropertyInfoObject("Mutations", ""));
        List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutations = system.getMutations();
        IntStream.range(0, mutations.size()).forEach(i -> {
            ret.add(new PropertyInfoObject("Mutation " + (i + 1), mutations.get(i).toString()));
        });

        ret.add(new PropertyInfoObject("Terminate By:", ""));
        if(info.getGensChecked()){
            ret.add(new PropertyInfoObject("Generations ", String.valueOf(info.getGensInput())));
        }

        if(info.getFitnessChecked()){
            ret.add(new PropertyInfoObject("Fitness ", String.valueOf(info.getFitnessInput())));
        }

        if(info.getTimeChecked()){
            ret.add(new PropertyInfoObject("Time ", String.valueOf(info.getTimeInput())));
        }

        return ret;
    }
}
