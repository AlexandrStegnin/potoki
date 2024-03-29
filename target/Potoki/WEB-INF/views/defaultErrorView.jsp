<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
    <title>Ошибка</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/app.css' />" rel="stylesheet" />
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>
<body>
<h3>Произошла ошибка.</h3>
<p><b>${exception}</b></p>
<p>Отчёт об ошибке отправлен администратору.</p>
</body>
</html>
