package servlets;

import evolutinary.TimeTableEvolutionarySystemImpel;
import utils.ETTXmlParser;
import utils.models.Problem;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="UploadServlet", urlPatterns = {"/pages/timeTableProblem/upload"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Part part = request.getParts().stream().findFirst().orElse(null);
        assert (part != null);
        try{
            TimeTableEvolutionarySystemImpel system = (TimeTableEvolutionarySystemImpel) ETTXmlParser.parse(part.getInputStream());
            String usernameFromSession = SessionUtils.getUsername(request);
            Problem problem = new Problem(usernameFromSession);
            problem.setSystem(system);
            ServletUtils.getProblemManager(getServletContext()).addProblem(problem);
            response.setStatus(200);
            out.println("Problem added successfully");
        }catch (Exception e){
            response.setStatus(500);
            out.println(e.getMessage());
        }
    }
}