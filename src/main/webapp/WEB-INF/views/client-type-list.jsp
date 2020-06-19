<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <sec:csrfMetaTags/>
    <title>Вид клиента</title>
    <link rel="stylesheet" href="<c:url value='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css' />"/>
    <link rel="stylesheet"
          href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.min.css' />">
    <link rel="shortcut icon" href="<c:url value='/resources/core/img/favicon.ico' />" type="image/x-icon">
</head>
<body>
<%@include file="header.jsp" %>
<div class="container-fluid">
    <button class="btn btn-success btn-md float-right" data-toggle="modal" data-target="#create-client-type" style="margin-bottom: 10px">
        Создать вид клиента
    </button>
</div>
<div class="container-fluid">
    <table class="table table-striped w-auto table-hover" id="client-type-table">
        <thead>
        <tr style="text-align: center">
            <th>ID</th>
            <th>Вид клиента</th>
            <th>Кто обновил</th>
            <sec:authorize access="hasRole('ADMIN') or hasRole('DBA')">
                <th>Действия</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody style="text-align: center">
        <c:forEach items="${clientTypes}" var="type">
            <tr id="${type.id}">
                <td>${type.id}</td>
                <td>${type.title}</td>
                <td>${type.modifiedBy}</td>
                <td>
                    <button type="button" class="btn btn-sm btn-success update-client-type" data-toggle="modal"
                            data-target="#create-client-type"
                            data-client-type-id="${type.id}"
                            data-client-type-title="${type.title}">
                        <i class="far fa-edit"></i>
                    </button>
                    <button type="button" class="btn btn-sm btn-danger delete-client-type"
                            data-client-type-id="${type.id}"
                            data-client-type-title="${type.title}">
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
<!-- Modal -->
<div class="modal fade" id="create-client-type" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Добавить вид клиента</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <input type="hidden" id="client-type-id" value="">
                    <div class="form-group">
                        <label for="client-type-title">Вид клиента</label>
                        <input type="text" class="form-control" id="client-type-title" placeholder="Введите вид клиента">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                <button type="button" class="btn btn-primary" id="save-client-type">Сохранить</button>
            </div>
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
<script src="<c:url value='https://kit.fontawesome.com/2b84e2f58d.js' />" crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value='/resources/core/js/client-types.js' />"></script>

</body>
</html>
