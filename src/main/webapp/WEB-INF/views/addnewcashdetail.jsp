<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Добавление деталей новых денег</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/popupScripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/forNewCashDetails.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="newCashDetails" class="form-horizontal" id="newCashDetails">
        <form:input type="hidden" path="id" id="id"/>
        <form:input type="hidden" path="" id="edit" value="${edit}"/>
        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="newDetail">Детали новых денег:</label>
                <div class="col-md-7">
                    <form:input type="text" path="newCashDetail" id="newDetail" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="newCashDetail" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Обновить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/viewnewcashdetails' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Создать" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/viewnewcashdetails' />">Отмена</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
<%@include file="loader.jsp" %>
<%@include file="popup.jsp" %>
<%@include file="slideDiv.jsp" %>
</body>
</html>
