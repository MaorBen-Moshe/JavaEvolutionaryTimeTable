package servlets;

import utils.*;
import utils.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static utils.Constants.USER;

@WebServlet(name="LoginServlet", urlPatterns = {"/pages/login/login"})
public class LoginServlet extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            String usernameFromParameter = request.getParameter(USER);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.setStatus(409);
                response.getOutputStream().println("Error occurred in server");
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (userManager.isUserExists(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";
                        response.setStatus(401);
                        response.getOutputStream().println(errorMessage);
                    }
                    else {
                        User user = new User(usernameFromParameter);
                        userManager.addUser(user);
                        request.getSession(true).setAttribute(Constants.USER, user);
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(200);
                        response.getOutputStream().println(Constants.Second_Page_URL);
                    }
                }
            }
        }else if(!usernameFromSession.equals(request.getParameter(USER))){
            String errorMessage = "Username " + usernameFromSession + " already logged in. Please ask him to logout first.";
            response.setStatus(401);
            response.getOutputStream().println(errorMessage);
        }else {
            response.setStatus(200);
            response.getOutputStream().println(Constants.Second_Page_URL);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}