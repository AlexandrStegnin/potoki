<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список пользователей</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/applic.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/users.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Список пользователей:</span></div>
        <div class="row" style="margin-top:10px; margin-left:10px; margin-bottom:10px; margin-right:10px">
            <label class="sr-only" for="userStatus">Статус:</label>
            <select id="userStatus" class="input-sm">
                <c:forEach items="${userStatuses}" var="status">
                    <option value="${status.title}">${status.title}</option>
                </c:forEach>
            </select>
            <button type="button" id="bth-search" class="btn btn-primary btn-sm">Фильтр</button>
            <sec:authorize access="isFullyAuthenticated()">
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="<c:url value='/users/create' />" class="btn btn-link btn-sm pull-right">Добавить
                        пользователя</a>
                </sec:authorize>
            </sec:authorize>
        </div>
        <table class="table table-hover" id="tblUsers">
            <thead>
            <tr>
                <th>ID</th>
                <th>Имя пользователя</th>
                <th>Email</th>
                <th>Подтверждён</th>
                <th>Роль</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th style="text-align: center;" colspan="2">Действие</th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user">
                <tr id="${user.id}">
                    <td>${user.id}</td>
                    <td>${user.login}</td>
                    <td>${user.profile.email}</td>
                    <c:choose>
                        <c:when test="${user.confirmed}">
                            <td>Да</td>
                        </c:when>
                        <c:otherwise>
                            <td>Нет</td>
                        </c:otherwise>
                    </c:choose>
                    <c:forEach items="${user.roles}" var="role">
                        <c:choose>
                            <c:when test="${role.role == 'ROLE_ADMIN'}">
                                <c:set var="roleName" value="Админ" />
                            </c:when>
                            <c:when test="${role.role == 'ROLE_INVESTOR'}">
                                <c:set var="roleName" value="Инвестор" />
                            </c:when>
                            <c:when test="${role.role == 'ROLE_MANAGER'}">
                                <c:set var="roleName" value="Управляющий" />
                            </c:when>
                        </c:choose>
                    </c:forEach>
                    <td>${roleName}</td>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td><a href="<c:url value='/edit-user-${user.id}' />" class="btn btn-success custom-width">Изменить</a>
                            </td>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ADMIN')">
                            <td><a href="<c:url value='/#' />" id="delete" data-user-id="${user.id}"
                                   class="btn btn-danger custom-width">Удалить</a></td>
                        </sec:authorize>
                    </sec:authorize>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="popup_modal.jsp" %>
<%@include file="ddk_loader.jsp" %>
</body>
</html>
