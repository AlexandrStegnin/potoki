<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <sec:csrfMetaTags />
    <title>Инвесторы. Ручная загрузка.</title>
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

    <script type="text/javascript" src="<c:url value='/resources/core/js/sockjs-0.3.4.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/stomp.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/applic.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/socketScripts.js' />" ></script>

    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Инвесторы. Ручная загрузка:</span></div>

        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasRole('ADMIN')">
                <div class="well">
                    <form:form method="POST" action="paysToInv" modelAttribute="fileBucket" enctype="multipart/form-data" class="form-horizontal">
                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="file">Загрузить файл</label>
                                <div class="col-md-7">
                                    <form:input type="file" path="file" id="file" class="form-control input-sm"/>
                                    <div class="has-error">
                                        <form:errors path="file" class="help-inline"/>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-actions floatRight">
                                <input type="submit" value="Загрузить" class="btn btn-primary btn-sm">
                            </div>
                        </div>
                    </form:form>
                </div>
            </sec:authorize>
        </sec:authorize>

        <table class="table table-hover" id="investorsSummary">
            <thead>
            <tr>
                <th>Название объекта</th>
                <th>Период</th>
                <th>ID инвестора</th>
                <th>Остаток по доле</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${investorsSummaries}" var="summary">
                <tr>
                    <td>${summary.facility}</td>
                    <td>${summary.getEndDateToLocalDate()}</td>
                    <td>${summary.investorId}</td>
                    <td><fmt:formatNumber value="${summary.ostatokPoDole}" type="currency"/></td>
                    </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>