<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="user-form-modal">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container">
                    <form:form method="POST" modelAttribute="userDTO" class="form-horizontal"
                               style="margin: 10px 0 10px 0" id="user-form">
                        <form:input type="hidden" path="id" id="id"/>
                        <input type="hidden" id="edit" value="false">
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="user-login">Имя пользователя:</label>
                            <div class="col-sm-6">
                                <form:input type="text" path="login" id="user-login" class="form-control input-sm"/>
                                <div class="has-error d-none" id="loginError">
                                    Логин должен быть более 3 символов
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="email">Email:</label>
                            <div class="col-md-6">
                                <form:input type="email" path="profile.email" id="email" class="form-control input-sm"/>
                                <div class="has-error d-none" id="emailError">
                                    Необходимо ввести корректный email
                                </div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="roles">Роли:</label>
                            <div class="col-md-6">
                                <form:select path="role" id="roles" items="${roles}" multiple="false"
                                             itemValue="id" itemLabel="humanized" class="form-control form-control-sm selectpicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Выберите роль"
                                />
                                <div class="has-error d-none" id="rolesError">
                                    Необходимо добавить роль
                                </div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="saleChanel">Канал привлечения:</label>
                            <div class="col-md-6">
                                <form:select path="partnerId" id="saleChanel" items="${investors}" multiple="false"
                                             itemValue="id" itemLabel="login" class="form-control form-control-sm selectpicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Выберите инвестора"
                                             disabled="true"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="kins">Степень родства:</label>
                            <div class="col-md-6">
                                <form:select path="kin" id="kins" items="${kins}" multiple="false"
                                             itemLabel="val" itemValue="val" class="form-control form-control-sm selectpicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Степень родства"
                                             disabled="true"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="lastName">Фамилия:</label>
                            <div class="col-md-6">
                                <form:input type="text" path="profile.lastName" id="lastName" class="form-control input-md"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="firstName">Имя:</label>
                            <div class="col-md-6">
                                <form:input type="text" path="profile.firstName" id="firstName" class="form-control input-md"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="patronymic">Отчество:</label>
                            <div class="col-md-6">
                                <form:input type="text" path="profile.patronymic" id="patronymic" class="form-control input-md"/>
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
