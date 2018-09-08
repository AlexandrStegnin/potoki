<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <sec:csrfMetaTags />
    <title>Инвесторы. Итоги.</title>
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
        <div class="panel-heading"><span class="lead">Инвесторы. Итоги:</span></div>
        <form:form modelAttribute="searchSummary" method="POST" class="form-inline" id="search-form">
            <div class="row" style="margin: 10px;">
                <label class="sr-only" for="facilities">Объект:</label>
                <form:select path="facility" id="facilities" items="${facilities}" multiple="false"
                             itemValue="id" itemLabel="facility" class="form-control input-sm" />
                <label for="beginPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">Период с:</label>
                <input id="beginPeriod" name="dateStart" type="date" class="form-control input-sm" value="">
                <label for="endPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">по:</label>
                <input id="endPeriod" name="dateEnd" type="date" class="form-control input-sm" value="" style="margin-right:5px">
                <button type="submit" id="bth-search" class="btn btn-primary btn-sm">Фильтр</button>
            </div>
        </form:form>
        <table class="table table-hover" id="investorsSummary">
            <thead>
            <tr>
                <th>Название объекта</th>
                <th>Период</th>
                <th>Вид оплаты (аренда, продажа)</th>
                <th>Аренда</th>
                <th>Месячная чистая прибыль</th>
                <th>Получено всего</th>
                <th>Ежемесячная прибыль с продажи*</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${investorsSummaries}" var="summary">
                <tr>
                    <td>${summary.facility}</td>
                    <td>${summary.getEndDateToLocalDate()}</td>
                    <td>${summary.aCorTag}</td>
                    <td><fmt:formatNumber value="${summary.fact_pay}" type="currency"/></td>
                    <td><fmt:formatNumber value="${summary.ostatok_po_dole}" type="currency"/></td>
                    <td><fmt:formatNumber value="${summary.summ}" type="currency"/></td>
                    <td><fmt:formatNumber value="${summary.pribil_s_prodazhi}" type="currency"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    *из расчета продажи объекта через 3 года с момента покупки
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>