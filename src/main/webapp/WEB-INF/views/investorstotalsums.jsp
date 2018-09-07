<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Инвесторы. Доходность.</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
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
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading">
            <span class="lead">Вложенные деньги:</span>
        <span style="float: right; margin-top: 5px; display: none" id="tmr"></span>
        </div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Название объекта</th>
                <th>Сумма вложений, руб.</th>
                <th>Детали</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${investorsTotalSums}" var="sums">
                <tr>
                    <td>${sums.facility}</td>
                    <td><fmt:formatNumber value="${sums.givedCash}" type="currency"/></td>
                    <td><a id="go" name="${sums.facility}" href="<c:url value='/sumdetails' />" class="btn btn-link custom-width">Подробно</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div id="modal_form"><!-- Сaмo oкнo -->
    <span id="modal_close">X</span> <!-- Кнoпкa зaкрыть -->
    <table class="table table-hover" id="sum-details">
        <thead>
        <tr>
            <th>Дата вложения</th>
            <th>Название объекта</th>
            <th>Сумма вложений, руб.</th>
        </tr>
        </thead>
        <tbody>

        </tbody>
    </table>
</div>
<div id="overlay"></div><!-- Пoдлoжкa -->
<%@include file="slideDiv.jsp" %>
</body>
</html>