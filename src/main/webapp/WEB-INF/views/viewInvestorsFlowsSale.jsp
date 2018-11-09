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
    <title>Выплаты инвесторам (продажа)</title>
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
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForFlowsSale.js' />"></script>
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
        <div class="panel-heading"><span class="lead">Выплаты инвесторам (продажа)</span></div>
        <form:form modelAttribute="searchSummary" method="POST" class="form-inline" id="filter-form">
            <div class="row" style="margin: 10px;">
                <label class="sr-only" for="fFacilities">Объект:</label>
                <form:select path="facility" id="fFacilities" items="${facilities}" multiple="false"
                             itemValue="id" itemLabel="facility" class="form-control input-sm"/>
                <label class="sr-only" for="uFacilities">Подобъект:</label>
                <form:select path="underFacility" id="uFacilities" multiple="false" class="form-control input-sm">
                    <c:forEach var="uf" items="${underFacilities}">
                        <form:option value="${uf.underFacility}" id="${uf.id}" data-parent-id="${uf.facilityId}">

                        </form:option>
                    </c:forEach>
                </form:select>
                <label class="sr-only" for="investors">Инвестор:</label>
                <form:select path="investor" id="investors" items="${investors}" multiple="false"
                             itemValue="id" itemLabel="login" class="form-control input-sm"/>
                <label for="beginPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">Период с:</label>
                <input id="beginPeriod" name="dateStart" type="date" class="form-control input-sm" value="">
                <label for="endPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">по:</label>
                <input id="endPeriod" name="dateEnd" type="date" class="form-control input-sm" value=""
                       style="margin-right:5px">
                <button type="submit" id="bth-search" class="btn btn-primary btn-sm">Фильтр</button>
            </div>
        </form:form>
        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasRole('ADMIN')">
                <form:form method="POST" modelAttribute="fileBucket" enctype="multipart/form-data" class="form-inline"
                           id="invFlows">
                    <div class="row" style="margin: 10px;">
                        <label class="sr-only" for="file">Загрузить файл</label>
                        <form:input cssStyle="width: 40%" type="file" path="file" id="file"
                                    class="form-control input-sm col-sm-8"/>
                        <div class="has-error">
                            <form:errors path="file" class="help-inline"/>
                        </div>
                        <input type="submit" value="Загрузить" class="btn btn-primary btn-sm" style="margin-left: 10px"
                               id="loadInvFlowsSaleAjax">
                        <a href="<c:url value='/deleteFlowsSale' />" class="btn btn-danger btn-sm"
                           style="margin-left: 10px">Удалить</a>
                        <div class="btn btn-primary btn-sm pull-right" id="checkAll">
                            <label class="checkbox-inline">
                                <input type="checkbox" id="checkIt" value="">Выделить всё</label>
                        </div>

                        <div class="dropdown pull-right" style="margin-right: 10px">
                            <button id="actions" type="button" data-toggle="dropdown"
                                    class="btn btn-success btn-sm dropdown-toggle pull-right">Действия <span
                                    class="glyphicon glyphicon-th-list"></span></button>
                            <ul class="dropdown-menu" id="reinvest">
                                <li id="reinvestAll"><a href="/#">Массовое реинвестирование</a></li>
                                <li id="deleteAll"><a href="/#">Удалить выбранные суммы</a></li>
                            </ul>
                        </div>

                    </div>
                </form:form>
            </sec:authorize>
        </sec:authorize>

        <table class="table table-hover" style="font-size: small" id="invFlowsSale">
            <thead>
            <tr>
                <th>Объект</th>
                <th>Инвестор</th>
                <th>Вид доли</th>
                <th>Вложено в объект</th>
                <th>Дата вложения</th>
                <th>Доля инвестора</th>
                <th>Вложено в подобъект</th>
                <th>Сколько прибыли на вывод (авто)</th>
                <th>Сколько прибыли на вывод (вручную)</th>
                <th>Сколько прибыли реинвест</th>
                <th>Подобъект</th>
                <th>Дата продажи</th>
                <th>Реинвестировать</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${investorsFlowsSale}" var="flows">
                <tr id="${flows.id}">
                    <td data-facility-id="${flows.facility.id}">${flows.facility.facility}</td>
                    <td data-investor-id="${flows.investor.id}">${flows.investor.login}</td>
                    <td data-share-kind-id="${flows.shareKind.id}">${flows.shareKind.shareKind}</td>
                    <td data-cash-facility="${flows.cashInFacility}">
                        <fmt:setLocale value="ru-RU" scope="session"/>
                        <fmt:formatNumber value="${flows.cashInFacility}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td data-date-gived="${flows.dateGived.time}">${flows.dateGivedToLocalDate}</td>
                    <td>
                        <fmt:formatNumber value="${flows.investorShare}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.cashInUnderFacility}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.profitToCashingAuto}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.profitToCashingMain}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td data-gived-cash="${flows.profitToReInvest}">
                        <fmt:formatNumber value="${flows.profitToReInvest}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td data-under-facility-id="${flows.underFacility.id}">${flows.underFacility.underFacility}</td>
                    <td data-date-sale="${flows.dateSale.time}">${flows.dateSaleToLocalDate}</td>
                    <td>
                        <c:choose>
                            <c:when test="${flows.isReinvest == 1}">
                                <c:set var="checked" value="checked"/>
                                <c:set var="disabled" value="disabled"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="checked" value=""/>
                                <c:set var="disabled" value=""/>
                            </c:otherwise>
                        </c:choose>
                        <input type="checkbox" title="Выбрать" ${checked} ${disabled}/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="loader.jsp" %>
<%@include file="popup.jsp" %>

<div id="reInvestModal" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 90%">
        <div class="modal-content" id="content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4></h4>
            </div>
            <div class="modal-body">
                <form:form method="POST" modelAttribute="searchSummary" class="form-horizontal" id="reInvestData">
                    <div class="row">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="dateGiv">Дата вложения:</label>
                            <div class="col-md-7">
                                <form:input type="date" path="period" id="dateGiv" class="form-control input-sm"/>
                                <div id="dateRepErr" style="color: red; display: none">Необходимо выбрать дату
                                    вложения
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="facilitiesRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="srcFacilities">Объект:</label>
                            <div class="col-md-7">
                                <form:select path="reFacility" id="srcFacilities" items="${facilities}" multiple="false"
                                             itemValue="id" itemLabel="facility" class="form-control input-sm"/>
                                <div id="facilityErr" style="color: red; display: none">Необходимо выбрать объект</div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="underFacilitiesRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="srcUnderFacilities">Подобъект:</label>
                            <div class="col-md-7">
                                <form:select path="reUnderFacility" id="srcUnderFacilities" items="${underFacilities}" multiple="false"
                                             itemValue="id" itemLabel="underFacility" class="form-control input-sm"/>
                                <div id="reFacilityErr" style="color: red; display: none">Необходимо выбрать подобъект</div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="invTypeRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="invType">Вид инвестора:</label>
                            <div class="col-md-7">
                                <form:select path="invType" id="invType" items="${investorsTypes}" multiple="false"
                                             itemValue="id" itemLabel="investorsType" class="form-control input-sm"/>
                                <div id="invTypeErr" style="color: red; display: none">Необходимо выбрать вид
                                    инвестора
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="shareKindNameRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="shareKindName">Вид доли:</label>
                            <div class="col-md-7">
                                <form:select path="shareKind" id="shareKindName" items="${shareKinds}" multiple="false"
                                             itemValue="id" itemLabel="shareKind" class="form-control input-sm"/>
                                <div id="shareKindErr" style="color: red; display: none">Необходимо выбрать вид доли
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-actions floatRight">
                            <input type="submit" value="Реинвестировать" class="btn btn-primary btn-sm"/> или <a
                                href="<c:url value='/#' />" id="cancelReinvest">Отмена</a>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<div id="msg-modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-body" id="msg" style="text-align: center"></div>
        </div>
    </div>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>