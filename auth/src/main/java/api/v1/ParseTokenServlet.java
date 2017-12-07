package api.v1;

import service.TokenGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ParseTokenServlet")
public class ParseTokenServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String parsedToken = parseToken(token);

        response.setContentType("application/json");
        PrintWriter output = response.getWriter();
        output.write(parsedToken);
        output.close();
    }

    private String parseToken(String token) {
        return TokenGenerator.parseJwt(token);
    }

}
