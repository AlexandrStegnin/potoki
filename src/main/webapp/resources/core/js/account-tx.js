let AccountTxDTO = function () {
}

AccountTxDTO.prototype = {
    txIds: [],
    build: function (txIds) {
        this.txIds = txIds
    }
}

let AccountSummaryDTO = function () {
}

AccountSummaryDTO.prototype = {
    accountId: null,
    ownerName: null,
    summary: null,
    build: function (accountId, ownerName, summary) {
        this.accountId = accountId
        this.ownerName = ownerName
        this.summary = summary
    }
}

let BalanceDTO = function () {}

BalanceDTO.prototype = {
    accountId: null,
    ownerName: null,
    summary: 0,
    build: function (accountId, ownerName, summary) {
        this.accountId = accountId
        this.ownerName = ownerName
        this.summary = summary
    }
}

let confirmDelete;

let balancePopup;

jQuery(document).ready(function ($) {
    confirmDelete = $('#confirm-delete');
    balancePopup = $('#balance-popup-table')
    showPageableResult()
    subscribeCheckAllClick()
    showConfirmDelete()
    acceptDelete()
    clearFilters()
    toggleAllRows()
    subscribeBalanceShowClick()
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
            $('#btn-search').click()
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
        $('#parentPayers').prop('selectedIndex', -1)
        $('#payers').prop('selectedIndex', -1)
        $('#cashTypes').prop('selectedIndex', -1)
        $('.selectpicker').selectpicker('refresh')
        $('#btn-search').click()
    })
}

/**
 * Отображать на 1 странице или постранично
 */
function toggleAllRows() {
    $('#all').on('change', function () {
        $('#btn-search').click()
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

/**
 * При клике посмотреть баланс
 */
function subscribeBalanceShowClick() {
    $('.show-balance').on('click', function () {
        let ownerId = $(this).data('owner-id')
        getBalance(ownerId)
    })
}

/**
 * Получить баланс по инвестору
 *
 * @param ownerId
 */
function getBalance(ownerId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let balanceDTO = {
        accountId: ownerId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "transactions/balance",
        data: JSON.stringify(balanceDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            createBalanceTable(data);
        })
        .fail(function (e) {
            showPopup('Что-то пошло не так [' + e.message + ']');
        })
        .always(function () {
            console.log('Закончили!');
        });
}

/**
 * Заполнить таблицу во всплывающей форме по данным с сервера
 *
 * @param balance {BalanceDTO}
 */
function createBalanceTable(balance) {
    let balanceTable = $('#balance-table');
    let tableBody = balanceTable.find('tbody');
    tableBody.empty();
    let row = createRow(balance);
    tableBody.append(row);
    balancePopup.modal('show');
}

/**
 * Создать строку с суммой
 *
 * @param balanceDTO {BalanceDTO} DTO транзакции
 * @returns строка таблицы
 */
function createRow(balanceDTO) {
    return $('<tr>').append(
        $('<td>').text(balanceDTO.ownerName),
        $('<td>').text((balanceDTO.summary).toLocaleString())
    );
}
