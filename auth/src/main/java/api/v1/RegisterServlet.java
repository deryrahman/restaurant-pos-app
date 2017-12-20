package api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.*;
import model.UserIdentity;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter output = response.getWriter();

        try {
            String jsonBody = extractBody(request);
            UserIdentity newUser = convertJSONToUserIdentity(jsonBody);

            long newId = UserService.createUserIdentity(newUser);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            output.write("{'userId' : '" + newId +"'}");

        } catch (DuplicateDataException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            output.write(e.getMessage());
        } catch (BadDataException e) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            output.write(e.getMessage());
        } catch (FailedCRUDOperationException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            output.write(e.getMessage());
        } finally {
            output.close();
        }

    }

    private String extractBody(HttpServletRequest request) throws IOException, FailedCRUDOperationException {
        if (request.getContentType().equals("application/json")) {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (body.equals("")) {
                throw new EmptyDataException("Empty request body.");
            } else {
                return body;
            }
        } else {
            throw new BadDataException("Invalid content type.");
        }
    }

    private UserIdentity convertJSONToUserIdentity(String jsonBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonBody, UserIdentity.class);
    }


}
