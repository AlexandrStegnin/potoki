<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en-RU">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <sec:csrfMetaTags />
    <title>Страница смены пароля</title>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
    <link rel="stylesheet" type="text/css" href="<c:url value='//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css"' />" />
</head>

<body>
<div id="mainWrapper">
    <div class="login-container">
        <div class="login-card">
            <div class="login-form">
                <form method="post" class="form-horizontal" id="changePassForm">
                    <div id="message" class="alert alert-danger" style="display: none"></div>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Новый пароль" required>
                    </div>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="matchPassword"><i class="fa fa-lock"></i></label>
                        <input type="password" class="form-control" id="matchPassword" name="matchPassword" placeholder="Повторите пароль" required>
                    </div>

                    <div class="form-actions">
                        <input type="submit" id="btn-save"
                               class="btn btn-block btn-primary btn-default" value="Сохранить">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<%@include file="ddk_loader.jsp" %>
</body>
</html>
