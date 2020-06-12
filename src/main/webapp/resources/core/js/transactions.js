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
        })
        .fail(function (e) {
            closeLoader();
            showMessage(e.toLocaleString());
        })
        .always(function () {
            closeLoader();
        });
}

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

function createRow(cash) {
    return $('<tr>').append(
        $('<td>').text(cash.investor),
        $('<td>').text(cash.facility),
        $('<td>').text(cash.dateGivenCash),
        $('<td>').text((cash.givenCash).toLocaleString())
    );
}

function showMessage(message) {
    let modal = $('#msg-modal');
    modal.find('#msg').html(message);
    modal.modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
