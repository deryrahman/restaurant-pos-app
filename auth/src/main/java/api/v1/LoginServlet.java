package api.v1;

import exception.AuthenticationServiceException;
import exception.InvalidCredentialsException;
import exception.UnsupportedMediaTypeException;
import service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static service.TokenService.generateJwtFromMap;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/plain");
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

        } catch (AuthenticationServiceException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            output.write(e.getMessage());

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

    private String generateTokenFromRequest(HttpServletRequest request)
            throws AuthenticationServiceException, UnsupportedMediaTypeException, SQLException {
        if (request.getContentType().equals("application/x-www-form-urlencoded")) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");

            if (LoginService.isValidCredential(username, password)) {
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("username", username);
                userInfo.put("userAgent", userAgent);
                userInfo.put("ipAddress", ipAddress);
                return generateJwtFromMap(userInfo);
            } else {
                throw new InvalidCredentialsException("Invalid username or password.");
            }

        } else {
            throw new UnsupportedMediaTypeException("Invalid content type.");
        }
    }

}
