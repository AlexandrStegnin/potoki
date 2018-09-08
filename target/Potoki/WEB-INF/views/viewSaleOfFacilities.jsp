<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Потоки инвесторов. Выплаты</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForSaleOfFacilities.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
    <style type="text/css">
        table, td, th {
            text-align: center;
        }

        #msg-modal .modal-dialog {
            -webkit-transform: translate(0, -50%);
            -o-transform: translate(0, -50%);
            transform: translate(0, -50%);
            top: 50%;
            margin: 0 auto;
        }

        .dropdown-menu > .active > a {
            color: #0c0c0c !important;
            background-color: transparent !important;
        }

        .dropdown-menu > .active > a:hover {
            text-shadow: 1px 1px 1px gray;
        }

    </style>
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Продажа объектов:</span></div>
        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasRole('ADMIN')">
                <form:form method="POST" modelAttribute="fileBucket" enctype="multipart/form-data" class="form-inline"
                           id="saleOfFacilities">
                    <div class="row" style="margin: 10px;">
                        <label class="sr-only" for="file">Загрузить файл</label>
                        <form:input cssStyle="width: 40%" type="file" path="file" id="file"
                                    class="form-control input-sm col-sm-8"/>
                        <div class="has-error">
                            <form:errors path="file" class="help-inline"/>
                        </div>
                        <input type="submit" value="Загрузить" class="btn btn-primary btn-sm" style="margin-left: 10px"
                               id="loadSaleOfFacilitiesAjax">
                        <a href="<c:url value='/deleteSaleOfFacilities' />" id="deleteSale"
                           class="btn btn-danger btn-sm" style="margin-left: 10px">Удалить</a>
                    </div>
                </form:form>
            </sec:authorize>
        </sec:authorize>

        <table class="table table-hover" style="font-size: small" id="saleOfFacilities">
            <thead>
            <tr>
                <th>Объект</th>
                <th>Инвестор</th>
                <th>Вложено в объект</th>
                <th>Доля инвестора</th>
                <th>Ареда в годовых</th>
                <th>Прибыль с аренды</th>
                <th>Прибыль с продажи (до налог. и вывода)</th>
                <th>Прибыль с продажи в годовых (от даты вложения)</th>
                <th>Чистая прибыль с продажи</th>
                <th>Чистая прибыль с продажи + Аренда</th>
                <th>Доходность общая</th>
                <th>Прирост капитала</th>
                <th>Капитал (в случае накопления аренды)</th>
                <th>Остаток капитала</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${saleOfFacilities}" var="sales">
                <tr id="${sales.id}">
                    <td data-facility-id="${sales.facility.id}">${sales.facility.facility}</td>
                    <td data-investor="${sales.investor}">${sales.investor}</td>
                    <td>
                        <fmt:setLocale value="ru-RU" scope="session"/>
                        <fmt:formatNumber value="${sales.cashInFacility}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.shareInvestor}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.leaseInYear}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.profitFromLease}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.profitFromSale}" type="currency" minFractionDigits="2"/>
                    </td>

                    <td>
                        <fmt:formatNumber value="${sales.profitFromSaleInYear}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.netProfitFromSale}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.netProfitFromSalePlusLease}" type="currency"
                                          minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.totalYield}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.capitalGains}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.capital}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${sales.balanceOfCapital}" type="currency" minFractionDigits="2"/>
                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="loader.jsp" %>
<%@include file="slideDiv.jsp" %>
<%@include file="popup.jsp" %>
</body>
</html>