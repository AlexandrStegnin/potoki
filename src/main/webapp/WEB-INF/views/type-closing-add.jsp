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
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/popupScripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/type-closing.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="typeClosing" class="form-horizontal" id="typeClosing">
        <form:input type="hidden" path="id" id="id"/>
        <form:input type="hidden" path="" id="edit" value="${edit}"/>
        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="typeClosInvest">Вид закрытия вложения:</label>
                <div class="col-md-7">
                    <form:input type="text" path="name" id="typeClosInvest" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="name" class="help-inline"/>
                    </div>
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
                <input type="submit" value="${title}" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/type-closing/list' />">Отмена</a>
            </div>
        </div>
    </form:form>
</div>
<%@include file="popup_modal.jsp"%>
<%@include file="ddk_loader.jsp"%>
</body>
</html>
