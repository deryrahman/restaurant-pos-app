package api.v1;

import exception.InvalidCredentialsException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static service.TokenGenerator.generateJwt;
import static service.UserService.isValid;

@WebServlet(name = "LoginService")
public class LoginService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter output = response.getWriter();

        try {
            String token = generateTokenFromRequest(request);
            response.setStatus(HttpServletResponse.SC_OK);
            output.write(token);
        } catch (InvalidCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            output.write(e.getMessage());
        } finally {
            output.close();
        }
    }

    private String generateTokenFromRequest(HttpServletRequest request) throws InvalidCredentialsException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        if (isValid(username, password)) {
            return generateJwt(username, ipAddress, userAgent);
        } else {
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }

}
