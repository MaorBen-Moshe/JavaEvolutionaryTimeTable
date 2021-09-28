package servlets;

import com.google.gson.Gson;
import utils.ProblemUtils;
import utils.UserUtils;
import utils.models.Problem;
import utils.models.ProcessingObject;
import utils.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="ProcessingServlet", urlPatterns = {"/processInfo"})
public class ProcessingInfoServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("application/json");
        Problem problem = ProblemUtils.getProblemByUser(request, getServletContext());
        User user = UserUtils.getUserByRequest(request, getServletContext());
        int gens = 0;
        double fitness = 0;
        boolean running = false;
        boolean paused = false;
        if(problem.getUsersSolveProblem().containsKey(user)){
            gens = problem.getUsersSolveProblem().get(user).getGenerationNumber();
            fitness = problem.getUsersSolveProblem().get(user).getCurrentBestFitness();
        }

        if(problem.getUsersSolveProblem().containsKey(user)){
            running = problem.getSystemByUser(user).isRunningProcess();
            paused = problem.getSystemByUser(user).isPauseOccurred();
        }

        ProcessingObject ret = new ProcessingObject(running,
                                                    paused,
                                                    gens,
                                                    fitness);
        response.setStatus(200);
        response.getOutputStream().print(new Gson().toJson(ret));
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
