<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/alphaExtractScripts.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="alphaExtract" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="debet">Дебет:</label>
                <div class="col-md-7">
                    <form:input type="number" path="debet" id="debet" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="debet" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="credit">Кредит:</label>
                <div class="col-md-7">
                    <form:input type="number" path="credit" id="credit" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="credit" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="purposePayment">Назначение платежа:</label>
                <div class="col-md-7">
                    <form:input type="text" path="purposePayment" id="purposePayment" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="purposePayment" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="facility">Объект:</label>
                <div class="col-md-7">
                    <form:select path="" id="facility" items="${facilities}" multiple="false"
                                 itemValue="id" itemLabel="facility" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="rentor">Арендатор:</label>
                <div class="col-md-7">
                    <form:select path="" id="rentor" items="${rentors}" multiple="false"
                                 itemValue="id" itemLabel="login" class="form-control input-sm" disabled="true" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="correctTags">Корректный тэг:</label>
                <div class="col-md-7">
                    <form:select path="tags" id="correctTags" items="${correctTags}" multiple="false"
                                 itemValue="id" itemLabel="correctTag" class="form-control input-sm" disabled="true" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="period">Период:</label>
                <div class="col-md-7">
                    <form:input type="date" path="period" id="period" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="period" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Обновить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/alphaextract' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Разделить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/alphaextract' />">Закончить</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>

<%@include file="loader.jsp" %>

</body>
</html>