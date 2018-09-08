<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Выписка Альфа банка</title>
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
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Выписка Альфа банка:</span></div>

        <sec:authorize access="isFullyAuthenticated()">
            <sec:authorize access="hasRole('ADMIN')">
                <div class="well">
                    <form:form method="POST" action="upload" modelAttribute="fileBucket" enctype="multipart/form-data" class="form-horizontal">

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="file">Загрузить файл</label>
                                <div class="col-md-7">
                                    <form:input type="file" path="file" id="file" class="form-control input-sm"/>
                                    <div class="has-error">
                                        <form:errors path="file" class="help-inline"/>
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary btn-sm">Загрузить</button>
                                <a href="<c:url value='/deletedata' />" class="btn btn-danger btn-sm">Удалить</a>
                            </div>
                        </div>


                    </form:form>
                </div>
            </sec:authorize>
        </sec:authorize>

        <table class="table table-hover" style="font-size: small">
            <thead>
            <tr>
                <th>ID</th>
                <th>Родительский ID</th>
                <th>Дата операции</th>
                <th>Дебет</th>
                <th>Кредит</th>
                <th>Название организации</th>
                <th>ИНН</th>
                <th>№ счёта</th>
                <th>Назначение платежа</th>
                <th>ТЭГ</th>
                <th>Период</th>
                <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                    <th>Действие</th>
                </sec:authorize>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${alphaExtracts}" var="alpha">
                <tr>
                    <td>${alpha.id}</td>
                    <td>${alpha.pId}</td>
                    <td>${alpha.getDateOperToLocalDate()}</td>
                    <td>${alpha.debet}</td>
                    <td>${alpha.credit}</td>
                    <td>${alpha.orgName}</td>
                    <td>${alpha.inn}</td>
                    <td>${alpha.account}</td>
                    <td>${alpha.purposePayment}</td>
                    <td>${alpha.tags.correctTag}</td>
                    <td>${alpha.getPeriodToLocalDate()}</td>
                    <sec:authorize access="isFullyAuthenticated()">
                        <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                            <td style="text-align: center">
                                <div class="btn-group">
                                    <button type="button" data-toggle="dropdown"
                                            class="btn btn-primary btn-sm dropdown-toggle">Действие <span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="<c:url value='/edit-alphaextract-${alpha.id}' />">Изменить</a></li>
                                        <li><a href="<c:url value='/double-alphaextract-${alpha.id}' />">Разделить</a></li>
                                        <li class="divider"></li>
                                        <li><a href="<c:url value='/delete-alphaextract-${alpha.id}' />" style="color: red">Удалить</a></li>
                                    </ul>
                                </div>
                            </td>
                        </sec:authorize>
                    </sec:authorize>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>