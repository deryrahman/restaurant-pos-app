package api;

import exception.InvalidCredentialsException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import model.response.ResponseBody;
import service.TokenService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class AuthenticationFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest servletRequest = (HttpServletRequest) req;
        HttpServletResponse servletResponse = (HttpServletResponse) resp;
        resp.setContentType("application/json");
        PrintWriter output = servletResponse.getWriter();

        Cookie[] cookies = servletRequest.getCookies();
        String token = getTokenFromCookies(cookies);

        ResponseBody<Object> responseBody = new ResponseBody<>();

        try {
            Map<String, Object> userInfo = TokenService.parseJwt(token);
            authorizeUserRequest(servletRequest, userInfo);
            servletResponse.setHeader("Access-Control-Allow-Origin", "*");
            chain.doFilter(req, resp);

        } catch (SignatureException | InvalidCredentialsException e) {
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseBody.setMessage("Invalid token.");
            output.write(responseBody.toJSON());

        } catch (ExpiredJwtException e) {
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            responseBody.setMessage("Expired token.");
            output.write(responseBody.toJSON());

        } catch (Exception e) {
            servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseBody.setMessage(e.getMessage());
            output.write(responseBody.toJSON());
            System.out.println(e.getMessage());
            e.printStackTrace();

        }



    }

    private void authorizeUserRequest(HttpServletRequest request, Map<String, Object> userInfo) throws Exception {
        String role = (String) userInfo.get("role");
        boolean isRoleValid = role.equals("admin") || role.equals("manager");
        boolean isIpValid = userInfo.get("ipAddress").equals(request.getRemoteAddr());
        boolean isUserAgentValid = userInfo.get("userAgent").equals(request.getHeader("User-Agent"));
        if (!isRoleValid || !isIpValid || !isUserAgentValid) {
            throw new InvalidCredentialsException();
        }
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("POSRESTAURANT")) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
