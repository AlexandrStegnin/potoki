<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <sec:csrfMetaTags/>
    <title>Приложения к договорам</title>
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
    <link href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"
          rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/progress.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js' />"></script>
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js' />"></script>
    <script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
</head>
<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <table class="table table-striped w-auto table-sm">
        <thead>
        <tr style="text-align: center">
            <th>Инвестор</th>
            <th>Название файла</th>
            <th>Дата загрузки</th>
            <th>Кто загрузил</th>
            <th>Прочитано</th>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th>Удалить</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${contracts}" var="annex">
            <tr>
                <td>${annex.investor}</td>
                <td>${annex.annexName}</td>
                <td>${annex.dateLoad}</td>
                <td>${annex.loader}</td>
                <c:choose>
                    <c:when test="${annex.read == 1}">
                        <td>Да</td>
                    </c:when>
                    <c:otherwise>
                        <td>Нет</td>
                    </c:otherwise>
                </c:choose>
                <td>
                    <button type="button" class="btn btn-sm btn-danger"><i class="far fa-trash-alt"></i></button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
</html>
