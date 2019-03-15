<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en-RU">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Расчёт долей инвесторов</title>
    <sec:csrfMetaTags/>
    <link href="<c:url value='/resources/core/css/old_bootstrap.min.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/applic.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/popup.css' />" rel="stylesheet"/>
    <link href="<c:url value='/resources/core/css/ajaxLoader.css' />" rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/jquery-3.2.1.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/bootstrap.min_old.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/scripts.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/AjaxLoader.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>

<body>
<div class="generic-container">
    <%@include file="old_authheader.jsp" %>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading">
            <span class="lead">Список долей:</span>
            <span class="pull-right"
                    <c:choose>
                        <c:when test="${messageResponse.trim().length() > 0}">
                            style="display: inline-block; color: green; font-weight: bold"
                        </c:when>
                        <c:otherwise>
                            style="display: none"
                        </c:otherwise>
                    </c:choose>
            >${messageResponse}
            </span>
        </div>
        <div class="row" style="margin-top:10px; margin-left:10px; margin-bottom:10px; margin-right:10px">
            <div class="col-sm-7">
                <form:form modelAttribute="invShareFilter" method="POST" action="/calculateInvShare" class="form-inline"
                           id="calc-period-form">
                    <label for="fromId" style="margin-left:10px; margin-right:5px; font-size:14px">Год с:</label>
                    <form:select path="yearFrom" id="fromId" multiple="false" class="form-control input-sm">
                        <c:forEach var="from" items="${yearFrom}" varStatus="status">
                            <option value="${from}" id="${from}" ${status.first ? 'selected' : '' }>${from}
                            </option>
                        </c:forEach>
                    </form:select>

                    <label for="toId" style="margin-left:10px; margin-right:5px; font-size:14px">по:</label>
                    <form:select path="yearTo" id="toId" multiple="false" class="form-control input-sm">
                        <c:forEach var="to" items="${yearFrom}" varStatus="status">
                            <option value="${to}" id="${to}" ${status.last ? 'selected' : '' }>${to}
                            </option>
                        </c:forEach>
                    </form:select>
                    <button type="submit" id="bth-calc-period" class="btn btn-primary btn-sm">Пересчитать (за период)
                    </button>
                </form:form>
            </div>
            <div class="col-sm-5">
                <form:form method="POST" action="/calculateInvShare" class="form-inline"
                           id="calc-form">
                    <button type="submit" id="bth-calc" class="btn btn-primary btn-sm pull-right">Пересчитать (всё)
                    </button>
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>