package servlets;

import utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            problem.setEngineInfoByUser(user, );
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
}
