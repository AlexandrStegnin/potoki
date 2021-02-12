<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <sec:csrfMetaTags/>
    <title>Приложения к договорам</title>
    <link rel="stylesheet"
          href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css' />">
    <link href="<c:url value='/resources/core/css/ddk_loader.css' />" rel="stylesheet"/>
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon/favicon.ico?v=eEY755nn99' />">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-5">
            <form:form modelAttribute="filter" method="POST" action="/investor/annexes" class="form-inline"
                       id="search-form">
                <div class="row" style="margin: 20px;">
                    <form:label path="" for="search-annex" cssStyle="margin: 0 10px 0 0">Название файла:</form:label>
                    <form:input path="annexName" type="text" id="search-annex" cssStyle="margin: 0 10px 0 0"/>
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
                    <button type="submit" class="btn btn-primary btn-sm" id="filter-btn" style="margin-left: 10px">Фильтр</button>
                </div>
            </form:form>
        </div>
        <div class="col-md-5">
            <form:form method="POST" action="/investor/annexes/upload" modelAttribute="files"
                       enctype="multipart/form-data" class="form-horizontal">
                <div class="input-group" style="margin: 20px 20px 20px 0">
                    <div class="custom-file">
                        <input type="file" id="file" name="uploadingFiles" class="form-control-file" multiple data-toggle="tooltip" data-placement="top" title="Размер файлов не должен превышать 20 МБ"/>
                    </div>
                    <button type="button" class="btn btn-primary btn-sm disabled" id="upload">Загрузить</button>
                </div>
            </form:form>
        </div>
        <div class="col-md-2 center-block">
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <div class="input-group" style="margin: 20px 0 20px 0;">
                    <button type="button" class="btn btn-md btn-danger" id="delete-annex-list" style="height: 100%">Удалить выбранные
                        <i class="far fa-trash-alt"></i>
                    </button>
                </div>
            </sec:authorize>
        </div>
    </div>
</div>
<div class="container-fluid d-none" id="pb" style="margin-bottom: 10px">
    <div class="progress">
        <div id="progressBar" class="progress-bar progress-bar-success" role="progressbar"
             aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0">0%</div>
    </div>
</div>
<div class="container-fluid">
    <table class="table table-striped w-auto table-hover" id="annex-table">
        <thead>
        <tr style="text-align: center">
            <th>Инвестор</th>
            <th>Название файла</th>
            <th>Дата загрузки</th>
            <th>Кто загрузил</th>
            <th>Прочитано</th>
            <th><input type="checkbox" id="check-all" value=""/></th>
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
                <td><input type="checkbox" id="${annex.id}"></td>
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
<script type="text/javascript" src="<c:url value='/resources/core/js/ddk_loader.js' />"></script>
</body>
</html>
