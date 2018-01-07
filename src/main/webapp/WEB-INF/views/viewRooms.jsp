<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список помещений</title>
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
    <style>
        th, td{
            text-align: center;
        }
    </style>
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Список помещений:</span>
            <sec:authorize access="isFullyAuthenticated()">
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="<c:url value='/newRoom' />" class="btn btn-success btn-sm pull-right">Добавить помещение</a>
                </sec:authorize>
            </sec:authorize>
        </div>

        <table class="table table-hover">
            <thead>
            <tr>
                <th>№ п/п</th>
                <th>Объект</th>
                <th>Подобъект</th>
                <th>Помещение</th>
                <th>Квадратура</th>
                <th>Стоимость</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th style="text-align: center">Действие</th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${rooms}" var="room">
                <tr>
                    <td>${room.id}</td>
                    <td>${room.underFacility.facility.facility}</td>
                    <td>${room.underFacility.underFacility}</td>
                    <td>${room.room}</td>
                    <td>
                        <fmt:setLocale value = "ru-RU" scope="session"/>
                        <fmt:formatNumber value="${room.roomSize}" type="number" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${room.coast}" type="currency" minFractionDigits="2"/>
                    </td>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td style="text-align: center">
                                <div class="btn-group">
                                    <button type="button" data-toggle="dropdown"
                                            class="btn btn-primary btn-sm dropdown-toggle">Действие <span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="<c:url value='/edit-room-${room.id}' />">Изменить</a></li>
                                        <li class="divider"></li>
                                        <li><a href="<c:url value='/delete-room-${room.id}' />" style="color: red">Удалить</a></li>
                                    </ul>
                                </div>
                            </td>
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

</div>
<%@include file="loader.jsp" %>
</body>
</html>