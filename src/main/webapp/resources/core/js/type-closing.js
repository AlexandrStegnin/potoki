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

let TypeClosingDTO = function () {}

TypeClosingDTO.prototype = {
    id: null,
    name: null,
    build: function (id, name) {
        this.id = id
        this.name = name
    }
}

let confirmForm;
let typeClosingForm;

jQuery(document).ready(function ($) {

    confirmForm = $('#confirm-form');
    typeClosingForm = $('#type-closing-modal-form');

    onCreateTypeClosingEvent()
    onUpdateTypeClosingEvent()
    onDeleteTypeClosingEvent()
    onAcceptFormEvent()
});

/**
 * Событие при создании вида закрытия
 */
function onCreateTypeClosingEvent() {
    $(document).on('click', '#create-type-closing',function (e) {
        e.preventDefault()
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
            title = 'Создание вида закрытия'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление вида закрытия'
            button = 'Обновить'
            break
    }
    typeClosingForm.find('#title').html(title)
    typeClosingForm.find('#accept').html(button)
    typeClosingForm.find('#accept').attr('data-action', action)
    typeClosingForm.modal('show')
}

/**
 * Событие при изменении вида закрытия
 */
function onUpdateTypeClosingEvent() {
    $(document).on('click', '#edit-type',function (e) {
        e.preventDefault()
        let typeId = $(this).attr('data-type-id')
        getTypeClosing(typeId)
        showForm(Action.UPDATE)
    })
}

/**
 * Получить вид закрытия по id
 *
 * @param typeId {number}
 */
function getTypeClosing(typeId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    let typeDTO = new TypeClosingDTO()
    typeDTO.build(typeId, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(typeDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader()
            showUpdateTypeClosingForm(data)
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
 * Показать форму для изменения вида закрытия
 *
 * @param data
 */
function showUpdateTypeClosingForm(data) {
    let typeDTO = new TypeClosingDTO()
    typeDTO.build(data.id, data.name)
    typeClosingForm.find('#type-closing-id').val(typeDTO.id)
    typeClosingForm.find('#edit').val(true)
    typeClosingForm.find('#name').val(typeDTO.name)
    typeClosingForm.find('#action').attr("data-action", Action.UPDATE)
}

/**
 * Событие при удалении вида закрытия
 */
function onDeleteTypeClosingEvent() {
    $(document).on('click', '#delete-type',function (e) {
        e.preventDefault()
        let typeId = $(this).attr('data-type-id')
        showConfirmForm('Удаление вида закрытия', 'Действительно хотите удалить вид закрытия?', typeId, Action.DELETE)
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
    typeClosingForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let typeDTO = getTypeClosingDTO()
        if (check(typeDTO)) {
            let action = typeClosingForm.find('#accept').attr('data-action')
            typeClosingForm.modal('hide')
            save(typeDTO, action)
        }
    })
    acceptConfirm()
}

/**
 * Собрать DTO с формы
 *
 * @return {TypeClosingDTO}
 */
function getTypeClosingDTO() {
    let typeDTO = new TypeClosingDTO()
    let typeId = typeClosingForm.find('#type-closing-id').val()
    let name = typeClosingForm.find('#name').val()
    typeDTO.build(typeId, name)
    return typeDTO
}

/**
 * Создать/обновить вид закрытия
 *
 * @param typeDTO {TypeClosingDTO} DTO вида закрытия
 * @param action {Action} действие
 */
function save(typeDTO, action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[action].url,
        data: JSON.stringify(typeDTO),
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
 * Проверить заполнение полей формы
 *
 * @param typeDTO {TypeClosingDTO} DTO для проверки
 * @return {boolean} результат проверки
 */
function check(typeDTO) {
    let typeClosingNameError = typeClosingForm.find('#typeClosingNameError');
    if (typeDTO.name.length === 0) {
        typeClosingNameError.addClass('d-block')
        return false
    } else {
        typeClosingNameError.removeClass('d-block')
    }
    return true
}

/**
 * Подтверждение с формы
 *
 */
function acceptConfirm() {
    confirmForm.find('#accept').on('click', function () {
        let typeId = confirmForm.find('#accept').attr('data-object-id')
        confirmForm.modal('hide')
        deleteTypeClosing(typeId)
        $('#type-closing-table').find('tr#' + typeId).remove();
    })
}

/**
 * Удалить вид закрытия вложений
 *
 * @param typeId id вида закрытия
 */
function deleteTypeClosing(typeId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let detailDTO = {
        id: typeId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.DELETE].url,
        data: JSON.stringify(detailDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader();
            showPopup(data.message);
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
