<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список помещений</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
    <style>
        th, td {
            text-align: center;
        }
    </style>
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Список помещений:</span>
            <sec:authorize access="isFullyAuthenticated()">
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="<c:url value='/rooms/create' />" class="btn btn-success btn-sm pull-right">Добавить
                        помещение</a>
                </sec:authorize>
            </sec:authorize>
        </div>

        <table class="table table-hover">
            <thead>
            <tr>
                <th>№ п/п</th>
                <th>Объект</th>
                <th>Подобъект</th>
                <th>Помещение</th>
                <th>Квадратура</th>
                <th>Стоимость</th>
                <th>Дата покупки</th>
                <th>Продано</th>
                <th>Дата продажи</th>
                <th>Цена продажи</th>
                <th>Годовая доходность</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th style="text-align: center">Действие</th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${rooms}" var="room">
                <tr>
                    <td>${room.id}</td>
                    <td>${room.underFacility.facility.name}</td>
                    <td>${room.underFacility.name}</td>
                    <td>${room.name}</td>
                    <td>
                        <fmt:setLocale value="ru-RU" scope="session"/>
                        <fmt:formatNumber value="${room.roomSize}" type="number" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${room.cost}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatDate value="${room.dateBuy}" pattern="dd.MM.yyyy"/>
                    </td>
                    <td>
                        <label class="checkbox-inline">
                            <input type="checkbox" name="sold" id="sold" value=""
                            <c:if test="${room.sold == true}"> checked="checked" </c:if>
                            disabled>
                        </label>
                    </td>
                    <td>
                        <fmt:formatDate value="${room.dateSale}" pattern="dd.MM.yyyy"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${room.salePrice}" type="currency" minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${room.totalYearProfit/100}" type="percent" minFractionDigits="2"/>
                    </td>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td style="text-align: center">
                                <div class="btn-group">
                                    <button type="button" data-toggle="dropdown"
                                            class="btn btn-primary btn-sm dropdown-toggle">Действие <span
                                            class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="<c:url value='edit/${room.id}' />">Изменить</a></li>
                                        <li class="divider"></li>
                                        <li><a href="<c:url value='delete/${room.id}' />" style="color: red">Удалить</a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                        </sec:authorize>
                    </sec:authorize>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>
<%@include file="popup_modal.jsp"%>
<%@include file="ddk_loader.jsp"%>
</body>
</html>
