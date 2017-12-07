package api.v1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static service.TokenGenerator.generateJwt;
import static service.UserValidator.isValid;

@WebServlet(name = "LoginService")
public class LoginService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userAgent = request.getHeader("Use-Agent");
        String ipAddress = request.getRemoteAddr();
        String jwt;

        PrintWriter output = response.getWriter();

        if (isValid(username, password)) {
            jwt = generateJwt(username, ipAddress, userAgent);
            output.write(jwt);
        } else {
            output.write("Invalid username.");
        }

        output.flush();
        output.close();
    }

}
