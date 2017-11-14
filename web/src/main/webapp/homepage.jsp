<%@ page import="com.blibli.future.pos.webapp.Config" %><%
    Config webConfig = (Config) config.getServletContext().getAttribute("webappConfig");
%>

<jsp:include page='template/header.jsp'></jsp:include>
<body>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">

        <div class="navbar-header">
            <a href="#" class="navbar-brand">PEREGRINE</a>
        </div>

        <div>
            <ul class="nav navbar-nav navbar-left">
                <li class="active"><a href="index.html">Admin Page</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">Iqbal</a></li>
                <li><a href="login.html">Logout</a></li>
            </ul>
        </div>

    </div>
</nav>

<div class="container">
    <div class="row">
        <jsp:include page='template/sidebar.jsp'></jsp:include>
        <div class="col-xs-12 overlay">
        </div>
        <jsp:include page='template/page-homepage.jsp'></jsp:include>
    </div>
</div>
</body>
<jsp:include page='template/footer.jsp'></jsp:include>
