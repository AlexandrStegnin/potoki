<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: Александр
  Date: 07.07.2017
  Time: 11:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Сайт недоступен</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/app.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>
<body>
<div id="mainWrapper">
    <div class="login-container" style="height: 300px">
        <div class="login-card" style="height: 100%; text-align: center;">
            <div class="login-form" align="center" style="height: 100%; text-align: center;">
                <br>
                <h3>Сайт не доступен.</h3>
                <h4>Приносим извинения за временные неудобства.</h4>
                <p><img src="<c:url value='/resources/core/img/kolesnik_logo_cmyk.png' />" width="100px"></p>
                <a href="/login">Вернуться на страницу входа</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
