package servlets;

import com.google.gson.Gson;
import utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name="CurrentRunningInfoServlet", urlPatterns = {"/runningInfo"})
public class CurrentRunningInfoServlet extends HttpServlet {
    private static class RetObject{
        private final int generations;
        private final double fitness;

        RetObject(int gens, double fit){
            this.fitness = fit;
            this.generations = gens;
        }

        public int getGenerations() {
            return generations;
        }

        public double getFitness() {
            return fitness;
        }


    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("text/plain;charset=UTF-8");
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            User user = userManager.getUserByName(usernameFromSession);
            int id = user.getLastSeenProblem();
            ProblemManager problemManager = ServletUtils.getProblemManager(getServletContext());
            Problem problem = problemManager.getProblemById(id);
            String json = createAnswer(user, problem);
            out.println(json);
            out.flush();
        }
    }

    private String createAnswer(User user, Problem problem){
        Gson gson = new Gson();
        int gens = problem.getUsersSolveProblem().get(user).getGenerationNumber();
        double fit = problem.getUsersSolveProblem().get(user).getCurrentBestFitness();
        RetObject ret = new RetObject(gens, fit);
        return gson.toJson(ret);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

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