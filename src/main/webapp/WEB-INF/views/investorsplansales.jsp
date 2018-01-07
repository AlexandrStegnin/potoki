<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Инвесторы. Плановая доходность.</title>
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
            <span class="lead">Инвесторы. Плановая доходность:</span>
            <span style="float: right; margin-top: 5px; display: none" id="tmr"></span>
        </div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Дата вложения денег</th>
                <th>Название объекта</th>
                <th>Сумма вложений, руб.</th>
                <th>Доля вложений в объект, %</th>
                <th>Чистый доход от аренды и продажи</th>
                <th>Общая доходность каждой суммы</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${investorsPlanSales}" var="sales">
                <tr>
                    <td>${sales.getDateGivedCashToLocalDate()}</td>
                    <td>${sales.facility}</td>
                    <td><fmt:formatNumber value="${sales.givedCash}" type="currency"/></td>
                    <td><fmt:formatNumber value="${sales.dolya}" type="percent" maxFractionDigits="4"/></td>
                    <td><fmt:formatNumber value="${sales.dohodNaRukiObshii}" type="currency"/></td>
                    <td><fmt:formatNumber value="${sales.dohodnostGodovaya3}" type="percent" maxFractionDigits="4"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>