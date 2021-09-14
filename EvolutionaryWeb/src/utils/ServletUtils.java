package utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static utils.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String Problem_MANAGER_ATTRIBUTE_NAME = "problemManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    private static final Object problemManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {
        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
            synchronized (userManagerLock) {
                if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                    servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
                }
            }
        }

        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static ProblemManager getProblemManager(ServletContext servletContext){
        if(servletContext.getAttribute(Problem_MANAGER_ATTRIBUTE_NAME) == null){
            synchronized (problemManagerLock){
                if(servletContext.getAttribute(Problem_MANAGER_ATTRIBUTE_NAME) == null){
                    servletContext.setAttribute(Problem_MANAGER_ATTRIBUTE_NAME, new ProblemManager());
                }
            }
        }

        return (ProblemManager) servletContext.getAttribute(Problem_MANAGER_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
            }
        }

        return INT_PARAMETER_ERROR;
    }
}
