function enableTooltips() {
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
}

jQuery(document).ready(function ($) {
    enableTooltips()
    $('.tx-show').on('click', function (e) {
        e.preventDefault();
        getTransactionCash($(this).attr('data-tx-id'));
    });
    $('.rollback-tx').on('click', function () {
        rollbackTransaction($(this).attr('data-tx-id'));
        $(this).closest('tr').remove();
    })
});

/**
 * Получить список денег по id транзакции
 *
 * @param txId id транзакции
 */
function getTransactionCash(txId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let logDTO = {
        id: txId
    };
    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "transactions/cash",
        data: JSON.stringify(logDTO),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            closeLoader();
            createCashTable(data);
        })
        .fail(function (e) {
            closeLoader();
            showMessage(e.toLocaleString());
        })
        .always(function () {
            closeLoader();
        });
}

/**
 * Откатить транзакцию по id
 *
 * @param txId id транзакции
 */
function rollbackTransaction(txId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let logDTO = {
        id: txId
    };
    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "transactions/rollback",
        data: JSON.stringify(logDTO),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            closeLoader();
            showMessage(data);
            unblockTransaction(txId);
        })
        .fail(function (e) {
            closeLoader();
            showMessage(e.toLocaleString());
        })
        .always(function () {
            closeLoader();
        });
}

/**
 * Разблокировать строку, которую блокировала откатываемая транзакция
 *
 * @param txId id транзакции
 */
function unblockTransaction(txId) {
    $.each($('#blocked-from-' + txId), function () {
        $(this).find('.rollback-tx').removeAttr('disabled');
    })
}

/**
 * Создать таблицу во всплывающем окне со списком сумм
 *
 * @param cashList список сумм
 */
function createCashTable(cashList) {
    let cashTable = $('#cash-table');
    let tableBody = cashTable.find('tbody');
    tableBody.empty();
    $.each(cashList, function (ind, el) {
        let row = createRow(el);
        tableBody.append(row);
    });
    $('#cash-modal').modal('show');
}

/**
 * Создать строку с суммой
 *
 * @param cash сумма
 * @returns строка таблицы
 */
function createRow(cash) {
    return $('<tr>').append(
        $('<td>').text(cash.investor),
        $('<td>').text(cash.facility),
        $('<td>').text(cash.dateGivenCash),
        $('<td>').text((cash.givenCash).toLocaleString()),
        $('<td>').text(cash.cashType)
    );
}

/**
 * Показать сообщение
 *
 * @param message сообщение
 */
function showMessage(message) {
    let modal = $('#msg-modal');
    modal.find('#msg').html(message);
    modal.modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
