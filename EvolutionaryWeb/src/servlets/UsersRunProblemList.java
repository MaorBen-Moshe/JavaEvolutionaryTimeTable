package servlets;

import com.google.gson.Gson;
import utils.*;
import utils.models.Problem;
import utils.models.ProblemConfigurations;
import utils.models.User;
import utils.models.UserRunProblemObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UsersRunProblem", urlPatterns = {"/usersRunProblemList"})
public class UsersRunProblemList extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("application/json");
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        User user = userManager.getUserByName(usernameFromSession);
        ProblemManager problemManager = ServletUtils.getProblemManager(getServletContext());
        Problem problem = problemManager.getProblemById(user.getLastSeenProblem());
        String json = createJson(problem);
        response.setStatus(200);
        response.getOutputStream().println(json);
    }

    private String createJson(Problem problem) {
        Gson gson = new Gson();
        List<UserRunProblemObject> usersToJson = new ArrayList<>();
        Map<User, ProblemConfigurations> problemUsers = problem.getUsersSolveProblem();
        problemUsers.forEach((key, val) -> {
            usersToJson.add(new UserRunProblemObject(key.getName(), val.getGenerationNumber(), val.getCurrentBestFitness()));
        });

        return gson.toJson(usersToJson);
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
