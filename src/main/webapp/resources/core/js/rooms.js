let Action = {
    CREATE: 'CREATE',
    UPDATE: 'UPDATE',
    FIND: 'FIND',
    DELETE: 'DELETE',
    properties: {
        CREATE: {
            url: 'create'
        },
        UPDATE: {
            url: 'update'
        },
        FIND: {
            url: 'find'
        },
        DELETE: {
            url: 'delete'
        }
    }
}

Object.freeze(Action)

let UnderFacilityDTO = function () {}

UnderFacilityDTO.prototype = {
    id: null,
    name: null,
    build: function (id, name) {
        this.id = id
        this.name = name
    }
}

let RoomDTO = function () {}

RoomDTO.prototype = {
    id: null,
    name: null,
    cost: null,
    roomSize: null,
    sold: false,
    dateSale: null,
    underFacility: null,
    dateBuy: null,
    salePrice: null,
    totalYearProfit: null,
    accountNumber: null,
    build: function (id, name, cost, roomSize, sold, dateSale, underFacility,
                     dateBuy, salePrice, totalYearProfit, accountNumber) {
        this.id = id
        this.name = name
        this.cost = cost
        this.roomSize = roomSize
        this.sold = sold
        this.dateSale = dateSale
        this.underFacility = underFacility
        this.dateBuy = dateBuy
        this.salePrice = salePrice
        this.totalYearProfit = totalYearProfit
        this.accountNumber = accountNumber
    }
}

let confirmForm;
let roomForm;

jQuery(document).ready(function ($) {

    confirmForm = $('#confirm-form');
    roomForm = $('#room-modal-form');

    onCreateRoomEvent()
    onUpdateRoomEvent()
    onDeleteRoomEvent()
    onAcceptFormEvent()
    onChangeSold()
});

/**
 * Событие при создании помещения
 */
function onCreateRoomEvent() {
    $(document).on('click', '#create-room',function (e) {
        e.preventDefault()
        clearForm()
        showForm(Action.CREATE)
    })
}

/**
 * Показать форму для создания/редактирования
 *
 * @param action {Action} действие
 */
function showForm(action) {
    let title = ''
    let button = ''
    switch (action) {
        case Action.CREATE:
            title = 'Создание помещения'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление помещения'
            button = 'Обновить'
            break
    }
    roomForm.find('#title').html(title)
    roomForm.find('#accept').html(button)
    roomForm.find('#accept').attr('data-action', action)
    roomForm.modal('show')
}

/**
 * Событие при изменении помещения
 */
function onUpdateRoomEvent() {
    $(document).on('click', '#edit-room',function (e) {
        e.preventDefault()
        let roomId = $(this).attr('data-room-id')
        getRoom(roomId)
        showForm(Action.UPDATE)
    })
}

/**
 * Получить помещение по id
 *
 * @param roomId {number}
 */
function getRoom(roomId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    let roomDTO = new RoomDTO()
    roomDTO.build(roomId, null, null, null, false,
        null, null, null, null, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(roomDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader()
            showUpdateRoomForm(data)
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
 * Показать форму для изменения помещения
 *
 * @param data
 */
function showUpdateRoomForm(data) {
    let roomDTO = new RoomDTO()
    roomDTO.build(data.id, data.name, data.cost, data.roomSize, data.sold, data.dateSale,
        data.underFacility, data.dateBuy, data.salePrice, data.totalYearProfit, data.accountNumber)
    roomForm.find('#room-id').val(roomDTO.id)
    roomForm.find('#edit').val(true)
    bindUnderFacility(roomDTO.underFacility.id)
    roomForm.find('#room').val(roomDTO.name)
    roomForm.find('#roomSize').val(roomDTO.roomSize)
    roomForm.find('#cost').val(roomDTO.cost)
    bindDate(roomDTO.dateBuy, 'dateBuy')
    bindSold(roomDTO.sold)
    bindDate(roomDTO.dateSale, 'dateSale')
    roomForm.find('#salePrice').val(roomDTO.salePrice)
    roomForm.find('#yearProfit').val(roomDTO.totalYearProfit)
    roomForm.find('#accountNumber').val(roomDTO.accountNumber)
    roomForm.find('#action').attr("data-action", Action.UPDATE)
}

/**
 * Событие при удалении токена
 */
function onDeleteRoomEvent() {
    $(document).on('click', '#delete-room',function (e) {
        e.preventDefault()
        let roomId = $(this).attr('data-room-id')
        showConfirmForm('Удаление помещения', 'Действительно хотите удалить помещение?', roomId, Action.DELETE)
    })
}

/**
 * Отобразить форму подтверждения
 *
 * @param title {String} заголовок формы
 * @param message {String} сообщение
 * @param objectId {String} идентификатор объекта
 * @param action {Action} действие
 */
function showConfirmForm(title, message, objectId, action) {
    confirmForm.find('#title').html(title)
    confirmForm.find('#message').html(message)
    confirmForm.find('#accept').attr('data-object-id', objectId)
    confirmForm.find('#accept').attr('data-action', action)
    confirmForm.modal('show')
}

function onAcceptFormEvent() {
    roomForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let roomDTO = getRoomDTO()
        if (check(roomDTO)) {
            let action = roomForm.find('#accept').attr('data-action')
            roomForm.modal('hide')
            save(roomDTO, action)
        }
    })
    acceptConfirm()
}

/**
 * Собрать DTO с формы
 *
 * @return {RoomDTO}
 */
function getRoomDTO() {
    let roomDTO = new RoomDTO()
    let roomId = roomForm.find('#room-id').val()
    let underFacilityId = roomForm.find('#underFacility option:selected').val()
    let underFacility = new UnderFacilityDTO()
    underFacility.build(underFacilityId, null, null)
    let name = roomForm.find('#room').val()
    let roomSize = roomForm.find('#roomSize').val()
    let cost = roomForm.find('#cost').val()
    let dateBuy = roomForm.find('#dateBuy').val()
    let soldAttr = roomForm.find('#sold option:selected').attr('id')
    let sold = soldAttr + '' !== '0';
    let dateSale = roomForm.find('#dateSale').val()
    let salePrice = roomForm.find('#salePrice').val()
    let yearProfit = roomForm.find('#yearProfit').val()

    roomDTO.build(roomId, name, cost, roomSize, sold, dateSale, underFacility, dateBuy, salePrice, yearProfit)
    return roomDTO
}

/**
 * Проверить заполнение полей формы
 *
 * @param roomDTO {RoomDTO} DTO для проверки
 * @return {boolean} результат проверки
 */
function check(roomDTO) {
    let underFacilityError = roomForm.find('#underFacilityError')
    if (roomDTO.underFacilityId === '0') {
        underFacilityError.addClass('d-block')
        return false
    } else {
        underFacilityError.removeClass('d-block')
    }
    let costError = roomForm.find('#costError')
    if (roomDTO.cost === 0) {
        costError.addClass('d-block')
        return false
    } else {
        costError.removeClass('d-block')
    }
    let nameError = roomForm.find('#nameError');
    if (roomDTO.name.length === 0) {
        nameError.addClass('d-block')
        return false
    } else {
        nameError.removeClass('d-block')
    }
    let sold = roomDTO.sold
    if (sold + '' === 1 + '') {
        let dateSaleError = roomForm.find('#dateSaleError');
        if (roomDTO.dateSale.length === 0) {
            dateSaleError.addClass('d-block')
            return false
        } else {
            dateSaleError.removeClass('d-block')
        }
    }
    return true
}

/**
 * Создать/обновить помещение
 *
 * @param roomDTO {RoomDTO} DTO помещения
 * @param action {Action} действие
 */
function save(roomDTO, action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[action].url,
        data: JSON.stringify(roomDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader();
            showPopup(data.message);
            window.location.href = 'list'
        })
        .fail(function (jqXHR) {
            $('#content').addClass('bg-warning')
            showPopup(jqXHR.responseText);
        })
        .always(function () {
            closeLoader()
        })
}

/**
 * Подтверждение с формы
 *
 */
function acceptConfirm() {
    confirmForm.find('#accept').on('click', function () {
        let roomId = confirmForm.find('#accept').attr('data-object-id')
        confirmForm.modal('hide')
        deleteRoom(roomId)
        $('#room-table').find('tr#' + roomId).remove();
    })
}

/**
 * Удалить помещение
 *
 * @param roomId id помещения
 */
function deleteRoom(roomId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let roomDTO = {
        id: roomId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.DELETE].url,
        data: JSON.stringify(roomDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader();
            showPopup(data.message);
            window.location.href = 'list'
        })
        .fail(function (jqXHR) {
            $('#content').addClass('bg-warning')
            showPopup(jqXHR.responseText);
        })
        .always(function () {
            closeLoader()
        })
}


function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

/**
 * Преобразовать id подобъекта в выделенный элемент выпадающего списка
 *
 * @param ufId id подобъекта
 */
function bindUnderFacility(ufId) {
    $.each(roomForm.find('#underFacility option'), function (ind, el) {
        if (el.value === (ufId + '')) {
            $(el).attr('selected', 'selected')
        }
    })
    roomForm.find('#underFacility').selectpicker('refresh')
}

/**
 * Преобразовать признак, что помещение продано в выделенный элемент списка
 *
 * @param isSale 0/1
 */
function bindSold(isSale) {
    let saleId = isSale ? 'Да' : 'Нет'
    $('#sold option[value="' + saleId + '"]').prop('selected', true);
    roomForm.find('#sold').selectpicker('refresh')
}

function onChangeSold() {
    let sold = $('#sold').val() === 'Да' ? 'block' : 'none';
    $('#dateSaleRow').css('display', sold);
    $('#dateSaleErr').css('display', 'none');

    $(document).on('change', '#sold', function () {
        if ($(this).find('option:selected').val() === 'Нет') {
            $('#soldHid').prop('checked', false);
            $('#dateSaleRow').attr('style', 'display: none');
        } else {
            $('#soldHid').prop('checked', true);
            $('#dateSaleRow').removeAttr('style');
        }
    });
    $(document).on('click', '#action', function (e) {
        if ($('#soldHid').prop('checked') && $('#dateSaleRow').find('#dateSale').val().length < 10) {
            e.preventDefault();
            $('#dateSaleErr').css('display', 'block');
        } else {
            $('#dateSaleErr').css('display', 'none');
        }
    })
}

/**
 * Функция для очистки полей формы
 */
function clearForm() {
    roomForm.find('#room-id').val()
    roomForm.find('#edit').val(false)
    bindUnderFacility(0)
    roomForm.find('#room').val('')
    roomForm.find('#roomSize').val('')
    roomForm.find('#cost').val('')
    roomForm.find('#dateBuy').val('')
    bindSold(false)
    roomForm.find('#dateSale').val('')
    roomForm.find('#salePrice').val('')
    roomForm.find('#yearProfit').val('')
    roomForm.find('#accountNumber').val('')
}

/**
 * Привязать дату к полю на форме
 *
 * @param dateInMillis {Date} дата в милисекундах
 * @param field поле даты (дата продажи/дата покупки)
 */
function bindDate(dateInMillis, field) {
    if (dateInMillis != null) {
        let date = new Date(dateInMillis)
        let dateToApply = date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2)
        roomForm.find('#' + field).val(dateToApply)
    }
}
