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

let NewCashDetailDTO = function () {}

NewCashDetailDTO.prototype = {
    id: null,
    name: null,
    build: function (id, name) {
        this.id = id
        this.name = name
    }
}

let confirmForm;
let detailForm;

jQuery(document).ready(function ($) {

    confirmForm = $('#confirm-form');
    detailForm = $('#cash-detail-modal-form');

    onCreateDetailEvent()
    onUpdateDetailEvent()
    onDeleteDetailEvent()
    onAcceptFormEvent()

    $('a#delete').click(function (event) {
        event.preventDefault();
        let newCashDetailId = $(this).attr('data-cash-detail-id');

        deleteDetail(newCashDetailId);
        $('#cash-details-table').find('tr#' + newCashDetailId).remove();
    });
});

/**
 * Событие при создании деталей
 */
function onCreateDetailEvent() {
    $(document).on('click', '#create-new-details',function (e) {
        e.preventDefault()
        showForm(Action.CREATE)
    })
}

/**
 * Показать форму для создания/редактирования деталей
 *
 * @param action {Action} действие
 */
function showForm(action) {
    let title = ''
    let button = ''
    switch (action) {
        case Action.CREATE:
            title = 'Создание деталей'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление деталей'
            button = 'Обновить'
            break
    }
    detailForm.find('#title').html(title)
    detailForm.find('#accept').html(button)
    detailForm.find('#accept').attr('data-action', action)
    detailForm.modal('show')
}

/**
 * Событие при изменении деталей
 */
function onUpdateDetailEvent() {
    $(document).on('click', '#edit-detail',function (e) {
        e.preventDefault()
        let detailId = $(this).attr('data-detail-id')
        getDetail(detailId)
        showForm(Action.UPDATE)
    })
}

/**
 * Получить детали по id
 *
 * @param detailId {number}
 */
function getDetail(detailId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    let detailDTO = new NewCashDetailDTO()
    detailDTO.build(detailId, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(detailDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }})
        .done(function (data) {
            closeLoader()
            showUpdateDetailForm(data)
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
 * Показать форму для изменения деталей
 *
 * @param data
 */
function showUpdateDetailForm(data) {
    let detailDTO = new NewCashDetailDTO()
    detailDTO.build(data.id, data.name)
    detailForm.find('#detail-id').val(detailDTO.id)
    detailForm.find('#edit').val(true)
    detailForm.find('#name').val(detailDTO.name)
    detailForm.find('#action').attr("data-action", Action.UPDATE)
}

/**
 * Событие при удалении деталей
 */
function onDeleteDetailEvent() {
    $(document).on('click', '#delete-detail',function (e) {
        e.preventDefault()
        let detailId = $(this).attr('data-detail-id')
        showConfirmForm('Удаление деталей', 'Действительно хотите удалить детали новых денег?', detailId, Action.DELETE)
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
    detailForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let detailDTO = getDetailDTO()
        if (check(detailDTO)) {
            let action = detailForm.find('#accept').attr('data-action')
            detailForm.modal('hide')
            save(detailDTO, action)
        }
    })
    acceptConfirm()
}

/**
 * Собрать DTO с формы
 *
 * @return {NewCashDetailDTO}
 */
function getDetailDTO() {
    let detailDTO = new NewCashDetailDTO()
    let detailId = detailForm.find('#new-cash-detail-id').val()
    let name = detailForm.find('#name').val()
    detailDTO.build(detailId, name)
    return detailDTO
}

/**
 * Создать/обновить детали новых денег
 *
 * @param detailDTO {NewCashDetailDTO} DTO деталей
 * @param action {Action} действие
 */
function save(detailDTO, action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[action].url,
        data: JSON.stringify(detailDTO),
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
 * @param detailDTO {NewCashDetailDTO} DTO для проверки
 * @return {boolean} результат проверки
 */
function check(detailDTO) {
    let detailNameError = detailForm.find('#detailNameError');
    if (detailDTO.name.length === 0) {
        detailNameError.addClass('d-block')
        return false
    } else {
        detailNameError.removeClass('d-block')
    }
    return true
}

/**
 * Подтверждение с формы
 *
 */
function acceptConfirm() {
    confirmForm.find('#accept').on('click', function () {
        let detailId = confirmForm.find('#accept').attr('data-object-id')
        confirmForm.modal('hide')
        deleteDetail(detailId)
        $('#cash-details-table').find('tr#' + detailId).remove();
    })
}

/**
 * Удалить детали новых денег
 *
 * @param detailId id деталей
 */
function deleteDetail(detailId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let detailDTO = {
        id: detailId
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
