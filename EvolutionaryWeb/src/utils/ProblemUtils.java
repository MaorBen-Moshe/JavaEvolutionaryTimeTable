package utils;

import utils.models.Problem;
import utils.models.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProblemUtils {
    public enum ProblemMethod {Start, Pause, Resume, Stop};

    public static void ProblemMethodDispatcher(ProblemMethod method, HttpServletRequest request, HttpServletResponse response, ServletContext ctx) throws Exception {
        response.setContentType("text/plain;charset=UTF-8");
        UserManager userManager = ServletUtils.getUserManager(ctx);
        String usernameFromSession = SessionUtils.getUsername(request);
        User user = userManager.getUserByName(usernameFromSession);
        int id = user.getLastSeenProblem();
        ProblemManager problemManager = ServletUtils.getProblemManager(ctx);
        Problem problem = problemManager.getProblemById(id);
        try{
            switch (method){
                case Start: problem.runProblem(user); break;
                case Pause: problem.pauseProblem(user); break;
                case Resume: problem.resumeProblem(user); break;
                case Stop: problem.stopProblem(user); break;
            }
            response.setStatus(200);
        }catch (Exception e){
            response.setStatus(400);
            response.getOutputStream().println(e.getMessage());
        }
    }
}