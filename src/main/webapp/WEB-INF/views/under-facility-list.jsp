<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список подобъектов</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading">
            <span class="lead">Список подобъектов:</span>
            <sec:authorize access="isFullyAuthenticated()">
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="<c:url value='/facilities/child/create' />" class="btn btn-success btn-sm pull-right">Добавить
                        подобъект</a>
                </sec:authorize>
            </sec:authorize>
        </div>
        <table class="table table-hover text-center">
            <thead>
            <tr>
                <th style="text-align: center">№ п/п</th>
                <th style="text-align: center">Объект</th>
                <th style="text-align: center">Подобъект</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th style="text-align: center">Действие</th>
                </sec:authorize>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${underFacilities}" var="under">
                <tr>
                    <td>${under.id}</td>
                    <td>${under.facility.name}</td>
                    <td>${under.name}</td>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td style="text-align: center">
                                <div class="btn-group">
                                    <button type="button" data-toggle="dropdown"
                                            class="btn btn-primary btn-sm dropdown-toggle">Действие <span
                                            class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="<c:url value='edit/${under.id}' />">Изменить</a></li>
                                        <li class="divider"></li>
                                        <li><a href="<c:url value='delete/${under.id}' />" style="color: red">Удалить</a>
                                        </li>
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
</div>
</body>
</html>
