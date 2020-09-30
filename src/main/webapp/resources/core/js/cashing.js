let CashingMoneyDTO = function () {}

CashingMoneyDTO.prototype = {
    facilityId: null,
    underFacilityId: null,
    investorsId: null,
    cash: 0.0,
    dateCashing: null,
    commission: 0.0,
    commissionNoMore: 0.0,
    build: function (facilityId, underFacilityId, investorsId, cash, dateCashing, commission, commissionNoMore) {
        this.facilityId = facilityId;
        this.underFacilityId = underFacilityId;
        this.investorsId = investorsId;
        this.cash = cash;
        this.dateCashing = dateCashing;
        this.commission = commission;
        this.commissionNoMore = commissionNoMore;
    }
}

let cashingForm;

jQuery(document).ready(function ($) {

    cashingForm = $('#cashing-form-modal')

    subscribeFormChange()

    $('#cashingMoney').on('click', function (e) {
        e.preventDefault()
        cashingForm.modal('show')
    })

    cashingForm.find('#accept').on('click', function (e) {
        e.preventDefault()
        let dto = getCashingDTO()
        if (checkDTO(dto)) {
            console.log("VALID")
        }
    })

});

function subscribeFormChange() {
    let validate = {
        'cashingAll': function () {
            validate.errors = false
            let facilityId = cashingForm.find('#facility').find('option:selected').val();
            let investorsIds = [];
            $.map(cashingForm.find('#investor').find('option:selected'), function (el) {
                investorsIds.push(el.value)
            })
            let cash = cashingForm.find('#cash').val();
            let dateCashing = cashingForm.find('#dateCashing').val();
            if (facilityId === '0' || investorsIds.length === 0 || cash.length === 0 || dateCashing.length === 0) {
                validate.errors = true;
                cashingForm.find('#cashing-all').addClass('d-none');
            } else {
                cashingForm.find('#cashing-all').removeClass('d-none');
            }
        }
    };
    cashingForm.find('#facility').on('change', function () {
        validate.cashingAll()
    })
    cashingForm.find('#investor').on('change', function () {
        validate.cashingAll()
    })
    cashingForm.find('#cash').on('input', function () {
        validate.cashingAll()
    })
    cashingForm.find('#dateCashing').on('input', function () {
        validate.cashingAll()
    })
}

/**
 * Получить DTO с формы вывода денег
 *
 * @returns {CashingMoneyDTO}
 */
function getCashingDTO() {
    let facility = $('#facility')
    let underFacility = $('#underFacility')
    let investor = $('#investor')
    let cash = $('#cash')
    let dateCashing = $('#dateCashing')
    let commission = $('#commission')
    let commissionNoMore = $('#commissionNoMore')
    let dto = new CashingMoneyDTO()

    let investorsIds = []
    $.map(investor.find('option:selected'), function (el) {
        investorsIds.push(el.value)
    })

    dto.build(facility.val(), underFacility.find('option:selected').attr('id'), investorsIds, cash.val(),
        dateCashing.val(), commission.val(), commissionNoMore.val())
    return dto
}

/**
 * Проверить полученное DTO на валидность
 *
 * @param dto
 */
function checkDTO(dto) {
    let facilityErr = $('#facilityError')
    if (dto.facilityId.length === 0 || dto.facilityId === '0') {
        facilityErr.addClass('d-block')
        return false
    } else {
        facilityErr.removeClass('d-block')
    }
    let underFacilityErr = $('#underFacilityError')
    if (dto.underFacilityId.length === 0 || dto.underFacilityId === '0') {
        underFacilityErr.addClass('d-block')
        return false
    } else {
        underFacilityErr.removeClass('d-block')
    }
    let investorErr = $('#investorError')
    if (dto.investorsId.length === 0) {
        investorErr.addClass('d-block')
        return false
    } else {
        investorErr.removeClass('d-block')
    }
    let cashErr = $('#toBigSumForCashing')
    if (dto.cash.length === 0 || dto.cash === '0') {
        cashErr.html('Укажите сумму вывода > 0')
        cashErr.addClass('d-block')
        return false
    } else {
        cashErr.html()
        cashErr.removeClass('d-block')
    }
    return true
}
