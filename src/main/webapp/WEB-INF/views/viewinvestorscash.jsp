<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список дненег инвесторов</title>
    <sec:csrfMetaTags/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/progress.js' />"></script>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/jquery-ui.min.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.0.4/popper.js" /> "></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-ui.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForInvestorsCash.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">

    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js' />"></script>
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js' />"></script>

    <style type="text/css">
        table, td, th {
            text-align: center;
        }

        td {
            word-wrap: break-word;
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
        <div class="panel-heading"><span class="lead">Список дненег инвесторов:</span>

        </div>

        <form:form modelAttribute="cashFilters" method="POST" action="/investorscash" class="form-inline"
                   id="search-form">
            <div class="row" style="margin: 10px;">
                <input type="hidden" id="pageNumber" name="pageNumber" value="0">
                <input type="hidden" id="pageSize" name="pageSize" value="${cashFilters.pageSize}">
                <input type="hidden" id="total" name="total" value="${page.content.size()}">
                <input type="hidden" id="filtered" name="filtered" value="${cashFilters.filtered}">
                <label class="sr-only" for="fFacilities">Объект:</label>
                <form:select path="facilities" id="fFacilities" multiple="true" class="selectpicker"
                             data-live-search="true" data-width="130px" title="Выберите объект..."
                             data-actions-box="true" data-select-all-text="Выбрать всё"
                             data-deselect-all-text="Очистить">
                    <c:forEach var="f" items="${facilitiesList}">
                        <option
                                <c:forEach var="filterFacility" items="${cashFilters.facilities}">
                                    <c:choose>
                                        <c:when test="${f.facility eq filterFacility}">selected="selected"</c:when>
                                    </c:choose>
                                </c:forEach>
                                value="${f.facility}" id="${f.id}"
                                data-facility-name="${f.facility}"
                        >${f.facility}
                        </option>
                    </c:forEach>
                </form:select>
                <label class="sr-only" for="uFacilities">Подобъект:</label>
                <form:select path="underFacilities" id="uFacilities" multiple="true" class="selectpicker"
                             data-live-search="true" data-width="160px" title="Выберите подобъект..."
                             data-actions-box="true" data-select-all-text="Выбрать всё"
                             data-deselect-all-text="Очистить">
                    <c:forEach var="uf" items="${underFacilitiesList}">
                        <option
                                <c:forEach var="filterUnderFacility" items="${cashFilters.underFacilities}">
                                    <c:choose>
                                        <c:when test="${uf.underFacility eq filterUnderFacility}">selected="selected"</c:when>
                                    </c:choose>
                                </c:forEach>
                                value="${uf.underFacility}" id="${uf.id}"
                                data-parent-id="${uf.facilityId}">${uf.underFacility}
                        </option>
                    </c:forEach>
                </form:select>
                <label class="sr-only" for="investors">Инвестор:</label>
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
                                    value="${inv.login}" id="${inv.id}" data-lastName="${inv.lastName}">${inv.login}
                            </option>
                        </c:if>
                    </c:forEach>
                </form:select>
                <label for="beginPeriod" style="margin-left:10px; margin-right:5px; font-size:14px" >Период с:</label>
                <input id="beginPeriod" name="fromDate" type="date" class="form-control input-sm" value="">
                <label for="endPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">по:</label>
                <input id="endPeriod" name="toDate" type="date" class="form-control input-sm" value="" style="margin-right:5px">
                <button type="submit" id="bth-search" class="btn btn-primary btn-sm">Фильтр</button>
                <button type="submit" id="bth-clear" class="btn btn-danger btn-sm">Сбросить фильтры</button>
                <div class="btn btn-info btn-sm" id="allRows">
                    <label class="checkbox-inline">
                        <input type="checkbox" name="allRows" id="all"
                        <c:if test="${cashFilters.allRows == true}"> checked="checked" </c:if> >На одной странице
                    </label>
                </div>
                <button data-table-id="investorsCash" type="button" id="unblock_operations" class="btn btn-danger btn-sm">Разблокировать операции</button>
                <sec:authorize access="isFullyAuthenticated()">
                    <sec:authorize access="hasRole('ADMIN')">
                        <div class="dropdown pull-right" style="margin-right: 10px">
                            <button id="addActions" type="button" data-toggle="dropdown"
                                    class="btn btn-warning btn-sm dropdown-toggle pull-right">Деньги <span
                                    class="glyphicon glyphicon-th-list"></span></button>
                            <ul class="dropdown-menu" id="addGetCash">
                                <li id="addCash"><a href="<c:url value='/newinvestorscash' />">Добавить деньги</a></li>
                                <li id="getCash"><a href="<c:url value='/getInvestorsCash' />">Вывести деньги</a></li>
                            </ul>
                        </div>

                        <div class="dropdown pull-right" style="margin-right: 10px">
                            <button id="actions" type="button" data-toggle="dropdown"
                                    class="btn btn-success btn-sm dropdown-toggle pull-right">Действия <span
                                    class="glyphicon glyphicon-th-list"></span></button>
                            <ul class="dropdown-menu" id="reinvest">
                                <li id="reinvestAll"><a href="#">Массовое реинвестирование</a></li>
                                <li id="divideAll"><a href="#">Массовое разделение сумм</a></li>
                                <li id="deleteAll"><a href="#">Удалить выбранные суммы</a></li>
                                <li id="closeAll"><a href="#">Закрыть выбранные суммы</a></li>
                            </ul>
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

        <table class="table table-hover" style="font-size: smaller; table-layout: fixed" id="investorsCash">
            <thead>
            <tr>
                <th>Объект</th>
                <th>Подобъект</th>
                <th>Инвестор</th>
                <th>Переданная сумма</th>
                <th>Дата передачи денег</th>
                <th>Источник денег</th>
                <th>Вид денег</th>
                <th>Детали новых денег</th>
                <th>Вид инвестора</th>
                <th>Дата закрытия вложения</th>
                <th>Вид закрытия вложения</th>
                <th>Вид доли</th>
                <th>Период расчёта</th>
                <th>Объект источник</th>
                <th>Подобъект источник</th>
                <th>Помещение</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th style="text-align: center">Действие</th>
                </sec:authorize>
                <th><input type="checkbox" id="checkIt" value=""></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.content}" var="cash">
                <tr id="${cash.id}">
                    <td data-facility-id="${cash.facility.id}">${cash.facility.facility}</td>
                    <td data-under-facility-id="${cash.underFacility.id}">${cash.underFacility.underFacility}</td>
                    <td data-investor-id="${cash.investor.id}">${cash.investor.login}</td>

                    <td data-gived-cash="${cash.givedCash}">
                        <fmt:setLocale value="ru-RU" scope="session"/>
                        <fmt:formatNumber value="${cash.givedCash}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td data-report-date="${cash.dateGivedCash.time}">${cash.getDateGivedCashToLocalDate()}</td>
                    <td data-cash-source-id="${cash.cashSource.id}">${cash.cashSource.cashSource}</td>
                    <td data-cash-type-id="${cash.cashType.id}">${cash.cashType.cashType}</td>
                    <td data-cash-details-id="${cash.newCashDetails.id}">${cash.newCashDetails.newCashDetail}</td>
                    <td data-investors-type-id="${cash.investorsType.id}">${cash.investorsType.investorsType}</td>
                    <td data-date-closing="${cash.dateClosingInvest.time}">${cash.getDateClosingInvestToLocalDate()}</td>
                    <td data-type-closing-id="${cash.typeClosingInvest.id}">${cash.typeClosingInvest.typeClosingInvest}</td>
                    <td data-share-kind-id="${cash.shareKind.id}">${cash.shareKind.shareKind}</td>
                    <td data-date-report="${cash.dateReport.time}">${cash.getDateReportToLocalDate()}</td>
                    <td data-source-facility-id="${cash.sourceFacility.id}">${cash.sourceFacility.facility}</td>
                    <td data-source-under-id="${cash.sourceUnderFacility.id}">${cash.sourceUnderFacility.underFacility}</td>
                    <td data-room-id="${cash.room.id}">${cash.room.room}</td>
                    <c:choose>
                        <c:when test="${cash.typeClosingInvest == null}">
                            <c:set var="isDisabledClass" value="isEnabled"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="isDisabledClass" value="isDisabled"/>
                        </c:otherwise>
                    </c:choose>

                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td style="text-align: center">
                                <div class="btn-group">
                                    <button type="button" data-toggle="dropdown"
                                            class="btn btn-primary btn-sm dropdown-toggle"><span
                                            class="glyphicon glyphicon-cog"></span></button>
                                    <ul class="dropdown-menu">
                                        <li id="liEdit"><a id="aEdit" href="<c:url value='/edit-cash-${cash.id}' />"
                                                           class="${isDisabledClass}">Изменить</a>
                                        </li>
                                        <li id="liDivide"><a id="aDivide"
                                                             href="<c:url value='/double-cash-${cash.id}' />">Разделить</a>
                                        </li>
                                        <li id="liDouble"><a id="aDouble"
                                                             href="<c:url value='/close-cash-${cash.id}' />">Закрыть</a>
                                        </li>
                                        <li class="divider"></li>
                                        <li id="liDelete"><a id="del" data-delete="${cash.id}"
                                                             href="<c:url value='/#' />"
                                                             style="color: #ff0000;">Удалить</a></li>
                                    </ul>
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
                            <c:when test="${cash.typeClosingInvest.typeClosingInvest.length() > 0 &&
                                            (cash.givedCash > 0 || cash.givedCash < 0)}">
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

    <sec:authorize access="isRememberMe()">
        <p>Вы вошли с помощью функции "Запомнить меня".
            Чтобы иметь все права на данную страницу, Вам необходимо снова
            <a href="<c:url value='/login' />">ВОЙТИ</a> в систему используя логин/пароль.
        </p>

    </sec:authorize>

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
                            <label class="col-md-3 control-lable" for="dateClose">Дата закрытия:</label>
                            <div class="col-md-7">
                                <form:input type="date" path="dateClose" id="dateClose" class="form-control input-sm"/>
                                <div id="dateCloseErr" style="color: red; display: none">Необходимо выбрать дату
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
                                             itemValue="id" itemLabel="facility" class="selectpicker form-control input-sm"
                                             data-live-search="true"/>
                                <div id="facilityErr" style="color: red; display: none">Необходимо выбрать объект</div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="underFacilityRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="srcUnderFacilities">Подобъект:</label>
                            <div class="col-md-7">
                                <form:select path="reUnderFacility" id="srcUnderFacilities" items="${underFacilities}"
                                             multiple="false"
                                             itemValue="id" itemLabel="underFacility" class="form-control input-sm"/>
                                <div id="underFacilityErr" style="color: red; display: none">Необходимо выбрать
                                    подобъект
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

<div id="divideModal" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 90%">
        <div class="modal-content" id="divideContent">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4></h4>
            </div>
            <div class="modal-body">
                <form:form method="POST" modelAttribute="searchSummary" class="form-horizontal" id="divideData">

                    <div class="row" id="underFacilitiesRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable selectric" for="underFacilities">Подобъект:</label>
                            <div class="col-md-7">
                                <form:select path="reUnderFacilities" id="underFacilities" items="${underFacilities}"
                                             multiple="true"
                                             itemValue="id" itemLabel="underFacility" class="form-control input-sm"/>
                                <div id="underFacilityErr" style="color: red; display: none">
                                    Необходимо выбрать подобъект
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="underFacilitiesListRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="underFacilitiesList">Из каких подобъектов состоит
                                остаток:</label>
                            <div class="col-md-7">
                                <form:select path="underFacilitiesList" id="underFacilitiesList"
                                             items="${underFacilities}" multiple="true"
                                             itemValue="id" itemLabel="underFacility" class="form-control input-sm"/>
                                <div id="underFacilityErr" style="color: red; display: none">Необходимо выбрать из каких
                                    подобъектов состоит остаток
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-actions floatRight">
                            <input type="submit" value="Разделить" class="btn btn-primary btn-sm"/> или <a
                                href="<c:url value='/#' />" id="cancelDivide">Отмена</a>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<div id="closeModal" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 90%">
        <div class="modal-content" id="closeContent">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4></h4>
            </div>
            <div class="modal-body">
                <form:form method="POST" modelAttribute="searchSummary" class="form-horizontal" id="closeData">
                    <div class="row">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="dateClosing">Дата закрытия:</label>
                            <div class="col-md-7">
                                <form:input type="date" path="dateClose" id="dateClosing"
                                            class="form-control input-sm"/>
                                <div id="dateCloseErr" style="color: red; display: none">Необходимо выбрать дату
                                    закрытия
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="realDateGiven">Дата реальной передачи денег:</label>
                            <div class="col-md-7">
                                <form:input type="date" path="realDateGiven" id="realDateGiven"
                                            class="form-control input-sm"/>
                                <div id="dateCloseErr" style="color: red; display: none">Необходимо выбрать дату
                                    передачи денег
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="typeClosingRow">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="typeClosing">Вид закрытия:</label>
                            <div class="col-md-7">
                                <form:select path="typeClosingInvest" id="typeClosing" items="${typeClosingInvest}"
                                             multiple="false"
                                             itemValue="id" itemLabel="typeClosingInvest"
                                             class="form-control input-sm"/>
                                <div id="typeClosingErr" style="color: red; display: none">Необходимо выбрать вид
                                    закрытия
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row" id="buyerRow" style="display: none">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="buyer">Инвестор покупатель:</label>
                            <div class="col-md-7">
                                <form:select path="user" id="buyer" items="${investors}" multiple="false"
                                             itemValue="id" itemLabel="login" class="form-control input-sm"/>
                                <div id="buyerErr" style="color: red; display: none">Необходимо выбрать инвестора
                                    покупателя
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-actions floatRight">
                            <input type="submit" value="Закрыть" class="btn btn-primary btn-sm"/> или <a
                                href="<c:url value='/#' />" id="cancelClose">Отмена</a>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<div id="msg-modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-sm modal-dialog-centered" role="document" style="text-align: center">
        <div class="modal-content" style="text-align: center">
            <div class="modal-body" id="msg"></div>
        </div>
    </div>
</div>
<%@include file="slideDiv.jsp" %>
<%@include file="loader.jsp" %>

<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.8.1/css/bootstrap-select.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.8.1/js/bootstrap-select.js"></script>

</body>
</html>
