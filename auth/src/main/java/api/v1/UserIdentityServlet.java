package api.v1;

import exception.BadDataException;
import exception.FailedCRUDOperationException;
import exception.UnsupportedMediaTypeException;
import service.UserIdentityService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class UserIdentityServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String extractBody(HttpServletRequest request) throws IOException, FailedCRUDOperationException, UnsupportedMediaTypeException {
        if (request.getContentType().equals("application/json")) {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (body.equals("")) {
                throw new BadDataException("Empty request body.");
            } else {
                return body;
            }
        } else {
            throw new UnsupportedMediaTypeException("Invalid content type.");
        }
    }

}
