let Action = {
    CREATE: 'CREATE',
    UPDATE: 'UPDATE',
    FIND: 'FIND',
    DELETE: 'DELETE',
    properties: {
        CREATE: {
            url: 'tokens/create'
        },
        UPDATE: {
            url: 'tokens/update'
        },
        FIND: {
            url: 'tokens/find'
        },
        DELETE: {
            url: 'tokens/delete'
        }
    }
}

Object.freeze(Action)

let TokenDTO = function () {}

TokenDTO.prototype = {
    id: null,
    appName: null,
    token: null,
    build: function (id, appName, token) {
        this.id = id
        this.appName = appName
        this.token = token
    }
}

let confirmForm;
let tokenForm;

jQuery(document).ready(function ($) {

    confirmForm = $('#confirm-form');
    tokenForm = $('#token-modal-form');

    onCreateTokenEvent()
    onUpdateTokenEvent()
    onDeleteTokenEvent()
    onAcceptFormEvent()
});

/**
 * Событие при создании токена
 */
function onCreateTokenEvent() {
    $(document).on('click', '#create-token',function (e) {
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
            title = 'Создание токена'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление токена'
            button = 'Обновить'
            break
    }
    tokenForm.find('#title').html(title)
    tokenForm.find('#accept').html(button)
    tokenForm.find('#accept').attr('data-action', action)
    tokenForm.modal('show')
}

/**
 * Событие при изменении токена
 */
function onUpdateTokenEvent() {
    $(document).on('click', '#edit-token',function (e) {
        e.preventDefault()
        let tokenId = $(this).attr('data-token-id')
        getToken(tokenId)
        showForm(Action.UPDATE)
    })
}

/**
 * Получить токен по id
 *
 * @param tokenId {number}
 */
function getToken(tokenId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    let tokenDTO = new TokenDTO()
    tokenDTO.build(tokenId, null, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(tokenDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader()
            showUpdateTokenForm(data)
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
 * Показать форму для изменения токена
 *
 * @param data
 */
function showUpdateTokenForm(data) {
    let tokenDTO = new TokenDTO()
    tokenDTO.build(data.id, data.appName, data.token)
    tokenForm.find('#token-id').val(tokenDTO.id)
    tokenForm.find('#edit').val(true)
    tokenForm.find('#appName').val(tokenDTO.appName)
    tokenForm.find('#action').attr("data-action", Action.UPDATE)
}

/**
 * Событие при удалении токена
 */
function onDeleteTokenEvent() {
    $(document).on('click', '#delete-token',function (e) {
        e.preventDefault()
        let tokenId = $(this).attr('data-token-id')
        showConfirmForm('Удаление токена', 'Действительно хотите удалить токен?', tokenId, Action.DELETE)
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
    tokenForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let tokenDTO = getTokenDTO()
        if (check(tokenDTO)) {
            let action = tokenForm.find('#accept').attr('data-action')
            tokenForm.modal('hide')
            save(tokenDTO, action)
        }
    })
    acceptConfirm()
}

/**
 * Собрать DTO с формы
 *
 * @return {TokenDTO}
 */
function getTokenDTO() {
    let tokenDTO = new TokenDTO()
    let tokenId = tokenForm.find('#token-id').val()
    let appName = tokenForm.find('#appName').val()
    let token = tokenForm.find('#token').val()
    tokenDTO.build(tokenId, appName, token)
    return tokenDTO
}

/**
 * Проверить заполнение полей формы
 *
 * @param tokenDTO {TokenDTO} DTO для проверки
 * @return {boolean} результат проверки
 */
function check(tokenDTO) {
    let appNameError = tokenForm.find('#appNameError');
    if (tokenDTO.appName.length === 0) {
        appNameError.addClass('d-block')
        return false
    } else {
        appNameError.removeClass('d-block')
    }
    return true
}

/**
 * Создать/обновить токен
 *
 * @param tokenDTO {TokenDTO} DTO токена
 * @param action {Action} действие
 */
function save(tokenDTO, action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[action].url,
        data: JSON.stringify(tokenDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader();
            showPopup(data.message);
            window.location.href = 'tokens'
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
        let typeId = confirmForm.find('#accept').attr('data-object-id')
        confirmForm.modal('hide')
        deleteToken(typeId)
        $('#type-closing-table').find('tr#' + typeId).remove();
    })
}

/**
 * Удалить токен
 *
 * @param tokenId id токена
 */
function deleteToken(tokenId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let tokenDTO = {
        id: tokenId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.DELETE].url,
        data: JSON.stringify(tokenDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader();
            showPopup(data.message);
            window.location.href = 'tokens'
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
