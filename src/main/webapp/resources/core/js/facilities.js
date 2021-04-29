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

let Facility = function () {}

Facility.prototype = {
    id: 0,
    name: '',
    fullName: '',
    city: '',
    build: function (id, name, fullName, city) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.city = city;
    }
}

let confirmForm;
let facilityForm;

jQuery(document).ready(function ($) {
    confirmForm = $('#confirm-form');
    facilityForm = $('#facility-modal-form');
    onCreateFacilityEvent()
    onUpdateFacilityEvent()
    onDeleteFacilityEvent()
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

function create() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let facility = createFacility(null);

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "create",
        data : JSON.stringify(facility),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            showPopup(data.message);
            if (data.status === 200) {
                clearForm();
            }
        },
        error: function(request, status, error){
            console.log(request.responseText);
            console.log(status);
            console.log(error);
        },
        always: function() {
            enableButton(true);
            closeLoader();
        }
    });
}

function enableButton(flag) {
    $("a#delete").prop("disabled", flag);
}

function createFacility(facilityId) {
    let facility = new Facility();
    let name = $('#f_name').val();
    let fullName = $('#full_name').val();
    let city = $('#city').val();
    facility.build(facilityId, name, fullName, city);
    return facility;
}

function clearForm() {
    $('#f_name').val('');
    $('#full_name').val('');
    $('#city').val('');
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
 * Показать форму для создания/редактирования объекта
 *
 * @param action {Action} действие
 */
function showForm(action) {
    let title = ''
    let button = ''
    switch (action) {
        case Action.CREATE:
            title = 'Создание объекта'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление объекта'
            button = 'Обновить'
            break
    }
    facilityForm.find('#title').html(title)
    facilityForm.find('#accept').html(button)
    facilityForm.find('#accept').attr('data-action', action)
    facilityForm.modal('show')
}

/**
 * Получить объект по id
 *
 * @param facilityId
 */
function getFacility(facilityId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let facilityDTO = new Facility()
    facilityDTO.build(facilityId, null, null, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(facilityDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showUpdateFacilityForm(data)
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error);
        }
    });
}

/**
 * Событие при создании объекта
 */
function onCreateFacilityEvent() {
    $(document).on('click', '#create-facility',function (e) {
        e.preventDefault()
        showForm(Action.CREATE)
    })
}

/**
 * Событие при изменении объекта
 */
function onUpdateFacilityEvent() {
    $(document).on('click', '#edit-facility',function (e) {
        e.preventDefault()
        let facilityId = $(this).attr('data-facility-id')
        getFacility(facilityId)
        showForm(Action.UPDATE)
    })
}

/**
 * Событие при удалении объекта
 */
function onDeleteFacilityEvent() {
    $(document).on('click', '#delete-facility',function (e) {
        e.preventDefault()
        let facilityId = $(this).attr('data-facility-id')
        showConfirmForm('Удаление объекта', 'Действительно хотите удалить объект?', facilityId, Action.DELETE)
    })
}

function onAcceptFormEvent() {
    facilityForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let facilityDTO = getFacilityDTO()
        if (check(facilityDTO)) {
            let action = facilityForm.find('#accept').attr('data-action')
            facilityForm.modal('hide')
            save(facilityDTO, action)
        }
    })
    acceptConfirm()
}

/**
 * Показать форму для изменения объекта
 *
 * @param data
 */
function showUpdateFacilityForm(data) {
    let facilityDTO = new Facility()
    facilityDTO.build(data.id, data.name, data.fullName, data.city)
    facilityForm.find('#facility-id').val(facilityDTO.id)
    facilityForm.find('#edit').val(true)
    facilityForm.find('#name').val(facilityDTO.name)
    facilityForm.find('#full-name').val(facilityDTO.fullName)
    facilityForm.find('#city').val(facilityDTO.city)
    facilityForm.find('#action').attr("data-action", Action.UPDATE)
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

/**
 * Собрать DTO с формы
 *
 * @return {Facility}
 */
function getFacilityDTO() {
    let facilityDTO = new Facility()
    let facilityId = facilityForm.find('#facility-id').val()
    let name = facilityForm.find('#name').val()
    let fullName = facilityForm.find('#full-name').val()
    let city = facilityForm.find('#city').val()
    facilityDTO.build(facilityId, name, fullName, city)
    return facilityDTO
}

/**
 * Проверить заполнение полей формы
 *
 * @param facilityDTO {Facility} DTO для проверки
 * @return {boolean} результат проверки
 */
function check(facilityDTO) {
    let facilityNameError = facilityForm.find('#facilityNameError');
    if (facilityDTO.name.length === 0) {
        facilityNameError.addClass('d-block')
        return false
    } else {
        facilityNameError.removeClass('d-block')
    }
    let fullNameError = facilityForm.find('#fullNameError')
    if (facilityDTO.fullName.length === 0) {
        fullNameError.addClass('d-block')
        return false
    } else {
        fullNameError.removeClass('d-block')
    }
    return true
}

/**
 * Создать/обновить Объект
 *
 * @param facilityDTO {Facility} DTO объекта
 * @param action {Action} действие
 */
function save(facilityDTO, action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[action].url,
        data: JSON.stringify(facilityDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message);
            window.location.href = 'list'
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error);
        }
    });
}

/**
 * Подтверждение с формы
 *
 */
function acceptConfirm() {
    confirmForm.find('#accept').on('click', function () {
        let facilityId = confirmForm.find('#accept').attr('data-object-id')
        confirmForm.modal('hide')
        deleteFacility(facilityId)
        $('#facilities-table').find('tr#' + facilityId).remove();
    })
}

/**
 * Удалить объект
 *
 * @param facilityId id объекта
 */
function deleteFacility(facilityId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let roleDTO = {
        id: facilityId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.DELETE].url,
        data: JSON.stringify(roleDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message)
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error)
        }
    });
}
