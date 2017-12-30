package api.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.*;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserIdentityServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();

        String message = "";
        List<IdentityPayload> payloads = new ArrayList<>();

        try {
            if (isPathProvided(request.getPathInfo())) {
                long id = getIdFromPath(request.getPathInfo());
                UserIdentity user = UserIdentityService.findById(id);
                payloads.add(new IdentityPayload(user));
                message = "Successfully get user identity.";

            } else {
                List<UserIdentity> userIdentities = UserIdentityService.getAll();
                payloads = toPayloadList(userIdentities);
                message = "Successfully get all user identities.";

            }
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (DataNotFoundException | NumberFormatException e) {
            message = "User not found.";
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } catch (SQLException e) {
            message = e.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            ResponseBody<IdentityPayload> responseBody = new ResponseBody<>(message, payloads);
            output.write(responseBody.toJSON());
            output.close();
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();

        String message = "";
        List<IdentityPayload> payloads = new ArrayList<>();

        String jsonBody;

        try {
            if (isPathProvided(request.getPathInfo())) {
                long id = getIdFromPath(request.getPathInfo());
                jsonBody = extractBody(request);
                Map<String, Object> userMap = jsonToMap(jsonBody);
                UserIdentity userIdentity = UserIdentityService.updateFromMap(userMap);
                payloads.add(new IdentityPayload(userIdentity));
                message = "Successfully updated user identity.";
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                message = "Invalid HTTP method.";
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (UnsupportedMediaTypeException e) {
            message = e.getMessage();
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);

        } catch (EmptyDataException | IOException | NullPointerException e) {
            message = new UserIdentity().toString();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } catch (SQLIntegrityConstraintViolationException e) {
            message = e.getMessage();
            response.setStatus(HttpServletResponse.SC_CONFLICT);

        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            ResponseBody<IdentityPayload> responseBody = new ResponseBody<>(message, payloads);
            output.write(responseBody.toJSON());
            output.close();
        }
    }

    private boolean isPathProvided(String pathInfo) {
        return !(pathInfo == null || pathInfo.equals("/"));
    }

    private long getIdFromPath(String pathInfo) throws NumberFormatException {
        return Long.parseLong(pathInfo.substring(1));
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
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();

        String message = "";
        List<IdentityPayload> payloads = new ArrayList<>();

        String jsonBody;

        try {
            jsonBody = extractBody(request);
            Map<String, Object> userMap = jsonToMap(jsonBody);
            UserIdentity userIdentity = UserIdentityService.createFromMap(userMap);
            payloads.add(new IdentityPayload(userIdentity));
            message = "Successfully created new user identity.";
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (UnsupportedMediaTypeException e) {
            message = e.getMessage();
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);

        } catch (EmptyDataException | IOException | NullPointerException e) {
            message = new UserIdentity().toString();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } catch (SQLIntegrityConstraintViolationException e) {
            message = e.getMessage();
            response.setStatus(HttpServletResponse.SC_CONFLICT);

        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            ResponseBody<IdentityPayload> responseBody = new ResponseBody<>(message, payloads);
            output.write(responseBody.toJSON());
            output.close();
        }

    }

    private Map<String, Object> jsonToMap(String jsonBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonBody, new TypeReference<Map<String, Object>>(){});
    }

    private String extractBody(HttpServletRequest request) throws IOException, EmptyDataException, UnsupportedMediaTypeException {
        if (request.getContentType().equals("application/json")) {
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            if (body.equals("")) {
                throw new EmptyDataException("Empty request body.");
            } else {
                return body;
            }
        } else {
            throw new UnsupportedMediaTypeException("Invalid content type.");
        }
    }

}
