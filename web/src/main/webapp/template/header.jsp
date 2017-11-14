<%@ page import="com.blibli.future.pos.webapp.Config" %>
<%
    Config webConfig = (Config) config.getServletContext().getAttribute("webappConfig");
    String basePath = webConfig.getBaseUrl();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Homepage</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%= basePath %>/assets/style.css">
    <link rel="stylesheet" type="text/css" href="<%= basePath %>/assets/custom.css">
</head>