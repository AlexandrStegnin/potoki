let AccountTxDTO = function () {
}

AccountTxDTO.prototype = {
    txIds: [],
    cashTypeIds: [],
    build: function (txIds, cashTypeIds) {
        this.txIds = txIds
        this.cashTypeIds = cashTypeIds
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

let confirmDelete;

let balancePopup;

let reinvestModal;

let msgModal;

jQuery(document).ready(function ($) {
    confirmDelete = $('#confirm-delete');
    balancePopup = $('#balance-popup-table')
    reinvestModal = $('#reinvest-form-modal')
    msgModal = $('msg-modal')
    showPageableResult()
    subscribeCheckAllClick()
    showConfirmDelete()
    acceptDelete()
    clearFilters()
    toggleAllRows()
    subscribeBalanceShowClick()
    subscribeReinvestClick()
    subscribeFacilitySelectChange()
    subscribeAcceptReinvest()
    subscribeCheckboxChange()
    subscribeReBuyClick()
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
            toggleStateDeleteButton(!checked)
            toggleStateReBuyButton(!checked)
        });
        checkItBtn.data('checked', !checked)
    })
}

/**
 * Показать форму подтверждения удаления
 */
function showConfirmDelete() {
    $('#delete-list').on('click', function () {
        if (linkHasClass($(this))) return false;
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
        let cashTypeIds = []
        $.each(options, function (ind, el) {
            checked.push($(el).data('object-id'))
            cashTypeIds.push($(el).data('cash-type'))
        })
        let accountTxDTO = new AccountTxDTO()
        accountTxDTO.build(checked, cashTypeIds)
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
    showLoader()
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
        .fail(function (jqXHR) {
            $('#content').addClass('bg-warning')
            showPopup(jqXHR.responseText);
        })
        .always(function () {
            closeLoader()
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
        $('#content').removeClass('bg-warning')
    }, 3000);
}

/**
 * При клике посмотреть баланс
 */
function subscribeBalanceShowClick() {
    $('.show-balance').on('click', function () {
        if (linkHasClass($(this))) return false;
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
    showLoader()
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
            closeLoader()
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
        $('<td>').text(getFormatter().format(balanceDTO.summary))
    );
}

/**
 * Получить форматтер для форматирования суммы денег
 *
 * @return {Intl.NumberFormat}
 */
function getFormatter() {
    return new Intl.NumberFormat('ru-RU', {
        style: 'currency',
        currency: 'RUB',
        // These options are needed to round to whole numbers if that's what you want.
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    })
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
 * Блокировать/разблокировать кнопку "Удалить выбранное"
 *
 * @param enable {boolean} признак блокировать или разблокировать
 */
function toggleStateDeleteButton(enable) {
    if (enable) {
        $('#delete-list').removeClass('disabled')
    } else {
        $('#delete-list').addClass('disabled')
    }
}

/**
 * Блокировать/разблокировать кнопку "Перепокупка доли"
 *
 * @param enable {boolean} признак блокировать или разблокировать
 */
function toggleStateReBuyButton(enable) {
    if (enable) {
        $('#re-buy-share').removeClass('disabled')
    } else {
        $('#re-buy-share').addClass('disabled')
    }
}

/**
 * Изменение состояния чекбоксов
 */
function subscribeCheckboxChange() {
    $(document).on('change', 'input:checkbox', function () {
        if ($(this).prop('checked')) {
            toggleStateReinvestButton(true)
            toggleStateDeleteButton(true)
            toggleStateReBuyButton(true)
        } else {
            let count = countChecked()
            toggleStateReinvestButton(count > 0)
            toggleStateDeleteButton(count > 0)
            toggleStateReBuyButton(count > 0)
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
 * Клик по кнопке "Реинвестировать"
 */
function subscribeReinvestClick() {
    $('#reinvest').on('click', function () {
        if (linkHasClass($(this))) return false;
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
        url: 'transactions/reinvest',
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
                $('#btn-search').click()
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
        accIds.push($(el).data('owner-id'))
    })
    return accIds
}

/**
 * Проверить наличие класса у элемента
 *
 * @param link элемент
 * @return {boolean} результат
 */
function linkHasClass(link) {
    if (link.hasClass('disabled')) return true;
}
