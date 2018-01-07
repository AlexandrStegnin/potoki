<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список подобъектов</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Список подобъектов:</span></div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>№ п/п</th>
                <th>Объект</th>
                <th>Подобъект</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th width="100"></th>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <th width="100"></th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${underFacilities}" var="under">
                <tr>
                    <td>${under.id}</td>
                    <td>${under.facility.facility}</td>
                    <td>${under.underFacility}</td>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td><a href="<c:url value='/edit-underfacility-${under.id}' />" class="btn btn-success custom-width">Изменить</a></td>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ADMIN')">
                            <td><a href="<c:url value='/delete-underfacility-${under.id}' />" class="btn btn-danger custom-width">Удалить</a></td>
                        </sec:authorize>
                    </sec:authorize>
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

    <sec:authorize access="isFullyAuthenticated()">
        <sec:authorize access="hasRole('ADMIN')">
            <div class="well">
                <a href="<c:url value='/newunderfacility' />">Добавить подобъект</a>
            </div>
        </sec:authorize>
    </sec:authorize>
</div>
</body>
</html>