<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/datatables.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/jquery-ui.min.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript"
            src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.0.4/popper.js" /> "></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-ui.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/forLoadPdfFiles.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/datatables.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/resources/core/js/profiles.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bitrix-contacts.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
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

    <div class="well lead" style="margin-right: 30px; min-height: 70px"><span class="pull-left">${title}</span>
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
        <button type="button" class="btn ${btnClass} btn-sm pull-right" id="unread">
            <c:out value="${annexTitle}"/> <span id="annexCnt" class="badge">${aCnt}</span></button>
        <button type="button" class="btn btn-warning btn-sm pull-right" id="clearLS" style="margin-right: 5px">
            <c:out value="Очистить local storage"/>
        </button>
        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasAnyRole('BIGDADDY', 'ADMIN')">
                <button type="button" class="btn btn-success btn-sm pull-right" id="updateBitrixContact"
                        style="margin-right: 5px">
                    <c:out value="Обновить контакты из Битрикс"/></button>
            </sec:authorize>
        </sec:authorize>
    </div>
    <div style="text-align: center; padding-bottom: 20px; color: red"><span id="errUnread">${errTitle}</span></div>


    <form:form method="POST" modelAttribute="user" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="password">Пароль</label>
                <div class="col-md-9">
                    <form:input type="password" path="password" id="password" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="password" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="email">Email</label>
                <div class="col-md-9">
                    <form:input type="text" path="profile.email" id="email" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="profile.email" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="acc-number">№ счёта</label>
                <div class="col-md-9">
                    <input type="text" id="acc-number" class="form-control input-sm" value="${accountNumber}" readonly />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group-sm col-md-12">
                <sec:authorize access="isRememberMe()">
                    <p>Вы вошли с помощью функции "Запомнить меня".
                        Чтобы иметь все права на данную страницу, Вам необходимо снова
                        <a href="<c:url value='/login' />">ВОЙТИ</a> в систему используя логин/пароль.
                    </p>

                </sec:authorize>

                <div class="form-group-sm col-md-5 pull-right">
                    <input id="send" type="submit" value="Сохранить" ${disabledSubmit}
                           class="btn btn-primary btn-sm col-md-offset-8"/> или
                    <a href="<c:url value='/welcome' />" id="cancel" ${disabledSubmit} class="btn btn-link btn-sm">Отменить</a>
                </div>
            </div>
        </div>
    </form:form>

</div>

<div id="allPdf" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 90%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4></h4>
            </div>
            <div class="modal-body">
                <table class="ui celled table compact" cellspacing="0" width="100%" id="tblAnnex">
                    <thead>
                    <tr>
                        <th style="text-align: center">ID</th>
                        <th style="text-align: center">Приложение</th>
                        <th style="text-align: center">Ссылка</th>
                        <th style="text-align: center">Ознакомление</th>
                        <th style="text-align: center">Дата ознакомления</th>

                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${usersAnnexToContractsList}" var="annex">
                        <tr id="${annex.id}">

                            <c:choose>
                                <c:when test="${annex.annexRead == 0}">
                                    <c:set var="checked" value=""/>
                                    <c:set var="disabled" value=""/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="checked" value="checked"/>
                                    <c:set var="disabled" value="disabled"/>
                                </c:otherwise>
                            </c:choose>

                            <td style="text-align: center">${annex.id}</td>
                            <td style="text-align: center">${annex.annex.annexName}</td>
                            <td data-annex-name="${annex.annex.annexName}" style="text-align: center"><a
                                    href="<c:url value='/annexToContract/${annex.annex.annexName}' />" target="_blank"
                            >Посмотреть</a></td>
                            <td style="text-align: center">
                                <label for="annexId${annex.id}">Ознакомлен</label>
                                <input type="checkbox" id="annexId${annex.id}" ${checked} ${disabled}
                                       data-user-id="${user.id}"
                                       data-annex-id="${annex.annex.id}"
                                       data-annex-name="${annex.annex.annexName}"/>
                            </td>
                            <td style="text-align: center"
                                id="annexDate-${annex.annex.id}">${annex.getDateReadToLocalDate()}</td>

                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer" style="text-align: center; color: red;">
                <span id="successDelete"></span>
                <button type="button" class="btn btn-primary pull-right" id="closeAllPdf">Закрыть</button>
            </div>
        </div>
    </div>
</div>
<div id="msg-modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-body" id="msg" style="text-align: center"></div>
        </div>
    </div>
</div>
<%@include file="slideDiv.jsp" %>

<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.8.1/css/bootstrap-select.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.8.1/js/bootstrap-select.js"></script>
</body>
</html>
