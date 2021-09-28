package utils;

import utils.interfaces.ConvertCollectionToJson;
import utils.models.Problem;
import utils.models.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CollectionsServletHelper {
    public static void ProcessCollectionsRequest(HttpServletRequest request, HttpServletResponse response, ServletContext ctx, ConvertCollectionToJson invoker) throws IOException {
        try{
            // get Engine info from data of request
            response.setContentType("application/json");
            UserManager userManager = ServletUtils.getUserManager(ctx);
            String usernameFromSession = SessionUtils.getUsername(request);
            User user = userManager.getUserByName(usernameFromSession);
            ProblemManager problemManager = ServletUtils.getProblemManager(ctx);
            Problem problem = problemManager.getProblemById(user.getLastSeenProblem());
            try(PrintWriter writer = response.getWriter()){
                writer.println(invoker.toJsonFromProblem(problem));
            }

            response.setStatus(200);

        }catch (Exception e){
            response.setStatus(500);
            response.getOutputStream().println(e.getMessage());
        }
    }
}