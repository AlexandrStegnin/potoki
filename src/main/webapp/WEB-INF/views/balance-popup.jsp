<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="xlink" uri="http://jakarta.apache.org/taglibs/standard/scriptfree" %>

<div class="modal fade" id="balance-popup-table" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-responsive" style="font-weight: 600; color: #11325b;" id="header">Баланс инвестора</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true" class="text-responsive">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="table-responsive">
                    <table id="balance-table" class="table table-striped table-hover">
                        <thead style="text-align: center">
                        <tr>
                            <th>Владелец счёта</th>
                            <th>Сумма</th>
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
