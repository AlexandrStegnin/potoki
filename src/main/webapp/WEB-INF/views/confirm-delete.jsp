<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal" tabindex="-1" role="dialog" id="confirm-delete">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Удаление сумм</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p id="message">Вы действительно хотите удалить выбранные суммы?</p>
            </div>
            <div class="modal-footer" data-action="" id="action">
                <button type="button" class="btn btn-primary" id="accept-delete">Да</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Нет</button>
            </div>
        </div>
    </div>
</div>
