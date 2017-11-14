<%@ page import="com.blibli.future.pos.webapp.Config" %><%
    Config webConfig = (Config) config.getServletContext().getAttribute("webappConfig");
    String baseUrl = webConfig.getBaseUrl();
%>

<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<h1>Success!</h1>
Deploy on : <%= baseUrl %>
</body>
</html>