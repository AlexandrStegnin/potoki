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
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
</head>

<body>
<%@include file="header.jsp" %>
<div class="container generic-container" style="margin-top: 10%">
    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA') or hasRole('BIGDADDY')">
        <a href="<c:url value='/users/list' />" class="btn btn-success btn-block" >Пользователи (app_user)</a>
        <a href="<c:url value='/facilities/list' />" class="btn btn-success btn-block">Объекты (facility)</a>
        <a href="<c:url value='/facilities/child/list' />" class="btn btn-success btn-block">Подобъекы (under_facility)</a>
        <a href="<c:url value='/rooms/list' />" class="btn btn-success btn-block">Помещения (room)</a>
        <a href="<c:url value='/money/list' />" class="btn btn-success btn-block">Деньги инвесторов (money)</a>
        <a href="<c:url value='/payments/sale' />" class="btn btn-success btn-block">Выплаты инвесторам (продажа) (sale_payment)</a>
        <a href="<c:url value='/cash-sources/list' />" class="btn btn-success btn-block">Источники получения денег (cash_source)</a>
        <a href="<c:url value='/new-cash-details/list' />" class="btn btn-success btn-block">Детали новых денег (new_cash_detail)</a>
        <a href="<c:url value='/type-closing/list' />" class="btn btn-success btn-block">Виды закрытия вложений (type_closing)</a>
        <a href="<c:url value='/marketing-tree' />" class="btn btn-success btn-block">Маркетинговое дерево (marketing_tree)</a>
        <a href="<c:url value='/investor/annexes' />" class="btn btn-success btn-block">Приложения к договорам инвесторов (UsersAnnexToContracts)</a>
        <a href="<c:url value='/transactions' />" class="btn btn-success btn-block">Лог операций (transaction_log)</a>
        <a href="<c:url value='/money/transactions' />" class="btn btn-success btn-block">Транзакции по счетам клиентов (account_transaction)</a>
        <a href="<c:url value='/money/transactions/summary' />" class="btn btn-success btn-block">Свободные средства клиентов</a>
        <a href="<c:url value='/agreements/list' />" class="btn btn-success btn-block">Инфо с кем заключён договор (user_agreement)</a>
    </sec:authorize>
</div>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
</body>
</html>
