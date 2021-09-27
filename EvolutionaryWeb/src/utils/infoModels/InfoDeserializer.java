package utils.infoModels;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InfoDeserializer implements JsonDeserializer<Info> {
    @Override
    public Info deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Info info = new Info();
        JsonObject jObj = jsonElement.getAsJsonObject();
        info.setPopulation(getIntOrDefault(jObj, "population"));
        info.setElitism(getIntOrDefault(jObj, "elitism"));
        info.setSelectionType(jObj.getAsJsonPrimitive("selectionType").getAsString());
        info.setSelectionInput(jObj.getAsJsonPrimitive("selectionInput").getAsString());
        info.setCrossoverType(jObj.getAsJsonPrimitive("crossoverType").getAsString());
        info.setCrossoverCutting(getIntOrDefault(jObj, "crossoverCutting"));
        info.setCrossoverAspect(jObj.getAsJsonPrimitive("crossoverAspect").getAsString());
        info.setMutations(getMutations(jObj));
        info.setJumps(getIntOrDefault(jObj, "jumps"));
        info.setGensChecked(jObj.getAsJsonPrimitive("gensChecked").getAsBoolean());
        info.setGensInput(getIntOrDefault(jObj, "gensInput"));
        info.setFitnessChecked(jObj.getAsJsonPrimitive("fitnessChecked").getAsBoolean());
        info.setFitnessInput(getIntOrDefault(jObj, "fitnessInput"));
        info.setTimeChecked(jObj.getAsJsonPrimitive("timeChecked").getAsBoolean());
        info.setTimeInput(getLongOrDefault(jObj, "timeInput"));

        return info;
    }

    private List<MutationInfo> getMutations(JsonObject jObj){
        List<MutationInfo> mutations = new ArrayList<>();
        JsonElement element = jObj.get("mutations");
        if(element.isJsonArray()){
            JsonArray arr = element.getAsJsonArray();
            for(int i = 0; i < arr.size(); i++){
                JsonObject currObj = arr.get(i).getAsJsonObject();
                MutationInfo curr = new MutationInfo();
                curr.setType(currObj.getAsJsonPrimitive("type").getAsString());
                curr.setProbability(getDoubleOrDefault(currObj, "probability"));
                curr.setTupples(getIntOrDefault(currObj, "tupples"));
                curr.setComponent(currObj.getAsJsonPrimitive("component").getAsString());
                mutations.add(curr);
            }
        }

        return mutations;
    }

    private int getIntOrDefault(JsonObject jObj, String tagName){
        int ret = 0;
        String elem = jObj.getAsJsonPrimitive(tagName).getAsString();
        if(!elem.trim().isEmpty()){
            ret = Integer.parseInt(elem);
        }

        return ret;
    }

    private long getLongOrDefault(JsonObject jObj, String tagName){
        long ret = 0;
        String elem = jObj.getAsJsonPrimitive(tagName).getAsString();
        if(!elem.trim().isEmpty()){
            ret = Long.parseLong(elem);
        }

        return ret;
    }

    private double getDoubleOrDefault(JsonObject jObj, String tagName){
        double ret = 0;
        String elem = jObj.getAsJsonPrimitive(tagName).getAsString();
        if(!elem.trim().isEmpty()){
            ret = Double.parseDouble(elem);
        }

        return ret;
    }
}