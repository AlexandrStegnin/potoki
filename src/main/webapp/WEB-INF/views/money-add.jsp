<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Форма внесения денег инвесторов</title>
    <sec:csrfMetaTags/>
    <link rel="stylesheet"
          href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css' />"/>
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <link rel="stylesheet"
          href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.14/css/bootstrap-select.min.css' />">
    <link href="<c:url value='/resources/core/css/generic-container.css' />" rel="stylesheet"/>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<%@include file="header.jsp" %>
<div class="container">
    <form:form method="POST" modelAttribute="money" class="form-horizontal generic-container"
               style="margin: 10px 0 10px 0" id="iCashTable">
        <form:input type="hidden" path="id" id="id"/>
        <input type="hidden" id="newCash" value="${newCash}"/>
        <input type="hidden" id="edit" value="${edit}"/>
        <input type="hidden" id="doubleCash" value="${doubleCash}"/>
        <input type="hidden" id="closeCash" value="${closeCash}"/>
        <div class="form-group row" id="facilitiesRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="facilities">Объект:</label>
            <div class="col-sm-6">
                <form:select path="facility" id="facilities" items="${facilities}" multiple="false"
                             itemValue="id" itemLabel="name" class="form-control form-control-sm selectpicker"
                             data-size="10" data-live-search="true" data-none-selected-text="Выберите объект"/>
                <div class="has-error">
                    <form:errors path="facility" class="help-inline"/>
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test="${doubleCash}">
                <c:set var="uf" value="${underFacilitiesList}"/>
            </c:when>
            <c:otherwise>
                <c:set var="uf" value="${underFacilities}"/>
            </c:otherwise>
        </c:choose>

        <div class="form-group row" id="underFacilitiesRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="underFacilities">Подобъект:</label>
            <div class="col-sm-6">
                <form:select path="underFacility" id="underFacilities" multiple="false"
                             class="form-control form-control-sm selectpicker" data-none-selected-text="Без подобъекта"
                             data-live-search="true" data-size="10">
                    <c:forEach var="uf" items="${uf}">
                        <form:option value="${uf.name}" id="${uf.id}" data-parent-id="${uf.facility.id}">

                        </form:option>
                    </c:forEach>
                </form:select>
                <div class="has-error">
                    <form:errors path="underFacility" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group row" id="investorRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="investor">Инвестор:</label>
            <div class="col-sm-6">
                <form:select path="investor" id="investor" items="${investors}" multiple="false"
                             itemValue="id" itemLabel="login" class="form-control form-control-sm selectpicker"
                             data-live-search="true" data-size="10" data-none-selected-text="Выберите инвестора"/>
                <div class="has-error">
                    <form:errors path="investor" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group row" id="cashRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="cash">Переданная сумма:</label>
            <div class="col-sm-6">
                <form:input type="number" path="givenCash" id="cash" class="form-control form-control-sm"
                            min="0.0" step="any"/>
            </div>
        </div>

<%--        <div class="form-group row" hidden>--%>
<%--            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="source">Источник:</label>--%>
<%--            <div class="col-sm-6">--%>
<%--                <form:input type="text" path="source" id="source" class="form-control form-control-sm selectpicker"/>--%>
<%--            </div>--%>
<%--        </div>--%>

        <div class="form-group row" id="dateGivedCashRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="dateGivedCash">Дата передачи денег:</label>
            <div class="col-sm-6">
                <form:input type="date" path="dateGiven" id="dateGivedCash" class="form-control form-control-sm"/>
                <div class="has-error">
                    <form:errors path="dateGiven" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group row" id="cashSrcRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="cashSrc">Источник получения денег:</label>
            <div class="col-sm-6">
                <form:select path="cashSource" id="cashSrc" items="${cashSources}" multiple="false"
                             itemValue="id" itemLabel="name" class="form-control form-control-sm selectpicker"
                             data-size="10" data-none-selected-text="Выберите источник денег"/>
                <div class="has-error">
                    <form:errors path="cashSource" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group row" id="cashDetailRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="cashDetail">Детали новых денег:</label>
            <div class="col-sm-6">
                <form:select path="newCashDetail" id="cashDetail" items="${newCashDetails}" multiple="false"
                             itemValue="id" itemLabel="name" class="form-control form-control-sm selectpicker"
                             data-none-selected-text="Выберите детали новых денег"/>
                <div class="has-error">
                    <form:errors path="newCashDetail" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group row" id="dateCloseInvRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="dateCloseInv">Дата закрытия вложения:</label>
            <div class="col-sm-6">
                <form:input type="date" path="dateClosing" id="dateCloseInv"
                            class="form-control form-control-sm"
                            readonly="${edit}"/>
                <div class="has-error" id="reInvestDateErr">
                </div>
            </div>
        </div>

        <div class="form-group row" id="typeClosingRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="typeClosing">Вид закрытия вложения:</label>
            <div class="col-sm-6">
                <form:select path="typeClosing" id="typeClosing" items="${typeClosingInvest}"
                             multiple="false"
                             itemValue="id" itemLabel="name" class="form-control form-control-sm selectpicker"
                             data-none-selected-text="Выберите вид закрытия"
                             readonly="${edit}"/>
                <div class="has-error">
                    <form:errors path="typeClosing" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group row" id="investorBuyerRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="investorBuyer">Инвестор покупатель:</label>
            <div class="col-sm-6">
                <form:select path="investorBuyer" id="investorBuyer" items="${investors}" multiple="false"
                             itemValue="id" itemLabel="login" class="form-control form-control-sm selectpicker"
                             data-size="10" data-none-selected-text="Выберите инвнстора"/>
                <div class="has-error" id="investorBuyerErr">
                </div>
            </div>
        </div>

        <div class="form-group row" id="dateRepRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="dateRep">Период расчёта:</label>
            <div class="col-sm-6">
                <form:input type="date" path="dateReport" id="dateRep" class="form-control form-control-sm"/>
                <div class="has-error" id="dateRepErr">
                </div>
            </div>
        </div>

        <div class="form-group row" style="display: none;" id="sourceFacility">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="sourceFacilities" id="sourceFacilitiesLbl">Объект
                реинвестирования:</label>
            <div class="col-sm-6">
                <form:select path="sourceFacility" id="sourceFacilities" items="${sourceFacilities}"
                             multiple="false"
                             itemValue="id" itemLabel="name" class="form-control form-control-sm selectpicker"
                             data-size="10" data-none-selected-text="Без объекта"/>
                <div class="has-error" id="sourceFacilityErr">
                </div>
            </div>
        </div>

        <div class="form-group row" style="display: none;" id="sourceUnderFacility">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="sourceUnderFacilities"
                   id="sourceUnderFacilitiesLbl">Подобъект
                реинвестирования:</label>
            <div class="col-sm-6">
                <form:select path="sourceUnderFacility" id="sourceUnderFacilities" multiple="false"
                             class="form-control form-control-sm selectpicker"
                             data-size="10">
                    <c:forEach var="uf" items="${sourceUnderFacilities}">
                        <form:option value="${uf.name}" id="${uf.id}" data-parent-id="${uf.facility.id}">

                        </form:option>
                    </c:forEach>
                </form:select>

                <div class="has-error" id="reUnderFacilityErr">
                </div>
            </div>
        </div>

        <div class="form-group row" id="shareKindNameRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="shareTypeName">Вид доли:</label>
            <div class="col-sm-6">
                <form:select path="shareType" id="shareTypeName" items="${shareTypes}" multiple="false"
                             itemValue="id" itemLabel="title" class="form-control form-control-sm selectpicker"/>
                <div class="has-error">
                    <form:errors path="shareType" class="help-inline"/>
                </div>
            </div>
        </div>

        <div class="form-group row" id="realDateGivenRow">
            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="realDateGiven">Дата реальной передачи денег:</label>
            <div class="col-sm-6">
                <form:input type="date" path="realDateGiven" id="realDateGiven" class="form-control form-control-sm"/>
                <div class="has-error" id="realDateGivenErr">
                </div>
            </div>
        </div>

        <div class="form-group row justify-content-end col-sm-8 offset-sm-2">
            <c:choose>
                <c:when test="${edit == true}">
                    <c:set var="title" value="Обновить"/>
                </c:when>
                <c:otherwise>
                    <c:set var="title" value="Создать"/>
                </c:otherwise>
            </c:choose>
            <div class="col-sm-2 col-sm-offset-2">
            <button type="submit" class="btn btn-primary btn-sm">${title}</button>
            </div>
            <a href="<c:url value='/money/list' />" class="btn btn-sm btn-danger" role="button">Отмена</a>
        </div>
    </form:form>
</div>
<%@include file="ddk_loader.jsp" %>
<%@include file="popup_modal.jsp" %>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js' />"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.14/js/bootstrap-select.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/monies.js' />"></script>
</body>
</html>
