<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список объектов</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/popupScripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForFacilities.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Список объектов:</span></div>
        <table class="table table-hover" id="tblFacilities">
            <thead>
            <tr>
                <th>ID</th>
                <th>Название</th>
                <th>Город</th>
                <th>Адрес</th>
                <th>Управляющий</th>
                <th>Инвесторы</th>
                <th>Арендаторы</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th width="100"></th>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <th width="100"></th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${facilities}" var="facility">
                <tr id="${facility.id}">
                    <td>${facility.id}</td>
                    <td>${facility.facility}</td>
                    <td>${facility.city}</td>
                    <td>${facility.address}</td>
                    <td>${facility.manager.login}</td>
                    <c:forEach items="${facility.investors}" var="investors" varStatus="stat">
                        <c:choose>
                            <c:when test="${fn:containsIgnoreCase(investors.userStuff.stuff, 'Инвестор') &&
                                            !fn:containsIgnoreCase(inv, investors.login.concat('; '))}">
                                <c:set var='inv' value="${stat.first ? '' : inv} ${investors.login.concat('; ')}" />
                            </c:when>
                            <c:when test="${fn:containsIgnoreCase(investors.userStuff.stuff, 'Арендатор') &&
                                            !fn:containsIgnoreCase(rent, investors.login.concat('; '))}">
                                <c:set var='rent' value="${stat.first ? '' : rent} ${investors.login.concat('; ')}" />
                            </c:when>
                        </c:choose>
                    </c:forEach>
                    <td>${inv}</td>
                    <td>${rent}</td>
                    <c:set var="rent" value="" />
                    <c:set var="inv" value="" />
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td><a href="<c:url value='/edit-facility-${facility.id}' />" class="btn btn-success custom-width">Изменить</a></td>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ADMIN')">
                            <td><a href="<c:url value='/deletefacility' />" class="btn btn-danger custom-width"
                                   id="delete" name="${facility.id}">Удалить</a></td>
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
                <a href="<c:url value='/newfacility' />">Добавить объект</a>
            </div>
        </sec:authorize>
    </sec:authorize>
</div>

<%@include file="loader.jsp" %>
<%@include file="popup.jsp" %>
<%@include file="slideDiv.jsp" %>
</body>
</html>