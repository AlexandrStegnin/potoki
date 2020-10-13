let CashingMoneyDTO = function () {}

CashingMoneyDTO.prototype = {
    facilityId: null,
    underFacilityId: null,
    investorsIds: null,
    cash: 0.0,
    dateCashing: null,
    commission: 0.0,
    commissionNoMore: 0.0,
    all: false,
    build: function (facilityId, underFacilityId, investorsIds, cash, dateCashing, commission, commissionNoMore, all) {
        this.facilityId = facilityId;
        this.underFacilityId = underFacilityId;
        this.investorsIds = investorsIds;
        this.cash = cash;
        this.dateCashing = dateCashing;
        this.commission = commission;
        this.commissionNoMore = commissionNoMore;
        this.all = all;
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
            dto.all = false
            cashing(dto)
        }
    })

    cashingForm.find('#cashing-all').on('click', function (e) {
        e.preventDefault()
        let dto = getCashingDTO()
        if (checkDTO(dto)) {
            dto.all = true
            cashing(dto)
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
            let commission = cashingForm.find('#commission').val();
            if (facilityId === '0' || investorsIds.length === 0 || cash.length === 0 || dateCashing.length === 0 || commission.length === 0) {
                validate.errors = true;
                cashingForm.find('#cashing-all').addClass('d-none');
            } else {
                cashingForm.find('#cashing-all').removeClass('d-none');
            }
        }
    };
    cashingForm.find('#facility').on('change', function () {
        validate.cashingAll()
        let facilityId = $(this).val()
        getUFFromLS(facilityId, 'underFacility')
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
    cashingForm.find('#commission').on('input', function () {
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
 * @param dto {CashingMoneyDTO}
 * @return {boolean}
 */
function checkDTO(dto) {
    let facilityErr = cashingForm.find('#facilityError')
    if (dto.facilityId.length === 0 || dto.facilityId === '0') {
        facilityErr.addClass('d-block')
        return false
    } else {
        facilityErr.removeClass('d-block')
    }
    let underFacilityErr = cashingForm.find('#underFacilityError')
    if (dto.underFacilityId.length === 0 || dto.underFacilityId === '0') {
        underFacilityErr.addClass('d-block')
        return false
    } else {
        underFacilityErr.removeClass('d-block')
    }
    let investorErr = cashingForm.find('#investorError')
    if (dto.investorsIds.length === 0) {
        investorErr.addClass('d-block')
        return false
    } else {
        investorErr.removeClass('d-block')
    }
    let commissionErr = cashingForm.find('#commissionError')
    if (dto.commission.length === 0) {
        commissionErr.addClass('d-block')
        return false
    } else {
        commissionErr.removeClass('d-block')
    }
    let cashErr = cashingForm.find('#toBigSumForCashing')
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

/**
 * Вывести деньги
 *
 * @param dto {CashingMoneyDTO}
 */
function cashing(dto) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "cashing",
        data: JSON.stringify(dto),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            let status = data.status
            if (status === 200) {
                cashingForm.modal('hide')
                closeLoader();
                showPopup(data.message);
                window.location.href = '/money/list';
            } else {
                closeLoader();
                let errorDiv = $('#toBigSumForCashing')
                errorDiv.html(data.error)
                errorDiv.addClass('d-block')
            }
        },
        error: function (e) {
            closeLoader();
            showPopup('Что-то пошло не так = [' + e.error + "]");
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
            option.setAttribute('value', item.underFacility);
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
            option.setAttribute('value', item.underFacility);
            option.innerText = item.underFacility;
            return option;
        });
        option = document.createElement('option');
        option.setAttribute('id', "0");
        option.setAttribute('value', 'Без подобъекта');
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
