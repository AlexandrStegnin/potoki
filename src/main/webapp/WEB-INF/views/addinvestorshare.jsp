<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Форма внесения долей инвесторов</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="investorsShare" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="facilities">Объект:</label>
                <div class="col-md-7">
                    <form:select path="facility" id="facilities" items="${facilities}" multiple="false"
                                 itemValue="id" itemLabel="facility" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="facility" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="investor">Инвестор:</label>
                <div class="col-md-7">
                    <form:select path="investor" id="investor" items="${investors}" multiple="false"
                                 itemValue="id" itemLabel="login" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="investor" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="share">Доля:</label>
                <div class="col-md-7">
                    <form:input type="number" path="share" id="share" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="share" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="managerExpenses">Расходы на управление:</label>
                <div class="col-md-7">
                    <form:input type="number" path="managerExpenses" id="managerExpenses" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="managerExpenses" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="taxationExpenses">Расходы на налоги:</label>
                <div class="col-md-7">
                    <form:input type="number" path="taxationExpenses" id="taxationExpenses" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="taxationExpenses" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cashingExpenses">Расходы на обналичку:</label>
                <div class="col-md-7">
                    <form:input type="number" path="cashingExpenses" id="cashingExpenses" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="cashingExpenses" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="ipManagerExpenses">Расходы на управление ИП:</label>
                <div class="col-md-7">
                    <form:input type="number" path="ipManagerExpenses" id="ipManagerExpenses" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="ipManagerExpenses" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateStManExp">Дата нач. расх. от управления:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateStManExp" id="dateStManExp" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="dateStManExp" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateEndManExp">Дата оконч. расх. от управления:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateEndManExp" id="dateEndManExp" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="dateEndManExp" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="tempExpenses">Временные расходы:</label>
                <div class="col-md-7">
                    <form:input type="number" path="tempExpenses" id="tempExpenses" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="tempExpenses" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateStTempExp">Дата начала вр. расходов:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateStTempExp" id="dateStTempExp" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="dateStTempExp" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateEndTempExp">Дата окончания вр. расходов:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateEndTempExp" id="dateEndTempExp" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="dateEndTempExp" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Обновить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/investorsshare' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Создать" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/investorsshare' />">Отмена</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
</body>
</html>