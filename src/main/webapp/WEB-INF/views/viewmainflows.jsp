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
    <title>Потоки инвесторов. Ручники</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForFlows.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
    <style type="text/css">
        table {
            text-align: center;
        }

        td {
            text-align: center;
        }
    </style>
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Потоки инвесторов. По платежам:</span></div>

        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasRole('ADMIN')">
                <div class="well">
                    <form:form method="POST" action="uploadflows" modelAttribute="fileBucket"
                               enctype="multipart/form-data" class="form-horizontal">

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="file">Загрузить файл</label>
                                <div class="col-md-7">
                                    <form:input type="file" path="file" id="file" class="form-control input-sm"/>
                                    <div class="has-error">
                                        <form:errors path="file" class="help-inline"/>
                                    </div>
                                </div>
                                <input type="submit" value="Загрузить" class="btn btn-primary btn-sm">
                            </div>
                        </div>
                    </form:form>
                </div>
            </sec:authorize>
        </sec:authorize>

        <table class="table table-hover" style="font-size: small">
            <thead>
            <tr>
                <th>План/факт</th>
                <th>Файл</th>
                <th>Дата</th>
                <th>Месяц</th>
                <th>Год</th>
                <th>Сумма</th>
                <th>Название организации</th>
                <th>ИНН</th>
                <th>Счёт</th>
                <th>Назначение платежа</th>
                <th>Платёж</th>
                <th>Подобъект</th>
                <th>Уровень 2</th>
                <th>Уровень 3</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mainFlows}" var="flows">
                <tr>
                    <td>${flows.planFact}</td>
                    <td>${flows.fileName}</td>
                    <td>${flows.getSettlementDateToLocalDate()}</td>
                    <td>${flows.getMonthSettlementDate()}</td>
                    <td>${flows.getYearSettlementDate()}</td>
                    <td>
                        <fmt:setLocale value="ru-RU" scope="session"/>
                        <fmt:formatNumber value="${flows.summa}" type="currency" minFractionDigits="2"/></td>
                    <td>${flows.orgName}</td>
                    <td>${flows.inn}</td>
                    <td>${flows.account}</td>
                    <td>${flows.purposePayment}</td>
                    <td>${flows.payment}</td>
                    <td>${flows.underFacilities.underFacility}</td>
                    <td>${flows.levelTwo}</td>
                    <td>${flows.levelThree}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>