package servlets;

import com.google.gson.Gson;
import evolutinary.EvolutionarySystem;
import models.TimeTable;
import models.TimeTableItem;
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
            try(PrintWriter writer = response.getWriter()){
                writer.println(createAnswer(user, problem, orientation, id));
            }

            response.setStatus(200);

        }catch (Exception e){
            response.setStatus(500);
            response.getOutputStream().println(e.getMessage());
        }
    }

    private String createAnswer(User user, Problem problem, String aspect, String aspectValue){
        EvolutionarySystem<TimeTable, TimeTableSystemDataSupplier> system = problem.getUsersSolveProblem().get(user).getSystem();
        TimeTable table = system.getBestSolution().getSolution();
        Map<Integer, Map<Integer, List<TimeTableItem>>> toRet;
        switch (aspect){
            case "Class": toRet = getByClass(aspectValue, system.getSystemInfo(), table); break;
            case "Teacher": toRet = getByTeacher(aspectValue, system.getSystemInfo(), table); break;
            default: throw new IllegalArgumentException(aspect + " is wrong you should only give 'Class' or 'Teacher' ");
        }

        return new Gson().toJson(toRet);
    }

    private Map<Integer, Map<Integer, List<TimeTableItem>>> getByClass(String aspectValue, TimeTableSystemDataSupplier info, TimeTable table) {
        int klass = info.getClasses().entrySet().stream().filter(e -> e.getValue().getId() == Integer.parseInt(aspectValue)).map(Map.Entry::getKey).findFirst().orElse(0);
        if(klass == 0) throw new IllegalArgumentException(aspectValue + " is not one of the classes!");
        Map<Integer, Map<Integer, List<TimeTableItem>>> dayHourTable = initializeTableView(info);
        table.getSortedItems().forEach(item -> {
            if(item.getSchoolClass().getId() == klass){
                dayHourTable.get(item.getDay()).get(item.getHour()).add(item);
            }
        });

        return dayHourTable;
    }

    private Map<Integer, Map<Integer, List<TimeTableItem>>> getByTeacher(String aspectValue, TimeTableSystemDataSupplier info, TimeTable table) {
        int teacher = info.getTeachers().entrySet().stream().filter(e -> e.getValue().getId() == Integer.parseInt(aspectValue)).map(Map.Entry::getKey).findFirst().orElse(0);
        if(teacher == 0) throw new IllegalArgumentException(aspectValue + " is not one of the teachers!");
        Map<Integer, Map<Integer, List<TimeTableItem>>> dayHourTable = initializeTableView(info);
        table.getSortedItems().forEach(item -> {
            if(item.getTeacher().getId() == teacher){
                dayHourTable.get(item.getDay()).get(item.getHour()).add(item);
            }
        });

        return dayHourTable;
    }

    private Map<Integer, Map<Integer, List<TimeTableItem>>> initializeTableView(TimeTableSystemDataSupplier supplier){
        Map<Integer, Map<Integer, List<TimeTableItem>>> dayHourTable = new HashMap<>();
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