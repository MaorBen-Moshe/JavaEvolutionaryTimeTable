package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import mutation.Mutation;
import utils.*;
import utils.infoModels.Info;
import utils.infoModels.InfoDeserializer;
import utils.infoModels.PropertyInfoObject;
import utils.models.Problem;
import utils.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@WebServlet(name = "SetEngineInfo", urlPatterns = {"/setEngineInfo"})
public class SetEngineInfoServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try{
            // get Engine info from data of request
            response.setContentType("application/json");
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            User user = userManager.getUserByName(usernameFromSession);
            ProblemManager problemManager = ServletUtils.getProblemManager(getServletContext());
            Problem problem = problemManager.getProblemById(user.getLastSeenProblem());
            String json = request.getParameter(Constants.JSON_PARAM);
            Gson gson = new GsonBuilder().registerTypeAdapter(Info.class, new InfoDeserializer()).create();
            Info info = gson.fromJson(new StringReader(json), Info.class);
            problem.setEngineInfoByUser(user, ParseUtils.parseInfo(info));
            response.setStatus(200);
            String retJson = new Gson().toJson(createEngineInfo(problem.getSystemByUser(user), info));
            response.getOutputStream().println(retJson);
        }catch (Exception e){
            response.setStatus(500);
            response.getOutputStream().println(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            response.setStatus(500);
            response.getOutputStream().println(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            response.setStatus(500);
            response.getOutputStream().println(e.getMessage());
        }
    }

    private List<PropertyInfoObject> createEngineInfo(EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> system, Info info){
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

        ret.add(new PropertyInfoObject("Jumps", String.valueOf(info.getJumps())));
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