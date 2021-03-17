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
    <title>Выплаты инвесторам (продажа)</title>
    <sec:csrfMetaTags/>
    <link rel="stylesheet"
          href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css' />">
    <link href="<c:url value='https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/css/bootstrap4-toggle.min.css' />"
          rel="stylesheet">
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <style type="text/css">
        .has-error {
            color: red;
            padding: 8px 0 0 8px;
        }
    </style>
    <style type="text/css">
        table, td, th {
            text-align: center;
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
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="d-flex flex-row justify-content-center" style="margin: 10px;">
        <form:form modelAttribute="filter" action="sale" method="POST" class="form-inline"
                   id="filter-form">
        <fmt:formatDate pattern="yyyy-MM-dd" value="${filter.fromDate}" var="fromDate"/>
        <fmt:formatDate pattern="yyyy-MM-dd" value="${filter.toDate}" var="toDate"/>
        <input type="hidden" id="pageNumber" name="pageNumber" value="0">
        <input type="hidden" id="pageSize" name="pageSize" value="${filter.pageSize}">
        <input type="hidden" id="total" name="total" value="${page.content.size()}">
        <div style="padding: 5px;">
            <form:select path="facility" id="fFacilities" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите объект">
                <c:forEach var="f" items="${facilities}">
                    <option
                            <c:choose>
                                <c:when test="${f.name eq 'Выберите объект'}">selected="selected"</c:when>
                                <c:when test="${f.name eq filter.facility}">selected="selected"</c:when>
                            </c:choose>
                            value="${f.name}" id="${f.id}">${f.name}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <div style="padding: 5px;">
            <form:select path="underFacility" id="uFacilities" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите подобъект">
                <c:forEach var="uf" items="${underFacilities}">
                    <option
                            <c:choose>
                                <c:when test="${uf.name eq 'Выберите подобъект'}">selected="selected"</c:when>
                                <c:when test="${uf.name eq filter.underFacility}">selected="selected"</c:when>
                            </c:choose>
                            value="${uf.name}" id="${uf.id}" data-parent-id="${uf.facility.id}">${uf.name}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <div style="padding: 5px;">
            <form:select path="investor" id="investors" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите инвестора">
                <c:forEach var="inv" items="${investors}">
                    <option
                            <c:choose>
                                <c:when test="${inv.login eq 'Выберите инвестора'}">selected="selected"</c:when>
                                <c:when test="${inv.login eq filter.investor}">selected="selected"</c:when>
                            </c:choose> value="${inv.login}" id="${inv.id}">${inv.login}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <form:label path="" for="beginPeriod"
                    style="margin-left:10px; margin-right:5px; font-size:14px">Период с:</form:label>
        <form:input path="fromDate" id="beginPeriod" name="startDate" type="date" class="form-control input-sm"
                    value="${fromDate}"/>
        <form:label path="" for="endPeriod"
                    style="margin-left:10px; margin-right:5px; font-size:14px">по:</form:label>
        <form:input path="toDate" id="endPeriod" name="endDate" type="date" class="form-control input-sm"
                    value="${toDate}"
                    style="margin-right:5px"/>
        <input id="all" name="allRows" type="checkbox"
        <c:if test="${filter.allRows == true}"> checked="checked" </c:if> data-toggle="toggle"
               data-on="На одной" data-off="По страницам" data-onstyle="success" data-offstyle="danger"
               data-size="input-sm">
        <button type="submit" id="bth-search" class="btn btn-primary btn-md" style="margin-left: 10px">Фильтр
        </button>
    </div>
    <div class="d-flex flex-row justify-content-center" style="margin-left: 10px">
        <form:form method="POST" modelAttribute="fileBucket" enctype="multipart/form-data"
                   class="form-horizontal" id="files">
            <div class="p-2" style="margin-left: 10px">
                <div class="input-group" style="margin-left: 10px">
                    <div class="custom-file">
                        <input type="file" id="file" name="uploadingFiles" class="form-control-file" style="overflow: hidden"/>
                    </div>
                    <button type="button" class="btn btn-primary btn-md" id="upload">Загрузить</button>
                </div>
            </div>
        </form:form>
        <div class="col-md-6 offset-md-2 p-2">
            <div class="row float-right">
                <button type="submit" id="bth-clear" class="btn btn-danger input-md" style="margin-right: 10px">
                    Сбросить фильтры
                </button>
                <button data-table-id="salePayments" type="button" id="unblock_operations"
                        class="btn btn-danger input-md" style="margin-right: 10px">Разблокировать операции</button>
                <button type="button" class="btn btn-primary input-md" id="checkIt" data-checked="false"
                        style="margin-right: 10px">
                    Выделить всё
                </button>
                <div class="dropdown pull-right" style="margin-right: 10px">
                    <button id="actions" type="button" data-toggle="dropdown"
                            class="btn btn-success input-md dropdown-toggle pull-right">Действия <span
                            class="fas fa-list"></span></button>
                    <div class="dropdown-menu" id="reinvest">
                        <a class="dropdown-item" id="reinvestAll" href="#">Массовое реинвестирование</a>
                        <a class="dropdown-item" id="delete-list" href="#" style="color: red">Удалить выбранные
                            суммы</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </form:form>

    <c:if test="${filter.allRows == false}">
        <nav class="text-center" aria-label="Выплаты инвесторам - продажа">
            <ul class="pagination pagination-sm justify-content-center">
                <c:forEach begin="1" end="${page.totalPages}" varStatus="page">
                    <c:choose>
                        <c:when test="${filter.pageNumber == page.index - 1}">
                            <c:set var="active" value="active"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="active" value=""/>
                        </c:otherwise>
                    </c:choose>
                    <li class="page-item ${active}" data-page="${page.index}">
                        <a id="${page.index}" name="page_${page.index}" class="page-link"
                           href="#">${page.index}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>

    <table class="table table-striped w-auto table-hover table-sm" style="table-layout: fixed" id="salePayments">
        <thead>
        <tr>
            <th>Объект</th>
            <th>Инвестор</th>
            <th>Вид доли</th>
            <th>Вложено в объект</th>
            <th>Дата вложения</th>
            <th>Вложено в подобъект</th>
            <th>Сколько прибыли реинвест</th>
            <th>Подобъект</th>
            <th>Дата продажи</th>
            <th>Дата реальной передачи</th>
            <th>Выделить</th>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th style="text-align: center">Действие</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="flows">
            <tr id="${flows.id}">
                <td data-facility-id="${flows.facility.id}">${flows.facility.name}</td>
                <td data-investor-id="${flows.investor.id}">${flows.investor.login}</td>
                <td data-share-kind-id="${flows.shareType.id}">${flows.shareType.title}</td>
                <td data-cash-facility="${flows.cashInFacility}">
                    <fmt:setLocale value="ru-RU" scope="session"/>
                    <fmt:formatNumber value="${flows.cashInFacility}" type="currency" minFractionDigits="2"/>
                </td>
                <td data-date-gived="${flows.dateGiven.time}">
                        <fmt:formatDate value="${flows.dateGiven}" pattern="dd.MM.yyyy" />
                </td>
                <td>
                    <fmt:formatNumber value="${flows.cashInUnderFacility}" type="currency" minFractionDigits="2"/>
                </td>
                <td data-gived-cash="${flows.profitToReInvest}">
                    <fmt:formatNumber value="${flows.profitToReInvest}" type="currency" minFractionDigits="2"/>
                </td>
                <td data-under-facility-id="${flows.underFacility.id}">${flows.underFacility.name}</td>
                <td data-date-sale="${flows.dateSale.time}">
                    <fmt:formatDate value="${flows.dateSale}" pattern="dd.MM.yyyy" />
                </td>
                <td data-real-date-given="${flows.realDateGiven.time}">
                    <fmt:formatDate value="${flows.realDateGiven}" pattern="dd.MM.yyyy" />
                </td>
                <td>
                    <c:choose>
                        <c:when test="${flows.isReinvest == 1}">
                            <c:set var="checked" value="checked "/>
                            <c:set var="disabled" value="disabled "/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="checked" value=""/>
                            <c:set var="disabled" value=""/>
                        </c:otherwise>
                    </c:choose>
                    <input type="checkbox" title="Выбрать" ${checked} ${disabled}/>
                </td>

                <td style="text-align: center">
                    <div class="dropdown pull-right" style="margin-right: 10px">
                        <button type="button" data-toggle="dropdown"
                                class="btn btn-success btn-sm dropdown-toggle pull-right"><span
                                class="fas fa-cog"></span></button>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" id="aDivide"
                               href="<c:url value='#' />">Разделить</a></div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div id="reInvestModal" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 90%">
        <div class="modal-content" id="content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4></h4>
            </div>
            <div class="modal-body">
                <form:form method="POST" modelAttribute="salePaymentDTO" class="form-horizontal" id="reInvestData">
                    <div class="form-group row" id="dateGivenRow">
                        <label class="col-md-3 col-form-label" for="dateGiven">Дата вложения:</label>
                        <div class="col-md-9">
                            <form:input type="date" path="dateGiven" id="dateGiven" class="form-control input-sm"/>
                            <div id="dateGivenErr" class="has-error col-md-9 d-none">Необходимо указать дату
                                вложения
                            </div>
                        </div>
                    </div>

                    <div class="form-group row" id="facilitiesRow">
                        <label class="col-md-3 col-form-label" for="srcFacility">Объект:</label>
                        <div class="col-md-9">
                            <form:select path="facilityId" id="srcFacility" items="${facilities}" multiple="false"
                                         itemValue="id" itemLabel="name" class="selectpicker form-control input-sm"
                                         data-size="10" data-live-search="true"
                                         data-none-selected-text="Выберите объект"/>
                            <div id="facilityErr" class="has-error col-md-9 d-none">Необходимо выбрать объект</div>
                        </div>
                    </div>

                    <div class="form-group row" id="underFacilityRow">
                        <label class="col-md-3 col-form-label" for="srcUnderFacility">Подобъект:</label>
                        <div class="col-md-9">
                            <form:select path="underFacilityId" id="srcUnderFacility" items="${underFacilities}"
                                         multiple="false"
                                         itemValue="id" itemLabel="name" class="selectpicker form-control input-sm"
                                         data-size="10" data-live-search="true"
                                         data-none-selected-text="Выберите подобъект"/>
                            <div id="underFacilityErr" class="has-error col-md-9 d-none">Необходимо выбрать подобъект
                            </div>
                        </div>
                    </div>

                    <div class="form-group row" id="shareTypeNameRow">
                        <label class="col-md-3 col-form-label" for="shareTypeName">Вид доли:</label>
                        <div class="col-md-9">
                            <form:select path="shareType" id="shareTypeName" items="${shareTypes}" multiple="false"
                                         itemValue="title" itemLabel="title"
                                         class="selectpicker form-control input-sm"/>
                            <div id="shareTypeErr" class="has-error col-md-9 d-none">Необходимо выбрать вид доли
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <input type="submit" class="btn btn-primary btn-md" data-action="" value="Реинвестировать"/>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
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
                    <form:input type="hidden" path="divideSumId" id="divideId"/>
                    <form:input type="hidden" path="" id="flowMaxSum"/>
                    <div class="form-group row" id="cashRow">
                        <label class="col-md-3 col-form-label" for="divideCash">Выделить сумму:</label>
                        <div class="col-md-9">
                            <form:input type="number" path="divideSum" id="divideCash" min="0.00" step="any"
                                        class="form-control input-sm"/>
                            <div id="divideCashErr" class="has-error col-md-9 d-none">
                                Необходимо ввести сумму > 0 и меньше разделяемой суммы
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <input type="submit" class="btn btn-primary btn-md" data-action="" value="Разделить"/>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>

<%@include file="popup_modal.jsp" %>
<%@include file="confirm-delete.jsp" %>
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
<script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/sale-payments.js' />"></script>
</body>
</html>
