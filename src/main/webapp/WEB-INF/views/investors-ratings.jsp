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
    <title>Рейтинг инвесторов</title>
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
        <form:form modelAttribute="filter" action="" method="POST" class="form-inline"
                   id="rating-filter-form">
        <label for="first-rating-date" style="margin-left:5px; margin-right:5px; font-size:14px">Дата первого рейтинга:</label>
        <input id="first-rating-date" name="firstRatingDate" type="date" class="form-control input-sm" value="">
        <label for="second-rating-date" style="margin-left:5px; margin-right:5px; font-size:14px">Дата второго рейтинга:</label>
        <input id="second-rating-date" name="secondRatingDate" type="date" class="form-control input-sm" value=""
               style="margin-right:5px">
        <button type="submit" id="calculate" class="btn btn-primary btn-md" style="margin-left: 10px">Сформировать рейтинг
        </button>
    </div>
    </form:form>

    <table aria-describedby="investor rating"
           class="table table-striped w-auto table-hover table-sm" style="table-layout: fixed"
           id="investors-ratings">
        <thead>
        <tr>
            <th scope="col">Место сейчас (место в прошлом рейтинге)</th>
            <th scope="col">Изменение места в рейтинге</th>
            <th scope="col">Инвестор</th>
            <th scope="col">Вложено на <fmt:formatDate value="${firstRatingDate}" pattern="dd.MM.yyyy" /></th>
            <th scope="col">Вложено на <fmt:formatDate value="${secondRatingDate}" pattern="dd.MM.yyyy" /></th>
            <th scope="col">Приток/отток капитала</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${ratings}" var="rating">
            <tr>
                <td>${rating.nowRPreviousR}</td>
                <td>${rating.changePosition}</td>
                <td>${rating.login}</td>
                <td><fmt:setLocale value="ru-RU" scope="session"/>
                    <fmt:formatNumber value="${rating.firstRatingCash}" type="currency" minFractionDigits="2"/></td>
                <td><fmt:setLocale value="ru-RU" scope="session"/>
                    <fmt:formatNumber value="${rating.secondRatingCash}" type="currency" minFractionDigits="2"/></td>
                <td><fmt:setLocale value="ru-RU" scope="session"/>
                    <fmt:formatNumber value="${rating.differenceCash}" type="currency" minFractionDigits="2"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@include file="ddk_loader.jsp" %>
<%@include file="popup_modal.jsp"%>

<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js' />"></script>
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
<script src="<c:url value='https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/investors-ratings.js' />"></script>
</body>
</html>
