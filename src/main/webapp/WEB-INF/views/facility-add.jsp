<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Форма создания объекта</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/facilities.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="well lead">Создание нового объекта</div>
    <form:form method="POST" modelAttribute="newFacility" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="f_name">Название:</label>
                <div class="col-md-7">
                    <form:input type="text" path="name" id="f_name" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="name" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="full_name">Название (1C):</label>
                <div class="col-md-7">
                    <form:input type="text" path="fullName" id="full_name" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="fullName" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="city">Город:</label>
                <div class="col-md-7">
                    <form:input type="text" path="city" id="city" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="city" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="accountNumber">№ счёта:</label>
                <div class="col-md-7">
                    <input type="text" id="accountNumber" class="form-control input-sm" value="${accountNumber}" readonly/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <c:set var="title" value="Обновить" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="title" value="Создать" />
                    </c:otherwise>
                </c:choose>
                <input type="submit" id="create" value="${title}" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/facilities/list' />">Отмена</a>
            </div>
        </div>
    </form:form>
</div>
<%@include file="popup_modal.jsp"%>
<%@include file="ddk_loader.jsp"%>
</body>
</html>
