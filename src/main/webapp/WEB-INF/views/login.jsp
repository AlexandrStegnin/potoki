<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en-RU">
<head>
    <link rel="apple-touch-icon" sizes="180x180" href="<c:url value='/resources/core/img/favicon/apple-touch-icon.png?v=lkv88RWlAB' />">
    <link rel="icon" type="image/png" sizes="32x32" href="<c:url value='/resources/core/img/favicon/favicon-32x32.png?v=lkv88RWlAB' />">
    <link rel="icon" type="image/png" sizes="16x16" href="<c:url value='/resources/core/img/favicon/favicon-16x16.png?v=lkv88RWlAB' />">
    <link rel="manifest" href="<c:url value='/resources/core/img/favicon/site.webmanifest?v=lkv88RWlAB' />">
    <link rel="mask-icon" href="<c:url value='/resources/core/img/favicon/safari-pinned-tab.svg?v=lkv88RWlAB' />" color="#4f004b">
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=lkv88RWlAB' />">
    <meta name="apple-mobile-web-app-title" content="Колесникъ">
    <meta name="application-name" content="Колесникъ">
    <meta name="msapplication-TileColor" content="#da532c">
    <meta name="theme-color" content="#ffffff">
    <title>Страница входа</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet" />
    <script src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script src="<c:url value='/resources/core/js/bootstrap.min_old.js' />" ></script>
    <script src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script src="<c:url value='/resources/core/js/ddk_loader.js' />" ></script>
    <link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/font-awesome/4.2.0/css/font-awesome.css" />
</head>

<body>
<div id="mainWrapper">
    <div class="login-container">
        <div class="login-card">
            <div class="login-form">
                <sec:csrfMetaTags/>
                <form:form action="login" method="post" class="form-horizontal">
                    <c:if test="${param.error != null}">
                        <div class="alert alert-danger">
                            <p>Неправильное имя пользователя или пароль.</p>
                        </div>
                    </c:if>
                    <c:if test="${param.logout != null}">
                        <div class="alert alert-success" id="success">
                            <p>Вы успешно вышли из системы.</p>
                        </div>
                    </c:if>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="username"><i class="fa fa-user"></i></label>
                        <input type="text" class="form-control" id="username" name="login" placeholder="Имя пользователя" required>
                    </div>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Пароль" required>
                    </div>

                    <div class="input-group input-sm">
                        <div class="checkbox">
                            <label><input type="checkbox" id="rememberme" name="remember-me">Запомнить меня</label>
                        </div>
                    </div>
<%--                    <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />--%>

                    <div class="form-actions">
                        <input type="submit"
                               class="btn btn-block btn-primary btn-default" value="Войти">
                    </div>
                    <br>
                    <a href="<c:url value='/forgotPassword' />" class="btn btn-link">Забыл пароль</a>
                </form:form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
