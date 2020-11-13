<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>Профиль</title>
    <sec:csrfMetaTags/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"
          rel="stylesheet"/>
<%--    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>--%>
    <link href="<c:url value='/resources/core/css/annex.css' />" rel="stylesheet"/>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<%@include file="annex_popup.jsp" %>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <c:choose>
        <c:when test="${annexCnt > 0}">
            <c:set var="annexTitle" value="Не прочитанные приложения"/>
            <c:set var="errTitle"
                   value="Чтобы продолжить пользоваться сайтом, необходимо ознакомиться со всеми приложениями"/>
            <c:set var="btnClass" value="btn-warning"/>
            <c:set var="aCnt" value="${annexCnt}"/>
            <c:set var="disabledSubmit" value="disabled"/>
        </c:when>
        <c:otherwise>
            <c:set var="annexTitle" value="Приложения"/>
            <c:set var="errTitle" value=""/>
            <c:set var="btnClass" value="btn-success"/>
            <c:set var="aCnt" value="${totalAnnex}"/>
            <c:set var="disabledSubmit" value=""/>
        </c:otherwise>
    </c:choose>

    <div class="d-flex flex-row justify-content-end">
        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasAnyRole('BIGDADDY', 'ADMIN')">
                <form:form method="POST" modelAttribute="user" class="form-horizontal col-md-4" id="flowsAdmin"
                           action="/investments">
                    <form:select path="login" id="investors" class="selectpicker" data-container="body"
                                 title="Выберите инвестора..." multiple="false"
                                 data-live-search="true" data-size="7">
                        <form:options items="${investors}" itemValue="login" itemLabel="login"/>
                    </form:select>
                    <form:button type="submit" class="btn btn-success btn-sm" id="viewInvestorData"
                            style="margin-right: 5px" disabled="true">
                        <c:out value="Посмотреть кабинет"/></form:button>
                </form:form>
            </sec:authorize>
        </sec:authorize>
        <button type="button" class="btn ${btnClass} btn-md" id="unread" style="margin-left: 5px">
            <c:out value="${annexTitle}"/> <span id="annexCnt" class="badge">${aCnt}</span></button>

        <button type="button" class="btn btn-warning btn-md pull-right" id="clearLS" style="margin-left: 5px">
            <c:out value="Очистить local storage"/>
        </button>
        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasAnyRole('BIGDADDY', 'ADMIN')">
                <button type="button" class="btn btn-success btn-md" id="updateBitrixContact"
                        style="margin-left: 5px">
                    <c:out value="Обновить контакты из Битрикс"/></button>
            </sec:authorize>
        </sec:authorize>
        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasRole('ADMIN')">
                <a id="updateMarketingTree" href="<c:url value='updateMarketingTree' />"
                   class="btn btn-success btn-md pull-right" style="margin-right: 5px">Обновить
                    маркетинговое дерево</a>
            </sec:authorize>
        </sec:authorize>
    </div>
    <div style="text-align: center; padding-bottom: 20px; color: red"><span id="errUnread">${errTitle}</span></div>


    <form:form method="POST" modelAttribute="user" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="form-group row">
            <label class="col-md-2 offset-md-2 col-form-label-md" for="password">Пароль:</label>
            <div class="col-md-6">
                <form:input type="password" path="password" id="password" class="form-control form-control-md"/>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-md-2 offset-md-2 col-form-label-md" for="email">Email</label>
            <div class="col-md-6">
                <form:input type="email" path="profile.email" id="email" class="form-control form-control-md"/>
                <div class="has-error">
                    <form:errors path="profile.email" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-md-2 offset-md-2 col-form-label-md" for="acc-number">№ счёта</label>
            <div class="col-md-6">
                <input type="text" id="acc-number" class="form-control input-sm" value="${accountNumber}" readonly />
            </div>
        </div>

        <div class="form-group row justify-content-end col-md-8 offset-md-2" style="padding-right: 5px">
            <button type="submit" class="btn btn-primary btn-md" id="submit">СОХРАНИТЬ</button>
            <a href="<c:url value='/welcome' />" class="btn btn-md btn-danger" style="margin-left: 5px;" role="button">ОТМЕНА</a>
        </div>
    </form:form>

</div>

<%--<%@include file="ddk_loader.jsp" %>--%>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
<link rel="stylesheet"
      href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.8.1/css/bootstrap-select.css' />">
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.8.1/js/bootstrap-select.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
<%--<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>--%>
<script type="text/javascript" src="<c:url value='/resources/core/js/annex.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/profiles.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/bitrix-contacts.js' />"></script>
</body>
</html>
