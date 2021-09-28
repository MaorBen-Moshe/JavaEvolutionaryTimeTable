package utils;

import utils.models.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class UserUtils {
    public static User getUserByRequest(HttpServletRequest request, ServletContext ctx) throws Exception {
        UserManager userManager = ServletUtils.getUserManager(ctx);
        String usernameFromSession = SessionUtils.getUsername(request);
        return userManager.getUserByName(usernameFromSession);
    }
}