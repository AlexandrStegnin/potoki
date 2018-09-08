<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Создание письма</title>
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
    <style type="text/css">
        #msg-modal .modal-dialog {
            -webkit-transform: translate(0,-50%);
            -o-transform: translate(0,-50%);
            transform: translate(0,-50%);
            top: 50%;
            margin: 0 auto;
        }
    </style>
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="well lead">Новое письмо</div>
    <form:form method="POST" modelAttribute="sendingMail" class="form-horizontal" enctype="multipart/form-data"
               id="sendMail">

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="mGroups">Группа рассылки:</label>
                <div class="col-md-7">
                    <form:select path="mailingGroups" id="mGroups" items="${mailingGroups}" multiple="true"
                                 itemValue="id" itemLabel="mailingGroup" class="form-control input-sm"
                                 size="7"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="users">Пользователи:</label>
                <div class="col-md-7">
                    <form:select path="users" id="users" items="${investors}" multiple="true"
                                 itemValue="id" itemLabel="login" class="form-control input-sm" size="10"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="mSubj">Тема:</label>
                <div class="col-md-7">
                    <form:input type="text" path="subject" id="mSubj" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="mBody">Сообщение:</label>
                <div class="col-md-7">
                    <form:textarea type="text" rows="10" path="body" id="mBody" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="file">Добавить вложения</label>
                <div class="col-md-7">
                    <form:input type="file" path="fileBucket" id="file" class="form-control input-sm"
                        multiple="true"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <input type="submit" id="createMail" value="Отправить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/profile' />">В личный кабинет</a>
            </div>
        </div>
    </form:form>
</div>

<%@include file="loader.jsp" %>
<%@include file="popup.jsp" %>
<%@include file="slideDiv.jsp" %>
<div id="msg-modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-body" id="msg" style="text-align: center"></div>
        </div>
    </div>
</div>
</body>
</html>