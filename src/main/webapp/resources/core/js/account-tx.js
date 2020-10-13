let AccountTxDTO = function () {
}

AccountTxDTO.prototype = {
    txIds: [],
    build: function (txIds) {
        this.txIds = txIds
    }
}

let confirmDelete;

jQuery(document).ready(function ($) {
    confirmDelete = $('#confirm-delete');
    showPageableResult()
    subscribeCheckAllClick()
    showConfirmDelete()
    acceptDelete()
    clearFilters()
})

/**
 * Показать результаты при клике на номер страницы
 */
function showPageableResult() {
    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 1;
        $('#pageSize').val(pageSize);
        $('#filter-form').submit();
    });
}

/**
 * Выделить/снять выделение со всех чекбоксов на странице
 */
function subscribeCheckAllClick() {
    $(document).on('click', '#checkIt', function () {
        let checkItBtn = $('#checkIt');
        const checked = checkItBtn.data('checked');
        $('table#transactions').find('> tbody').find('> tr').each(function () {
            $(this).find(':checkbox:not(:disabled)').prop('checked', !checked);
        });
        checkItBtn.data('checked', !checked)
    })
}

/**
 * Показать форму подтверждения удаления
 */
function showConfirmDelete() {
    $('#delete-list').on('click', function () {
        confirmDelete.modal('show')
    })
}

/**
 * Подтверждение удаления
 */
function acceptDelete() {
    confirmDelete.find('#accept-delete').on('click', function () {
        let options = $('table#transactions').find('input:checkbox:checked:not(disabled)')
        let checked = []
        $.each(options, function (ind, el) {
            checked.push($(el).data('object-id'))
        })
        let accountTxDTO = new AccountTxDTO()
        accountTxDTO.build(checked)
        deleteTransactions(accountTxDTO)
    })
}

/**
 * Удалить выбранные суммы
 *
 * @param accountTxDTO {AccountTxDTO} DTO для удаления сумм
 */
function deleteTransactions(accountTxDTO) {
    confirmDelete.modal('hide')
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "transactions/delete",
        data: JSON.stringify(accountTxDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            showPopup(data.message);
            $('#bth-search').click()
        })
        .fail(function (e) {
            showPopup('Что-то пошло не так [' + e.message + ']');
        })
        .always(function () {
            console.log('Закончили!');
        });

}

/**
 * Очистить фильтры
 */
function clearFilters() {
    $(document).on('click', '#btn-clear',function (e) {
        e.preventDefault()
        $('#owners').prop('selectedIndex', -1)
        $('#recipients').prop('selectedIndex', -1)
        $('#cashTypes').prop('selectedIndex', -1)
        $('.selectpicker').selectpicker('refresh')
        $('#bth-search').click()
    })
}

/**
 * Показать сообщение
 *
 * @param message {String}
 */
function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
