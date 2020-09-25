<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Список пользователей</title>
    <sec:csrfMetaTags/>
    <link rel="stylesheet"
          href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css' />">
    <link href="<c:url value='https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/css/bootstrap4-toggle.min.css' />"
          rel="stylesheet">
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
</head>

<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="d-flex flex-row justify-content-between" style="margin: 10px;">
            <form:form modelAttribute="filter" method="POST" action="list"
                       class="form-inline" id="search-form">
                <input type="hidden" id="pageNumber" name="pageNumber" value="0">
                <input type="hidden" id="pageSize" name="pageSize" value="${filter.pageSize}">
                <input type="hidden" id="total" name="total" value="${page.content.size()}">
                <div style="padding: 5px;">
                    <form:select path="login" id="login" multiple="false" class="selectpicker"
                                 data-size="10" data-live-search="true" data-none-selected-text="Выберите инвестора">
                        <c:forEach var="user" items="${investors}">
                            <option
                                    <c:choose>
                                        <c:when test="${user.login eq 'Выберите инвестора'}">selected="selected"</c:when>
                                        <c:when test="${user.login eq filter.login}">selected="selected"</c:when>
                                    </c:choose>
                                    value="${user.login}" id="${user.id}">${user.login}
                            </option>
                        </c:forEach>
                    </form:select>
                </div>
                <button type="submit" id="bth-search" class="btn btn-primary btn-md" style="margin-left: 10px">Фильтр
                </button>
            </form:form>
                <input id="deactivated" name="deactivated" type="checkbox"
                <c:if test="${filter.deactivated == false}"> checked="checked" </c:if> data-toggle="toggle"
                       data-on="Все пользователи" data-off="Деактивированные" data-onstyle="success" data-offstyle="danger"
                       data-size="input-sm">


<%--        <label class="sr-only" for="userStatus">Статус:</label>--%>
<%--        <select id="userStatus" class="input-sm selectpicker">--%>
<%--            <c:forEach items="${userStatuses}" var="status">--%>
<%--                <option value="${status.title}">${status.title}</option>--%>
<%--            </c:forEach>--%>
<%--        </select>--%>

<%--        <sec:authorize access="isFullyAuthenticated()">--%>
<%--            <sec:authorize access="hasRole('ADMIN')">--%>
<%--                <a href="<c:url value='/users/create' />" class="btn btn-success btn-md pull-right">Создать</a>--%>
<%--            </sec:authorize>--%>
<%--        </sec:authorize>--%>
        </div>
</div>
<div class="container-fluid">
    <table class="table table-striped w-auto table-hover table-sm" style="table-layout: fixed"
           id="tblUsers">
        <thead style="text-align: center">
        <tr>
            <th>ID</th>
            <th>Имя пользователя</th>
            <th>Email</th>
            <th>Подтверждён</th>
            <th>Деактивирован</th>
            <th>Роль</th>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th style="text-align: center">Действие</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${page.content}" var="user">
            <tr id="${user.id}">
                <td>${user.id}</td>
                <td>${user.login}</td>
                <td>${user.profile.email}</td>
                <c:choose>
                    <c:when test="${user.confirmed}">
                        <td>Да</td>
                    </c:when>
                    <c:otherwise>
                        <td>Нет</td>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${user.profile.locked}">
                        <td>Да</td>
                    </c:when>
                    <c:otherwise>
                        <td>Нет</td>
                    </c:otherwise>
                </c:choose>
                <c:forEach items="${user.roles}" var="role">
                    <c:choose>
                        <c:when test="${role.name == 'ROLE_ADMIN'}">
                            <c:set var="roleName" value="Админ"/>
                        </c:when>
                        <c:when test="${role.name == 'ROLE_INVESTOR'}">
                            <c:set var="roleName" value="Инвестор"/>
                        </c:when>
                        <c:when test="${role.name == 'ROLE_MANAGER'}">
                            <c:set var="roleName" value="Управляющий"/>
                        </c:when>
                    </c:choose>
                </c:forEach>
                <td>${roleName}</td>
                <sec:authorize access="isFullyAuthenticated()">
                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                        <td style="text-align: center">
                            <div class="dropdown pull-right" style="margin-right: 10px">
                                <button type="button" data-toggle="dropdown"
                                        class="btn btn-success btn-sm dropdown-toggle pull-right"><span
                                        class="fas fa-cog"></span></button>
                                <div class="dropdown-menu">
                                    <a class="dropdown-item" id="edit-user" href="<c:url value='edit/${user.id}' />">Изменить</a>
                                    <li class="divider"></li>
                                    <a class="dropdown-item" id="delete"
                                       href="<c:url value='delete/${user.id}' />">Удалить</a></div>
                            </div>
                        </td>
                    </sec:authorize>
                </sec:authorize>

<%--                <sec:authorize access="isFullyAuthenticated()">--%>
<%--                    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">--%>
<%--                        <td style="text-align: center">--%>
<%--                            <div class="btn-group">--%>
<%--                                <button type="button" data-toggle="dropdown"--%>
<%--                                        class="btn btn-primary btn-sm dropdown-toggle">Действие <span--%>
<%--                                        class="caret"></span></button>--%>
<%--                                <ul class="dropdown-menu">--%>
<%--                                    <li><a href="<c:url value='/edit-user-${user.id}' />">Изменить</a></li>--%>
<%--                                    <li><a href="<c:url value='#' />" data-user-id="${user.id}" class="deactivate">Деактивировать</a>--%>
<%--                                    </li>--%>
<%--                                    <li class="divider"></li>--%>
<%--                                    <li><a href="<c:url value='/#' />" id="delete" data-user-id="${user.id}"--%>
<%--                                           style="color: red">Удалить</a>--%>
<%--                                    </li>--%>
<%--                                </ul>--%>
<%--                            </div>--%>
<%--                        </td>--%>
<%--                    </sec:authorize>--%>
<%--                </sec:authorize>--%>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="popup_modal.jsp" %>
<%@include file="ddk_loader.jsp" %>
<%@include file="confirm-form.jsp" %>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js' />"></script>
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
<script src="<c:url value='https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/progress.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
</body>
</html>
