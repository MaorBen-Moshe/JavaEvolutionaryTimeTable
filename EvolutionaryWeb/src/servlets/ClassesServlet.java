package servlets;

import DTO.ModelToDTOConverter;
import DTO.SchoolClassDTO;
import com.google.gson.Gson;
import utils.CollectionsServletHelper;
import utils.models.Problem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ClassesServlet", urlPatterns = {"/classes"})
public class ClassesServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        CollectionsServletHelper.ProcessCollectionsRequest(request, response, getServletContext(), this::createAnswer);
    }

    private String createAnswer(Problem problem) {
        List<SchoolClassDTO> classes = new ArrayList<>();
        ModelToDTOConverter converter = new ModelToDTOConverter();
        problem.getClassesModel().forEach(klass -> {
            classes.add(converter.createClassDTO(klass));
        });

        return new Gson().toJson(classes);
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
