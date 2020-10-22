let AccountSummaryDTO = function () {
}

AccountSummaryDTO.prototype = {
    accountId: null,
    ownerName: null,
    payerName: null,
    parentPayer: null,
    build: function (accountId, ownerName, payerName, parentPayer) {
        this.accountId = accountId
        this.ownerName = ownerName
        this.payerName = payerName
        this.parentPayer = parentPayer
    }
}

let AccountTransactionDTO = function () {}

AccountTransactionDTO.prototype = {
    id: null,
    txDate: null,
    operationType: null,
    payer: null,
    owner: null,
    recipient: null,
    cash: null,
    cashType: null,
    blocked: null,
    build: function (id, txDate, operationType, payer, owner, recipient, cash, cashType, blocked) {
        this.id = id
        this.txDate = txDate
        this.operationType = operationType
        this.payer = payer
        this.owner = owner
        this.recipient = recipient
        this.cash = cash
        this.cashType = cashType
        this.blocked = blocked
    }
}

let AccFilter = function () {}

AccFilter.prototype = {
    ownerName: null,
    payerName: null,
    parentPayer: null,
    build: function (ownerName, payerName, parentPayer) {
        this.ownerName = ownerName
        this.payerName = payerName
        this.parentPayer = parentPayer
    }
}

let AccountTxDTO = function () {
}

AccountTxDTO.prototype = {
    txIds: [],
    build: function (txIds) {
        this.txIds = txIds
    }
}

let popupTable;

jQuery(document).ready(function ($) {
    popupTable = $('#popup-table')
    showPageableResult()
    clearFilters()
    toggleAllRows()
    subscribeTxShowClick()
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
 * Получить детализацию по счёту
 *
 * @param accSummaryDTO {AccountSummaryDTO} DTO для удаления сумм
 */
function getDetails(accSummaryDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "details",
        data: JSON.stringify(accSummaryDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            createDetailTable(data);
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
        $('#payers').prop('selectedIndex', -1)
        $('.selectpicker').selectpicker('refresh')
        $('#all').prop('checked', false)
        $('#bth-search').click()
    })
}

/**
 * Отображать на 1 странице или постранично
 */
function toggleAllRows() {
    $('#all').on('change', function () {
        $('#bth-search').click()
    })
}

/**
 * Нажатие кнопки "Просмотреть"
 */
function subscribeTxShowClick() {
    $('.tx-show').on('click', function () {
        let txId = $(this).data('tx-id')
        let accSummaryDTO = new AccountSummaryDTO()
        let accFilter = getFilter()
        accSummaryDTO.build(txId, accFilter.ownerName, accFilter.payerName, accFilter.parentPayer)
        getDetails(accSummaryDTO)
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
    closeLoader()
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

/**
 * Заполнить таблицу во всплывающей форме по данным с сервера
 *
 * @param details {[AccountTransactionDTO]}
 */
function createDetailTable(details) {
    let detailTable = $('#detail-table');
    let tableBody = detailTable.find('tbody');
    tableBody.empty();
    $.each(details, function (ind, el) {
        let row = createRow(el);
        tableBody.append(row);
    });
    popupTable.modal('show');
}

/**
 * Создать строку с суммой
 *
 * @param transactionDTO {AccountTransactionDTO} DTO транзакции
 * @returns строка таблицы
 */
function createRow(transactionDTO) {
    return $('<tr>').append(
        $('<td>').text(getDate(transactionDTO.txDate).toLocaleDateString()),
        $('<td>').text(transactionDTO.owner),
        $('<td>').text((transactionDTO.cash).toLocaleString()),
        $('<td>').text(transactionDTO.operationType),
        $('<td>').text(transactionDTO.cashType),
        $('<td>').text(transactionDTO.payer),
        $('<td>').text(transactionDTO.recipient)
    );
}

/**
 * Получить дату из числа типа long
 *
 * @param number {number}
 */
function getDate(number) {
    let dateTime = new Date(number)
    return new Date(dateTime.getUTCFullYear(), dateTime.getUTCMonth(), dateTime.getUTCDate())
}

/**
 * Собрать фильтр со страницы
 *
 * @return {AccFilter} собранный фильтр
 */
function getFilter() {
    let accFilter = new AccFilter()
    let ownerName = $('#owners').find('option:selected').val()
    let payerName = $('#payers').find('option:selected').val()
    let parentPayer = $('#parentPayers').find('option:selected').val()
    accFilter.build(ownerName, payerName, parentPayer)
    return accFilter
}
