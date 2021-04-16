let reBuyModal;

let ReBuyDTO = function () {
}

ReBuyDTO.prototype = {
    buyerId: null,
    buyerCash: 0.0,
    sellerId: null,
    facilityId: null,
    openedCash: null,
    realDateGiven: null,
    build: function (buyerId, buyerCash, sellerId, facilityId, openedCash, realDateGiven) {
        this.buyerId = buyerId
        this.buyerCash = buyerCash
        this.sellerId = sellerId
        this.facilityId = facilityId
        this.openedCash = openedCash
        this.realDateGiven = realDateGiven
    }
}

let InvestorCashDTO = function () {}

InvestorCashDTO.prototype = {
    id: null,
    givenCash: 0.0
}

let FacilityDTO = function () {}

FacilityDTO.prototype = {
    id: 0,
    name: '',
    build: function (id, name) {
        this.id = id
        this.name = name
    }
}

jQuery(document).ready(function ($) {
    reBuyModal = $('#re-buy-share-form-modal')
    subscribeAcceptReBuy()
    subscribeReBuyClick()
    subscribeSellerChange()
    subscribeProjectChange()
    openedCashChange()
})

/**
 * Клик по кнопке "Перепокупка доли"
 */
function subscribeReBuyClick() {
    $('#re-buy-share').on('click', function () {
        if (linkHasClass($(this))) return false;
        let txTable = $('#transactions')
        let checkBoxesChecked = txTable.find('td').find('input[type=checkbox]:checked')
        let ownersIds = []
        let ownersNames = []
        let ownersAccIds = []
        $.each(checkBoxesChecked, function (ind, el) {
            ownersIds.push($(el).data('owner-id'))
            ownersNames.push($(el).data('owner-name'))
            ownersAccIds.push($(el).data('owner-acc-id'))
        })
        let idOwners = unique(ownersIds)
        let nameOwners = unique(ownersNames)
        if (idOwners.length > 1) {
            $('#toManyOwnersErr').addClass('d-block')
            $('button#accept').attr('disabled', true)
        } else {
            let ownerId = idOwners[0]
            let accId = ownersAccIds[0]
            getAccBalance(accId)
            $('#toManyOwnersErr').removeClass('d-block')
            $('button#accept').removeAttr('disabled')
            if (idOwners.length === 1) {
                reBuyModal.find('#buyerId').val(ownerId)
                reBuyModal.find('#buyerLogin').val(nameOwners[0])
            }
        }
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
 * @param reBuyDTO {ReBuyDTO} DTO с формы
 * @return {boolean} результат проверки
 */
function checkReBuyDTO(reBuyDTO) {
    let sellerError = reBuyModal.find('#sellerError')
    if (reBuyDTO.sellerId === "0") {
        sellerError.addClass('d-block')
        return false
    } else {
        sellerError.removeClass('d-block')
    }
    let facilityErr = $('#facilityError')
    if (reBuyDTO.facilityId === "0") {
        facilityErr.addClass('d-block')
        return false
    } else {
        facilityErr.removeClass('d-block')
    }
    let openedCashError = $('#openedCashError')
    if (reBuyDTO.openedCash.length === 0) {
        openedCashError.addClass('d-block')
        return false
    } else {
        openedCashError.removeClass('d-block')
    }
    let realDateGivenError = $('#realDateError')
    if (reBuyDTO.realDateGiven.length === 0) {
        realDateGivenError.addClass('d-block')
        return false
    } else {
        realDateGivenError.removeClass('d-block')
    }
    let veryBigSumToBuy = $('#veryBigSumToBuy')
    let openedCashSum = 0
    $.each(reBuyDTO.openedCash, function (ind, el) {
        openedCashSum += parseFloat(el)
    })
    let sellerSum = $(reBuyModal.find('#buyerCash')).data('cash')
    if (openedCashSum > parseFloat(sellerSum)) {
        veryBigSumToBuy.addClass('d-block')
        return false
    } else {
        veryBigSumToBuy.removeClass('d-block')
    }
    return true
}

/**
 * При подтверждении перепродажи
 */
function subscribeAcceptReBuy() {
    reBuyModal.find('#accept').on('click', function () {
        let buyerId = reBuyModal.find('#buyerId').val()
        let buyerCash = parseFloat($(reBuyModal.find('#buyerCash')).data('cash')) | 0

        let sellerId = reBuyModal.find('#seller').val()
        let facilityId = reBuyModal.find('#projects').val()
        let selectedCash = $('#re-buy-share-form-modal').find('#openedCash').find('option:selected')
        let openCash = []
        $.each(selectedCash, function(ind, el) {
            let cashDTO = new InvestorCashDTO()
            cashDTO.id = el.id
            cashDTO.givenCash = el.value
            openCash.push(cashDTO)
        })

        let realDateGiven = reBuyModal.find('#realDate').val()

        let reBuyDTO = new ReBuyDTO()
        reBuyDTO.build(buyerId, buyerCash, sellerId, facilityId, openCash, realDateGiven)
        if (checkReBuyDTO(reBuyDTO)) {
            resaleShare(reBuyDTO)
        }
    })
}

/**
 * Событие изменения/выбора продавца из выпадающего списка
 */
function subscribeSellerChange() {
    reBuyModal.find('#seller').on('change', function () {
        let sellerId = $(this).find('option:selected').val()
        getOpenedProjects(sellerId)
    })
}

/**
 * Найти проекты, в которых у продавца есть открытые суммы
 *
 * @param sellerId {number} id продавца
 */
function getOpenedProjects(sellerId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: '/facilities/opened',
        data: JSON.stringify(sellerId),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            fillFacilitiesSelect(data)
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
 * Заполнить выпадающий список объектами продавца
 * @param facilities {[FacilityDTO]} DTO объектов
 */
function fillFacilitiesSelect(facilities) {
    closeLoader();
    let option
    let options = facilities.map(function (facility) {
        option = document.createElement('option');
        option.setAttribute('id', facility.id);
        option.setAttribute('value', facility.id);
        option.innerText = facility.name;
        return option;
    });
    option = document.createElement('option');
    option.setAttribute('id', "0");
    option.setAttribute('value', '0');
    option.innerText = 'Выберите объект';
    options.unshift(option);
    $('#projects')
        .find('option')
        .remove()
        .end()
        .append(options)
        .selectpicker('refresh');
}

/**
 * Событие изменения/выбора продавца из выпадающего списка
 */
function subscribeProjectChange() {
    reBuyModal.find('#projects').on('change', function () {
        let projectId = $(this).find('option:selected').val()
        let sellerId = reBuyModal.find('#seller').find('option:selected').val()
        getOpenedMonies(projectId, sellerId)
    })
}

/**
 * Найти открытые суммы по конкретному проекту
 *
 * @param projectId {number} id объекта/проекта
 * @param sellerId {number} id инвестора продавца
 */
function getOpenedMonies(projectId, sellerId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader();

    let reBuyDTO = new ReBuyDTO()
    reBuyDTO.sellerId = sellerId
    reBuyDTO.facilityId = projectId

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: '/money/opened',
        data: JSON.stringify(reBuyDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            fillOpenedMoniesSelect(data)
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
 * Заполнить выпадающий список с открытыми суммами в проекте
 *
 * @param reBuyDTOs {[InvestorCashDTO]}
 */
function fillOpenedMoniesSelect(reBuyDTOs) {
    closeLoader();
    let option
    let options = reBuyDTOs.map(function (money) {
        option = document.createElement('option');
        option.setAttribute('id', money.id);
        option.setAttribute('value', money.givenCash);
        option.innerText = Intl.NumberFormat('ru', {
            style: "currency",
            currency: "RUB"
        }).format(money.givenCash);
        return option;
    });
    options.unshift(option);
    $('#openedCash')
        .find('option')
        .remove()
        .end()
        .append(options)
        .selectpicker('refresh');
}

function openedCashChange() {
    let oCashSelPicker = reBuyModal.find('#openedCash')
    oCashSelPicker.on('changed.bs.select', function () {
        let $title = $(this).parent().find('.filter-option-inner-inner')
        let values = $(this).val()
        let total = 0
        $.each(values, function (ind, el) {
            total += parseFloat(el)
        })
        $title.text($(this).data('prefix') + Intl.NumberFormat('ru', { style: "currency", currency: "RUB" }).format(total));
    });
}

function unique(array) {
    return $.grep(array, function (el, index) {
        return index === $.inArray(el, array);
    });
}

/**
 * Получить баланс по инвестору
 *
 * @param ownerId
 */
function getAccBalance(ownerId) {
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
            let buyerCashInp = reBuyModal.find('#buyerCash')
            buyerCashInp.val(Intl.NumberFormat('ru', {
                style: "currency",
                currency: "RUB"
            }).format(data.summary))
            $(buyerCashInp).data('cash', data.summary)
        })
        .fail(function (e) {
            showPopup('Что-то пошло не так [' + e.message + ']');
        })
}

/**
 * Перепродать долю
 *
 * @param reBuyDTO {ReBuyDTO} DTO для перепродажи
 */
function resaleShare(reBuyDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    reBuyModal.modal('hide')
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "buy-share",
        data: JSON.stringify(reBuyDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            showPopup(data.message)
        })
        .fail(function (jqXHR) {
            reBuyModal.modal('hide')
            showPopup(jqXHR.responseText);
        })
        .always(function () {
            closeLoader()
        })
}
