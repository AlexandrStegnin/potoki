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
    <title>Список транзакций</title>
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
        table, td, th {
            text-align: center;
        }
    </style>
</head>

<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="d-flex flex-row justify-content-center" style="margin: 10px;">
        <form:form modelAttribute="filter" action="transactions" method="POST" class="form-inline"
                   id="filter-form">
        <input type="hidden" id="pageNumber" name="pageNumber" value="0">
        <input type="hidden" id="pageSize" name="pageSize" value="${filter.pageSize}">
        <input type="hidden" id="total" name="total" value="${page.content.size()}">
        <div style="padding: 5px;">
            <form:select path="owner" id="owners" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите владельца">
                <c:forEach var="owner" items="${owners}">
                    <option
                            <c:choose>
                                <c:when test="${owner eq 'Выберите владельца'}">selected="selected"</c:when>
                                <c:when test="${owner eq filter.owner}">selected="selected"</c:when>
                            </c:choose>
                            value="${owner}" id="${owner}">${owner}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <div style="padding: 5px;">
            <form:select path="parentPayer" id="parentPayers" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите объект">
                <c:forEach var="parentPayer" items="${parentPayers}">
                    <option
                            <c:choose>
                                <c:when test="${parentPayer eq 'Выберите объект'}">selected="selected"</c:when>
                                <c:when test="${parentPayer eq filter.parentPayer}">selected="selected"</c:when>
                            </c:choose> value="${parentPayer}" id="${parentPayer}">${parentPayer}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <div style="padding: 5px;">
            <form:select path="payer" id="payers" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите подобъект">
                <c:forEach var="payer" items="${payers}">
                    <option
                            <c:choose>
                                <c:when test="${payer eq 'Выберите подобъект'}">selected="selected"</c:when>
                                <c:when test="${payer eq filter.payer}">selected="selected"</c:when>
                            </c:choose>
                            value="${payer}" id="${payer}">${payer}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <div style="padding: 5px;">
            <form:select path="cashType" id="cashTypes" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите вид денег">
                <form:option value="Выберите вид денег"/>
                <form:options items="${cashTypes}" itemValue="title" itemLabel="title"/>
            </form:select>
        </div>

        <input id="all" name="allRows" type="checkbox"
        <c:if test="${filter.allRows == true}"> checked="checked" </c:if> data-toggle="toggle"
               data-on="На одной" data-off="По страницам" data-onstyle="success" data-offstyle="danger"
               data-size="input-sm">
        <button type="submit" id="bth-search" class="btn btn-primary btn-md" style="margin-left: 10px">Фильтр
        </button>
        <div>
            <button type="button" id="btn-clear" class="btn btn-warning input-md" style="margin-left: 10px">
                Сбросить фильтры
            </button>
        </div>
    </div>
    <div class="d-flex flex-row justify-content-center" style="margin: 10px;">
        <button type="button" class="btn btn-primary input-md" id="checkIt" data-checked="false"
                style="margin-left: 10px">Выделить всё
        </button>
        <button type="button" class="btn btn-danger input-md" id="delete-list"
                style="margin-left: 10px">Удалить выбранные суммы
        </button>
        <button type="button" class="btn btn-warning input-md disabled" id="reinvest" data-checked="false"
                style="margin-left: 10px">Реинвестировать
        </button>
    </div>
    </form:form>

    <c:if test="${filter.allRows == false}">
        <nav class="text-center" aria-label="Транзакции по счетам клиентов">
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

    <table class="table table-striped w-auto table-hover table-sm" style="table-layout: fixed" id="transactions">
        <thead>
        <tr>
            <th>Дата транзакции</th>
            <th>Владелец счёта</th>
            <th>Сумма</th>
            <th>Вид транзакции</th>
            <th>Вид денег</th>
            <th>Отправитель</th>
            <th style="width: 5%">Выбрать</th>
            <th>Баланс</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="tx">
            <tr id="${tx.id}">
                <td><fmt:formatDate pattern="dd.MM.yyyy" value="${tx.txDate}"/></td>
                <td>${tx.owner.ownerName}</td>
                <td>
                    <fmt:setLocale value="ru-RU" scope="session"/>
                    <fmt:formatNumber value="${tx.cash}" type="currency" minFractionDigits="2"/>
                </td>
                <td>${tx.operationType.title}</td>
                <td>${tx.cashType.title}</td>
                <td>${tx.payer.ownerName}</td>
                <td style="text-align: center"><input type="checkbox" data-object-id="${tx.id}">
                </td>
                <td>
                    <button type="button" class="btn btn-sm btn-success show-balance" data-toggle="tooltip"
                            data-placement="left" title="Посмотреть" data-owner-id="${tx.owner.id}">
                        <i class="fas fa-coins"></i>
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@include file="popup_modal.jsp" %>
<%@include file="confirm-delete.jsp" %>
<%@include file="ddk_loader.jsp" %>
<%@include file="balance-popup.jsp" %>

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
