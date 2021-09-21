package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername (HttpServletRequest request) {
        User user = getUser(request);
        return user != null ? user.getName() : null;
    }

    public static User getUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USER) : null;
        return sessionAttribute != null ? (User) sessionAttribute : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}