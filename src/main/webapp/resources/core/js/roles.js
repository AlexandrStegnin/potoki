let Action = {
    CREATE: 'CREATE',
    UPDATE: 'UPDATE',
    properties: {
        CREATE: {
            url: 'create'
        },
        UPDATE: {
            url: 'update'
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
        showForm(Action.UPDATE)
    })
    roleForm.find('#accept').on('click',function (e) {
        e.preventDefault()
        let roleDTO = getRoleDTO()
        if (check(roleDTO)) {
            console.log(roleDTO)
        }
    })
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
