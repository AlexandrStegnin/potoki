<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Форма вывода денег инвесторов</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForInvestorsCash.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="searchSummary" class="form-horizontal">
        <%--<form:input type="hidden" path="id" id="id"/>--%>

        <div class="row" id="facilitiesRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="facilities">Объект:</label>
                <div class="col-md-7">
                    <form:select path="investorsCash.facility" id="facilities" items="${facilities}" multiple="false"
                                 itemValue="id" itemLabel="facility" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <c:set var="uf" value="${underFacilities}"/>

        <div class="row" id="underFacilitiesRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="underFacilities">Подобъект:</label>
                <div class="col-md-7">
                    <form:select path="investorsCash.underFacility" id="underFacilities" multiple="false"
                                 class="form-control input-sm">
                        <c:forEach var="uf" items="${uf}">
                            <form:option value="${uf.underFacility}" id="${uf.id}" data-parent-id="${uf.facilityId}">

                            </form:option>
                        </c:forEach>
                    </form:select>
                </div>
            </div>
        </div>

        <div class="row" id="investorRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="investor">Инвестор:</label>
                <div class="col-md-7">
                    <form:select path="investorsCash.investor" id="investor" items="${investors}" multiple="false"
                                 itemValue="id" itemLabel="login" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row" id="cashRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cash">Сумма к выводу:</label>
                <div class="col-md-7">
                    <form:input type="number" path="investorsCash.givedCash" id="cash" class="form-control input-sm"
                                min="0.0" step="any"/>
                    <div class="has-error">
                        <form:errors path="investorsCash.givedCash" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="dateGivedCashRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateGivedCash">Дата вывода:</label>
                <div class="col-md-7">
                    <form:input type="date" path="investorsCash.dateGivedCash" id="dateGivedCash"
                                class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row" id="cashingRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cashing">Комиссия за вывод:</label>
                <div class="col-md-7">
                    <form:input type="number" path="commission" id="cashing" class="form-control input-sm"
                                min="0.00" max="100" step="any"/>
                </div>
            </div>
        </div>

        <div class="row" id="commissionNoMoreRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="commissionNoMore">Но не более:</label>
                <div class="col-md-7">
                    <form:input type="number" path="commissionNoMore" id="commissionNoMore" class="form-control input-sm"
                                min="0.00" step="any"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <input type="button" value="Вывести всё" id="allMoneyCashing" class="btn btn-danger btn-sm" style="margin-right: 10px; display: none">
                <input type="submit" value="Вывести" class="btn btn-primary btn-sm" style="margin-right: 10px;"/>
                <a href="<c:url value='/investorscash/1' />">Отмена</a>
            </div>
        </div>
    </form:form>
</div>
<%@include file="loader.jsp" %>
<%@include file="popup.jsp" %>
<%@include file="slideDiv.jsp" %>
</body>
</html>