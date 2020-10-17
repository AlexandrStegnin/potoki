<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="xlink" uri="http://jakarta.apache.org/taglibs/standard/scriptfree" %>

<div class="modal fade" id="popup-table" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document" style="min-width: 75%">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-responsive" style="font-weight: 600; color: #11325b;" id="header">Детальная информация</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true" class="text-responsive">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table id="detail-table" class="table table-striped table-hover">
                        <thead style="text-align: center">
                        <tr>
                            <th scope="col" class="text-responsive">Дата транзакции</th>
                            <th scope="col" class="text-responsive">Владелец счёта</th>
                            <th scope="col" class="text-responsive">Сумма</th>
                            <th scope="col" class="text-responsive">Вид транзакции</th>
                            <th scope="col" class="text-responsive">Вид денег</th>
                            <th scope="col" class="text-responsive">Отправитель</th>
                            <th scope="col" class="text-responsive">Получатель</th>
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
