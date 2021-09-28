package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.*;
import utils.infoModels.Info;
import utils.infoModels.InfoDeserializer;
import utils.models.Problem;
import utils.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;

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
            problem.setProblemRawInfo(info);
            response.setStatus(200);
            String retJson = new Gson().toJson(EngineInfoCreator.createEngineInfo(problem.getSystemByUser(user), info));
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
}