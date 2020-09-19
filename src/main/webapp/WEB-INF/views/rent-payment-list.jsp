<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Выплаты инвесторам (аренда)</title>
    <sec:csrfMetaTags/>
    <link rel="stylesheet"
          href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css' />">
    <link href="<c:url value='https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/css/bootstrap4-toggle.min.css' />"
          rel="stylesheet">
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <style type="text/css">
        .bootstrap-select > select {
            margin: 10px !important;
        }
    </style>
</head>

<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="row" style="margin: 10px;">
        <div class="col-md-12">
        <form:form modelAttribute="searchSummary" method="GET" action="payments/rent"
                   class="form-inline d-flex flex-row justify-content-center"
                   id="filter-form">
            <div style="padding: 5px;">
                <form:select path="facilityStr" id="fFacilities" multiple="false" class="selectpicker">
                    <c:forEach var="f" items="${facilitiesList}">
                        <option
                                <c:choose>
                                    <c:when test="${f.name eq 'Выберите объект'}">selected="selected"</c:when>
                                    <c:when test="${f.name eq searchSummary.facilityStr}">selected="selected"</c:when>
                                </c:choose>
                                value="${f.id}" id="${f.id}">${f.name}
                        </option>
                    </c:forEach>
                </form:select>
            </div>
            <div style="padding: 5px;">
                <form:select path="underFacility" id="uFacilities" multiple="false" class="selectpicker">
                    <c:forEach var="uf" items="${underFacilities}">
                        <option
                                <c:choose>
                                    <c:when test="${uf.name eq 'Выберите подобъект'}">selected="selected"</c:when>
                                    <c:when test="${uf.name eq searchSummary.underFacility}">selected="selected"</c:when>
                                </c:choose>
                                value="${uf.name}" id="${uf.id}" data-parent-id="${uf.facility.id}">${uf.name}
                        </option>
                    </c:forEach>
                </form:select>
            </div>
            <div style="padding: 5px;">
                <form:select path="investor" id="investors" multiple="false" class="selectpicker">
                    <c:forEach var="inv" items="${investors}">
                        <option
                                <c:choose>
                                    <c:when test="${inv.login eq 'Выберите инвестора'}">selected="selected"</c:when>
                                    <c:when test="${inv.login eq searchSummary.investor}">selected="selected"</c:when>
                                </c:choose> value="${inv.login}" id="${inv.id}">${inv.login}
                        </option>
                    </c:forEach>
                </form:select>
            </div>
                <form:label path="" for="beginPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">Период с:</form:label>
                <form:input path="startDate" id="beginPeriod" name="startDate" type="date" class="form-control input-sm" value="${searchSummary.startDate}" />
                <form:label path="" for="endPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">по:</form:label>
                <form:input path="endDate" id="endPeriod" name="endDate" type="date" class="form-control input-sm" value="${searchSummary.endDate}"
                       style="margin-right:5px" />
                <button type="submit" id="bth-search" class="btn btn-primary btn-sm">Фильтр</button>
<%--                <button type="submit" id="bth-clear" class="btn btn-danger btn-sm">Сбросить фильтры</button>--%>
<%--                <button data-table-id="invFlows" type="button" id="unblock_operations" class="btn btn-danger btn-sm">Разблокировать операции</button>--%>
            </div>

        <div class="d-flex flex-row justify-content-center">
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
                               id="loadInvFlowsAjax">
                        <a href="<c:url value='/deleteFlows' />" class="btn btn-danger btn-sm"
                           style="margin-left: 10px">Удалить</a>
<%--                        <div class="btn btn-primary btn-sm pull-right" id="checkAll">--%>
<%--                            <label class="checkbox-inline">--%>
<%--                                <input type="checkbox" id="checkIt" value="">Выделить всё</label>--%>
<%--                        </div>--%>
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
        </div>

        <nav class="text-center" aria-label="Деньги инвесторов">
            <ul class="pagination pagination-sm justify-content-center">
                <c:forEach begin="1" end="${pageCount}" varStatus="page">
                    <c:set var="link" value="/paysToInv?page=${page.index - 1}&size=100${queryParams}" />
                    <li class="page-item" data-page="${page.index}">
                        <a id="page_${page.index}" class="page-link"
                           href="<c:url value='${link}' />">${page.index}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
        </form:form>
        </div>
    </div>
</div>
        <table class="table table-hover" style="font-size: small" id="invFlows">
            <thead>
            <tr>
                <th>Дата</th>
                <th>Объект</th>
                <th>Подобъект</th>
                <th>Помещение</th>
                <th>Инвестор</th>
                <th>Вид доли</th>
                <th>Сумма в объекте</th>
                <th>Сумма в подобъекте</th>
                <th>Доля для сводной</th>
                <th>Доля</th>
                <th>Налог</th>
                <th>Вывод денег</th>
                <th>Сумма за месяц</th>
                <th>По инвестору</th>
                <th>После налогов</th>
                <th>После расходов по пустому помещению</th>
                <th>После вывода</th>
                <th style="width: 30px"><input type="checkbox" id="checkIt" value=""></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${rentPayments}" var="flows">
                <tr id="${flows.id}">
                    <td data-report-date="${flows.reportDate.time}">${flows.getReportDateToLocalDate()}</td>
                    <td data-facility-id="${flows.facility.id}">${flows.facility.name}</td>
                    <td data-under-facility-id="${flows.underFacility.id}">${flows.underFacility.name}</td>
                    <td data-room-id="${flows.room.id}">${flows.room.name}</td>
                    <td data-investor-id="${flows.investor.id}">${flows.investor.login}</td>
                    <td>${flows.shareKind}</td>
                    <td data-gived-cash="${flows.givedCash}">
                        <fmt:setLocale value="ru-RU" scope="session"/>
                        <fmt:formatNumber value="${flows.givedCash}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.sumInUnderFacility}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.shareForSvod}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.share}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.taxation}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.cashing}" type="percent" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.summa}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.onInvestors}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.afterTax}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${flows.afterDeductionEmptyFacility}" type="currency"
                                          minFractionDigits="2"/>
                    </td>
                    <td data-gived-cash="${flows.afterCashing}">
                        <fmt:formatNumber value="${flows.afterCashing}" type="currency" minFractionDigits="2"/>
                    </td>
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
                                <form:select path="reFacility" id="srcFacilities" items="${facilitiesList}"
                                             multiple="false"
                                             itemValue="id" itemLabel="name" class="form-control input-sm"/>
                                <div id="facilityErr" style="color: red; display: none">Необходимо выбрать объект</div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="shareKindNameRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="shareKindName">Вид доли:</label>
                            <div class="col-md-7">
                                <form:select path="shareKind" id="shareKindName" items="${shareTypes}" multiple="false"
                                             itemValue="id" itemLabel="title" class="form-control input-sm"/>
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

<%@include file="popup_modal.jsp" %>
<%@include file="ddk_loader.jsp" %>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js' />"></script>
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
<script src="<c:url value='https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/progress.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/progress.js' />"></script>
</body>
</html>
