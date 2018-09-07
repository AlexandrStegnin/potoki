<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
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
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="facilitiesServiceContracts" class="form-horizontal">
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
                <label class="col-md-3 control-lable" for="rentors">Арендатор:</label>
                <div class="col-md-7">
                    <form:select path="rentor" id="rentors" items="${rentors}" multiple="false"
                                 itemValue="id" itemLabel="login" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="rentor" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cashpayments">Вид обслуживания:</label>
                <div class="col-md-7">
                    <form:select path="paymentsMethod" id="cashpayments" items="${paymentsMethod}" multiple="false"
                                 itemValue="id" itemLabel="payment" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="paymentsMethod" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cNumber">Номер договора:</label>
                <div class="col-md-7">
                    <form:input type="text" path="contractNumber" id="cNumber" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="facility" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateStartContract">Дата начала:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateStartContract" id="dateStartContract" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="dateStartContract" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dayToPay">Срок оплаты:</label>
                <div class="col-md-7">
                    <form:input type="number" path="dayToPay" id="dayToPay" class="form-control input-sm"
                                min="0.0" step="1"/>
                    <div class="has-error">
                        <form:errors path="dayToPay" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="area">Квадратура (м2):</label>
                <div class="col-md-7">
                    <form:input type="number" path="area" id="area" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="area" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="summPayment">Ежемесячный платёж:</label>
                <div class="col-md-7">
                    <form:input type="number" path="summPayment" id="summPayment" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="summPayment" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="discount">Скидка:</label>
                <div class="col-md-7">
                    <form:input type="number" path="discount" id="discount" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                    <div class="has-error">
                        <form:errors path="discount" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="timeDiscount">Сколько месяцев:</label>
                <div class="col-md-7">
                    <form:input type="number" path="timeDiscount" id="timeDiscount" class="form-control input-sm"
                                min="0.0" step="1"/>
                    <div class="has-error">
                        <form:errors path="timeDiscount" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="comments">Комментарий:</label>
                <div class="col-md-7">
                    <form:input type="text" path="comments" id="comments" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="comments" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Обновить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/viewfacilitiesservicecontracts' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Создать" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/viewfacilitiesservicecontracts' />">Отмена</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>