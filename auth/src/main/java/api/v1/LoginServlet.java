package api.v1;

import exception.InvalidCredentialsException;
import exception.UnsupportedMediaTypeException;

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

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter output = response.getWriter();

        String jwt = null;
        try {
            String token = generateTokenFromRequest(request);
            jwt = token;
            response.setStatus(HttpServletResponse.SC_OK);
            output.write(token);

        } catch (UnsupportedMediaTypeException e) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            output.write(e.getMessage());

        } catch (InvalidCredentialsException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            output.write(e.getMessage());

        } finally {
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
            output.close();
        }
    }

    private String generateTokenFromRequest(HttpServletRequest request) throws InvalidCredentialsException, UnsupportedMediaTypeException {
        if (request.getContentType().equals("application/x-www-form-urlencoded")) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");

            if (isValid(username, password)) {
                return generateJwt(username, ipAddress, userAgent);
            } else {
                throw new InvalidCredentialsException("Invalid username or password.");
            }

        } else {
            throw new UnsupportedMediaTypeException("Invalid content type.");
        }
    }

}
