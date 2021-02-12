<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <title>Маркетинговое дерево</title>
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
        <form:form modelAttribute="filter" action="marketing-tree" method="POST" class="form-inline"
                   id="filter-form">
        <input type="hidden" id="pageNumber" name="pageNumber" value="0">
        <input type="hidden" id="pageSize" name="pageSize" value="${filter.pageSize}">
        <input type="hidden" id="total" name="total" value="${page.content.size()}">
        <div style="padding: 5px;">
            <form:select path="investor" id="investors" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите инвестора">
                <c:forEach var="inv" items="${investors}">
                    <option
                            <c:choose>
                                <c:when test="${inv.login eq filter.investor}">selected="selected"</c:when>
                            </c:choose>
                            value="${inv.login}" id="${inv.id}">${inv.login}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <div style="padding: 5px;">
            <form:select path="partner" id="partners" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите партнёра">
                <c:forEach var="p" items="${partners}">
                    <option
                            <c:choose>
                                <c:when test="${p.login eq filter.partner}">selected="selected"</c:when>
                            </c:choose>
                            value="${p.login}" id="${p.id}">${p.login}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <div style="padding: 5px;">
            <form:select path="kin" id="kins" multiple="false" class="selectpicker"
                         data-size="10" data-live-search="true" data-none-selected-text="Выберите родство">
                <c:forEach var="kin" items="${kins}">
                    <option
                            <c:choose>
                                <c:when test="${kin.name() eq 'EMPTY'}">
                                    <c:set var="kinName" value="Выберите родство"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="kinName" value="${kin.val}"/>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${kin.name() eq filter.kin}">selected="selected"</c:when>
                            </c:choose>
                            value="${kin.name()}" id="${kin.ordinal()}">${kinName}
                    </option>
                </c:forEach>
            </form:select>
        </div>
        <label for="beginPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">Период с:</label>
        <form:input path="fromDate" id="beginPeriod" name="fromDate" type="date" class="form-control input-sm" />
        <label for="endPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">по:</label>
        <form:input path="toDate" id="endPeriod" name="toDate" type="date" class="form-control input-md" style="margin-right:5px" />
        <input id="all" name="allRows" type="checkbox"
        <c:if test="${filter.allRows == true}"> checked="checked" </c:if> data-toggle="toggle"
               data-on="На одной" data-off="По страницам" data-onstyle="success" data-offstyle="danger"
               data-size="input-sm">
        <button type="submit" id="btn-search" class="btn btn-primary btn-md" style="margin-left: 5px">Фильтр</button>
    </div>
    <div class="d-flex flex-row justify-content-center align-center">
        <div class="p-2">
            <button type="button" id="btn-clear" class="btn btn-danger btn-md" style="margin-left: 5px">Сбросить
                фильтры
            </button>
        </div>
        <div class="p-2">
            <sec:authorize access="isFullyAuthenticated()">
                <sec:authorize access="hasRole('ADMIN')">
                    <a id="updateMarketingTree" href="<c:url value='#' />"
                       class="btn btn-success btn-md pull-right">Обновить
                        маркетинговое дерево</a>
                </sec:authorize>
            </sec:authorize>
        </div>
    </div>
</div>
</form:form>
</div>

<c:if test="${filter.allRows == false}">
    <nav class="text-center" aria-label="Маркетинговое дерево">
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

<table class="table table-hover" id="marketingTree">
    <thead>
    <tr>
        <th>№ п/п</th>
        <th>Инвестор</th>
        <th>Партнёр</th>
        <th>Родство</th>
        <th>Дата первого вложения</th>
        <th>Дней до окончания статуса активен</th>
        <th>Статус инвестора</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.content}" var="tree">
        <tr>
            <td>${tree.id}</td>
            <td>${tree.investor.login}</td>
            <td>${tree.partner.login}</td>
            <td>${tree.kin.val}</td>
            <td><fmt:formatDate pattern="dd.MM.yyyy"
                                value="${tree.firstInvestmentDate}"/></td>
            <td>${tree.daysToDeactivate}</td>
            <td>${tree.invStatus.val}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>
<%@include file="popup_modal.jsp" %>
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
<script type="text/javascript" src="<c:url value='/resources/core/js/marketing-tree.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>

</body>
</html>
