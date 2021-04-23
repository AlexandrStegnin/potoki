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
    <title>С кем заключены договора</title>
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
        table, td, th {
            text-align: center;
        }
    </style>
</head>

<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="d-flex flex-row justify-content-center" style="margin: 10px;">
        <form:form modelAttribute="filter" method="POST" class="form-inline"
                   id="filter-form">
        <input type="hidden" id="pageNumber" name="pageNumber" value="0">
        <input type="hidden" id="pageSize" name="pageSize" value="${filter.pageSize}">
        <input type="hidden" id="total" name="total" value="${page.content.size()}">
        <div style="padding: 5px;">
            <form:select path="investorsId" id="investors" multiple="true" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите инвестора"
                         data-actions-box="true" data-select-all-text="Выбрать всё"
                         data-deselect-all-text="Очистить">
                <c:forEach var="investor" items="${investors}">
                    <option
                            <c:forEach var="investorId" items="${filter.investorsId}">
                                <c:choose>
                                    <c:when test="${investor.id eq investorId}">selected="selected"</c:when>
                                </c:choose>
                            </c:forEach>
                            value="${investor.id}" id="${investor.id}">${investor.login}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <div style="padding: 5px;">
            <form:select path="facilitiesId" id="facilities" multiple="true" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите объект"
                         data-actions-box="true" data-select-all-text="Выбрать всё"
                         data-deselect-all-text="Очистить">
                <c:forEach var="facility" items="${facilities}">
                    <option
                            <c:forEach var="facilityId" items="${filter.facilitiesId}">
                                <c:choose>
                                    <c:when test="${facility.id eq facilityId}">selected="selected"</c:when>
                                </c:choose>
                            </c:forEach>
                            value="${facility.id}" id="${facility.name}">${facility.name}
                    </option>
                </c:forEach>
            </form:select>
        </div>

        <input id="all" name="allRows" type="checkbox"
        <c:if test="${filter.allRows == true}"> checked="checked" </c:if> data-toggle="toggle"
               data-on="На одной" data-off="По страницам" data-onstyle="success" data-offstyle="danger"
               data-size="input-sm">
        <button type="submit" id="btn-search" class="btn btn-primary btn-md" style="margin-left: 10px">Фильтр
        </button>
        <div>
            <button type="button" id="btn-clear" class="btn btn-warning input-md" style="margin-left: 10px">
                Сбросить фильтры
            </button>
        </div>
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
            <th>Наименование объекта</th>
            <th>Инвестор</th>
            <th>С кем заключён договор</th>
            <th>От кого заключен договор</th>
            <th>Налоговая ставка</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.content}" var="agr">
            <tr id="${agr.id}">
                <td>${agr.facility.name}</td>
                <td>${agr.concludedFrom.login}</td>
                <td>${agr.concludedWith}</td>
                <td>${agr.organization}</td>
                <td>${agr.taxRate}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
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
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
<script src="<c:url value='https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/account-tx.js' />"></script>

</body>
</html>
