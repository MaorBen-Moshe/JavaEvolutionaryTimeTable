package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import crossover.Crossover;
import models.*;
import mutation.FlippingMutation;
import mutation.Mutation;
import mutation.SizerMutation;
import selection.RouletteWheelSelection;
import selection.Selection;
import selection.TournamentSelection;
import selection.TruncationSelection;
import utils.*;
import utils.infoModels.Info;
import utils.infoModels.InfoDeserializer;
import utils.infoModels.MutationInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@WebServlet(name = "SetEngineInfo", urlPatterns = {"/setEngineInfo"})
public class SetEngineInfoServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try{
            // get Engine info from data of request
            response.setContentType("text/plain;charset=UTF-8");
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            User user = userManager.getUserByName(usernameFromSession);
            ProblemManager problemManager = ServletUtils.getProblemManager(getServletContext());
            Problem problem = problemManager.getProblemById(user.getLastSeenProblem());
            String json = request.getParameter(Constants.JSON_PARAM);
            Gson gson = new GsonBuilder().registerTypeAdapter(Info.class, new InfoDeserializer()).create();
            Info info = gson.fromJson(new StringReader(json), Info.class);
            problem.setEngineInfoByUser(user, parseInfo(info));
            response.setStatus(200);
            response.getOutputStream().println("Info added successfully");
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

    private EngineInfoObject parseInfo(Info info) throws Exception {
        Selection<TimeTable> selection = ETTXmlParser.createSelection(info.getSelectionType(), info.getSelectionInput());
        Crossover<TimeTable, TimeTableSystemDataSupplier> crossover = ETTXmlParser.createCrossover(info.getCrossoverType() + "Oriented", info.getCrossoverCutting(), info.getCrossoverAspect().toUpperCase(Locale.ROOT));
        List<Mutation<TimeTable, TimeTableSystemDataSupplier>> mutations = createMutations(info.getMutations());
        Set<TerminateRule> terminate = createTerminates(info.getGensChecked(), info.getGensInput(), info.getFitnessChecked(), info.getFitnessInput(), info.getTimeChecked(), info.getTimeInput());
        return new EngineInfoObject(info.getPopulation(), info.getElitism(), info.getJumps(), terminate,selection,crossover, mutations);
    }

    private List<Mutation<TimeTable, TimeTableSystemDataSupplier>> createMutations(List<MutationInfo> mutations) {
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

    private Set<TerminateRule> createTerminates(boolean gensChecked, int gensInput, boolean fitnessChecked, int fitnessInput, boolean timeChecked, long timeInput) {
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