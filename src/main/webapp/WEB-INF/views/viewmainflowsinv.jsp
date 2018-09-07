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
    <title>Потоки инвесторов</title>
    <style type="text/css">
        table {
            text-align: center;
        }

        td {
            text-align: center;
            padding: 0;
        }

        .d24 {
            background: #7CFF18;
            height: 20px;
            width: 4px;
            position: relative;
            left: 0%;
            margin-right: 10%;
            margin-left: 0;
        }

        .d24:after {
            content: "";
            height: 4px;
            width: 20px;
            background: #7CFF18;
            position: absolute;
            left: -8px;
            top: 8px;
        }

        .d24 {
            background: #7CFF18;
            height: 20px;
            width: 4px;
            position: relative;
            left: 0%;
            margin-right: 10%;
            margin-left: 0;
        }

        .d24:after {
            content: "";
            height: 4px;
            width: 20px;
            background: #7CFF18;
            position: absolute;
            left: -8px;
            top: 8px;
        }

        .d25 {
            background: #fff;
            height: 20px;
            width: 4px;
            position: relative;
            left: 0%;
            margin-right: 10%;
            margin-left: 0;
        }

        .d25:after {
            content: "";
            height: 4px;
            width: 20px;
            background: #F97174;
            position: absolute;
            left: -8px;
            top: 8px;
        }
    </style>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForFlows.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container" style="height: 95%">
    <%@include file="old_authheader.jsp" %>

    <form:form modelAttribute="searchSummaryF" method="POST" id="searchFacility" class="form-inline">
        <div class="row" style="margin: 10px 10px 10px 0; padding-left: 9%">
            <label class="sr-only" for="facilities">Объект:</label>
            <form:select path="facility" id="facilities" items="${facilities}" multiple="false"
                         class="form-control input-sm"/>
            <button type="submit" name="facilitySubmit" id="bth-search" class="btn btn-primary btn-sm">Фильтр</button>
        </div>
    </form:form>

    <div class="row">
        <div class="col-md-6"
             style="margin-bottom: 10px; height: 50%; overflow-y: scroll; padding-left: 10%; padding-right: 10%">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading"><span class="lead" style="font-weight: bold">${monthAndYear}</span></div>
                <form:form modelAttribute="searchSummary" method="POST" class="form-inline" id="search-form-months">
                    <div class="row" style="margin: 10px;">
                        <label class="sr-only" for="paysMonth">Платёж:</label>
                        <form:select path="pay" id="paysMonth" items="${pays}" multiple="false"
                                     class="form-control input-sm"/>
                        <label class="sr-only" for="underFacilitiesMonth">Подобъект:</label>
                        <form:select path="underFacility" id="underFacilitiesMonth" items="${uFacilities}"
                                     multiple="false" class="form-control input-sm"/>
                        <button type="submit" name="allSubmit" id="bth-search" class="btn btn-primary btn-sm">Фильтр
                        </button>
                    </div>
                </form:form>
                <table class="table table-hover" id="invFlows">
                    <thead>
                    <tr>
                        <th style="text-align: center; width: 20%">Подобъект</th>
                        <th style="text-align: center; width: 20%">Платёж</th>
                        <th style="text-align: center; width: 20%">Сумма</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${finalFlows}" var="flows">
                        <tr>
                            <td style="padding: 0">${flows.underFacilities.underFacility}</td>
                            <td style="padding: 0">${flows.payment}</td>
                            <c:choose>
                                <c:when test="${flows.summa >= 0}">
                                    <fmt:formatNumber var="percent" value="${(flows.summa / finalMaxSum)*100}"
                                                      maxFractionDigits="0"/>
                                    <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>

                                    <td style="background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ${middle}%,
                                            white ${percent}%); text-align: right; padding: 0">
                                        <fmt:setLocale value="ru-RU" scope="session"/>
                                        <fmt:formatNumber value="${flows.summa}" type="currency" minFractionDigits="2"/>
                                    </td>
                                </c:when>
                                <c:when test="${flows.summa < 0}">
                                    <fmt:formatNumber var="percent" value="${(flows.summa / finalMinSum)*100}"
                                                      maxFractionDigits="0"/>
                                    <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>

                                    <td style="background: linear-gradient(to left, white 50%, rgb(255, 0, 0) 50%, rgb(255, 210, 210) ${middle}%,
                                            white ${percent}%); text-align: right; padding: 0">
                                        <fmt:setLocale value="ru-RU" scope="session"/>
                                        <fmt:formatNumber value="${flows.summa}" type="currency" minFractionDigits="2"/>
                                    </td>
                                </c:when>
                            </c:choose>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td style="text-align: center; font-weight: bold; padding: 0">Итого:</td>
                        <td style="padding: 0"></td>
                        <td style="text-align: right; padding: 0">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${finalSumma}" type="currency" minFractionDigits="2"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="col-md-6"
             style="height: 50%; overflow-y: scroll; padding-right: 10%; padding-left: 10%; padding-top: 5%">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading"><span class="lead" style="font-weight: bold">Вложено:</span></div>
                <table class="table table-hover" id="investorsCash">
                    <thead>
                    <tr>
                        <th style="text-align: center;">Объект</th>
                        <th style="text-align: center;">Сумма</th>
                        <th style="text-align: center;">Подробности</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${invCash}" var="cash">
                        <tr>
                            <td style="padding: 0">${cash.facility.facility}</td>
                            <td style="padding: 0; text-align: right">
                                <fmt:setLocale value="ru-RU" scope="session"/>
                                <fmt:formatNumber value="${cash.givedCash}" type="currency" minFractionDigits="2"/>
                            </td>
                            <td style="padding: 0"><a id="goCash" name="${cash.facility.facility}"
                                                      href="<c:url value='/sumdetails' />"
                                                      class="btn btn-link custom-width" style="padding: 0">Подробно</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td style="text-align: center; font-weight: bold; padding: 0">Итого:</td>
                        <td style="text-align: right; padding: 0">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${summaCash}" type="currency" minFractionDigits="2"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </div>

    <div class="row">
        <div class="col-md-6"
             style="margin-bottom: 10px; height: 50%; overflow-y: scroll; padding-left: 10%; padding-right: 10%">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading"><span class="lead" style="font-weight: bold">Все потоки</span></div>
                <form:form modelAttribute="searchSummary" method="POST" class="form-inline" id="search-form">
                    <div class="row" style="margin: 10px;">
                        <label class="sr-only" for="years">Год:</label>
                        <form:select path="iYear" id="years" items="${years}" multiple="false"
                                     class="form-control input-sm"/>
                        <label class="sr-only" for="months">Месяц:</label>
                        <form:select path="iMonth" id="months" items="${months}" multiple="false"
                                     class="form-control input-sm"/>
                        <label class="sr-only" for="pays">Платёж:</label>
                        <form:select path="pay" id="pays" items="${pays}" multiple="false"
                                     class="form-control input-sm"/>
                        <label class="sr-only" for="underFacilities">Подобъект:</label>
                        <form:select path="underFacility" id="underFacilities" items="${uFacilities}" multiple="false"
                                     class="form-control input-sm"/>
                        <button type="submit" id="bth-search" class="btn btn-primary btn-sm">Фильтр</button>
                    </div>
                </form:form>
                <table class="table table-hover" id="allInvFlows">
                    <thead>
                    <tr>
                        <th style="text-align: center;">Подобъект</th>
                        <th style="text-align: center;">Платёж</th>
                        <th style="text-align: center;">Сумма</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${allMainFlows}" var="flows">
                        <tr>
                            <td style="padding: 0">${flows.underFacilities.underFacility}</td>
                            <td style="padding: 0">${flows.payment}</td>
                            <c:choose>
                                <c:when test="${flows.summa >= 0}">
                                    <fmt:formatNumber var="percent" value="${(flows.summa / maxSum)*100}"
                                                      maxFractionDigits="0"/>
                                    <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                                    <td style="background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ${middle}%,
                                            white ${percent}%); text-align: right; padding: 0">
                                        <fmt:setLocale value="ru-RU" scope="session"/>
                                        <fmt:formatNumber value="${flows.summa}" type="currency" minFractionDigits="2"/>
                                    </td>
                                </c:when>
                                <c:when test="${flows.summa < 0}">
                                    <fmt:formatNumber var="percent" value="${(flows.summa / minSum)*100}"
                                                      maxFractionDigits="0"/>
                                    <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                                    <td style="background: linear-gradient(to left, white 50%, rgb(255, 0, 0) 50%, rgb(255, 210, 210) ${middle}%,
                                            white ${percent}%); text-align: right; padding: 0">
                                        <fmt:setLocale value="ru-RU" scope="session"/>
                                        <fmt:formatNumber value="${flows.summa}" type="currency" minFractionDigits="2"/>
                                    </td>
                                </c:when>
                            </c:choose>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td style="text-align: center; font-weight: bold; padding: 0">Итого:</td>
                        <td style="padding: 0"></td>
                        <td style="text-align: right; padding: 0">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${summa}" type="currency" minFractionDigits="2"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="col-md-6"
             style="height: 50%; overflow-y: scroll; padding-right: 10%; padding-left: 10%; padding-top: 5%">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading"><span class="lead" style="font-weight: bold">Если объект продать, то Вы получите:</span>
                </div>
                <table class="table table-hover" id="totals">
                    <thead>
                    <tr>
                        <th style="text-align: center;">Объект</th>
                        <th style="text-align: center;">Сумма</th>
                        <th style="text-align: center;">Подробности</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr>
                        <td style="padding: 0">Тест</td>
                        <td style="padding: 0; text-align: right"><fmt:formatNumber value="1" type="currency"
                                                                                    minFractionDigits="2"/></td>
                        <td style="padding: 0"><a id="go" name="тест"
                                                  href="<c:url value='#' />" class="btn btn-link custom-width"
                                                  style="padding: 0">Подробно</a></td>
                    </tr>

                    <tr>
                        <td style="text-align: center; font-weight: bold; padding: 0">Итого:</td>
                        <td style="text-align: right; padding: 0">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="1" type="currency" minFractionDigits="2"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<%@include file="loader.jsp" %>

<div id="modal_form"><!-- Сaмo oкнo -->
    <span id="modal_close">X</span> <!-- Кнoпкa зaкрыть -->
    <table class="table table-hover" id="cash-details">
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