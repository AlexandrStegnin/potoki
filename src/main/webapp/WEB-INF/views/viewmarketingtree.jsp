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
    <title>${title}</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/jquery-ui.min.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.0.4/popper.js" /> "></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-ui.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/marketing-tree.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">${title}</span>
            <form:form modelAttribute="filters" action="/marketingTree" method="POST" class="form-inline"
                       id="filter-form">
                <div class="row" style="margin: 10px;">
                    <input type="hidden" id="pageNumber" name="pageNumber" value="0">
                    <input type="hidden" id="pageSize" name="pageSize" value="${filters.pageSize}">
                    <input type="hidden" id="total" name="total" value="${page.content.size()}">

                    <label class="sr-only" for="investors">Инвестор:</label>
                    <form:select path="investor" id="investors" multiple="false" class="selectpicker"
                                 data-live-search="true" data-width="150px">
                        <c:forEach var="inv" items="${investors}">
                            <option
                                    <c:choose>
                                        <c:when test="${inv.login eq filters.investor}">selected="selected"</c:when>
                                    </c:choose>
                                    value="${inv.login}" id="${inv.id}">${inv.login}
                            </option>
                        </c:forEach>
                    </form:select>
                    <label class="sr-only" for="partners">Партнёр:</label>
                    <form:select path="partner" id="partners" multiple="false" class="selectpicker"
                                 data-live-search="true" data-width="150px">
                        <c:forEach var="p" items="${partners}">
                            <option
                                    <c:choose>
                                        <c:when test="${p.login eq filters.partner}">selected="selected"</c:when>
                                    </c:choose>
                                    value="${p.login}" id="${p.id}">${p.login}
                            </option>
                        </c:forEach>
                    </form:select>
                    <label class="sr-only" for="partners">Родство:</label>
                    <form:select path="kin" id="kins" multiple="false" class="selectpicker"
                                 data-actions-box="true" data-width="150px">
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
                                        <c:when test="${kinName eq filters.kin}">selected="selected"</c:when>
                                    </c:choose>
                                    value="${kin.name()}" id="${kin.ordinal()}">${kinName}
                            </option>
                        </c:forEach>
                    </form:select>
                    <label for="beginPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">Период
                        с:</label>
                    <input id="beginPeriod" name="fromDate" type="date" class="form-control input-sm" value="">
                    <label for="endPeriod" style="margin-left:10px; margin-right:5px; font-size:14px">по:</label>
                    <input id="endPeriod" name="toDate" type="date" class="form-control input-sm" value=""
                           style="margin-right:5px">
                    <button type="submit" id="bth-search" class="btn btn-primary btn-sm">Фильтр</button>
                    <button type="submit" id="bth-clear" class="btn btn-danger btn-sm">Сбросить фильтры</button>
                    <div class="btn btn-info btn-sm" id="allRows">
                        <label class="checkbox-inline">
                            <input type="checkbox" name="allRows" id="all"
                                <c:choose>
                                    <c:when test="${filters.allRows == true}">
                                           checked="checked"
                                    </c:when>
                                </c:choose>
                            >На одной странице
                        </label>
                    </div>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN')">
                            <a id="updateMarketingTree" href="<c:url value='/updateMarketingTree' />"
                               class="btn btn-success btn-sm pull-right" style="margin-top: 2px; margin-bottom: 2px">Обновить
                                маркетинговое дерево</a>
                        </sec:authorize>
                    </sec:authorize>
                </div>
            </form:form>
        </div>

        <c:if test="${filters.allRows == false}">
            <nav class="text-center" aria-label="Маркетинговое дерево">
                <ul class="pagination pagination-sm justify-content-center">
                    <c:forEach begin="1" end="${page.totalPages}" varStatus="page">
                        <c:choose>
                            <c:when test="${filters.pageNumber == page.index - 1}">
                                <c:set var="active" value="active" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="active" value="" />
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
                    <td><fmt:formatDate pattern = "dd.MM.yyyy"
                                        value = "${tree.firstInvestmentDate}" /></td>
                    <td>${tree.daysToDeactivate}</td>
                    <td>${tree.invStatus.val}</td>
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
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.8.1/css/bootstrap-select.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.8.1/js/bootstrap-select.js"></script>
<%@include file="loader.jsp" %>
<%@include file="slideDiv.jsp" %>
</body>
</html>
