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

    </style>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/forToggleTables.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForFlows.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/forToggleTables.js' />"></script>
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
                <div class="panel-heading"><span class="lead" style="font-weight: bold"
                                                 id="monthAndYear">${monthAndYear}</span></div>

                <table class="table bordered" id="invPaysMonth">
                    <thead>
                    <tr>
                        <th style="padding: 0"></th>
                        <th style="text-align: center; width: 1px; overflow: visible">Сумма</th>
                        <th style="text-align: center; width: 20%">Подробно</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Вы заработали</td>
                        <c:choose>
                            <c:when test="${finalMaxSumMonth <= 0}">
                                <fmt:formatNumber var="percent" value="${(0 / 1)*100}" maxFractionDigits="0"/>
                                <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber var="percent" value="${(summaPaysMonth / finalMaxSumMonth)*100}"
                                                  maxFractionDigits="0"/>
                                <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                            </c:otherwise>
                        </c:choose>


                        <td style="background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ${middle}%,
                                white ${percent}%);" class="sum">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${summaPaysMonth}" type="currency" minFractionDigits="2"/>
                        </td>
                        <td><a id="goPays" name="monthPays" href="<c:url value='/#' />"
                               class="btn btn-link custom-width">Подробно</a></td>
                    </tr>
                    <tr>
                        <td>Аренда</td>
                        <c:choose>
                            <c:when test="${finalMaxSumMonth <= 0}">
                                <fmt:formatNumber var="percent" value="${(0 / 1)*100}" maxFractionDigits="0"/>
                                <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber var="percent" value="${(summaArendiMonth / finalMaxSumMonth)*100}"
                                                  maxFractionDigits="0"/>
                                <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                            </c:otherwise>
                        </c:choose>

                        <td style="background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ${middle}%,
                                white ${percent}%);" class="sum">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${summaArendiMonth}" type="currency" minFractionDigits="2"/>
                        </td>
                        <td><a id="goArendaMonth" name="monthArenda" href="<c:url value='/#' />"
                               class="btn btn-link custom-width">Подробно</a></td>
                    </tr>
                    <tr>
                        <td>Расходы</td>
                        <td style="background: linear-gradient(to left, white 50%, rgb(255, 0, 0) 50%, rgb(255, 210, 210) 100%,
                                white 100%);" class="sum">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${summaRashodiMonth}" type="currency" minFractionDigits="2"/>
                        </td>
                        <td><a id="goRashodiMonth" name="monthRashodi" href="<c:url value='/#' />"
                               class="btn btn-link custom-width">Подробно</a></td>
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
                <table class="table bordered" id="investorsCash">
                    <thead>
                    <tr>
                        <th style="text-align: center;">Объект</th>
                        <th style="text-align: center; width: 1px; overflow: visible">Сумма</th>
                        <th style="text-align: center;">Подробно</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${invCash}" var="cash">
                        <tr>
                            <td>${cash.facility.facility}</td>
                            <td class="sum">
                                <fmt:setLocale value="ru-RU" scope="session"/>
                                <fmt:formatNumber value="${cash.givedCash}" type="currency" minFractionDigits="2"/>
                            </td>
                            <td><a id="goCash" name="${cash.facility.facility}"
                                   href="<c:url value='/sumdetails' />" class="btn btn-link custom-width">Подробно</a>
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

                <table class="table table-hover" id="invPaysAll">
                    <thead>
                    <tr>
                        <th style="padding: 0"></th>
                        <th style="text-align: center; width: 1px; overflow: visible">Сумма</th>
                        <th style="text-align: center; width: 30%">Подробно</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Вы заработали</td>
                        <fmt:formatNumber var="percent" value="${(summaPays / finalMaxSumAll)*100}"
                                          maxFractionDigits="0"/>
                        <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                        <td style="background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ${middle}%,
                                white ${percent}%); text-align: right; padding: 0">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${summaPays}" type="currency" minFractionDigits="2"/>
                        </td>
                        <td><a id="goPaysAll" name="allPays" href="<c:url value='/#' />"
                               class="btn btn-link custom-width">Подробно</a></td>
                    </tr>
                    <tr>
                        <td>Аренда</td>
                        <fmt:formatNumber var="percent" value="${(summaArendiAll / finalMaxSumAll)*100}"
                                          maxFractionDigits="0"/>
                        <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                        <td style="background: linear-gradient(to right, white 50%, rgb(100, 195, 132) 50%, rgb(229, 245, 234) ${middle}%,
                                white ${percent}%); text-align: right; padding: 0">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${summaArendiAll}" type="currency" minFractionDigits="2"/>
                        </td>
                        <td><a id="goArendaAll" name="allArenda" href="<c:url value='/#' />"
                               class="btn btn-link custom-width">Подробно</a></td>
                    </tr>
                    <tr>
                        <td>Расходы</td>
                        <fmt:formatNumber var="percent" value="${(summaArendiAll / finalMaxSumAll)*100}"
                                          maxFractionDigits="0"/>
                        <fmt:formatNumber var="middle" value="${(percent + 100)/2}" maxFractionDigits="0"/>
                        <td style="background: linear-gradient(to left, white 50%, rgb(255, 0, 0) 50%, rgb(255, 210, 210) ${middle}%,
                                white ${percent}%); text-align: right; padding: 0">
                            <fmt:setLocale value="ru-RU" scope="session"/>
                            <fmt:formatNumber value="${summaRashodiAll}" type="currency" minFractionDigits="2"/>
                        </td>
                        <td><a id="goRashodiAll" name="allRashodi" href="<c:url value='/#' />"
                               class="btn btn-link custom-width">Подробно</a></td>
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
                        <th style="text-align: center; width: 1px; overflow: visible">Сумма</th>
                        <th style="text-align: center;">Подробно</th>
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
<%@include file="bootstrapModal.jsp" %>
<div id="modal_form"><!-- Сaмo oкнo -->
    <span id="modal_close">X</span> <!-- Кнoпкa зaкрыть -->
    <table id="cash-details" style="margin: 50px auto; width: 100%">

    </table>
</div>
<div id="overlay"></div><!-- Пoдлoжкa -->
<%@include file="slideDiv.jsp" %>
</body>
</html>