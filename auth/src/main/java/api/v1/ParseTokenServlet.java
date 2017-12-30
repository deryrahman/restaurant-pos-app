package api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.UnsupportedMediaTypeException;
import io.jsonwebtoken.ExpiredJwtException;
import service.TokenService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

public class ParseTokenServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();
        String jsonResponse;
        String status = "";
        Map<String, Object> parsedInfo = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        try {
            payload = parseTokenFromRequest(request);
            status = "ok";
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (UnsupportedMediaTypeException e) {
            status = "invalid";
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            System.out.println(e.getMessage());
        } catch (SignatureException e) {
            status = "invalid";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(e.getMessage());
        } catch (ExpiredJwtException e) {
            status = "expired";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            status = "unknown";
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            parsedInfo.put("status", status);
            parsedInfo.put("payload", payload);

            jsonResponse = mapToJson(parsedInfo);

            output.write(jsonResponse);
            output.close();
        }

    }

    private Map<String, Object> parseTokenFromRequest(HttpServletRequest request) throws Exception {
        if (request.getContentType().equals("application/x-www-form-urlencoded")) {
            String token = request.getParameter("token");
            return TokenService.parseJwt(token);

        } else {
            throw new UnsupportedMediaTypeException();
        }

    }

    private String mapToJson(Map<String, Object> map) {
        String parsedJson;
        try {
            ObjectMapper mapper = new ObjectMapper();
            parsedJson = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Failed to parse token. \nError: " + e.getMessage();
        }
        return parsedJson;
    }


}
