<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="xlink" uri="http://jakarta.apache.org/taglibs/standard/scriptfree" %>

<div class="modal fade" id="tx-popup-table" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document" style="min-width: 75%">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-responsive" style="font-weight: 600; color: #11325b;" id="header">Транзакции</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true" class="text-responsive">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="d-flex flex-row justify-content-end">
                    <button type="button" class="btn btn-danger pull-right" style="margin-bottom: 10px" id="delete-list">Удалить выбранные</button>
                </div>
                <div class="table-responsive">
                    <table id="tx-table" class="table table-striped table-hover">
                        <thead style="text-align: center">
                        <tr>
                            <th>Дата транзакции</th>
                            <th>Владелец счёта</th>
                            <th>Сумма</th>
                            <th>Вид транзакции</th>
                            <th>Вид денег</th>
                            <th>Отправитель</th>
                            <th style="width: 5%">Выбрать</th>
                        </tr>
                        </thead>
                        <tbody class="text-responsive">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
