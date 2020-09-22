<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar fixed-top navbar-light bg-faded">
    <sec:authentication var="user" property="principal" />
    <sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal.username" var="username" />
    </sec:authorize>
    <sec:authorize access="!isAuthenticated()">
        <sec:authentication property="principal" var="username" />
    </sec:authorize>
    <!-- Контейнер (определяет ширину Navbar) -->
    <div class="container-fluid">
        <!-- Заголовок -->
        <div class="navbar-header">
            <!-- Кнопка «Гамбургер» отображается только в мобильном виде (предназначена для открытия основного содержимого Navbar) -->
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-main">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <!-- Бренд или название сайта (отображается в левой части меню) -->
            <a class="navbar-brand" href="#" style="margin-left: 60px;">
                <span style="max-width: 5px; float: left; margin-left: -60px; margin-top: 20px">
                    <img src="<c:url value='/resources/core/img/logo.png' />" style="max-width:60px; margin-top: -18px;"
                         alt="ДД Колесникъ"></span><p style="margin-left: 10px; margin-top: 20px">ДД Колесникъ</p>
            </a>
        </div>
        <!-- Основная часть меню (может содержать ссылки, формы и другие элементы) -->
        <div class="collapse navbar-collapse" id="navbar-main">
            <!-- Содержимое основной части -->
            <ul class="nav navbar-nav navbar-right">
                <sec:authorize access="hasRole('INVESTOR') and !hasRole('ADMIN')">
                <li>
                    <a id="balance" href="#">
                        <span class="glyphicon glyphicon-rub"style="margin-right: 5px; color: dodgerblue;"></span>
                        Баланс: <span>0</span>
                    </a>
                </li>
                </sec:authorize>
                <li>
                    <a id="profile" href="/profile">
                        <span class="glyphicon glyphicon-lock"style="margin-right: 5px; color: dodgerblue;"></span>
                        Личный кабинет / <b>${username}</b>
                    </a>
                </li>
                <li>
                    <a id="home" href="/welcome">
                        <span class="glyphicon glyphicon-home"style="margin-right: 5px; color: dodgerblue;"></span>
                        На главную
                    </a>
                </li>
                <li>
                    <a id="exit" href="/logout">
                        <span class="glyphicon glyphicon-log-out"style="margin-right: 5px; color: dodgerblue;"></span>
                        Выйти
                    </a>
                </li>
                <sec:authorize access="hasRole('ADMIN')">
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <span class="glyphicon glyphicon-cog" style="margin-right: 5px; color: dodgerblue;">
                        </span>Администрирование<span class="caret" style="margin-left: 5px;
                            color: dodgerblue;"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/catalogue">
                            <span style="margin-right: 5px; color: dodgerblue;"
                                  class="glyphicon glyphicon-book"></span>Справочники</a></li>
                            <li><a href="/users/create">
                            <span style="margin-right: 5px;  color: dodgerblue;"
                                  class="glyphicon glyphicon-plus"></span>Добавить пользователя</a></li>
                            <li><a href="/facilities/create">
                            <span style="margin-right: 5px;  color: dodgerblue;"
                                  class="glyphicon glyphicon-plus"></span>Добавить объект</a></li>
                            <li><a href="#"><span style="margin-right: 5px;  color: dodgerblue;" class="glyphicon glyphicon-off"></span><div class="material-switch pull-right" style="margin-top: 10px;">
                                <input id="someSwitchOptionWarning" name="someSwitchOption001" type="checkbox">
                                <label for="someSwitchOptionWarning" id="someSwitchOptionWarningLbl" class="label-warning"></label>
                            </div>Отключить сайт</a></li>

                        </ul>
                    </li>
                </sec:authorize>

            </ul>
        </div>
    </div>
</nav>
