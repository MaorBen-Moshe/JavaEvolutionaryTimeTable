package utils;

import crossover.Crossover;
import models.*;
import mutation.FlippingMutation;
import mutation.Mutation;
import mutation.SizerMutation;
import selection.Selection;
import utils.infoModels.EngineInfoObject;
import utils.infoModels.Info;
import utils.infoModels.MutationInfo;

import java.util.*;

public class ParseUtils {
    public static EngineInfoObject parseInfo(Info info) throws Exception {
        Selection<TimeTable> selection = ETTXmlParser.createSelection(info.getSelectionType(), info.getSelectionInput());
        Crossover<TimeTable, TimeTableSystemDataSupplier> crossover = ETTXmlParser.createCrossover(info.getCrossoverType() + "Oriented", info.getCrossoverCutting(), info.getCrossoverAspect().toUpperCase(Locale.ROOT));
        List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutations = createMutations(info.getMutations());
        Set<TerminateRule> terminate = createTerminates(info.getGensChecked(), info.getGensInput(), info.getFitnessChecked(), info.getFitnessInput(), info.getTimeChecked(), info.getTimeInput());
        return new EngineInfoObject(info.getPopulation(), info.getElitism(), info.getJumps(), terminate,selection,crossover, mutations);
    }

    private static List<Mutation<TimeTable, TimeTableSystemDataSupplier>> createMutations(List<MutationInfo> mutations) {
        List<Mutation<TimeTable, TimeTableSystemDataSupplier>> ret = new ArrayList<>();
        for(MutationInfo curr : mutations){
            double prob = curr.getProbability();
            int tupples = curr.getTupples();
            switch (curr.getType()){
                case "Flipping":
                    ret.add(new FlippingMutation(prob, tupples, FlippingMutation.Component.valueOf(curr.getComponent())));
                    break;
                case "Sizer":
                    ret.add(new SizerMutation(prob, tupples));
                    break;
                default: throw new IllegalArgumentException(curr.getType() + " is not a valid mutation");
            }
        }

        return ret;
    }

    private static Set<TerminateRule> createTerminates(boolean gensChecked, int gensInput, boolean fitnessChecked, int fitnessInput, boolean timeChecked, long timeInput) {
        Set<TerminateRule> ret = new HashSet<>();
        if(gensChecked){
            ret.add(new GenerationTerminateRule(gensInput));
        }

        if(fitnessChecked){
            ret.add(new FitnessTerminateRule(fitnessInput));
        }

        if(timeChecked){
            ret.add(new TimeTerminateRule(timeInput));
        }

        return ret;
    }
}