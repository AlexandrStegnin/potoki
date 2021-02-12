<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html lang="en-RU">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>Страница восстановления пароля</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
</head>

<body>
<div id="mainWrapper">
    <div class="login-container">
        <div class="login-card">
            <div class="login-form" align="center">
                <p id="welcome_msg">Укажите адрес электронной почты и логин, который указан в Вашем профиле</p>
                <br>
                <form method="post" class="form-horizontal">
                    <div id="message" style="display: none"></div>

                    <div class="input-group input-md">
                        <label class="input-group-addon" for="login"><i class="fa fa-user fa-fw"></i></label>
                        <input type="email" class="form-control" id="login" name="login" placeholder="Логин" required>
                    </div>
                    <br>
                    <div class="input-group input-md">
                        <label class="input-group-addon" for="email"><i class="fa fa-envelope-o fa-fw"></i></label>
                        <input type="email" class="form-control" id="email" name="email" placeholder="Адрес электронной почты" required>
                    </div>
                    <br>
                    <div class="form-actions">
                        <input type="submit" id="reset"
                               class="btn btn-block btn-primary btn-default" value="Восстановить">
                    </div>
                    <br>
                    <a href="/login" class="btn btn-link">Вернуться на страницу входа</a>
                </form>
            </div>
        </div>
    </div>
</div>
<%@include file="ddk_loader.jsp" %>
</body>
</html>
