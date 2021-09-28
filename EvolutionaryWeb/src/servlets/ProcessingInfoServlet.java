package servlets;

import com.google.gson.Gson;
import utils.ProblemUtils;
import utils.models.Problem;
import utils.models.ProcessingObject;

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
        ProcessingObject ret = new ProcessingObject(problem.getSystem().isRunningProcess(), problem.getSystem().isPauseOccurred());
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
