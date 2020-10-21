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
    txDate: null,
    operationType: null,
    payer: null,
    owner: null,
    recipient: null,
    cash: null,
    cashType: null,
    blocked: null,
    build: function (txDate, operationType, payer, owner, recipient, cash, cashType, blocked) {
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

let AccountTXReinvestDTO = function () {}

AccountTXReinvestDTO.prototype = {
    dateReinvest: null,
    cash: null,
    facilityId: null,
    underFacilityId: null,
    shareType: null,
    accountsIds: [],
    build: function (dateReinvest, cash, facilityId, underFacilityId, shareType, accountsIds) {
        this.dateReinvest = dateReinvest
        this.cash = cash
        this.facilityId = facilityId
        this.underFacilityId = underFacilityId
        this.shareType = shareType
        this.accountsIds = accountsIds
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

let popupTable;

let reinvestModal;

let txModalTable;

jQuery(document).ready(function ($) {
    popupTable = $('#popup-table')
    reinvestModal = $('#reinvest-form-modal')
    txModalTable = $('#tx-popup-table')
    showPageableResult()
    subscribeCheckAllClick()
    subscribeCheckboxChange()
    clearFilters()
    toggleAllRows()
    subscribeTxShowClick()
    subscribeReinvestClick()
    subscribeFacilitySelectChange()
    subscribeAcceptReinvest()
    subscribeShowTxs()
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
            toggleStateReinvestButton(!checked)
        });
        checkItBtn.data('checked', !checked)
    })
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
 * Изменение состояния чекбоксов
 */
function subscribeCheckboxChange() {
    $(document).on('change', 'input:checkbox', function () {
        if ($(this).prop('checked')) {
            toggleStateReinvestButton(true)
        } else {
            let count = countChecked()
            toggleStateReinvestButton(count > 0)
        }
    })
}

/**
 * Посчитать кол-во отмеченных чекбоксов
 * @return {Number} кол-во отмеченных чекбоксов
 */
function countChecked() {
    return $('table#transactions').find('input:checkbox:checked').length
}

/**
 * Блокировать/разблокировать кнопку "Реинвестировать"
 *
 * @param enable {boolean} признак блокировать или разблокировать
 */
function toggleStateReinvestButton(enable) {
    if (enable) {
        $('#reinvest').removeClass('disabled')
    } else {
        $('#reinvest').addClass('disabled')
    }
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
 * Клик по кнопке "Реинвестировать"
 */
function subscribeReinvestClick() {
    $('#reinvest').on('click', function () {
        reinvestModal.modal('show')
    })
}

/**
 * Заполнить выпадающий список подобъектов на основе выбранного объекта
 *
 * @param facilityId id объекта
 * @param ufSelectorId id выпадающего списка
 */
function getUFFromLS(facilityId, ufSelectorId) {
    let underFacilities;
    underFacilities = JSON.parse(localStorage.getItem('uf'));
    let option;

    let options;
    if (underFacilities === null) populateStorageUnderFacilities(ufSelectorId);
    if (facilityId === '0') {
        options = underFacilities.map(function (item) {
            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parent-id', item.facilityId);
            option.setAttribute('value', item.id);
            option.innerText = item.underFacility;
            return option;
        });
    } else {
        options = underFacilities.filter(function (item) {
            return item.facilityId === parseInt(facilityId);
        }).map(function (item) {
            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parent-id', item.facilityId);
            option.setAttribute('value', item.id);
            option.innerText = item.underFacility;
            return option;
        });
        option = document.createElement('option');
        option.setAttribute('id', "0");
        option.setAttribute('value', '0');
        option.innerText = 'Без подобъекта';
        options.unshift(option);
    }

    $('#' + ufSelectorId)
        .find('option')
        .remove()
        .end()
        .append(options)
        .selectpicker('refresh');
}

/**
 * При выборе объекта из выпадающего списка
 */
function subscribeFacilitySelectChange() {
    reinvestModal.find('#facility').on('change', function () {
        let facilityId = $(this).val()
        getUFFromLS(facilityId, 'underFacility')
    })
}

/**
 * При подтверждении реивестирования
 */
function subscribeAcceptReinvest() {
    reinvestModal.find('#accept').on('click', function () {
        let dateReinvest = reinvestModal.find('#dateReinvest').val()
        let cash = reinvestModal.find('#cash').val()
        let facilityId = reinvestModal.find('#facility').val()
        let underFacilityId = reinvestModal.find('#underFacility').val()
        let shareType = reinvestModal.find('#shareType').val()
        let accTxReinvestDTO = new AccountTXReinvestDTO()
        accTxReinvestDTO.build(dateReinvest, cash, facilityId, underFacilityId, shareType)
        if (checkDTO(accTxReinvestDTO)) {
            accTxReinvestDTO.accountsIds = getAccountsIds()
            reinvest(accTxReinvestDTO)
        }
    })
}

/**
 * Проверить правильность заполнения формы реинвестирования
 *
 * @param accTxReinvestDTO {AccountTXReinvestDTO} DTO с формы
 * @return {boolean} результат проверки
 */
function checkDTO(accTxReinvestDTO) {
    let dateReinvestErr = reinvestModal.find('#dateReinvestErr')
    if (accTxReinvestDTO.dateReinvest.length === 0) {
        dateReinvestErr.addClass('d-block')
        return false
    } else {
        dateReinvestErr.removeClass('d-block')
    }
    let facilityErr = $('#facilityErr')
    if (accTxReinvestDTO.facilityId === '0') {
        facilityErr.addClass('d-block')
        return false
    } else {
        facilityErr.removeClass('d-block')
    }
    let underFacilityErr = $('#underFacilityErr')
    if (accTxReinvestDTO.underFacilityId === '0') {
        underFacilityErr.addClass('d-block')
        return false
    } else {
        underFacilityErr.removeClass('d-block')
    }
    let shareTypeErr = $('#shareTypeErr')
    if (accTxReinvestDTO.shareType === 'Не определена') {
        shareTypeErr.addClass('d-block')
        return false
    } else {
        shareTypeErr.removeClass('d-block')
    }
    return true
}

/**
 * Реинвестировать суммы
 *
 * @param accTxReinvestDTO {AccountTXReinvestDTO} DTO для реинвестирования
 */
function reinvest(accTxReinvestDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    reinvestModal.modal('hide')
    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: 'reinvest',
        data: JSON.stringify(accTxReinvestDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            if (data.status === 200) {
                showPopup(data.message)
            } else {
                showPopup(data.error)
            }
        },
        error: function (e) {
            closeLoader()
            showPopup(e);
        },
        always: function () {
            closeLoader()
        }
    });
}

/**
 * Получить список id аккаунтов, с которых реинвестировать
 *
 * @return {[]}
 */
function getAccountsIds() {
    let accIds = []
    let checkedAccounts = $('table#transactions').find('input:checkbox:checked:not(disabled)')
    $.each(checkedAccounts, function (ind, el) {
        accIds.push($(el).data('object-id'))
    })
    return accIds
}

/**
 * При клике на кнопке "Посмотреть транзакции"
 */
function subscribeShowTxs() {
    $('#show-txs').on('click', function () {
        let filter = getFilter()
        showTxs(filter)
        // txModalTable.modal('show')
    })
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

/**
 * Показать список отфильтрованных транзакций
 *
 * @param filter {AccFilter} фильтр
 */
function showTxs(filter) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: 'popup',
        data: JSON.stringify(filter),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            createTxTable(data)
        },
        error: function (e) {
            closeLoader()
            showPopup(e);
        },
        always: function () {
            closeLoader()
        }
    });
}

/**
 * Заполнить таблицу во всплывающей форме по данным с сервера
 *
 * @param txs {[AccountTransactionDTO]}
 */
function createTxTable(txs) {
    let txTable = $('#tx-table');
    let tableBody = txTable.find('tbody');
    tableBody.empty();
    $.each(txs, function (ind, el) {
        let row = createTxRow(el);
        tableBody.append(row);
    });
    txModalTable.modal('show');
}

/**
 * Создать строку с суммой
 *
 * @param transactionDTO {AccountTransactionDTO} DTO транзакции
 * @returns строка таблицы
 */
function createTxRow(transactionDTO) {
    return $('<tr>').append(
        $('<td>').text(getDate(transactionDTO.txDate).toLocaleDateString()),
        $('<td>').text(transactionDTO.owner),
        $('<td>').text((transactionDTO.cash).toLocaleString()),
        $('<td>').text(transactionDTO.operationType),
        $('<td>').text(transactionDTO.cashType),
        $('<td>').text(transactionDTO.payer),
        $('<td><input type="checkbox"/></td>')
    );
}
