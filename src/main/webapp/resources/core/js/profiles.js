let popupTable;

let AccountSummaryDTO = function () {
}

AccountSummaryDTO.prototype = {
    accountId: null,
    payers: [],
    build: function (accountId, payers) {
        this.accountId = accountId
        this.payers = payers
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

jQuery(document).ready(function ($) {
    popupTable = $('#popup-table')
    onChangeBsSelect()
    onDisposeOfFundsClick()
    subscribeTxShowClick()
    subscribeSendEmail()
    onSendButtonClick()
});

/**
 * Нажатие кнопки "Просмотреть"
 */
function subscribeTxShowClick() {
    $('#free-cash').on('click', function () {
        let ownerId = $(this).data('owner-id')
        let accSummaryDTO = new AccountSummaryDTO()
        accSummaryDTO.build(ownerId, null)
        getDetails(accSummaryDTO)
    })
}

/**
 * При клике на кнопке "Распорядиться"
 */
function onDisposeOfFundsClick() {
    $('#disposeOfFunds').on('click', function (e) {
        e.preventDefault()
        showPopup('Функционал в разработке (;')
    })
}

/**
 * При изменении в выпадающем списке
 */
function onChangeBsSelect() {
    $('#investors').selectpicker().on('changed.bs.select', function () {
        let options = $("#investors option");
        options.sort(function (a, b) {
            if ($(a).attr('data-lastName') > $(b).attr('data-lastName')) return 1;
            else if ($(a).attr('data-lastName') < $(b).attr('data-lastName')) return -1;
            else return 0;
        });
        if ($('#investors option:selected').text() === "Выберите инвестора") {
            $('#viewInvestorData').attr('disabled', 'true');
        } else {
            $('#viewInvestorData').removeAttr('disabled');
        }
    });
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
 * Получить детализацию по счёту
 *
 * @param accSummaryDTO {AccountSummaryDTO} DTO
 */
function getDetails(accSummaryDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "money/transactions/details",
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
 * Заполнить таблицу во всплывающей форме по данным с сервера
 *
 * @param details {[AccountTransactionDTO]}
 */
function createDetailTable(details) {
    let detailTable = $('#detail-table');
    detailTable.addClass('table-sm')
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
        $('<td>').text(getFormatter().format(transactionDTO.cash)),
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

let sendEmailForm = $('#send-email-modal-form')

function subscribeSendEmail() {
    $('#send-email').on('click', function () {
        sendEmailForm.modal('show')
    })
}

function onSendButtonClick() {
    $('#send').on('click', function () {
        if (checkInvestor()) {
            let userId = sendEmailForm.find('#user option:selected').val()
            sendEmail(userId)
        }
    })
}

function checkInvestor() {
    let userId = sendEmailForm.find('#user option:selected').val()
    let investorError = sendEmailForm.find('#investorError')
    if (userId + '' === '0') {
        investorError.addClass('d-block')
        return false
    } else {
        investorError.addClass('d-none')
    }
    return true
}

/**
 * Отправка email
 * @param userId {Number} id пользователя
 */
function sendEmail(userId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    let userDTO = {
        id: userId
    }
    let emailDTO = {
        user: userDTO
    }
    sendEmailForm.modal('hide')
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: '/send/welcome',
        data: JSON.stringify(emailDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader()
            showPopup(data.message)
        })
        .fail(function (jqXHR) {
            $('#content').addClass('bg-warning')
            showPopup(jqXHR.responseText);
        })
        .always(function () {
            closeLoader()
        });
}
