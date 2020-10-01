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
