package servlets;

import com.google.gson.Gson;
import utils.*;
import utils.infoModels.PropertyInfoObject;
import utils.models.Problem;
import utils.models.User;
import utils.models.UserInProblemObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "InProblemConfigsServlet", urlPatterns = {"/inProblemConfigsServlet"})
public class InProblemConfigsServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try{
            // get Engine info from data of request
            response.setContentType("application/json");
            Problem problem = ProblemUtils.getProblemByUser(request, getServletContext());
            User user = UserUtils.getUserByRequest(request, getServletContext());
            response.setStatus(200);
            boolean inProblem = problem.getUsersSolveProblem().containsKey(user);
            List<PropertyInfoObject> properties = inProblem ? EngineInfoCreator.createEngineInfo(problem.getSystemByUser(user), problem.getProblemRawInfo())
                                                            : null;
            UserInProblemObject retVal = new UserInProblemObject(inProblem, properties);
            response.getOutputStream().print(new Gson().toJson(retVal));
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
