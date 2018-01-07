<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Домашняя страница</title>
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
    <script type="text/javascript" src="<c:url value='/resources/core/js/sockjs-0.3.4.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/stomp.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/applic.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/socketScripts.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading">
            <span class="lead">Управление:</span>
            <span style="float: right; margin-top: 5px; display: none" id="tmr"></span>
        </div>
    </div>
    <div class="well">
        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA') or hasRole('BIGDADDY')">
            <a href="<c:url value='/viewpayments' />" class="btn btn-success btn-block">Мои платежи</a>
            <a href="<c:url value='/viewcashpayments' />" class="btn btn-success btn-block">Мои наличные платежи</a>
            <a href="<c:url value='/alphaextract' />" class="btn btn-success btn-block">Посмотреть выгрузки Альфа банка</a>
            <a href="<c:url value='/toshlextract' />" class="btn btn-success btn-block">Посмотреть выгрузки Toshl</a>
            <a href="<c:url value='/catalogue' />" class="btn btn-success btn-block">Справочники</a>

        </sec:authorize>
        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA') or hasRole('BIGDADDY') or hasRole('INVESTOR')">
            <a href="<c:url value='/investorstotalsum' />" class="btn btn-success btn-block">Инвесторы. Вложено денег</a>
            <a href="<c:url value='/investorsflows' />" class="btn btn-success btn-block">Инвесторы. Потоки</a>
        </sec:authorize>
        <sec:authorize access="hasRole('OLD')">
            <a href="<c:url value='/investorssummary' />" class="btn btn-success btn-block">Инвесторы. Итоги</a>
            <a href="<c:url value='/investorstotalsum' />" class="btn btn-success btn-block">Инвесторы. Вложено денег</a>
            <a href="<c:url value='/investorsplansale' />" class="btn btn-success btn-block">Инвесторы. Плановая доходность</a>
        </sec:authorize>
    </div>

</body>
</html>