<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="cashing-form-modal">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="title">Вывод денег</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container">
                    <form:form method="POST" modelAttribute="cashingDTO" class="form-horizontal"
                               style="margin: 10px 0 10px 0" id="user-form">
                        <div class="form-group row" id="facilityRow">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="facility">Объект:</label>
                            <div class="col-sm-6">
                                <form:select path="facilityId" id="facility" items="${facilities}" multiple="false"
                                             itemValue="id" itemLabel="name" class="form-control form-control-sm selectpicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Выберите объект"/>
                                <div class="has-error d-none" id="facilityError">
                                    Необходимо выбрать объект
                                </div>
                            </div>
                        </div>
                        <c:set var="uf" value="${underFacilities}"/>

                        <div class="form-group row" id="underFacilityRow">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="underFacility">Подобъект:</label>
                            <div class="col-sm-6">
                                <form:select path="underFacilityId" id="underFacility" multiple="false"
                                             class="form-control form-control-sm selectpicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Выберите подобъект">
                                    <c:forEach var="uf" items="${uf}">
                                        <form:option value="${uf.name}" id="${uf.id}" data-parent-id="${uf.facility.id}">

                                        </form:option>
                                    </c:forEach>
                                </form:select>
                                <div class="has-error d-none" id="underFacilityError">
                                    Необходимо выбрать подобъект
                                </div>
                            </div>
                        </div>

                        <div class="form-group row" id="investorRow">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="investor">Инвестор:</label>
                            <div class="col-sm-6">
                                <form:select path="investorsIds" id="investor" items="${investorsMulti}" multiple="true"
                                             itemValue="id" itemLabel="login"
                                             class="form-control form-control-sm selectpicker investorPicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Выберите инвесторов"/>
                                <div class="has-error d-none" id="investorError">
                                    Необходимо выбрать инвесторов
                                </div>
                            </div>
                        </div>

                        <div class="form-group row" id="dateCashingRow">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="dateCashing">Дата вывода:</label>
                            <div class="col-sm-6">
                                <form:input type="date" path="dateCashing" id="dateCashing"
                                            class="form-control form-control-sm"/>
                                <div class="has-error d-none" id="investorError">
                                    Необходимо выбрать дату вывода
                                </div>
                            </div>
                        </div>

                        <div class="form-group row" id="commissionRow">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="commission">Комиссия за вывод (%):</label>
                            <div class="col-sm-6">
                                <form:input type="number" path="commission" id="commission" class="form-control form-control-sm"
                                            min="0.00" max="100" step="any"/>
                                <div class="has-error d-none" id="commissionError">
                                    Необходимо указать комиссию в (%)
                                </div>
                            </div>
                        </div>

                        <div class="form-group row" id="commissionNoMoreRow">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="commissionNoMore">Но не более:</label>
                            <div class="col-sm-6">
                                <form:input type="number" path="commissionNoMore" id="commissionNoMore"
                                            class="form-control form-control-sm"
                                            min="0.00" step="any"/>
                            </div>
                        </div>

                        <div class="form-group row" id="cashRow">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="cash">Сумма к выводу:</label>
                            <div class="col-sm-6">
                                <form:input type="number" path="cash" id="cash" class="form-control form-control-sm"
                                            min="0.0" step="any"/>
                                <div id="toBigSumForCashing" class="has-error d-none">
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
            <div class="modal-footer" data-action="" id="action">
                <button type="button" class="btn btn-danger d-none" id="cashing-all">Вывести всё</button>
                <button type="button" class="btn btn-primary" id="accept">Вывести</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
            </div>
        </div>
    </div>
</div>
