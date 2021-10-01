package servlets;

import com.google.gson.Gson;
import evolutinary.EvolutionarySystem;
import javafx.scene.control.Cell;
import models.*;
import utils.*;
import utils.models.Problem;
import utils.models.SolutionObject;
import utils.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@WebServlet(name = "TableServlet", urlPatterns = {"/tableDisplay"})
public class TableServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try{
            // get Engine info from data of request
            response.setContentType("application/json");
            String orientation = request.getParameter("orientation");
            String id = request.getParameter("id");
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            User user = userManager.getUserByName(usernameFromSession);
            ProblemManager problemManager = ServletUtils.getProblemManager(getServletContext());
            Problem problem = problemManager.getProblemById(user.getLastSeenProblem());
            response.getOutputStream().print(createAnswer(user, problem, orientation, id));
            response.setStatus(200);

        }catch (Exception e){
            response.setStatus(500);
            response.getOutputStream().println(e.getMessage());
        }
    }

    private String createAnswer(User user, Problem problem, String aspect, String aspectValue) throws Exception {
        EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> system = problem.getSystemByUser(user);
        BestSolutionItem<TimeTable, TimeTableSystemDataSupplier> bestItem = system.getBestSolution();
        if(bestItem == null) throw new Exception("No best solution available. Are you sure you run the problem at least one time?");
        TimeTable table = bestItem.getSolution();
        if(table == null) throw new Exception("No best solution available. Are you sure you run the problem at least one time?");
        Map<Integer, Map<Integer, List<SolutionObject.CellItem>>> info;
        switch (aspect){
            case "Class": info = getByClass(aspectValue, system.getSystemInfo(), table); break;
            case "Teacher": info = getByTeacher(aspectValue, system.getSystemInfo(), table); break;
            default: throw new IllegalArgumentException(aspect + " is wrong you should only give 'Class' or 'Teacher' ");
        }

        SolutionObject solution = new SolutionObject(table.getSoftRulesAvg(), table.getHardRulesAvg(), bestItem.getGenerationCreated(), bestItem.getFitness(), table.getRulesScore(), info);
        return new Gson().toJson(solution);
    }

    private Map<Integer, Map<Integer, List<SolutionObject.CellItem>>> getByClass(String aspectValue, TimeTableSystemDataSupplier info, TimeTable table) {
        int klass = info.getClasses().entrySet().stream().filter(e -> e.getValue().getId() == Integer.parseInt(aspectValue)).map(Map.Entry::getKey).findFirst().orElse(0);
        if(klass == 0) throw new IllegalArgumentException(aspectValue + " is not one of the classes!");
        Map<Integer, Map<Integer, List<SolutionObject.CellItem>>> dayHourTable = initializeTableView(info);
        table.getSortedItems().forEach(item -> {
            Teacher currTeacher = item.getTeacher();
            Subject currSubject = item.getSubject();
            if(item.getSchoolClass().getId() == klass){
                dayHourTable.get(item.getDay()).get(item.getHour()).add(new SolutionObject.CellItem(currTeacher.getName(), currTeacher.getId(), currSubject.getName(), currSubject.getId()));
            }
        });

        return dayHourTable;
    }

    private Map<Integer, Map<Integer, List<SolutionObject.CellItem>>> getByTeacher(String aspectValue, TimeTableSystemDataSupplier info, TimeTable table) {
        int teacher = info.getTeachers().entrySet().stream().filter(e -> e.getValue().getId() == Integer.parseInt(aspectValue)).map(Map.Entry::getKey).findFirst().orElse(0);
        if(teacher == 0) throw new IllegalArgumentException(aspectValue + " is not one of the teachers!");
        Map<Integer, Map<Integer, List<SolutionObject.CellItem>>> dayHourTable = initializeTableView(info);
        table.getSortedItems().forEach(item -> {
            SchoolClass currClass = item.getSchoolClass();
            Subject currSubject = item.getSubject();
            if(item.getTeacher().getId() == teacher){
                dayHourTable.get(item.getDay()).get(item.getHour()).add(new SolutionObject.CellItem(currClass.getName(), currClass.getId(), currSubject.getName(), currSubject.getId()));
            }
        });

        return dayHourTable;
    }

    private Map<Integer, Map<Integer, List<SolutionObject.CellItem>>> initializeTableView(TimeTableSystemDataSupplier supplier){
        Map<Integer, Map<Integer, List<SolutionObject.CellItem>>> dayHourTable = new HashMap<>();
        IntStream.range(1, supplier.getDays() + 1).forEach(i -> {
            dayHourTable.put(i, new HashMap<>());
            IntStream.range(1, supplier.getHours() + 1).forEach(j -> dayHourTable.get(i).put(j, new ArrayList<>()));
        });

        return dayHourTable;
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