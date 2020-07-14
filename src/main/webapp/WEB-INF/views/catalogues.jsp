<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Страница справочников</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/applic.js' />" ></script>
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js' />"></script>
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js' />"></script>

    <script type="text/javascript" src="<c:url value='/resources/core/js/socketScripts.js' />" ></script>

    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Управление справочниками:</span></div>

    </div>
    <sec:authorize access="hasRole('ADMIN') or hasRole('DBA') or hasRole('BIGDADDY')">
    <div class="well">
        <a href="<c:url value='/admin' />" class="btn btn-success btn-block">Пользователи (USERS)</a>
        <a href="<c:url value='/facilities/list' />" class="btn btn-success btn-block">Объекты (facility)</a>
        <a href="<c:url value='/underfacilities' />" class="btn btn-success btn-block">Подобъекы (UnderFacilities)</a>
        <a href="<c:url value='/rooms' />" class="btn btn-success btn-block">Помещения (Rooms)</a>
        <a href="<c:url value='/investorscash' />" class="btn btn-success btn-block">Деньги инвесторов (InvestorsCash)</a>
        <a href="<c:url value='/paysToInv' />" class="btn btn-success btn-block">Выплаты инвесторам (аренда) (InvestorsFlows)</a>
        <a href="<c:url value='/flowsSale' />" class="btn btn-success btn-block">Выплаты инвесторам (продажа) (InvestorsFlowsSale)</a>
        <a href="<c:url value='/viewcashsources' />" class="btn btn-success btn-block">Источники получения денег (CashSources)</a>
        <a href="<c:url value='/viewnewcashdetails' />" class="btn btn-success btn-block">Детали новых денег (NewCashDetails)</a>
        <a href="<c:url value='/viewTypesClosingInvest' />" class="btn btn-success btn-block">Виды закрытия вложений (TypeClosingInvest)</a>
        <a href="<c:url value='/marketingTree' />" class="btn btn-success btn-block">Маркетинговое дерево (MarketingTree)</a>
        <a href="<c:url value='/calculateInvShare' />" class="btn btn-success btn-block">Доли инвесторов (InvestorShare/InvestorShareTmp)</a>
        <a href="<c:url value='/investor/annexes' />" class="btn btn-success btn-block">Приложения к договорам инвесторов (UsersAnnexToContracts)</a>
        <a href="<c:url value='/transactions' />" class="btn btn-success btn-block">Лог операций (transaction_log)</a>
        <a href="<c:url value='/tokens' />" class="btn btn-success btn-block">Токены приложений (app_tokens)</a>
    </sec:authorize>
    </div>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>
