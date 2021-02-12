<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en-RU">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/users.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="user" class="form-horizontal" id="save">
        <form:input type="hidden" path="id" id="id"/>
        <form:input type="hidden" path="" id="edit" value="${edit}"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="login">Имя пользователя:</label>
                <div class="col-md-7">
                    <form:input type="text" path="login" id="login" class="form-control input-sm"/>
                    <div class="has-error help-inline" id="loginErr">
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="email">Email</label>
                <div class="col-md-7">
                    <form:input type="text" path="profile.email" id="email" class="form-control input-sm" />
                    <div class="has-error help-inline" id="emailErr">
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="saleChanel">Канал привлечения:</label>
                <div class="col-md-7">
                    <form:select path="partnerId" id="saleChanel" items="${investors}" multiple="false"
                                 itemValue="id" itemLabel="login" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="kins">Степень родства:</label>
                <div class="col-md-7">
                    <form:select path="kin" id="kins" items="${kins}" multiple="false"
                                 itemLabel="val" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="roles">Роли:</label>
                <div class="col-md-7">
                    <form:select path="roles" id="roles" items="${roles}" multiple="true"
                                 itemValue="id" itemLabel="name" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="lastName">Фамилия:</label>
                <div class="col-md-7">
                    <form:input type="text" path="profile.lastName" id="lastName" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="firstName">Имя:</label>
                <div class="col-md-7">
                    <form:input type="text" path="profile.firstName" id="firstName" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="patronymic">Отчество:</label>
                <div class="col-md-7">
                    <form:input type="text" path="profile.patronymic" id="patronymic" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" id="send" value="Обновить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/users/list' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" id="send" value="Создать" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/users/list' />">Отмена</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
<%@include file="popup_modal.jsp"%>
<%@include file="ddk_loader.jsp"%>
</body>
</html>
