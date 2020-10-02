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

let AppRoleDTO = function () {}

AppRoleDTO.prototype = {
    id: 0,
    name: null,
    humanized: null,
    build: function (id, name, humanized) {
        this.id = id;
        this.name = name;
        this.humanized = humanized;
    }
}

let confirmForm;
let roleForm;

jQuery(document).ready(function ($) {
    confirmForm = $('#confirm-form');
    roleForm = $('#role-modal-form');
    $(document).on('click', '#create-role',function (e) {
        e.preventDefault()
        showForm(Action.CREATE)
    })
    $(document).on('click', '#edit-role',function (e) {
        e.preventDefault()
        let roleId = $(this).attr('data-role-id')
        getRole(roleId)
        showForm(Action.UPDATE)
    })
    $(document).on('click', '#delete-role',function (e) {
        e.preventDefault()
        let roleId = $(this).attr('data-role-id')
        showConfirmForm('Удаление роли', 'Действительно хотите удалить роль?', roleId, Action.DELETE)
    })
    roleForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let roleDTO = getRoleDTO()
        if (check(roleDTO)) {
            let action = roleForm.find('#accept').attr('data-action')
            roleForm.modal('hide')
            save(roleDTO, action)
        }
    })
    acceptConfirm()
})

/**
 * Показать форму для создания/редактирования роли
 *
 * @param action {Action} действие
 */
function showForm(action) {
    let title = ''
    let button = ''
    switch (action) {
        case Action.CREATE:
            title = 'Создание роли'
            button = 'Создать'
            break
        case Action.UPDATE:
            title = 'Обновление роли'
            button = 'Обновить'
            break
    }
    roleForm.find('#title').html(title)
    roleForm.find('#accept').html(button)
    roleForm.find('#accept').attr('data-action', action)
    roleForm.modal('show')
}

/**
 * Собрать DTO с формы
 *
 * @return {AppRoleDTO}
 */
function getRoleDTO() {
    let roleDTO = new AppRoleDTO()
    let roleId = roleForm.find('#role-id').val()
    let roleName = roleForm.find('#role-name').val()
    let humanized = roleForm.find('#humanized').val()
    roleDTO.build(roleId, roleName, humanized)
    return roleDTO
}

/**
 * Проверить заполнение полей формы
 *
 * @param roleDTO {AppRoleDTO} DTO для проверки
 * @return {boolean} результат проверки
 */
function check(roleDTO) {
    let roleNameError = roleForm.find('#roleNameError');
    if (roleDTO.name.length === 0) {
        roleNameError.addClass('d-block')
        return false
    } else {
        roleNameError.removeClass('d-block')
    }
    let humanizedError = roleForm.find('#humanized')
    if (roleDTO.humanized.length === 0) {
        humanizedError.addClass('d-block')
        return false
    } else {
        humanizedError.removeClass('d-block')
    }
    return true
}

/**
 * Создать/обновить роль
 *
 * @param roleDTO {AppRoleDTO} DTO роли
 * @param action {Action} действие
 */
function save(roleDTO, action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[action].url,
        data: JSON.stringify(roleDTO),
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
 * Получить роль по шв
 *
 * @param roleId
 */
function getRole(roleId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let roleDTO = new AppRoleDTO()
    roleDTO.build(roleId, null, null)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: Action.properties[Action.FIND].url,
        data: JSON.stringify(roleDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showUpdateRoleForm(data)
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error);
        }
    });
}

/**
 * Показать форму для изменения роли
 *
 * @param data
 */
function showUpdateRoleForm(data) {
    let roleDTO = new AppRoleDTO()
    roleDTO.build(data.id, data.name, data.humanized)
    roleForm.find('#role-id').val(roleDTO.id)
    roleForm.find('#edit').val(true)
    roleForm.find('#role-name').val(roleDTO.name)
    roleForm.find('#humanized').val(roleDTO.humanized)
    roleForm.find('#action').attr("data-action", Action.UPDATE)
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
 * Подтверждение с формы
 *
 */
function acceptConfirm() {
    confirmForm.find('#accept').on('click', function () {
        let roleId = confirmForm.find('#accept').attr('data-object-id')
        confirmForm.modal('hide')
        deleteRole(roleId)
        $('#roles-table').find('tr#' + roleId).remove();
    })
}

/**
 * Удалить роль
 *
 * @param roleId id роли
 */
function deleteRole(roleId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let roleDTO = {
        id: roleId
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
