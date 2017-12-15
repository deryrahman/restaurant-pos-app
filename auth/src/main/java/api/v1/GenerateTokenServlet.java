package api.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.InvalidTokenRequestException;
import service.TokenGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "GenerateTokenServlet")
public class GenerateTokenServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = getBody(request);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body;

        PrintWriter output = response.getWriter();
        if (!requestBody.isEmpty()) {
            body = mapper.readValue(requestBody, new TypeReference<Map<String, Object>>(){});
            Map<String, String> userInfo = (LinkedHashMap<String, String>) body.get("payload");

            try {
                String token = TokenGenerator.generateJwtFromMap(userInfo);
                output.write(token);
            } catch (InvalidTokenRequestException e) {
                output.write(e.getMessage());
            }
        } else {
            response.setStatus(400);
            output.write("Empty request body.");
        }


        output.close();
    }

    private String getBody(HttpServletRequest request) throws IOException{
        if ("POST".equals(request.getMethod())) {
            return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            return "";
        }
    }

}
