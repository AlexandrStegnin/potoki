<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="re-buy-share-form-modal">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="title">Перепокупка доли</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container">
                    <form:form method="POST" modelAttribute="reBuyShareDTO" class="form-horizontal"
                               style="margin: 10px 0 10px 0" id="re-buy-share-form">
                        <input type="hidden" id="buyerId" value="">
                        <div class="form-group row" id="buyerIdRow">
                            <label class="col-sm-4 offset-sm-1 col-form-label-sm" for="buyerLogin">Покупатель:</label>
                            <div class="col-sm-6">
                                <input type="text" id="buyerLogin" class="form-control form-control-sm" readonly="readonly"/>
                            </div>
                        </div>
                        <div class="form-group row" id="buyerCashRow">
                            <label class="col-sm-4 offset-sm-1 col-form-label-sm" for="buyerCash">Свободно у покупателя:</label>
                            <div class="col-sm-6">
                                <input data-cash="0" type="text" id="buyerCash" class="form-control form-control-sm" readonly="readonly"
                                value="Считаем..."/>
                            </div>
                        </div>
                        <div class="form-group row" id="sellerRow">
                            <label class="col-sm-4 offset-sm-1 col-form-label-sm" for="seller">Продавец:</label>
                            <div class="col-sm-6">
                                <form:select path="sellerId" id="seller" items="${sellers}" multiple="false"
                                             itemValue="id" itemLabel="login" class="form-control form-control-sm selectpicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Выберите продавца"/>
                                <div class="has-error d-none" id="sellerError">
                                    Необходимо выбрать продавца
                                </div>
                            </div>
                        </div>

                        <div class="form-group row" id="facilityRow">
                            <label class="col-sm-4 offset-sm-1 col-form-label-sm" for="projects">Объект:</label>
                            <div class="col-sm-6">
                                <form:select path="facilityId" id="projects" items="${projects}" multiple="false"
                                             itemValue="id" itemLabel="name" class="form-control form-control-sm selectpicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Выберите объект"/>
                                <div class="has-error d-none" id="facilityError">
                                    Необходимо выбрать объект
                                </div>
                            </div>
                        </div>

                        <div class="form-group row" id="openedCashRow">
                            <label class="col-sm-4 offset-sm-1 col-form-label-sm" for="openedCash">Открытые суммы:</label>
                            <div class="col-sm-6">
                                <form:select data-prefix="Выбрано: " path="openedCash" id="openedCash" items="${openedCashes}" multiple="true"
                                             itemValue="id" itemLabel="givenCash"
                                             class="form-control form-control-sm selectpicker"
                                             data-size="10" data-live-search="true" data-none-selected-text="Выбрано: 0,00 &#8381;"/>
                                <div class="has-error d-none" id="openedCashError">
                                    Необходимо выбрать суммы перепокупки
                                </div>
                            </div>
                        </div>

                        <div class="form-group row" id="realDateRow">
                            <label class="col-sm-4 offset-sm-1 col-form-label-sm" for="realDate">Дата реальной передачи:</label>
                            <div class="col-sm-6">
                                <form:input type="date" path="realDateGiven" id="realDate"
                                            class="form-control form-control-sm"/>
                                <div class="has-error d-none" id="realDateError">
                                    Необходимо выбрать дату реальной передачи
                                </div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-1 col-form-label-sm" for="toManyOwnersErr"></label>
                            <div class="col-sm-8">
                                <div class="has-error d-none" id="toManyOwnersErr">
                                    Выбрано более 1 владельца для перепокупки
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-sm-2 offset-sm-1 col-form-label-sm" for="veryBigSumToBuy"></label>
                            <div class="col-sm-8">
                                <div class="has-error d-none" id="veryBigSumToBuy">
                                    Сумма инвестора покупателя меньше суммы покупаемой доли
                                </div>
                            </div>
                        </div>

                    </form:form>
                </div>
            </div>
            <div class="modal-footer" data-action="" id="action">
                <button type="button" class="btn btn-primary" id="accept">Купить долю</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
            </div>
        </div>
    </div>
</div>
