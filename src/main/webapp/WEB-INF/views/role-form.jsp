<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="role-modal-form">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container">
                    <form:form method="POST" modelAttribute="roleDTO" class="form-horizontal"
                               style="margin: 10px 0 10px 0" id="role-form">
                        <form:input type="hidden" path="id" id="role-id"/>
                        <input type="hidden" id="edit" value="false">
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="role-name">Название:</label>
                            <div class="col-sm-6">
                                <form:input type="text" path="name" id="role-name" class="form-control input-sm"/>
                                <div class="has-error d-none" id="roleNameError">
                                    Название должно быть более 3 символов
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="humanized">Понятное название:</label>
                            <div class="col-md-6">
                                <form:input type="text" path="humanized" id="humanized" class="form-control input-sm"/>
                                <div class="has-error d-none" id="humanizedError">
                                    Необходимо ввести понятное название роли
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
            <div class="modal-footer" data-action="" id="action">
                <button type="button" class="btn btn-primary" id="accept">Создать</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
            </div>
        </div>
    </div>
</div>
