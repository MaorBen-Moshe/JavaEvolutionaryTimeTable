package utils;

import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class EngineUtils {
    private final static Map<String, EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier>> evoSystems;

    static {
        evoSystems = new HashMap<>();
    }

    public static void createEvoSystem(HttpServletRequest request){
        HttpSession session = request.getSession();
        // also need to get a file, validates it and create a new engine of it;
    }
}
