<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <sec:csrfMetaTags/>
    <title>Администрирование</title>
    <sec:csrfMetaTags/>
    <link rel="stylesheet"
          href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='/resources/core/css/style.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='/resources/core/css/generic-container.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css' />">
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<%@include file="header.jsp" %>
<div class="container generic-container" style="margin-top: 10%">
    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA') or hasRole('BIGDADDY')">
        <a href="<c:url value='/admin' />" class="btn btn-success btn-block" >Пользователи (app_user)</a>
        <a href="<c:url value='/facilities/list' />" class="btn btn-success btn-block">Объекты (facility)</a>
        <a href="<c:url value='/facilities/child/list' />" class="btn btn-success btn-block">Подобъекы (under_facility)</a>
        <a href="<c:url value='/rooms/list' />" class="btn btn-success btn-block">Помещения (room)</a>
        <a href="<c:url value='/investor-cash/list' />" class="btn btn-success btn-block">Деньги инвесторов (investor_cash)</a>
        <a href="<c:url value='/paysToInv' />" class="btn btn-success btn-block">Выплаты инвесторам (аренда) (InvestorsFlows)</a>
        <a href="<c:url value='/flowsSale' />" class="btn btn-success btn-block">Выплаты инвесторам (продажа) (InvestorsFlowsSale)</a>
        <a href="<c:url value='/cash-sources/list' />" class="btn btn-success btn-block">Источники получения денег (cash_source)</a>
        <a href="<c:url value='/new-cash-details/list' />" class="btn btn-success btn-block">Детали новых денег (new_cash_detail)</a>
        <a href="<c:url value='/type-closing/list' />" class="btn btn-success btn-block">Виды закрытия вложений (type_closing)</a>
        <a href="<c:url value='/marketingTree' />" class="btn btn-success btn-block">Маркетинговое дерево (MarketingTree)</a>
        <a href="<c:url value='/investor/annexes' />" class="btn btn-success btn-block">Приложения к договорам инвесторов (UsersAnnexToContracts)</a>
        <a href="<c:url value='/transactions' />" class="btn btn-success btn-block">Лог операций (transaction_log)</a>
        <a href="<c:url value='/tokens' />" class="btn btn-success btn-block">Токены приложений (app_tokens)</a>
    </sec:authorize>
</div>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
</body>
</html>
