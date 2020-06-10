<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<head>
    <title>Список операций</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <sec:csrfMetaTags/>
    <title>Приложения к договорам</title>
    <link rel="stylesheet" href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css' />">
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <form:form modelAttribute="filter" method="POST" action="/transactions" class="form-inline" id="search-form">
                <div class="row" style="margin: 20px 20px 20px 0;">
                    <form:select path="creator" id="creators" class="selectpicker" data-live-search="true" multiple="false">
                        <c:forEach var="creator" items="${creators}">
                            <option
                                    <c:forEach var="cr" items="${filter.creator}">
                                        <c:choose>
                                            <c:when test="${creator eq cr}">selected="selected"</c:when>
                                        </c:choose>
                                    </c:forEach>
                                    value="${creator}" id="${creator}">${creator}
                            </option>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="row" style="margin: 20px 0 20px 0;">
                    <form:select path="investor" id="investors" class="selectpicker" data-live-search="true"
                                 multiple="false">
                        <c:forEach var="inv" items="${investors}">
                            <option
                                    <c:forEach var="investor" items="${filter.investor}">
                                        <c:choose>
                                            <c:when test="${inv eq investor}">selected="selected"</c:when>
                                        </c:choose>
                                    </c:forEach>
                                    value="${inv}" id="${inv}">${inv}
                            </option>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="row" style="margin: 20px 0 20px 20px;">
                    <form:select path="type" id="types" class="selectpicker" data-live-search="true"
                                 multiple="false">
                        <c:forEach var="type" items="${types}">
                            <option
                                    <c:forEach var="t" items="${filter.type}">
                                        <c:choose>
                                            <c:when test="${type.value eq t}">selected="selected"</c:when>
                                        </c:choose>
                                    </c:forEach>
                                    value="${type.value}" id="${type.key}">${type.value}
                            </option>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="row" style="margin: 20px 20px 20px 20px;">
                    <label for="txDate" style="margin-right:10px; font-size:16px; font-weight: bold" >Дата операции:</label>
                    <form:input path="txDate" id="txDate" name="txDate" type="date" class="form-control input-sm" value="" />
                    <button type="submit" class="btn btn-primary btn-sm" style="margin-left: 10px">Фильтр</button>
                </div>
            </form:form>
        </div>
    </div>
</div>
<div class="container-fluid">
    <table class="table table-striped w-auto table-hover">
        <thead>
        <tr style="text-align: center">
            <th>Кем создана</th>
            <th>Дата создания</th>
            <th>Вид операции</th>
            <th>Клиент</th>
            <th>Список сумм</th>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th>Действия</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${transactions}" var="tx">
            <tr>
                <td>${tx.createdBy}</td>
                <td>${tx.txDate}</td>
                <td>${tx.type}</td>
                <td>${tx.investor}</td>
                <td><button type="button" class="btn btn-xs btn-success tx-show" data-tx-id="${tx.id}">Посмотреть</button></td>
                <td>
                    <button type="button" class="btn btn-sm btn-danger rollback-tx" data-tx-id="${tx.id}">
                        <i class="far fa-trash-alt"></i>
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
<script src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js' />"></script>
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/progress.js' />"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/investor-annex.js' />"></script>
</body>
</html>
