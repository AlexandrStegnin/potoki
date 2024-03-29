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
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/applic.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/sockjs-0.3.4.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/stomp.js' />" ></script>

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
        <a href="<c:url value='/mailinggroups' />" class="btn btn-success btn-block">Группы рассылок (MailingGroups)</a>
        <a href="<c:url value='/admin_facility' />" class="btn btn-success btn-block">Объекты (FACILITYES)</a>
        <a href="<c:url value='/underfacilities' />" class="btn btn-success btn-block">Подобъекы (UnderFacilities)</a>
        <a href="<c:url value='/rooms' />" class="btn btn-success btn-block">Помещения (Rooms)</a>
        <a href="<c:url value='/facilitiesbuysales' />" class="btn btn-success btn-block">Покупка/продажа объектов (FacilitiesBuySales)</a>
        <a href="<c:url value='/viewfacilitiesservicecontracts' />" class="btn btn-success btn-block">Договоры обслуживания (FacilitiesServiceContracts)</a>
        <a href="<c:url value='/rentorsdetails' />" class="btn btn-success btn-block">Реквизиты арендаторов (RentorsDetails)</a>
        <a href="<c:url value='/bonustypes' />" class="btn btn-success btn-block">Типы бонусов (BonusTypes)</a>
        <a href="<c:url value='/typeexp' />" class="btn btn-success btn-block">Типы расходов (TypeExpenses)</a>
        <a href="<c:url value='/investorscash/1' />" class="btn btn-success btn-block">Деньги инвесторов (InvestorsCash)</a>
        <a href="<c:url value='/allowanceip' />" class="btn btn-success btn-block">Надбавки ИП (AllowanceIp)</a>
        <a href="<c:url value='/facilitiesreserves' />" class="btn btn-success btn-block">Резервы (FacilitiesReserves)</a>
        <a href="<c:url value='/bonuses' />" class="btn btn-success btn-block">Бонусы (Bonuses)</a>
        <a href="<c:url value='/investorsexp' />" class="btn btn-success btn-block">Расходы инвесторов (InvestorsExpenses)</a>
        <a href="<c:url value='/alphacorrecttags' />" class="btn btn-success btn-block">Тэги Альфа банка (AlphaCorrectTags)</a>
        <a href="<c:url value='/toshlcorrecttags' />" class="btn btn-success btn-block">Тэги Toshl (ToshlCorrectTags)</a>
        <a href="<c:url value='/investorsshare' />" class="btn btn-success btn-block">Доли инвесторов (INVESTORS_SHARE)</a>
        <a href="<c:url value='/paysToInv' />" class="btn btn-success btn-block">Выплаты инвесторам (аренда) (InvestorsFlows)</a>
        <a href="<c:url value='/flowsSale' />" class="btn btn-success btn-block">Выплаты инвесторам (продажа) (InvestorsFlowsSale)</a>
        <a href="<c:url value='/investorsAllFlows' />" class="btn btn-success btn-block">Потоки объектов (MainFlows)</a>
        <a href="<c:url value='/viewcashsources' />" class="btn btn-success btn-block">Источники получения денег (CashSources)</a>
        <a href="<c:url value='/viewcashtypes' />" class="btn btn-success btn-block">Виды денег (CashTypes)</a>
        <a href="<c:url value='/viewnewcashdetails' />" class="btn btn-success btn-block">Детали новых денег (NewCashDetails)</a>
        <a href="<c:url value='/viewinvestorstypes' />" class="btn btn-success btn-block">Типы инвесторов (InvestorsTypes)</a>
        <a href="<c:url value='/viewTypesClosingInvest' />" class="btn btn-success btn-block">Виды закрытия вложений (TypeClosingInvest)</a>
        <a href="<c:url value='/viewShareKind' />" class="btn btn-success btn-block">Виды долей (ShareKind)</a>
        <a href="<c:url value='/saleOfFacilities' />" class="btn btn-success btn-block">Продажа объектов (SaleOfFacilities)</a>

        </sec:authorize>
    </div>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>