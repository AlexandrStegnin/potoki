<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Форма внесения денег инвесторов</title>
    <sec:csrfMetaTags />
    <link href="<c:url value='/resources/core/css/bootstrap.min.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet" />
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet" />
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jsFunctions.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/mailingScripts.js' />" ></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scriptsForInvestorsCash.js' />" ></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="authheader.jsp" %>
    <div class="well lead">${title}</div>
    <form:form method="POST" modelAttribute="investorsCash" class="form-horizontal" id="iCashTable">
        <form:input type="hidden" path="id" id="id"/>
        <form:input type="hidden" path="" id="newCash" value="${newCash}"/>
        <form:input type="hidden" path="" id="edit" value="${edit}"/>
        <form:input type="hidden" path="" id="doubleCash" value="${doubleCash}"/>
        <form:input type="hidden" path="" id="closeCash" value="${closeCash}"/>
        <div class="row" id="facilitiesRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="facilities">Объект:</label>
                <div class="col-md-7">
                    <form:select path="facility" id="facilities" items="${facilities}" multiple="false"
                                 itemValue="id" itemLabel="facility" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="facility" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test="${doubleCash}">
                <c:set var="uf" value="${underFacilitiesList}" />
            </c:when>
            <c:otherwise>
                <c:set var="uf" value="${underFacilities}" />
            </c:otherwise>
        </c:choose>

        <div class="row" id="underFacilitiesRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="underFacilities">Подобъект:</label>
                <div class="col-md-7">
                    <form:select path="underFacility" id="underFacilities" multiple="false" class="form-control input-sm">
                        <c:forEach var="uf" items="${uf}">
                            <form:option value="${uf.underFacility}" id="${uf.id}" data-parent-id="${uf.facilityId}">

                            </form:option>
                        </c:forEach>
                    </form:select>
                    <div class="has-error">
                        <form:errors path="underFacility" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="investorRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="investor">Инвестор:</label>
                <div class="col-md-7">
                    <form:select path="investor" id="investor" items="${investors}" multiple="false"
                                 itemValue="id" itemLabel="login" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="investor" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="cashRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cash">Переданная сумма:</label>
                <div class="col-md-7">
                    <form:input type="number" path="givedCash" id="cash" class="form-control input-sm"
                                min="0.0" step="any"/>
                    <div class="has-error">
                        <form:errors path="givedCash" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" hidden>
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="source">Источник:</label>
                <div class="col-md-7">
                    <form:input type="number" path="source" id="source" class="form-control input-sm"
                                min="0.0" step="any"/>
                </div>
            </div>
        </div>

        <div class="row" id="dateGivedCashRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateGivedCash">Дата передачи денег:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateGivedCash" id="dateGivedCash" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="dateGivedCash" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="cashSrcRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cashSrc">Источник получения денег:</label>
                <div class="col-md-7">
                    <form:select path="cashSource" id="cashSrc" items="${cashSources}" multiple="false"
                                 itemValue="id" itemLabel="cashSource" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="cashSource" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="cashTypRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cashTyp">Вид денег:</label>
                <div class="col-md-7">
                    <form:select path="cashType" id="cashTyp" items="${cashTypes}" multiple="false"
                                 itemValue="id" itemLabel="cashType" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="cashType" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="cashDetailRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="cashDetail">Детали новых денег:</label>
                <div class="col-md-7">
                    <form:select path="newCashDetails" id="cashDetail" items="${newCashDetails}" multiple="false"
                                 itemValue="id" itemLabel="newCashDetail" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="newCashDetails" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="invTypeRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="invType">Вид инвестора:</label>
                <div class="col-md-7">
                    <form:select path="investorsType" id="invType" items="${investorsTypes}" multiple="false"
                                 itemValue="id" itemLabel="investorsType" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="investorsType" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="dateCloseInvRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateCloseInv">Дата закрытия вложения:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateClosingInvest" id="dateCloseInv" class="form-control input-sm"/>
                    <div class="has-error" id="reInvestDateErr">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="typeClosingRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="typeClosing">Вид закрытия вложения:</label>
                <div class="col-md-7">
                    <form:select path="typeClosingInvest" id="typeClosing" items="${typeClosingInvest}" multiple="false"
                                 itemValue="id" itemLabel="typeClosingInvest" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="typeClosingInvest" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="display: none;" id="investorBuyerRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="investorBuyer">Инвестор покупатель:</label>
                <div class="col-md-7">
                    <form:select path="" id="investorBuyer" items="${investors}" multiple="false"
                                 itemValue="id" itemLabel="login" class="form-control input-sm"/>
                    <div class="has-error" id="investorBuyerErr">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="display: none;" id="dateRepRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="dateRep">Период расчёта:</label>
                <div class="col-md-7">
                    <form:input type="date" path="dateReport" id="dateRep" class="form-control input-sm"/>
                    <div class="has-error" id="dateRepErr">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="display: none;" id="sourceFacility">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="sourceFacilities" id="sourceFacilitiesLbl">Объект реинвестирования:</label>
                <div class="col-md-7">
                    <form:select path="" id="sourceFacilities" items="${sourceFacilities}" multiple="false"
                                 itemValue="id" itemLabel="facility" class="form-control input-sm"/>
                    <div class="has-error" id="sourceFacilityErr">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="display: none;" id="sourceUnderFacility">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="sourceUnderFacilities" id="sourceUnderFacilitiesLbl">Подобъект реинвестирования:</label>
                <div class="col-md-7">
                    <form:select path="" id="sourceUnderFacilities" multiple="false" class="form-control input-sm">
                        <c:forEach var="uf" items="${sourceUnderFacilities}">
                            <form:option value="${uf.underFacility}" id="${uf.id}" data-parent-id="${uf.facilityId}">

                            </form:option>
                        </c:forEach>
                    </form:select>

                    <div class="has-error" id="reUnderFacilityErr">
                    </div>
                </div>
            </div>
        </div>

        <div class="row" id="shareKindNameRow">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="shareKindName">Вид доли:</label>
                <div class="col-md-7">
                    <form:select path="shareKind" id="shareKindName" items="${shareKinds}" multiple="false"
                                 itemValue="id" itemLabel="shareKind" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="shareKind" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Обновить" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/investorscash' />">Отмена</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Создать" class="btn btn-primary btn-sm"/> или <a href="<c:url value='/investorscash' />">Отмена</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
<%@include file="loader.jsp"%>
<%@include file="popup.jsp"%>
</body>
</html>