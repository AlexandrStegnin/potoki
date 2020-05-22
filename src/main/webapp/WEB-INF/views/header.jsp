<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-expand-md navbar-light sticky-top" style="background-color: white;">
    <sec:authentication var="user" property="principal"/>
    <sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal.username" var="username"/>
    </sec:authorize>
    <sec:authorize access="!isAuthenticated()">
        <sec:authentication property="principal" var="username"/>
    </sec:authorize>
    <a href="#" class="navbar-brand">
        <img src="<c:url value='/resources/core/img/logo.png' />" height="56" alt="ДД Колесникъ">
    </a>
    <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarCollapse">
        <div class="navbar-nav ml-auto">
            <sec:authorize access="hasRole('INVESTOR') AND !hasRole('ADMIN')">
                <li class="nav-item mr-2">
                    <a id="balance" href="#" class="nav-link" style="color: #0D345D;">
                        <span class="fas fa-ruble-sign" style="color: #0D345D;"></span>
                        Баланс: <span id="balanceText">0</span>
                    </a>
                </li>
            </sec:authorize>
            <li class="nav-item mr-2">
                <a id="profile" href="<c:url value='/profile' />" class="nav-link" style="color: #0D345D;">
                    <span class="fas fa-lock" style="color: #0D345D;"></span>
                    Личный кабинет / <b>${username}</b>
                </a>
            </li>
            <li class="nav-item mr-2">
                <a id="home" href="<c:url value='/welcome' />" class="nav-link" style="color: #0D345D;">
                    <span class="fas fa-home" style="color: #0D345D;"></span>
                    На главную
                </a>
            </li>

            <sec:authorize access="hasRole('ADMIN')">
            <li class="nav-item dropdown mr-2">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button"
                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="color: #0D345D;">
                         <span class="fas fa-cogs" style="color: #0D345D;"></span> Администрирование<span class="caret" style="color: #0D345D;"></span>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                        <a href="<c:url value='/catalogue' />" class="dropdown-item">
                            <span style="color: #0D345D;" class="fas fa-book"></span> Справочники
                        </a>
                        <a href="<c:url value='/newuser' />" class="dropdown-item">
                            <span style="color: #0D345D;" class="fas fa-plus"></span> Добавить пользователя
                        </a>
                        <a href="<c:url value='/newfacility' />" class="dropdown-item">
                            <span style="color: #0D345D;" class="fas fa-plus"></span> Добавить объект
                        </a>
                        <a href="<c:url value='/updateInvestorDemo' />" id="updateInvestorDemo" class="dropdown-item">
                            <span style="color: #0D345D;" class="fas fa-sync-alt"></span> Обновить инвестор-демо
                        </a>
                </div>
            </li>
            </sec:authorize>
            <li class="nav-item mr-2">
                <a id="exit" href="<c:url value='/logout' />" class="nav-link" style="color: #0D345D;">
                    <span class="fas fa-sign-out-alt" style="color: #0D345D;"></span>
                    Выйти
                </a>
            </li>
        </div>
    </div>
</nav>
