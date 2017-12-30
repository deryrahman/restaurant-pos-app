package api.v1;

import exception.BadDataException;
import exception.FailedCRUDOperationException;
import exception.UnsupportedMediaTypeException;
import model.response.IdentityPayload;
import model.response.ResponseBody;
import model.uid.UserIdentity;
import service.UserIdentityService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserIdentityServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();

        String message = "";
        List<IdentityPayload> payloads = new ArrayList<>();

        try {
            List<UserIdentity> userIdentities = UserIdentityService.getAll();
            payloads = toPayloadList(userIdentities);
            message = "Get success.";
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException e) {
            message = (e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            ResponseBody<IdentityPayload> responseBody = new ResponseBody<>(message, payloads);
            output.write(responseBody.toJSON());
        }
    }

    private List<IdentityPayload> toPayloadList(List<UserIdentity> userIdentities) {
        List<IdentityPayload> payloads = new ArrayList<>();
        for (UserIdentity uid :
                userIdentities) {
            payloads.add(new IdentityPayload(uid));
        }
        return payloads;
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
