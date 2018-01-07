<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Список видов долей</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForInvestorsCash.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/popupScripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/forShareKind.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Вид доли:</span></div>
        <table class="table table-hover" id="tblShareKinds">
            <thead>
            <tr>
                <th>№ п/п</th>
                <th>Вид доли</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th style="text-align: center" colspan="2">Действие</th>
                </sec:authorize>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${shareKindList}" var="kinds">
                <tr id="${kinds.id}">
                    <td>${kinds.id}</td>
                    <td>${kinds.shareKind}</td>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td style="text-align: center"><a href="<c:url value='/edit-shareKind-${kinds.id}' />" id="editShare" name="${kinds.id}"
                                                              data-toggle="${kinds.shareKind}"
                                                              class="btn btn-success custom-width">Изменить</a></td>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ADMIN')">
                            <td style="text-align: center"><a href="<c:url value='/deleteShareKind'/>" id="delete" name="${kinds.id}"
                                                              class="btn btn-danger custom-width">Удалить</a></td>
                        </sec:authorize>
                    </sec:authorize>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>


    <sec:authorize access="isRememberMe()">
        <p>Вы вошли с помощью функции "Запомнить меня".
            Чтобы иметь все права на данную страницу, Вам необходимо снова
            <a href="<c:url value='/login' />">ВОЙТИ</a> в систему используя логин/пароль.
        </p>

    </sec:authorize>

    <sec:authorize access="isFullyAuthenticated()">
        <sec:authorize access="hasRole('ADMIN')">
            <div class="well">
                <a href="<c:url value='/newShareKind' />" id="newShareKind">Добавить вид доли</a>
            </div>
        </sec:authorize>
    </sec:authorize>
</div>
<%@include file="loader.jsp" %>
<%@include file="popup.jsp" %>

<div id="bs-modal-shareKind" class="modal fade" role="dialog">
    <div class="modal-dialog" style="width: 90%">
        <div class="modal-content" id="shareKind-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 id="formTitle"></h4>
            </div>
            <div class="modal-body">
                <form:form method="POST" modelAttribute="shareKind" class="form-horizontal" id="shareKind">
                    <form:input type="hidden" path="id" id="id"/>
                    <div class="row">
                        <div class="form-group col-md-12">
                            <label class="col-md-3 control-lable" for="shareKindName">Вид доли:</label>
                            <div class="col-md-7">
                                <form:input type="text" path="shareKind" id="shareKindName" class="form-control input-sm"/>
                                <div class="has-error">
                                    <form:errors path="shareKind" class="help-inline"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
            <div class="modal-footer">
                <div class="row">
                    <div class="form-actions floatRight">
                        <a href="<c:url value='/#' />" class="btn btn-primary btn-sm" id="saveEdit">Создать</a> или
                        <a href="<c:url value='/viewShareKind' />" class="btn btn-link btn-sm" id="cancelShare">Отмена</a>
                    </div>
                </div>
            </div>
            </div>

        </div>
    </div>
</div>

</body>
</html>