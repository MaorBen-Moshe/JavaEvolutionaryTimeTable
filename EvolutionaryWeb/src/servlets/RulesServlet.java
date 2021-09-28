package servlets;

import DTO.ModelToDTOConverter;
import DTO.RulesDTO;
import com.google.gson.Gson;
import utils.CollectionsServletHelper;
import utils.models.Problem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RulesServlet", urlPatterns = {"/rules"})
public class RulesServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CollectionsServletHelper.ProcessCollectionsRequest(request, response, getServletContext(), this::createAnswer);
    }

    private String createAnswer(Problem problem) {
        ModelToDTOConverter converter = new ModelToDTOConverter();
        RulesDTO rules = converter.createRulesObjectDTO(problem.getRulesModel());
        return new Gson().toJson(rules);
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
