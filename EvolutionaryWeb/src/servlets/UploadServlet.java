package servlets;

import com.google.gson.Gson;
import evolutinary.TimeTableEvolutionarySystemImpel;
import utils.ETTXmlParser;
import utils.Problem;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name="UploadServlet", urlPatterns = {"/pages/timeTableProblem/upload"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadServlet extends HttpServlet {
    private static int fileCounter;

    static {
        fileCounter = 1;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();
        String path;
        synchronized (this){
            path = "./problem" + fileCounter++;
        }

        File file = new File(path);
        file.createNewFile();
        file.setWritable(true);
        try(FileWriter writer = new FileWriter(path, false)){
            for(Part part : parts){
                writer.write(new Scanner(part.getInputStream()).useDelimiter("\\Z").next());
            }
        }

        try{
            TimeTableEvolutionarySystemImpel system = (TimeTableEvolutionarySystemImpel) ETTXmlParser.parse(path);
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