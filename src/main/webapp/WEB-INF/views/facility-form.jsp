<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="facility-modal-form">
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
                    <form:form method="POST" modelAttribute="facilityDTO" class="form-horizontal"
                               style="margin: 10px 0 10px 0" id="facility-form">
                        <form:input type="hidden" path="id" id="facility-id"/>
                        <input type="hidden" id="edit" value="false">
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="name">Название:</label>
                            <div class="col-sm-6">
                                <form:input type="text" path="name" id="name" class="form-control input-sm"/>
                                <div class="has-error d-none" id="facilityNameError">
                                    Название должно быть более 3 символов
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="full-name">Название из 1С:</label>
                            <div class="col-md-6">
                                <form:input type="text" path="fullName" id="full-name" class="form-control input-sm"/>
                                <div class="has-error d-none" id="fullNameError">
                                    Необходимо указать название из 1С
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="city">Город:</label>
                            <div class="col-md-6">
                                <form:input type="text" path="city" id="city" class="form-control input-sm"/>
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