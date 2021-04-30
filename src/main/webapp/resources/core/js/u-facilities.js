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
    facility: null,
    build: function (id, name, facility) {
        this.id = id;
        this.name = name;
        this.facility = facility;
    }
}

let FacilityDTO = function () {}

FacilityDTO.prototype = {
    id: null,
    name: null,
    build: function (id, name) {
        this.id = id
        this.name = name
    }
}

let confirmForm;
let ufForm;

jQuery(document).ready(function ($) {
    confirmForm = $('#confirm-form');
    ufForm = $('#uf-modal-form');
    onCreateUnderFacilityEvent()
    onUpdateUnderFacilityEvent()
    onDeleteUnderFacilityEvent()
    onAcceptFormEvent()
    $('#create').click(function (event) {
        showLoader();
        let action = $(this).data('action')
        if (action === 'create') {
            event.preventDefault();
            create();
        }
        closeLoader();
    })
});

/**
 * Событие при создании подобъекта
 */
function onCreateUnderFacilityEvent() {
    $(document).on('click', '#create-uf',function (e) {
        e.preventDefault()
        showForm(Action.CREATE)
    })
}

/**
 * Событие при изменении подобъекта
 */
function onUpdateUnderFacilityEvent() {
    $(document).on('click', '#edit-uf',function (e) {
        e.preventDefault()
        showLoader()
        let ufId = $(this).attr('data-uf-id')
        getUnderFacility(ufId)
    })
}

/**
 * Показать форму для создания/редактирования подобъекта
 *
 * @param action {Action} действие
 */
function showForm(action) {
    let title = ''
    let button = ''
    switch (action) {
        case Action.CREATE:
            title = 'Создание подобъекта'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление подобъекта'
            button = 'Обновить'
            break
    }
    ufForm.find('#title').html(title)
    ufForm.find('#accept').html(button)
    ufForm.find('#accept').attr('data-action', action)
    ufForm.modal('show')
}

/**
 * Получить подобъект по id
 *
 * @param ufId {number}
 */
function getUnderFacility(ufId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let ufDTO = new UnderFacilityDTO()
    ufDTO.build(ufId, null, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(ufDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        })
        .done(function (data) {
            showUpdateUnderFacilityForm(data)
        })
        .fail(function (jqXHR) {
            $('#content').addClass('bg-warning')
            showPopup(jqXHR.responseText);
        })
        .always(function () {
            console.log('Закончили!');
        });
}

/**
 * Показать форму для изменения подобъекта
 *
 * @param data
 */
function showUpdateUnderFacilityForm(data) {
    let ufDTO = new UnderFacilityDTO()
    ufDTO.build(data.id, data.name, data.facility)
    ufForm.find('#uf-id').val(ufDTO.id)
    ufForm.find('#edit').val(true)
    ufForm.find('#name').val(ufDTO.name)
    bindFacility(ufDTO.facility)
    ufForm.find('#action').attr("data-action", Action.UPDATE)
    closeLoader()
    showForm(Action.UPDATE)
}

/**
 * Преобразовать объект в выделенный элемент выпадающего списка
 *
 * @param facility
 */
function bindFacility(facility) {
    $.each(ufForm.find('#facility option'), function (ind, el) {
        if (el.value === (facility.id + '')) {
            $(el).attr('selected', 'selected')
        }
    })
    ufForm.find('#facility').selectpicker('refresh')
}

function onAcceptFormEvent() {
    ufForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let ufDTO = getUnderFacilityDTO()
        if (check(ufDTO)) {
            let action = ufForm.find('#accept').attr('data-action')
            ufForm.modal('hide')
            save(ufDTO, action)
        }
    })
    acceptConfirm()
}

/**
 * Собрать DTO с формы
 *
 * @return {UnderFacilityDTO}
 */
function getUnderFacilityDTO() {
    let ufDTO = new UnderFacilityDTO()
    let ufId = ufForm.find('#uf-id').val()
    let name = ufForm.find('#name').val()
    let facilityId = ufForm.find('#facility option:selected').val()
    let facilityName = ufForm.find('#facility option:selected').text()
    let facilityDTO = new FacilityDTO()
    facilityDTO.build(facilityId, facilityName)
    ufDTO.build(ufId, name, facilityDTO)
    return ufDTO
}

/**
 * Проверить заполнение полей формы
 *
 * @param ufDTO {UnderFacilityDTO} DTO для проверки
 * @return {boolean} результат проверки
 */
function check(ufDTO) {
    let facilityError = ufForm.find('#facilityError')
    if (ufDTO.facility.id === '0') {
        facilityError.addClass('d-block')
        return false
    } else {
        facilityError.removeClass('d-block')
    }
    let ufNameError = ufForm.find('#ufNameError');
    if (ufDTO.name.length === 0) {
        ufNameError.addClass('d-block')
        return false
    } else {
        ufNameError.removeClass('d-block')
    }
    return true
}

/**
 * Создать/обновить подобъект
 *
 * @param ufDTO {UnderFacilityDTO} DTO подобъекта
 * @param action {Action} действие
 */
function save(ufDTO, action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[action].url,
        data: JSON.stringify(ufDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader()
            showPopup(data.message);
            window.location.href = 'list'
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
 * Подтверждение с формы
 *
 */
function acceptConfirm() {
    confirmForm.find('#accept').on('click', function () {
        let ufId = confirmForm.find('#accept').attr('data-object-id')
        confirmForm.modal('hide')
        deleteUnderFacility(ufId)
        $('#u-facilities-table').find('tr#' + ufId).remove();
    })
}

/**
 * Удалить объект
 *
 * @param ufId {number} id объекта
 */
function deleteUnderFacility(ufId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader()

    let ufDto = {
        id: ufId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.DELETE].url,
        data: JSON.stringify(ufDto),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader()
            showPopup(data.message);
            window.location.href = 'list'
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
 * Событие при удалении подобъекта
 */
function onDeleteUnderFacilityEvent() {
    $(document).on('click', '#delete-uf',function (e) {
        e.preventDefault()
        let ufId = $(this).attr('data-uf-id')
        showConfirmForm('Удаление подобъекта', 'Действительно хотите удалить подобъект?', ufId, Action.DELETE)
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
