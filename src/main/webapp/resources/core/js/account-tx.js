let AccountTxDTO = function () {}

AccountTxDTO.prototype = {
    id: null,
    facilityId: null,
    underFacilityId: null,
    dateReinvest: null,
    shareType: null,
    build: function (id, facilityId, underFacilityId, dateReinvest, shareType) {
        this.id = id
        this.facilityId = facilityId
        this.underFacilityId = underFacilityId
        this.dateReinvest = dateReinvest
        this.shareType = shareType
    }
}

let freeCash;
let reinvestForm;

jQuery(document).ready(function ($) {
    freeCash = $('#free-cash')
    reinvestForm = $('#reinvest-form-modal')
    showReinvestForm()
    acceptReinvest()
    onChangeFacilitySelect()
})

/**
 * Показать форму реинвестирования
 */
function showReinvestForm() {
    freeCash.find('#reinvest').on('click', function (e) {
        e.preventDefault()
        reinvestForm.find('#acc-tx-id').val($(this).attr('data-object-id'))
        reinvestForm.modal('show')
    })
}

/**
 * Подтверждение реинвестирования
 */
function acceptReinvest() {
    reinvestForm.find('#accept').on('click', function (e) {
        e.preventDefault()
        let accountTxDTO = getAccountTxDTO()
        if (checkDTO(accountTxDTO)) {
            reinvest(accountTxDTO)
        }
    })
}

/**
 * Получить DTO с формы
 *
 * @return {AccountTxDTO}
 */
function getAccountTxDTO() {
    let accountTxDTO = new AccountTxDTO()
    let accTxId = reinvestForm.find('#acc-tx-id').val()
    let facilityId = reinvestForm.find('#facility').find('option:selected').val()
    let underFacilityId = reinvestForm.find('#underFacility').find('option:selected').val()
    let dateReinvest = reinvestForm.find('#dateReinvest').val()
    let shareType = reinvestForm.find('#shareType').find('option:selected').val()
    accountTxDTO.build(accTxId, facilityId, underFacilityId, dateReinvest, shareType)
    return accountTxDTO
}

/**
 * Проверить правильность введённых данных
 *
 * @param accountTxDTO {AccountTxDTO}
 * @return {boolean} результат проверки
 */
function checkDTO(accountTxDTO) {
    let dateReinvestError = reinvestForm.find('#dateReinvestErr')
    if (accountTxDTO.dateReinvest.length === 0) {
        dateReinvestError.addClass('d-block')
        return false
    } else {
        dateReinvestError.removeClass('d-block')
    }
    let facilityError = reinvestForm.find('#facilityErr')
    if (accountTxDTO.facilityId === '0') {
        facilityError.addClass('d-block')
        return false
    } else {
        facilityError.removeClass('d-block')
    }
    let underFacilityError = reinvestForm.find('#underFacilityErr')
    if (accountTxDTO.underFacilityId === '0') {
        underFacilityError.addClass('d-block')
        return false
    } else {
        underFacilityError.removeClass('d-block')
    }
    let shareTypeError = reinvestForm.find('#shareTypeErr')
    if (accountTxDTO.shareType === 'Не определена') {
        shareTypeError.addClass('d-block')
        return false
    } else {
        shareTypeError.removeClass('d-block')
    }
    return true
}

/**
 * Событие при изменении списка объектов на форме реинвестирования
 */
function onChangeFacilitySelect() {
    reinvestForm.find('#facility').on('change', function () {
        let facilityId = $(this).find('option:selected').val()
        getUFFromLS(facilityId, 'underFacility')
    })
}

/**
 * Реинвестировать сумму
 *
 * @param accountTxDTO {AccountTxDTO} DTO для реинвестирования
 */
function reinvest(accountTxDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "free/reinvest",
        data: JSON.stringify(accountTxDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            let status = data.status
            if (status === 200) {
                reinvestForm.modal('hide')
                closeLoader();
                showPopup(data.message);
                window.location.href = '/money/free';
            } else {
                closeLoader();
                showPopup(data.message);
            }
        },
        error: function (e) {
            closeLoader();
            showPopup('Что-то пошло не так = [' + e.message + "]");
        },
        done: function (e) {
            enableSearchButton(true);
        }
    });
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

    reinvestForm.find('#' + ufSelectorId)
        .find('option')
        .remove()
        .end()
        .append(options)
        .selectpicker('refresh');
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
