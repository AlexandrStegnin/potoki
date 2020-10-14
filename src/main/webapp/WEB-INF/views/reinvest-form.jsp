<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="reinvest-form-modal">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="title">Реинвестирование суммы</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container">
                    <form:form method="POST" modelAttribute="accountTxDTO" class="form-horizontal"
                               style="margin: 10px 0 10px 0" id="reinvest-form">
                        <div class="form-group row" id="dateReinvestRow">
                            <label class="col-md-3 col-form-label" for="dateReinvest">Дата вложения:</label>
                            <div class="col-md-9">
                                <form:input type="date" path="dateReinvest" id="dateReinvest" class="form-control input-sm"/>
                                <div id="dateReinvestErr" class="has-error col-md-9 d-none">Необходимо указать дату реинвестирования
                                </div>
                            </div>
                        </div>
                        <div class="form-group row" id="facilityRow">
                            <label class="col-md-3 col-form-label" for="facility">Объект:</label>
                            <div class="col-md-9">
                                <form:select path="facilityId" id="facility" items="${facilities}" multiple="false"
                                             itemValue="id" itemLabel="name" class="selectpicker form-control input-sm"
                                             data-size="10" data-live-search="true"
                                             data-none-selected-text="Выберите объект"/>
                                <div id="facilityErr" class="has-error col-md-9 d-none">Необходимо выбрать объект</div>
                            </div>
                        </div>

                        <div class="form-group row" id="underFacilityRow">
                            <label class="col-md-3 col-form-label" for="underFacility">Подобъект:</label>
                            <div class="col-md-9">
                                <form:select path="underFacilityId" id="underFacility" items="${underFacilities}"
                                             multiple="false"
                                             itemValue="id" itemLabel="name" class="selectpicker form-control input-sm"
                                             data-size="10" data-live-search="true"
                                             data-none-selected-text="Выберите подобъект"/>
                                <div id="underFacilityErr" class="has-error col-md-9 d-none">Необходимо выбрать подобъект
                                </div>
                            </div>
                        </div>

                        <div class="form-group row" id="shareTypeNameRow">
                            <label class="col-md-3 col-form-label" for="shareType">Вид доли:</label>
                            <div class="col-md-9">
                                <form:select path="shareType" id="shareType" items="${shareTypes}" multiple="false"
                                             itemValue="title" itemLabel="title"
                                             class="selectpicker form-control input-sm"/>
                                <div id="shareTypeErr" class="has-error col-md-9 d-none">Необходимо выбрать вид доли
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <input type="submit" class="btn btn-primary btn-md" id="accept" value="Реинвестировать"/>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
