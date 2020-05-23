<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Добавить помещение</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/roomsScripts.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="rooms" class="form-horizontal">
        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="underFacility">Подобъект:</label>
                <div class="col-md-7">
                    <form:select path="underFacility" id="underFacility" items="${underFacilities}" multiple="false"
                                 itemValue="id" itemLabel="underFacility" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="room">Помещение:</label>
                <div class="col-md-7">
                    <form:input type="text" path="room" id="room" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="roomSize">Квадратура:</label>
                <div class="col-md-7">
                    <form:input type="number" path="roomSize" id="roomSize" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="coast">Стоимость:</label>
                <div class="col-md-7">
                    <form:input type="number" path="coast" id="coast" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="coast">Дата покупки:</label>
                <div class="col-md-7">
                    <form:input type="date" path="buyDate" id="buyDate" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="sold">Продано:</label>
                <div class="col-md-7">
                    <select id="sold" class="form-control input-sm">
                        <option <c:if test="${rooms.sold == true}"> selected="selected" </c:if> value="Нет" id="0">Нет</option>
                        <option <c:if test="${rooms.sold == true}"> selected="selected" </c:if> value="Да" id="1">Да</option>
                    </select>
                    <label for="soldHid" style="display: none;"></label>
                    <form:checkbox path="sold" id="soldHid" style="display: none;" />
                </div>
            </div>
        </div>

        <div class="row" id="dateSaleRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateSale">Дата продажи:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateOfSale" id="dateSale" class="form-control input-sm"/>
                </div>
                <div id="dateSaleErr" style="color: red; display: none" class="col-md-2 input-sm">
                    Необходимо ввести дату продажи
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="coast">Цена продажи:</label>
                <div class="col-md-7">
                    <form:input type="number" path="salePrice" id="salePrice" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="coast">Годовая доходность:</label>
                <div class="col-md-7">
                    <form:input type="number" path="totalYearProfit" id="yearProfit" class="form-control input-sm"
                                min="0.0" step="0.01"/>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Обновить" class="btn btn-primary btn-sm"/> или <a
                            href="<c:url value='/rooms' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Создать" class="btn btn-primary btn-sm"/> или <a
                            href="<c:url value='/rooms' />">Отмена</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
<%@include file="slideDiv.jsp" %>
</body>
</html>
