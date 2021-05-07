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
                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="underFacility">Подобъект:</label>
                                <div class="col-md-7">
                                    <form:select path="underFacility" id="underFacility" items="${underFacilities}" multiple="false"
                                                 itemValue="id" itemLabel="name" class="form-control input-sm"/>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="room">Помещение:</label>
                                <div class="col-md-7">
                                    <form:input type="text" path="name" id="room" class="form-control input-sm"/>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="roomSize">Квадратура:</label>
                                <div class="col-md-7">
                                    <form:input type="number" path="roomSize" id="roomSize" class="form-control input-sm"
                                                min="0.0" step="0.01"/>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="cost">Стоимость:</label>
                                <div class="col-md-7">
                                    <form:input type="number" path="cost" id="cost" class="form-control input-sm"
                                                min="0.0" step="0.01"/>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="dateBuy">Дата покупки:</label>
                                <div class="col-md-7">
                                    <form:input type="date" path="dateBuy" id="dateBuy" class="form-control input-sm"/>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="sold">Продано:</label>
                                <div class="col-md-7">
                                    <select id="sold" class="form-control input-sm">
                                        <option <c:if test="${room.sold == true}"> selected="selected" </c:if> value="Нет" id="0">Нет</option>
                                        <option <c:if test="${room.sold == true}"> selected="selected" </c:if> value="Да" id="1">Да</option>
                                    </select>
                                    <label for="soldHid" style="display: none;"></label>
                                    <form:checkbox path="sold" id="soldHid" style="display: none;" />
                                </div>
                            </div>
                        </div>

                        <div class="row" id="dateSaleRow">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="dateSale">Дата продажи:</label>
                                <div class="col-md-7">
                                    <form:input type="date" path="dateSale" id="dateSale" class="form-control input-sm"/>
                                </div>
                                <div id="dateSaleErr" style="color: red; display: none" class="col-md-2 input-sm">
                                    Необходимо ввести дату продажи
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="salePrice">Цена продажи:</label>
                                <div class="col-md-7">
                                    <form:input type="number" path="salePrice" id="salePrice" class="form-control input-sm"
                                                min="0.0" step="0.01"/>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="yearProfit">Годовая доходность:</label>
                                <div class="col-md-7">
                                    <form:input type="number" path="totalYearProfit" id="yearProfit" class="form-control input-sm"
                                                min="0.0" step="0.01"/>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-12">
                                <label class="col-md-3 control-lable" for="accountNumber">№ счёта:</label>
                                <div class="col-md-7">
                                    <input type="text" id="accountNumber" class="form-control input-sm" value="${accountNumber}" readonly/>
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
