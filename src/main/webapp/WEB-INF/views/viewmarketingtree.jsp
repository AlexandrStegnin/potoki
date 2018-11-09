<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">${title}</span>
            <sec:authorize access="isFullyAuthenticated()">
                <sec:authorize access="hasRole('ADMIN')">
                    <a id="updateMarketingTree" href="<c:url value='/updateMarketingTree' />" class="btn btn-success btn-sm pull-right">Обновить маркетинговое дерево</a>
                </sec:authorize>
            </sec:authorize>
        </div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>№ п/п</th>
                <th>Инвестор</th>
                <th>Партнёр</th>
                <th>Родство</th>
                <th>Дата первого вложения</th>
                <th>Дней до окончания статуса активен</th>
                <th>Статус инвестора</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${trees}" var="tree">
                <tr>
                    <td>${tree.id}</td>
                    <td>${tree.investor.login}</td>
                    <td>${tree.partner.login}</td>
                    <td>${tree.kin.val}</td>
                    <td>${tree.firstInvestmentDate}</td>
                    <td>${tree.daysToDeactivate}</td>
                    <td>${tree.invStatus.val}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <sec:authorize access="isRememberMe()">
        <p>Вы вошли с помощью функции "Запомнить меня".
            Чтобы иметь все права на данную страницу, Вам необходимо снова
            <a href="<c:url value='/login' />">ВОЙТИ</a> в систему используя логин/пароль.
        </p>
    </sec:authorize>
</div>
<%@include file="loader.jsp" %>
<%@include file="slideDiv.jsp" %>
</body>
</html>