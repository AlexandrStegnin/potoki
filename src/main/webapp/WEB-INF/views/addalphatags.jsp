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
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="alphaCorrectTags" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="inn">ИНН:</label>
                <div class="col-md-7">
                    <form:input type="text" path="inn" id="inn" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="inn" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="account">Счёт:</label>
                <div class="col-md-7">
                    <form:input type="text" path="account" id="account" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="account" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="enums">Дебет/Кредит:</label>
                <div class="col-md-7">
                    <form:select path="debetOrCredit" id="enums" items="${enums}" multiple="false"
                                 itemLabel="val" class="form-control input-sm" />
                </div>
            </div>
        </div>

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
                <label class="col-md-3 control-lable" for="description">Описание:</label>
                <div class="col-md-7">
                    <form:input type="text" path="description" id="description" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="description" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="correctTag">Корректный тэг:</label>
                <div class="col-md-7">
                    <form:input type="text" path="correctTag" id="correctTag" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="correctTag" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="docNumber">Номер документа:</label>
                <div class="col-md-7">
                    <form:input type="text" path="docNumber" id="docNumber" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="docNumber" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateOper">Дата операции:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateOper" id="dateOper" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="dateOper" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Обновить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/alphacorrecttags' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Создать" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/alphacorrecttags' />">Отмена</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
</body>
</html>