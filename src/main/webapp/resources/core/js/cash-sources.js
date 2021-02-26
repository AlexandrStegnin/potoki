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

let CashSource = function () {}

CashSource.prototype = {
    id: 0,
    name: '',
    organizationId: '',
    build: function (id, name, organizationId) {
        this.id = id;
        this.name = name;
        this.organizationId = organizationId;
    }
}

let confirmForm;
let cashSourceForm;

jQuery(document).ready(function ($) {

    confirmForm = $('#confirm-form');
    cashSourceForm = $('#cash-source-modal-form');
    onCreateCashSourceEvent()
    onUpdateCashSourceEvent()
    onDeleteCashSourceEvent()
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

    let facility = createCashSource(null);

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

function createCashSource(cashSourceId) {
    let cashSource = new CashSource();
    let name = $('#name').val();
    let organizationId = $('#organization-id').val();
    cashSource.build(cashSourceId, name, organizationId);
    return cashSource;
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
 * Показать форму для создания/редактирования источника денег
 *
 * @param action {Action} действие
 */
function showForm(action) {
    let title = ''
    let button = ''
    switch (action) {
        case Action.CREATE:
            title = 'Создание источника денег'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление источника денег'
            button = 'Обновить'
            break
    }
    cashSourceForm.find('#title').html(title)
    cashSourceForm.find('#accept').html(button)
    cashSourceForm.find('#accept').attr('data-action', action)
    cashSourceForm.modal('show')
}

/**
 * Получить источник денег по id
 *
 * @param cashSourceId
 */
function getCashSource(cashSourceId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let cashSourceDTO = new CashSource()
    cashSourceDTO.build(cashSourceId, null, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(cashSourceDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showUpdateCashSourceForm(data)
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error);
        }
    });
}

/**
 * Событие при создании источника денег
 */
function onCreateCashSourceEvent() {
    $(document).on('click', '#create-cash-source',function (e) {
        e.preventDefault()
        showForm(Action.CREATE)
    })
}

/**
 * Событие при изменении источника денег
 */
function onUpdateCashSourceEvent() {
    $(document).on('click', '#edit-cash-source',function (e) {
        e.preventDefault()
        let cashSourceId = $(this).attr('data-cash-source-id')
        getCashSource(cashSourceId)
        showForm(Action.UPDATE)
    })
}

/**
 * Событие при удалении источника денег
 */
function onDeleteCashSourceEvent() {
    $(document).on('click', '#delete-cash-source',function (e) {
        e.preventDefault()
        let cashSourceId = $(this).attr('data-cash-source-id')
        showConfirmForm('Удаление источника денег', 'Действительно хотите удалить источник денег?', cashSourceId, Action.DELETE)
    })
}

function onAcceptFormEvent() {
    cashSourceForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let cashSourceDTO = getCashSourceDTO()
        if (check(cashSourceDTO)) {
            let action = cashSourceForm.find('#accept').attr('data-action')
            cashSourceForm.modal('hide')
            save(cashSourceDTO, action)
        }
    })
    acceptConfirm()
}

/**
 * Собрать DTO с формы
 *
 * @return {CashSource}
 */
function getCashSourceDTO() {
    let cashSourceDTO = new CashSource()
    let cashSourceId = cashSourceForm.find('#cash-source-id').val()
    let name = cashSourceForm.find('#name').val()
    let organizationId = cashSourceForm.find('#organization-id').val()
    cashSourceDTO.build(cashSourceId, name, organizationId)
    return cashSourceDTO
}

/**
 * Проверить заполнение полей формы
 *
 * @param cashSourceDTO {CashSource} DTO для проверки
 * @return {boolean} результат проверки
 */
function check(cashSourceDTO) {
    let cashSourceNameError = cashSourceForm.find('#cashSourceNameError');
    if (cashSourceDTO.name.length === 0) {
        cashSourceNameError.addClass('d-block')
        return false
    } else {
        cashSourceNameError.removeClass('d-block')
    }
    let organizationIdError = cashSourceForm.find('#organizationIdError')
    if (cashSourceDTO.organizationId.length === 0) {
        organizationIdError.addClass('d-block')
        return false
    } else {
        organizationIdError.removeClass('d-block')
    }
    return true
}

/**
 * Создать/обновить источник денег
 *
 * @param cashSourceDTO {CashSource} DTO источника денег
 * @param action {Action} действие
 */
function save(cashSourceDTO, action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[action].url,
        data: JSON.stringify(cashSourceDTO),
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
 * Показать форму для изменения источника денег
 *
 * @param data источник денег
 */
function showUpdateCashSourceForm(data) {
    let cashSourceDTO = new CashSource()
    cashSourceDTO.build(data.id, data.name, data.organizationId)
    cashSourceForm.find('#cash-source-id').val(cashSourceDTO.id)
    cashSourceForm.find('#edit').val(true)
    cashSourceForm.find('#name').val(cashSourceDTO.name)
    cashSourceForm.find('#organization-id').val(cashSourceDTO.organizationId)
    cashSourceForm.find('#action').attr("data-action", Action.UPDATE)
}

/**
 * Подтверждение с формы
 *
 */
function acceptConfirm() {
    confirmForm.find('#accept').on('click', function () {
        let cashSourceId = confirmForm.find('#accept').attr('data-object-id')
        confirmForm.modal('hide')
        deleteCashSource(cashSourceId)
        $('#tblCashSrc').find('tr#' + cashSourceId).remove();
    })
}

/**
 * Удалить источник денег
 *
 * @param cashSourceId id источника денег
 */
function deleteCashSource(cashSourceId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let cashSourceDTO = {
        id: cashSourceId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.DELETE].url,
        data: JSON.stringify(cashSourceDTO),
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
