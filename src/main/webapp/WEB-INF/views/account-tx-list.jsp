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
    <title>Свободные средства инвесторов</title>
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
<%--    <div class="d-flex flex-row justify-content-center" style="margin: 10px;">--%>
<%--        <form:form modelAttribute="filter" action="sale" method="POST" class="form-inline"--%>
<%--                   id="filter-form">--%>
<%--        <fmt:formatDate pattern="yyyy-MM-dd" value="${filter.fromDate}" var="fromDate"/>--%>
<%--        <fmt:formatDate pattern="yyyy-MM-dd" value="${filter.toDate}" var="toDate"/>--%>
<%--        <input type="hidden" id="pageNumber" name="pageNumber" value="0">--%>
<%--        <input type="hidden" id="pageSize" name="pageSize" value="${filter.pageSize}">--%>
<%--        <input type="hidden" id="total" name="total" value="${page.content.size()}">--%>
<%--        <div style="padding: 5px;">--%>
<%--            <form:select path="facility" id="fFacilities" multiple="false" class="selectpicker"--%>
<%--                         data-size="10" data-live-search="true" data-none-selected-text="Выберите объект">--%>
<%--                <c:forEach var="f" items="${facilities}">--%>
<%--                    <option--%>
<%--                            <c:choose>--%>
<%--                                <c:when test="${f.name eq 'Выберите объект'}">selected="selected"</c:when>--%>
<%--                                <c:when test="${f.name eq filter.facility}">selected="selected"</c:when>--%>
<%--                            </c:choose>--%>
<%--                            value="${f.name}" id="${f.id}">${f.name}--%>
<%--                    </option>--%>
<%--                </c:forEach>--%>
<%--            </form:select>--%>
<%--        </div>--%>
<%--        <div style="padding: 5px;">--%>
<%--            <form:select path="underFacility" id="uFacilities" multiple="false" class="selectpicker"--%>
<%--                         data-size="10" data-live-search="true" data-none-selected-text="Выберите подобъект">--%>
<%--                <c:forEach var="uf" items="${underFacilities}">--%>
<%--                    <option--%>
<%--                            <c:choose>--%>
<%--                                <c:when test="${uf.name eq 'Выберите подобъект'}">selected="selected"</c:when>--%>
<%--                                <c:when test="${uf.name eq filter.underFacility}">selected="selected"</c:when>--%>
<%--                            </c:choose>--%>
<%--                            value="${uf.name}" id="${uf.id}" data-parent-id="${uf.facility.id}">${uf.name}--%>
<%--                    </option>--%>
<%--                </c:forEach>--%>
<%--            </form:select>--%>
<%--        </div>--%>
<%--        <div style="padding: 5px;">--%>
<%--            <form:select path="investors" id="investors" multiple="false" class="selectpicker"--%>
<%--                         data-size="10" data-live-search="true" data-none-selected-text="Выберите инвестора">--%>
<%--                <c:forEach var="inv" items="${investors}">--%>
<%--                    <option--%>
<%--                            <c:choose>--%>
<%--                                <c:when test="${inv.login eq 'Выберите инвестора'}">selected="selected"</c:when>--%>
<%--                                <c:when test="${inv.login eq filter.investors}">selected="selected"</c:when>--%>
<%--                            </c:choose> value="${inv.login}" id="${inv.id}">${inv.login}--%>
<%--                    </option>--%>
<%--                </c:forEach>--%>
<%--            </form:select>--%>
<%--        </div>--%>
<%--        <form:label path="" for="beginPeriod"--%>
<%--                    style="margin-left:10px; margin-right:5px; font-size:14px">Период с:</form:label>--%>
<%--        <form:input path="fromDate" id="beginPeriod" name="startDate" type="date" class="form-control input-sm"--%>
<%--                    value="${fromDate}"/>--%>
<%--        <form:label path="" for="endPeriod"--%>
<%--                    style="margin-left:10px; margin-right:5px; font-size:14px">по:</form:label>--%>
<%--        <form:input path="toDate" id="endPeriod" name="endDate" type="date" class="form-control input-sm"--%>
<%--                    value="${toDate}"--%>
<%--                    style="margin-right:5px"/>--%>
<%--        <input id="all" name="allRows" type="checkbox"--%>
<%--        <c:if test="${filter.allRows == true}"> checked="checked" </c:if> data-toggle="toggle"--%>
<%--               data-on="На одной" data-off="По страницам" data-onstyle="success" data-offstyle="danger"--%>
<%--               data-size="input-sm">--%>
<%--        <button type="submit" id="bth-search" class="btn btn-primary btn-md" style="margin-left: 10px">Фильтр--%>
<%--        </button>--%>
<%--    </div>--%>
<%--    <div class="d-flex flex-row justify-content-center" style="margin-left: 10px">--%>
<%--        <form:form method="POST" modelAttribute="fileBucket" enctype="multipart/form-data"--%>
<%--                   class="form-horizontal" id="files">--%>
<%--            <div class="p-2" style="margin-left: 10px">--%>
<%--                <div class="input-group" style="margin-left: 10px">--%>
<%--                    <div class="custom-file">--%>
<%--                        <input type="file" id="file" name="uploadingFiles" class="form-control-file"/>--%>
<%--                    </div>--%>
<%--                    <button type="button" class="btn btn-primary btn-md" id="upload">Загрузить</button>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </form:form>--%>
<%--        <div class="col-md-6 offset-md-2 p-2">--%>
<%--            <div class="row float-right">--%>
<%--                <button type="submit" id="bth-clear" class="btn btn-danger input-md" style="margin-right: 10px">--%>
<%--                    Сбросить фильтры--%>
<%--                </button>--%>
<%--                <button data-table-id="salePayments" type="button" id="unblock_operations"--%>
<%--                        class="btn btn-danger input-md" style="margin-right: 10px">Разблокировать операции</button>--%>
<%--                <button type="button" class="btn btn-primary input-md" id="checkIt" data-checked="false"--%>
<%--                        style="margin-right: 10px">--%>
<%--                    Выделить всё--%>
<%--                </button>--%>
<%--                <div class="dropdown pull-right" style="margin-right: 10px">--%>
<%--                    <button id="actions" type="button" data-toggle="dropdown"--%>
<%--                            class="btn btn-success input-md dropdown-toggle pull-right">Действия <span--%>
<%--                            class="fas fa-list"></span></button>--%>
<%--                    <div class="dropdown-menu" id="reinvest">--%>
<%--                        <a class="dropdown-item" id="reinvestAll" href="#">Массовое реинвестирование</a>--%>
<%--                        <a class="dropdown-item" id="delete-list" href="#" style="color: red">Удалить выбранные--%>
<%--                            суммы</a>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--    </form:form>--%>

<%--    <c:if test="${filter.allRows == false}">--%>
<%--        <nav class="text-center" aria-label="Выплаты инвесторам - продажа">--%>
<%--            <ul class="pagination pagination-sm justify-content-center">--%>
<%--                <c:forEach begin="1" end="${page.totalPages}" varStatus="page">--%>
<%--                    <c:choose>--%>
<%--                        <c:when test="${filter.pageNumber == page.index - 1}">--%>
<%--                            <c:set var="active" value="active"/>--%>
<%--                        </c:when>--%>
<%--                        <c:otherwise>--%>
<%--                            <c:set var="active" value=""/>--%>
<%--                        </c:otherwise>--%>
<%--                    </c:choose>--%>
<%--                    <li class="page-item ${active}" data-page="${page.index}">--%>
<%--                        <a id="${page.index}" name="page_${page.index}" class="page-link"--%>
<%--                           href="#">${page.index}</a>--%>
<%--                    </li>--%>
<%--                </c:forEach>--%>
<%--            </ul>--%>
<%--        </nav>--%>
<%--    </c:if>--%>

    <table class="table table-striped w-auto table-hover table-sm" style="table-layout: fixed" id="free-cash">
        <thead>
        <tr>
            <th>Дата транзакции</th>
            <th>Владелец счёта</th>
            <th>Сумма</th>
            <th>Вид транзакции</th>
            <th>Вид денег</th>
            <th>№ счёта отправителя</th>
            <th>Заблокирована</th>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th style="text-align: center">Действие</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="tx">
            <tr id="${tx.id}">
                <td><fmt:formatDate pattern="dd.MM.yyyy" value="${tx.txDate}" /></td>
                <c:choose>
                    <c:when test="${not empty tx.salePayment}">
                        <c:set var="givenCash" value="${tx.salePayment.getGivenCash()}" />
                    </c:when>
                    <c:when test="${not empty tx.money}">
                        <c:set var="givenCash" value="${tx.money.getGivenCash()}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="givenCash" value="0" />
                    </c:otherwise>
                </c:choose>
                <td>${tx.owner.ownerName}</td>
                <td>
                    <fmt:setLocale value="ru-RU" scope="session"/>
                    <fmt:formatNumber value="${givenCash}" type="currency" minFractionDigits="2"/>
                </td>
                <td>${tx.operationType.title}</td>
                <td>${tx.cashType.title}</td>
                <td>${tx.payer.ownerName}</td>
                <c:set var="blocked" value="Нет" />
                <c:if test="${tx.blocked}">
                    <c:set var="blocked" value="Да" />
                </c:if>
                <td>${blocked}</td>
                <td style="text-align: center">
                    <div class="dropdown pull-right" style="margin-right: 10px">
                        <button type="button" data-toggle="dropdown"
                                class="btn btn-success btn-sm dropdown-toggle pull-right"><span
                                class="fas fa-cog"></span></button>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" id="reinvest" data-object-id="${tx.id}"
                               href="<c:url value='#' />">Реинвестировать</a></div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@include file="popup_modal.jsp" %>
<%@include file="confirm-delete.jsp" %>
<%@include file="ddk_loader.jsp" %>
<%@include file="reinvest-form.jsp" %>

<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js' />"></script>
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
<script src="<c:url value='https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/account-tx.js' />"></script>

</body>
</html>
