<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta charset="utf-8">
    <sec:csrfMetaTags/>
    <title>Приложения к договорам</title>
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js' />"></script>
    <link href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"
          rel="stylesheet"/>
    <script type="text/javascript" src="<c:url value='/resources/core/js/progress.js' />"></script>
    <script type="text/javascript" src="<c:url value='/resources/core/js/investor-annex.js' />"></script>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js' />"></script>
    <script type="text/javascript"
            src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js' />"></script>
    <script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
</head>
<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-6">
            <form:form modelAttribute="filter" method="POST" action="/investor/annexes" class="form-inline"
                       id="search-form">
                <div class="row" style="margin: 20px;">
                    <label class="sr-only" for="investors">Инвестор:</label>
                    <form:select path="investor" id="investors" class="selectpicker" data-container="body"
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
                    <button type="submit" class="btn btn-primary btn-sm" style="margin-left: 10px">Фильтр</button>
                </div>
            </form:form>
        </div>
        <div class="col-md-6">
            <form:form method="POST" action="/investor/annexes/upload" modelAttribute="files"
                       enctype="multipart/form-data" class="form-horizontal">
                <div class="row" style="margin: 20px">
                    <div class="col-md-10">
                        <input type="file" id="file" name="uploadingFiles" multiple/>
                    </div>
                    <button type="button" class="btn btn-primary btn-sm" id="upload">Загрузить</button>
                </div>
            </form:form>
        </div>
    </div>
</div>
<div class="container-fluid">
    <table class="table table-striped w-auto table-hover">
        <thead>
        <tr style="text-align: center">
            <th>Инвестор</th>
            <th>Название файла</th>
            <th>Дата загрузки</th>
            <th>Кто загрузил</th>
            <th>Прочитано</th>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th>Удалить</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${contracts}" var="annex">
            <tr>
                <td>${annex.investor}</td>
                <td>${annex.annexName}</td>
                <td>${annex.dateLoad}</td>
                <td>${annex.loader}</td>
                <c:choose>
                    <c:when test="${annex.read == 1}">
                        <td>Да</td>
                    </c:when>
                    <c:otherwise>
                        <td>Нет</td>
                    </c:otherwise>
                </c:choose>
                <td>
                    <button type="button" class="btn btn-sm btn-danger delete-annex" data-annex-id="${annex.id}">
                        <i class="far fa-trash-alt"></i>
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div id="msg-modal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-sm modal-dialog-centered" role="document" style="text-align: center">
        <div class="modal-content" style="text-align: center">
            <div class="modal-body" id="msg"></div>
        </div>
    </div>
</div>
<script type="text/javascript"
        src="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js' />"></script>
<script type="text/javascript"
        src="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js' />"></script>
</body>
</html>
