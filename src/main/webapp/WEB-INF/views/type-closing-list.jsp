<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список видов закрытия вложений</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/popupScripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/type-closing.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Вид закрытия вложения:</span>
            <sec:authorize access="isFullyAuthenticated()">
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="<c:url value='/type-closing/create' />" class="btn btn-success btn-sm pull-right">Добавить
                        вид закрытия</a>
                </sec:authorize>
            </sec:authorize>
        </div>
        <table class="table table-hover" id="tblTypeClosInv">
            <thead>
            <tr>
                <th>№ п/п</th>
                <th>Вид закрытия вложения</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th style="text-align: center">Действие</th>
                </sec:authorize>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${typeClosingInvestsList}" var="type">
                <tr id="${type.id}">
                    <td>${type.id}</td>
                    <td>${type.name}</td>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td style="text-align: center">
                                <div class="btn-group">
                                    <button type="button" data-toggle="dropdown"
                                            class="btn btn-primary btn-sm dropdown-toggle">Действие <span
                                            class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="<c:url value='edit/${type.id}' />">Изменить</a></li>
                                        <li class="divider"></li>
                                        <li><a href="<c:url value='/#' />" id="delete" data-type-closing-id="${type.id}" style="color: red">Удалить</a>
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
<%@include file="popup_modal.jsp" %>
<%@include file="ddk_loader.jsp" %>
</body>
</html>
