<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page trimDirectiveWhitespaces="true" %>
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
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <style type="text/css">
        .has-error {
            color: red;
            padding: 8px 0 0 8px;
        }
    </style>
</head>

<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="d-flex flex-row justify-content-center" style="margin: 10px;">
        <form:form modelAttribute="filter" method="POST" action="rent"
                   class="form-inline" id="filter-form">
            <fmt:formatDate pattern="yyyy-MM-dd" value="${filter.fromDate}" var="fromDate"/>
            <fmt:formatDate pattern="yyyy-MM-dd" value="${filter.toDate}" var="toDate"/>
            <input type="hidden" id="pageNumber" name="pageNumber" value="0">
            <input type="hidden" id="pageSize" name="pageSize" value="${filter.pageSize}">
            <input type="hidden" id="total" name="total" value="${page.content.size()}">
            <div style="padding: 5px;">
                <form:select path="facility" id="fFacilities" multiple="false" class="selectpicker"
                             data-size="10" data-live-search="true" data-none-selected-text="Выберите объект">
                    <c:forEach var="f" items="${facilitiesList}">
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
            <button type="submit" id="btn-search" class="btn btn-primary btn-md" style="margin-right:5px">Фильтр
            </button>
        </form:form>
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
                <button type="submit" id="btn-clear" class="btn btn-danger input-md" style="margin-right: 10px">
                    Сбросить фильтры
                </button>
                <button data-table-id="rentPayments" type="button" id="unblock_operations"
                        class="btn btn-danger input-md" style="margin-right: 10px">Разблокировать операции</button>
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

    <c:if test="${filter.allRows == false}">
        <div class="d-flex flex-row justify-content-center">
            <nav class="text-center" aria-label="Выплаты инвесторам - аренда">
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
        </div>
    </c:if>
</div>
<div class="container-fluid">
    <table class="table table-striped w-auto table-hover table-sm" style="table-layout: fixed"
           id="rentPayments">
        <thead style="text-align: center">
        <tr>
            <th>Дата</th>
            <th>Объект</th>
            <th>Подобъект</th>
            <th>Помещение</th>
            <th>Инвестор</th>
            <th>После вывода</th>
            <th style="width: 30px"><input type="checkbox" id="checkAll" value=""></th>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${page.content}" var="payment">
            <tr id="${payment.id}">
                <td>${payment.getDateReportFormatted()}</td>
                <td data-facility-id="${payment.facility.id}">${payment.facility.name}</td>
                <td>${payment.underFacility.name}</td>
                <td>${payment.room.name}</td>
                <td>${payment.investor.login}</td>
                <td>
                    <fmt:formatNumber value="${payment.afterCashing}" type="currency" minFractionDigits="2"/>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${payment.isReinvest == 1}">
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
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div id="reInvestModal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 90%">
        <div class="modal-content" id="content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4></h4>
            </div>
            <div class="modal-body">
                <form:form method="POST" modelAttribute="rentPaymentDTO" class="form-horizontal" id="reInvestData">
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
                            <form:select path="facilityId" id="srcFacility" items="${facilitiesList}" multiple="false"
                                         itemValue="id" itemLabel="name" class="selectpicker form-control input-sm"
                                         data-size="10" data-live-search="true"
                                         data-none-selected-text="Выберите объект"/>
                            <div id="facilityErr" class="has-error col-md-9 d-none">Необходимо выбрать объект</div>
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
<script type="text/javascript" src="<c:url value='/resources/core/js/rent-payments.js' />"></script>
</body>
</html>
