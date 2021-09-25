package servlets;

import utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PauseProblem", urlPatterns = {"/pauseProblem"})
public class PauseProblemServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ProblemUtils.ProblemMethodDispatcher(ProblemUtils.ProblemMethod.Pause, request, response, getServletContext());
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
