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
        PrintWriter output = response.getWriter();

        try {
            String requestBody = getBody(request);

            Map<String, String> userInfo = extractPayloadFromRequestBody(requestBody);

            String token = TokenGenerator.generateJwtFromMap(userInfo);
            output.write(token);
        } catch (InvalidTokenRequestException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            output.write(e.getMessage());
        }

        output.close();
    }

    private Map<String, String> extractPayloadFromRequestBody(String requestBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body = mapper.readValue(requestBody, new TypeReference<Map<String, Object>>(){});
        if (body.get("payload") instanceof Map) {
            return (Map<String, String>) body.get("payload");
        } else {
            return new HashMap<>();
        }
    }

    private String getBody(HttpServletRequest request) throws IOException, InvalidTokenRequestException {
        if (request.getContentType().equals("application/json")) {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (body.equals("")) {
                throw new InvalidTokenRequestException("Empty request body.");
            } else {
                return body;
            }
        } else {
            throw new InvalidTokenRequestException("Invalid content type.");
        }
    }

}
