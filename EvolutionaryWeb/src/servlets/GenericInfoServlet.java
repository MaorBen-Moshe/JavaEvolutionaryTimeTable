package servlets;

import DTO.ModelToDTOConverter;
import DTO.TeacherDTO;
import com.google.gson.Gson;
import utils.CollectionsServletHelper;
import utils.models.GenericProblemInfo;
import utils.models.Problem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GenericInfoServlet", urlPatterns = {"/genericInfo"})
public class GenericInfoServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CollectionsServletHelper.ProcessCollectionsRequest(request, response, getServletContext(), this::createAnswer);
    }

    private String createAnswer(Problem problem) {
        return new Gson().toJson(new GenericProblemInfo(problem.getDays(), problem.getHours()));
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
