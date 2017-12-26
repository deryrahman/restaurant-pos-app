package api.v1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        String jwt = null;

        PrintWriter output = response.getWriter();

        if (isValid(username, password)) {
            jwt = generateJwt(username, ipAddress, userAgent);
            output.write(jwt);
        } else {
            output.write("Invalid username.");
        }

        Cookie cookie = null;
        if (request.getCookies() != null) {
            for (Cookie cookie1 : request.getCookies()) {
                if (cookie1.getName().equals("POSRESTAURANT")) {
                    cookie = cookie1;
                }
            }
        }

        if(jwt!=null) {
            if(!jwt.isEmpty()) {
                if(cookie == null) {
                    cookie = new Cookie("POSRESTAURANT", jwt);
                }
                cookie.setValue(jwt);
                cookie.setMaxAge(60 * 60 * 24 * 365);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        output.flush();
        output.close();
    }

}
