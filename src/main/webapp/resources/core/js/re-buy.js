let reBuyModal;

jQuery(document).ready(function ($) {
    reBuyModal = $('#re-buy-share-form-modal')
    subscribeAcceptReBuy()
    subscribeReBuyClick()
})

/**
 * Клик по кнопке "Перепокупка доли"
 */
function subscribeReBuyClick() {
    $('#re-buy-share').on('click', function () {
        if (linkHasClass($(this))) return false;
        reBuyModal.modal('show')
    })
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

/**
 * Проверить правильность заполнения формы реинвестирования
 *
 * @param accTxReinvestDTO {AccountTXReinvestDTO} DTO с формы
 * @return {boolean} результат проверки
 */
function checkReBuyDTO(accTxReinvestDTO) {
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
 * При подтверждении перепродажи
 */
function subscribeAcceptReBuy() {
    reBuyModal.find('#accept').on('click', function () {
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
