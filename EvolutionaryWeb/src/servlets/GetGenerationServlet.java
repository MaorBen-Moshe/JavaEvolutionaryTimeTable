package servlets;

import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableSystemDataSupplier;
import utils.*;
import utils.models.Problem;
import utils.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="GetGenerationServlet", urlPatterns = {"/getGeneration"})
public class GetGenerationServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        try{
            Problem problem = ProblemUtils.getProblemByUser(request, getServletContext());
            User user = UserUtils.getUserByRequest(request, getServletContext());
            if(problem.getUsersSolveProblem().containsKey(user)){
                EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> system = problem.getSystemByUser(user);
                if(system.getGenerationFitnessHistory() == null || system.getGenerationFitnessHistory().size() == 0){
                    response.setStatus(401);
                    response.getOutputStream().println("There is no history for this user. Are you sure you run this problem?");
                }else{
                    response.setStatus(200);
                    response.getOutputStream().print(String.valueOf(system.getCurrentNumberOfGenerations()));
                }

            }else{
                response.setStatus(401);
                response.getOutputStream().println("User " + user.getName() + " does not run the problem");
            }
        }catch (Exception e){
             response.setStatus(500);
             response.getOutputStream().println(e.getMessage());
        }
    }

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
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
