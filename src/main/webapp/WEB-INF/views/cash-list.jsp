<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Список дненег инвесторов</title>
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
            <form:form modelAttribute="cashFilters" method="POST" action="/investor-cash/list"
                       class="form-inline  d-flex flex-row justify-content-center"
                       id="search-form">
            <input type="hidden" id="pageNumber" name="pageNumber" value="0">
            <input type="hidden" id="pageSize" name="pageSize" value="${cashFilters.pageSize}">
            <input type="hidden" id="total" name="total" value="${page.content.size()}">
            <input type="hidden" id="filtered" name="filtered" value="${cashFilters.filtered}">
            <div style="padding: 5px">
                <form:select path="facilities" id="fFacilities" multiple="true" class="selectpicker"
                             data-live-search="true" title="Выберите объект..."
                             data-actions-box="true" data-select-all-text="Выбрать всё"
                             data-deselect-all-text="Очистить">
                    <form:options items="${facilitiesList}" itemValue="id" itemLabel="name"/>
                </form:select>
            </div>
            <div style="padding: 5px">
                <form:select path="underFacilities" id="uFacilities" multiple="true" class="selectpicker"
                             data-live-search="true" title="Выберите подобъект..."
                             data-actions-box="true" data-select-all-text="Выбрать всё"
                             data-deselect-all-text="Очистить">
                    <c:forEach var="uf" items="${underFacilitiesList}">
                        <option
                                <c:forEach var="filterUnderFacility" items="${cashFilters.underFacilities}">
                                    <c:choose>
                                        <c:when test="${uf.name eq filterUnderFacility}">selected="selected"</c:when>
                                    </c:choose>
                                </c:forEach>
                                value="${uf.name}" id="${uf.id}"
                                data-parent-id="${uf.facility.id}">${uf.name}
                        </option>
                    </c:forEach>
                </form:select>
            </div>
            <div style="padding: 5px">
                <form:select path="investors" id="investors" class="selectpicker" data-container="body"
                             title="Выберите инвестора..." multiple="true"
                             data-live-search="true" data-size="7"
                             data-actions-box="true" data-select-all-text="Выбрать всё"
                             data-deselect-all-text="Очистить">
                    <c:forEach var="inv" items="${investors}">
                        <c:if test="${inv.login ne 'Выберите инвестора'}">
                            <option
                                    <c:forEach var="investor" items="${cashFilters.investors}">
                                        <c:choose>
                                            <c:when test="${inv.login eq investor}">selected="selected"</c:when>
                                        </c:choose>
                                    </c:forEach>
                                    value="${inv.login}" id="${inv.id}"
                                    data-lastName="${inv.profile.lastName}">${inv.login}
                            </option>
                        </c:if>
                    </c:forEach>
                </form:select>
            </div>
            <label for="beginPeriod" style="margin-left:5px; margin-right:5px; font-size:14px">Период с:</label>
            <input id="beginPeriod" name="fromDate" type="date" class="form-control input-sm" value="">
            <label for="endPeriod" style="margin-left:5px; margin-right:5px; font-size:14px">по:</label>
            <input id="endPeriod" name="toDate" type="date" class="form-control input-sm" value=""
                   style="margin-right:5px">
                <%--                <input type="checkbox" checked data-toggle="toggle"--%>
                <%--                       data-on="Все" data-off="Из 1С" data-onstyle="success" data-offstyle="danger"--%>
                <%--                       data-size="input-sm">--%>
            <form:select path="fromApi" class="selectpicker" data-width="130px">
                <form:option value="true" label="Из 1С"/>
                <form:option value="false" label="Остальные"/>
            </form:select>
            <input id="all" name="allRows" type="checkbox"
            <c:if test="${cashFilters.allRows == true}"> checked="checked" </c:if> data-toggle="toggle"
                   data-on="На одной" data-off="По страницам" data-onstyle="success" data-offstyle="danger"
                   data-size="input-sm">

            <button type="submit" id="bth-search" class="btn btn-primary input-sm" style="margin-left: 10px">Фильтр
            </button>
        </div>
    </div>
    <div class="d-flex flex-row justify-content-center" style="margin: 0 10px 10px 10px">
        <div class="p-2">
            <button type="submit" id="bth-clear" class="btn btn-danger input-sm">Сбросить фильтры</button>
        </div>
        <div class="p-2">
            <button data-table-id="investorCash" type="button" id="unblock_operations"
                    class="btn btn-danger input-sm">Разблокировать операции
            </button>
        </div>
        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasRole('ADMIN')">
                <div class="p-2">
                    <div class="dropdown pull-right" style="margin-right: 10px">
                        <button id="addActions" type="button" data-toggle="dropdown"
                                class="btn btn-warning input-sm dropdown-toggle pull-right">Деньги <span
                                class="fas fa-list"></span></button>
                        <div class="dropdown-menu dropdown-menu-right" id="addGetCash">
                            <a class="dropdown-item" id="addCash" href="<c:url value='/investor-cash/create' />">Добавить
                                деньги</a>
                            <a class="dropdown-item" id="getCash" href="<c:url value='/getInvestorsCash' />">Вывести
                                деньги</a>
                        </div>
                    </div>
                </div>
                <div class="p-2">
                    <div class="dropdown pull-right" style="margin-right: 10px">
                        <button id="actions" type="button" data-toggle="dropdown"
                                class="btn btn-success input-sm dropdown-toggle pull-right">Действия <span
                                class="fas fa-list"></span></button>
                        <div class="dropdown-menu" id="reinvest">
                            <a class="dropdown-item" id="reinvestAll" href="#">Массовое реинвестирование</a>
                            <a class="dropdown-item" id="divideAll" href="#">Массовое разделение сумм</a>
                            <a class="dropdown-item" id="closeAll" href="#">Закрыть выбранные суммы</a>
                            <a class="dropdown-item" id="deleteAll" href="#" style="color: red">Удалить выбранные суммы</a>
                        </div>
                    </div>
                </div>
            </sec:authorize>
        </sec:authorize>
    </div>
    <c:if test="${cashFilters.allRows == false}">
        <nav class="text-center" aria-label="Деньги инвесторов">
            <ul class="pagination pagination-sm justify-content-center">
                <c:forEach begin="1" end="${page.totalPages}" varStatus="page">
                    <li class="page-item" data-page="${page.index}">
                        <a id="${page.index}" name="page_${page.index}" class="page-link"
                           href="#">${page.index}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>
    </form:form>
</div>
<div class="container-fluid">
    <table class="table table-striped w-auto table-hover table-sm" style="font-size: smaller; table-layout: fixed"
           id="investorsCash">
        <thead style="text-align: center">
        <tr>
            <th>Объект</th>
            <th>Подобъект</th>
            <th>Инвестор</th>
            <th>Переданная сумма</th>
            <th>Дата передачи денег</th>
            <th>Источник денег</th>
            <th>Детали новых денег</th>
            <th>Дата закрытия вложения</th>
            <th>Вид закрытия вложения</th>
            <th>Из 1С</th>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th style="text-align: center">Действие</th>
            </sec:authorize>
            <th style="width: 30px"><input type="checkbox" id="checkIt" value=""></th>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${page.content}" var="cash">
            <tr id="${cash.id}">
                <td data-facility-id="${cash.facility.id}">${cash.facility.name}</td>
                <td data-under-facility-id="${cash.underFacility.id}">${cash.underFacility.name}</td>
                <td data-investor-id="${cash.investor.id}">${cash.investor.login}</td>

                <td data-gived-cash="${cash.givenCash}">
                    <fmt:setLocale value="ru-RU" scope="session"/>
                    <fmt:formatNumber value="${cash.givenCash}" type="currency" minFractionDigits="2"/>
                </td>
                <td data-report-date="${cash.dateGiven.time}">${cash.getDateGivenToLocalDate()}</td>
                <td data-cash-source-id="${cash.cashSource.id}">${cash.cashSource.name}</td>
                <td data-cash-details-id="${cash.newCashDetail.id}">${cash.newCashDetail.name}</td>
                <td data-date-closing="${cash.dateClosing.time}">${cash.getDateClosingToLocalDate()}</td>
                <td data-type-closing-id="${cash.typeClosing.id}">${cash.typeClosing.name}</td>
                <c:set var="is1C" value="Да"/>
                <c:if test="${empty cash.transactionUUID}">
                    <c:set var="is1C" value="Нет"/>
                </c:if>
                <td>${is1C}</td>
                <c:choose>
                    <c:when test="${cash.typeClosing == null}">
                        <c:set var="isDisabledClass" value="isEnabled"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="isDisabledClass" value="isDisabled"/>
                    </c:otherwise>
                </c:choose>

                <sec:authorize access="isFullyAuthenticated()">
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td style="text-align: center">
                            <div class="dropdown pull-right" style="margin-right: 10px">
                                <button type="button" data-toggle="dropdown"
                                        class="btn btn-success btn-sm dropdown-toggle pull-right"><span
                                        class="fas fa-cog"></span></button>
                                <div class="dropdown-menu">
                                    <a class="dropdown-item" id="aEdit" href="<c:url value='/edit-cash-${cash.id}' />">Изменить</a>
                                    <a class="dropdown-item" id="aDivide" href="<c:url value='/double-cash-${cash.id}' />">Разделить</a>
                                    <a class="dropdown-item" id="aDouble" href="<c:url value='/close-cash-${cash.id}' />">Закрыть</a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item" id="del" data-delete="${cash.id}" href="<c:url value='/#' />" style="color: #ff0000;">Удалить</a>
                                </div>
                            </div>
                        </td>
                    </sec:authorize>
                </sec:authorize>
                <td>
                    <c:choose>
                        <c:when test="${cash.isReinvest == 1}">
                            <c:set var="checked" value="checked "/>
                            <c:set var="disabled" value="disabled "/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="checked" value=""/>
                            <c:set var="disabled" value=""/>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${cash.typeClosing.name.length() > 0 &&
                                            (cash.givenCash > 0 || cash.givenCash < 0)}">
                            <c:set var="enabled" value="disabled "/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="enabled" value=""/>
                        </c:otherwise>
                    </c:choose>
                    <input type="checkbox" title="Выбрать" ${checked} ${disabled} ${enabled}/>
                </td>
                <td hidden data-source="${cash.source}"></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div id="all-modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 90%">
        <div class="modal-content" id="content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4></h4>
            </div>
            <div class="modal-body">
                <form:form method="POST" modelAttribute="searchSummary" class="form-horizontal" id="save-cash">
                        <div class="form-group row" id="dateClosingRow">
                            <label class="col-md-3 col-form-label" for="dateClose">Дата закрытия:</label>
                            <div class="col-md-9">
                                <form:input type="date" path="dateClose" id="dateClose" class="form-control input-sm"/>
                                <div id="dateCloseErr" style="color: red; display: none">Необходимо выбрать дату
                                    вложения
                                </div>
                            </div>
                        </div>
                        <div class="form-group row" id="facilitiesRow">
                            <label class="col-md-3 col-form-label" for="srcFacilities">Объект:</label>
                            <div class="col-md-9">
                                <form:select path="reFacility" id="srcFacilities" items="${facilities}" multiple="false"
                                             itemValue="id" itemLabel="name" class="selectpicker form-control input-sm"
                                             data-live-search="true"/>
                                <div id="facilityErr" style="color: red; display: none">Необходимо выбрать объект</div>
                            </div>
                        </div>
                        <div class="form-group row" id="underFacilityRow">
                            <label class="col-md-3 col-form-label" for="srcUnderFacilities">Подобъект:</label>
                            <div class="col-md-9">
                                <form:select path="reUnderFacility" id="srcUnderFacilities" items="${underFacilities}"
                                             multiple="true"
                                             data-none-selected-text="Без подобъекта"
                                             itemValue="id" itemLabel="name"
                                             class="form-control input-sm selectpicker"/>
                                <div id="underFacilityErr" style="color: red; display: none">Необходимо выбрать
                                    подобъект
                                </div>
                            </div>
                        </div>
                        <div class="form-group row" id="shareTypeNameRow">
                            <label class="col-md-3 col-form-label" for="shareTypeName">Вид доли:</label>
                            <div class="col-md-9">
                                <form:select path="shareType" id="shareTypeName" items="${shareTypes}" multiple="false"
                                             itemValue="id" itemLabel="title" class="form-control input-sm"/>
                                <div id="shareTypeErr" style="color: red; display: none">Необходимо выбрать вид доли
                                </div>
                            </div>
                        </div>
                        <div class="form-group row" id="underFacilitiesListRow">
                            <label class="col-md-3 col-form-label" for="underFacilitiesList">Из каких подобъектов состоит
                                остаток:</label>
                            <div class="col-md-9">
                                <form:select path="underFacilityList" id="underFacilitiesList"
                                             items="${underFacilitiesList}"
                                             itemValue="id" itemLabel="name" class="form-control input-sm selectpicker"
                                             title="Выберите подобъекты..." multiple="true"
                                             data-live-search="true" data-size="7"
                                             data-actions-box="true" data-select-all-text="Выбрать всё"
                                             data-deselect-all-text="Очистить"/>
                                <div id="underFacilityErr" style="color: red; display: none">Необходимо выбрать из каких
                                    подобъектов состоит остаток
                                </div>
                            </div>
                        </div>
                        <div class="form-group row" id="real-date">
                            <label class="col-md-3 col-form-label" for="realDateGiven">Дата реальной передачи
                                денег:</label>
                            <div class="col-md-9">
                                <form:input type="date" path="realDateGiven" id="realDateGiven"
                                            class="form-control input-sm"/>
                                <div id="dateCloseErr" style="color: red; display: none">Необходимо выбрать дату
                                    передачи денег
                                </div>
                            </div>
                        </div>
                        <div class="form-group row" id="typeClosingRow">
                            <label class="col-md-3 col-form-label" for="typeClosing">Вид закрытия:</label>
                            <div class="col-md-9">
                                <form:select path="typeClosingInvest" id="typeClosing" items="${typeClosingInvest}"
                                             multiple="false"
                                             itemValue="id" itemLabel="name"
                                             class="form-control input-sm selectpicker"/>
                                <div id="typeClosingErr" style="color: red; display: none">Необходимо выбрать вид
                                    закрытия
                                </div>
                            </div>
                        </div>
                        <div class="form-group row" id="buyerRow">
                            <label class="col-md-3 col-form-label" for="buyer">Инвестор покупатель:</label>
                            <div class="col-md-9">
                                <form:select path="user" id="buyer" items="${investors}" multiple="false"
                                             itemValue="id" itemLabel="login" class="form-control input-sm selectpicker"
                                data-live-search="true"/>
                                <div id="buyerErr" style="color: red; display: none">Необходимо выбрать инвестора
                                    покупателя
                                </div>
                            </div>
                        </div>
                </form:form>
            </div>
            <div class="modal-footer">
                <input type="submit" class="btn btn-primary" data-action="" id="action" />
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
            </div>
        </div>
    </div>
</div>

<div class="modal" tabindex="-1" role="dialog" id="confirm-delete">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Удаление сумм</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>Вы действительно хотите удалить выбранные суммы?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="accept-delete">Да</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Нет</button>
            </div>
        </div>
    </div>
</div>

<%--<div id="reInvestModal" class="modal fade" role="dialog">--%>
<%--    <div class="modal-dialog" style="width: 90%">--%>
<%--        <div class="modal-content" id="content">--%>
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal">&times;</button>--%>
<%--                <h4></h4>--%>
<%--            </div>--%>
<%--            <div class="modal-body">--%>
<%--                <form:form method="POST" modelAttribute="searchSummary" class="form-horizontal" id="reInvestData">--%>
<%--                    <div class="row">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="dateClose">Дата закрытия:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:input type="date" path="dateClose" id="dateClose" class="form-control input-sm"/>--%>
<%--                                <div id="dateCloseErr" style="color: red; display: none">Необходимо выбрать дату--%>
<%--                                    вложения--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row" id="facilitiesRow">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="srcFacilities">Объект:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:select path="reFacility" id="srcFacilities" items="${facilities}" multiple="false"--%>
<%--                                             itemValue="id" itemLabel="name" class="selectpicker form-control input-sm"--%>
<%--                                             data-live-search="true"/>--%>
<%--                                <div id="facilityErr" style="color: red; display: none">Необходимо выбрать объект</div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row" id="underFacilityRow">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="srcUnderFacilities">Подобъект:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:select path="reUnderFacility" id="srcUnderFacilities" items="${underFacilities}"--%>
<%--                                             multiple="false"--%>
<%--                                             data-none-selected-text="Без подобъекта"--%>
<%--                                             itemValue="id" itemLabel="name" class="form-control input-sm selectpicker"/>--%>
<%--                                <div id="underFacilityErr" style="color: red; display: none">Необходимо выбрать--%>
<%--                                    подобъект--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row" id="shareKindNameRow">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="shareTypeName">Вид доли:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:select path="shareType" id="shareTypeName" items="${shareTypes}" multiple="false"--%>
<%--                                             itemValue="id" itemLabel="title" class="form-control input-sm"/>--%>
<%--                                <div id="shareKindErr" style="color: red; display: none">Необходимо выбрать вид доли--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row">--%>
<%--                        <div class="form-actions floatRight">--%>
<%--                            <input type="submit" value="Реинвестировать" class="btn btn-primary btn-sm"/> или <a--%>
<%--                                href="<c:url value='/#' />" id="cancelReinvest">Отмена</a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </form:form>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<%--<div id="divideModal" class="modal fade" role="dialog">--%>
<%--    <div class="modal-dialog" style="width: 90%">--%>
<%--        <div class="modal-content" id="divideContent">--%>
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal">&times;</button>--%>
<%--                <h4></h4>--%>
<%--            </div>--%>
<%--            <div class="modal-body">--%>
<%--                <form:form method="POST" modelAttribute="searchSummary" class="form-horizontal" id="divideData">--%>

<%--                    <div class="row" id="underFacilitiesRow">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable selectric" for="underFacility">Подобъект:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:select path="reUnderFacility" id="underFacility" items="${underFacilities}"--%>
<%--                                             itemValue="id" itemLabel="name" class="form-control input-sm"--%>
<%--                                             title="Выберите подобъект..." multiple="true"--%>
<%--                                             data-live-search="true" data-size="7"--%>
<%--                                             data-actions-box="true" data-select-all-text="Выбрать всё"--%>
<%--                                             data-deselect-all-text="Очистить"/>--%>
<%--                                <div id="underFacilityErr" style="color: red; display: none">--%>
<%--                                    Необходимо выбрать подобъект--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row" id="underFacilitiesListRow">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="underFacilityList">Из каких подобъектов состоит--%>
<%--                                остаток:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:select path="underFacilityList" id="underFacilityList"--%>
<%--                                             items="${underFacilitiesList}"--%>
<%--                                             itemValue="id" itemLabel="name" class="form-control input-sm selectpicker"--%>
<%--                                             title="Выберите подобъекты..." multiple="true"--%>
<%--                                             data-live-search="true" data-size="7"--%>
<%--                                             data-actions-box="true" data-select-all-text="Выбрать всё"--%>
<%--                                             data-deselect-all-text="Очистить"/>--%>
<%--                                <div id="underFacilityErr" style="color: red; display: none">Необходимо выбрать из каких--%>
<%--                                    подобъектов состоит остаток--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row">--%>
<%--                        <div class="form-actions floatRight">--%>
<%--                            <input type="submit" value="Разделить" class="btn btn-primary btn-sm"/> или <a--%>
<%--                                href="<c:url value='/#' />" id="cancelDivide">Отмена</a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </form:form>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<%--<div id="closeModal" class="modal fade" role="dialog">--%>
<%--    <div class="modal-dialog" style="width: 90%">--%>
<%--        <div class="modal-content" id="closeContent">--%>
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal">&times;</button>--%>
<%--                <h4></h4>--%>
<%--            </div>--%>
<%--            <div class="modal-body">--%>
<%--                <form:form method="POST" modelAttribute="searchSummary" class="form-horizontal" id="closeData">--%>
<%--                    <div class="row">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="dateClosing">Дата закрытия:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:input type="date" path="dateClose" id="dateClosing"--%>
<%--                                            class="form-control input-sm"/>--%>
<%--                                <div id="dateCloseErr" style="color: red; display: none">Необходимо выбрать дату--%>
<%--                                    закрытия--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="realDateGiven">Дата реальной передачи денег:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:input type="date" path="realDateGiven" id="realDateGiven"--%>
<%--                                            class="form-control input-sm"/>--%>
<%--                                <div id="dateCloseErr" style="color: red; display: none">Необходимо выбрать дату--%>
<%--                                    передачи денег--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row" id="typeClosingRow">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="typeClosing">Вид закрытия:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:select path="typeClosingInvest" id="typeClosing" items="${typeClosingInvest}"--%>
<%--                                             multiple="false"--%>
<%--                                             itemValue="id" itemLabel="name"--%>
<%--                                             class="form-control input-sm"/>--%>
<%--                                <div id="typeClosingErr" style="color: red; display: none">Необходимо выбрать вид--%>
<%--                                    закрытия--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row" id="buyerRow" style="display: none">--%>
<%--                        <div class="form-group col-md-12">--%>
<%--                            <label class="col-md-3 control-lable" for="buyer">Инвестор покупатель:</label>--%>
<%--                            <div class="col-md-7">--%>
<%--                                <form:select path="user" id="buyer" items="${investors}" multiple="false"--%>
<%--                                             itemValue="id" itemLabel="login" class="form-control input-sm"/>--%>
<%--                                <div id="buyerErr" style="color: red; display: none">Необходимо выбрать инвестора--%>
<%--                                    покупателя--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="row">--%>
<%--                        <div class="form-actions floatRight">--%>
<%--                            <input type="submit" value="Закрыть" class="btn btn-primary btn-sm"/> или <a--%>
<%--                                href="<c:url value='/#' />" id="cancelClose">Отмена</a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </form:form>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<%--<div id="msg-modal" class="modal fade" role="dialog">--%>
<%--    <div class="modal-dialog modal-sm modal-dialog-centered" role="document" style="text-align: center">--%>
<%--        <div class="modal-content" style="text-align: center">--%>
<%--            <div class="modal-body" id="msg"></div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>
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
<script type="text/javascript" src="<c:url value='/resources/core/js/investor-annex.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/investors-cash.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/progress.js' />"></script>
</body>
</html>
