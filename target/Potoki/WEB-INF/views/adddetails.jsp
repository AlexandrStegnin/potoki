<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Форма добавления реквизитов</title>
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
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="well lead">Добавление реквизитов</div>
    <form:form method="POST" modelAttribute="rentorsDetails" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="facilities">Объект:</label>
                <div class="col-md-7">
                    <form:select path="facility" id="facilities" items="${facilities}" multiple="false"
                                 itemValue="id" itemLabel="facility" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="facility" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="rentors">Арендатор:</label>
                <div class="col-md-7">
                    <form:select path="rentor" id="rentors" items="${rentors}" multiple="false"
                                 itemValue="id" itemLabel="login" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="rentor" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="more">Дополнительно:</label>
                <div class="col-md-7">
                    <form:input type="text" path="moreInfo" id="more" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="moreInfo" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="inn">ИНН:</label>
                <div class="col-md-7">
                    <form:input type="text" path="Inn" id="inn" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="Inn" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="org">Организация:</label>
                <div class="col-md-7">
                    <form:input type="text" path="organization" id="org" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="organization" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="acc">Счёт:</label>
                <div class="col-md-7">
                    <form:input type="text" path="account" id="acc" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="account" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Обновить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/rentorsdetails' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Создать" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/rentorsdetails' />">Отмена</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>