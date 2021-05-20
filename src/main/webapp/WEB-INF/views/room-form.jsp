<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="room-modal-form">
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
                    <form:form method="POST" modelAttribute="roomDTO" class="form-horizontal"
                               style="margin: 10px 0 10px 0" id="room-form">
                        <form:input type="hidden" path="id" id="room-id"/>
                        <input type="hidden" id="edit" value="false">
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="underFacility">Подобъект:</label>
                            <div class="col-sm-6">
                                <form:select path="underFacility" id="underFacility" items="${underFacilities}" multiple="false"
                                             itemValue="id" itemLabel="name"
                                             class="form-control form-control-sm selectpicker"
                                             data-live-search="true" data-size="10"/>
                                <div class="has-error d-none" id="underFacilityError">
                                    Необходимо выбрать подобъект
                                </div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="room">Помещение:</label>
                            <div class="col-sm-6">
                                <form:input type="text" path="name" id="room" class="form-control form-control-sm"/>
                                <div class="has-error d-none" id="nameError">
                                    Название помещения должно быть более 3 символов
                                </div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="roomSize">Квадратура:</label>
                            <div class="col-sm-6">
                                <form:input type="number" path="roomSize" id="roomSize" class="form-control form-control-sm"
                                            min="0.0" step="0.01"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="cost">Стоимость:</label>
                            <div class="col-sm-6">
                                <form:input type="number" path="cost" id="cost" class="form-control form-control-sm"
                                            min="0.0" step="0.01"/>
                                <div class="has-error d-none" id="costError">
                                    Необходимо указать стоимость
                                </div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="dateBuy">Дата покупки:</label>
                            <div class="col-sm-6">
                                <form:input type="date" path="dateBuy" id="dateBuy" class="form-control form-control-sm"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="sold">Продано:</label>
                            <div class="col-sm-6">
                                <select id="sold" class="form-control form-control-sm">
                                    <option <c:if test="${room.sold == true}"> selected="selected" </c:if> value="Нет" id="0">Нет</option>
                                    <option <c:if test="${room.sold == true}"> selected="selected" </c:if> value="Да" id="1">Да</option>
                                </select>
                                <label for="soldHid" style="display: none;"></label>
                                <form:checkbox path="sold" id="soldHid" style="display: none;" />
                            </div>
                        </div>

                        <div class="form-group row" id="dateSaleRow">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="dateSale">Дата продажи:</label>
                            <div class="col-sm-6">
                                <form:input type="date" path="dateSale" id="dateSale" class="form-control form-control-sm"/>
                                <div class="has-error d-none" id="dateSaleError">
                                    Необходимо указать дату продажи
                                </div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="salePrice">Цена продажи:</label>
                            <div class="col-sm-6">
                                <form:input type="number" path="salePrice" id="salePrice" class="form-control form-control-sm"
                                            min="0.0" step="0.01"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="yearProfit">Годовая доходность:</label>
                            <div class="col-sm-6">
                                <form:input type="number" path="totalYearProfit" id="yearProfit" class="form-control form-control-sm"
                                            min="0.0" step="0.01"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-2 col-form-label-sm" for="accountNumber">№ счёта:</label>
                            <div class="col-sm-6">
                                <input type="text" id="accountNumber" class="form-control form-control-sm" value="${accountNumber}" readonly/>
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
