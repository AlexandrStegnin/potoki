let OperationEnum = {
    SAVE: 'SAVE',
    UPDATE: 'UPDATE',
    DELETE: 'DELETE',
    DEACTIVATE: 'DEACTIVATE',
    properties: {
        SAVE: {
            url: 'save'
        },
        UPDATE: {
            url: 'save'
        },
        DELETE: {
            url: 'delete'
        },
        DEACTIVATE: {
            url: 'deactivate'
        }
    }
}

Object.freeze(OperationEnum)

let UserDTO = function () {}

let RoleDTO = function () {}

let UserProfileDTO = function () {}

RoleDTO.prototype = {
    id: 0,
    name: '',
    build: function (id, name) {
        this.id = id;
        this.name = name;
    }
}

UserDTO.prototype = {
    id: 0,
    login: '',
    facilities: [],
    role: null,
    partnerId: 0,
    profile: null,
    kin: null,
    build: function (id, login, role, partnerId, kin) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.partnerId = partnerId;
        this.kin = kin;
    },
    buildPartner: function (id, login) {
        this.id = id;
        this.login = login;
    }
}

UserProfileDTO.prototype = {
    id: 0,
    lastName: '',
    firstName: '',
    patronymic: '',
    email: '',
    build: function (id, lastName, firstName, patronymic, email) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
    }
}

let confirmForm

jQuery(document).ready(function ($) {
    confirmForm = $('#confirm-form');
    let userForm = $('#user-form-modal');

    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 1;
        $('#pageSize').val(pageSize);
        $('#search-form').submit();
    });

    $('#create-user').on('click', function (e) {
        e.preventDefault()
        userForm.find('#title').html('Создание пользователя')
        userForm.find('#accept').html('Создать')
        userForm.modal('show')
    })

    $('.edit-user').on('click', function (e) {
        e.preventDefault()
        userForm.find('#title').html('Обновить пользователя')
        userForm.find('#accept').html('Обновить')
        userForm.find('#accept').attr('data-action', OperationEnum.UPDATE)
        let userId = $(this).attr('data-user-id')
        getUser(userId)
    })

    userForm.find('#accept').on('click', function () {
        let userDTO = getUserDTO()
        if (checkUserDTO(userDTO)) {
            saveUser(userDTO)
        }
    })

    $('#inactive').on('change', function () {
        $('#deactivated').val(!$(this).prop('checked'))
        $('#search-form').submit()
    })

    $('#confirm').on('change', function () {
        $('#confirmed').val(!$(this).prop('checked'))
        $('#search-form').submit()
    })

    $('.deactivate').on('click', function (e) {
        e.preventDefault()
        let userId = $(this).data('user-id')
        showConfirmForm('Деактивация пользователя', 'Действительно хотите деактивировать пользователя?', userId, OperationEnum.DEACTIVATE)
    })

    let isValid = {
        'login': function () {
            let loginErr = $('#loginErr');
            let login = $('#login');
            let rus = new RegExp(".*?[А-Яа-я $\/].*?");
            if (login.val().length < 4 || login.val() === '' || login.val().length > 16) {
                isValid.errors = true;
                loginErr.html('Имя пользователя должно быть от 4 до 16 символов').show();
            } else if (rus.test(login.val())) {
                isValid.errors = true;
                loginErr.html('Имя пользователя может содержать только буквы латинского алфавита, ' +
                    'цифры, знак подчёркивания (_) и точку (.)').show();
            } else {
                isValid.errors = false;
                loginErr.html('').hide();
            }
        },

        'readAnnexes': function () {
            let errUnread = $('#errUnread');
            isValid.errors = !errUnread.css('display', 'block');
        },

        'email': function () {
            let emailErr = $('#emailErr');
            let email = $('#email');
            let emailValid = new RegExp("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$");
            if (!emailValid.test(email.val())) {
                isValid.errors = true;
                emailErr.html('Введите Email в формате mymail@example.ru').show();
            } else {
                emailErr.html('').hide();
                isValid.errors = false;
            }
        },
        'sendIt': function () {
            if (!isValid.errors) {
                prepareUserSave();
            }
        }
    };

    $('#send').click(function (event) {
        event.preventDefault();
        let isEdit = $('#edit').val();
        isValid.errors = false;
        if (isEdit === true) {
            isValid.login();
            isValid.readAnnexes();
        }
        isValid.sendIt();
        return false;
    });

    $('#login').blur(isValid.login);
    $('#email').blur(isValid.email);

    $('a#delete').click(function (event) {
        event.preventDefault();
        let userId = $(this).attr('data-user-id');
        showConfirmForm('Удаление пользователя', 'Действительно хотите удалить пользователя?', userId, OperationEnum.DELETE)
    });

    confirmForm.find('#accept').on('click', function () {
        let userId = confirmForm.find('#accept').attr('data-object-id')
        let action = confirmForm.find('#accept').attr('data-action')
        confirmForm.modal('hide')
        switch (action) {
            case OperationEnum.DELETE:
                deleteUser(userId)
                $('#users-table').find('tr#' + userId).remove();
                break
            case OperationEnum.DEACTIVATE:
                deactivate(userId)
                break
        }
    })

});

/**
 * Проверить правильность заполения формы
 *
 * @param userDTO {UserDTO}
 * @return {boolean}
 */
function checkUserDTO(userDTO) {
    let rus = new RegExp(".*?[А-Яа-я $\/].*?")
    let loginError = $('#loginError')

    if (userDTO.login.length < 4 || userDTO.login.length > 16) {
        loginError.html('Имя пользователя должно быть от 4 до 16 символов')
        loginError.addClass('d-block')
        return false
    } else if (rus.test(userDTO.login)) {
        loginError.html('Имя пользователя может содержать только буквы латинского алфавита, ' +
            'цифры, знак подчёркивания (_) и точку (.)')
        loginError.addClass('d-block')
        return false
    } else {
        loginError.removeClass('d-block')
    }

    let emailErr = $('#emailError')
    let emailValid = new RegExp("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$")
    if (!emailValid.test(userDTO.profile.email)) {
        emailErr.html('Введите email в формате mymail@example.ru')
        emailErr.addClass('d-block')
        return false
    } else {
        emailErr.removeClass('d-block')
    }
    let rolesError = $('#rolesError')
    if (userDTO.role.id === '0') {
        rolesError.addClass('d-block')
        return false
    } else {
        rolesError.removeClass('d-block')
    }
    return true
}

/**
 * Создать DTO пользователя
 *
 * @returns {UserDTO}
 */
function getUserDTO() {
    let login = $('#user-login').val()
    let role = getRole();
    let partner = getPartner();
    let kin = $('#kins').val();
    let userId = $('#id').val();

    let userDTO = new UserDTO()
    userDTO.build(userId, login, role, partner.id, kin)
    userDTO.profile = createProfile(userId, $('#lastName').val(), $('#firstName').val(), $('#patronymic').val(), $('#email').val());

    return userDTO
}

/**
 * Подготовить пользователя к сохранению
 *
 */
function prepareUserSave() {
    let userDTO = getUserDTO()
    saveUser(userDTO);
}

/**
 * Получить список ролей
 *
 * @returns {RoleDTO}
 */
function getRole() {
    let roleDTO = new RoleDTO();
    let roleOption = $('#roles').find('option:selected');
    roleDTO.build(roleOption.val(), roleOption.text());
    return roleDTO;
}

/**
 * Получить партнёра, если выбран канал продаж
 *
 * @returns {UserDTO}
 */
function getPartner() {
    let saleChanel = $('#saleChanel');
    let partnerDTO = new UserDTO();
    partnerDTO.buildPartner(saleChanel.find(':selected').val(), saleChanel.find(':selected').text());
    if (partnerDTO.partnerId === 0) {
        partnerDTO.partnerId = null;
    }
    return partnerDTO;
}

/**
 * Создать профиль для DTO пользователя
 *
 * @param userId id пользователя
 * @param lastName Фамилия
 * @param firstName Имя
 * @param patronymic Отчество
 * @param email адрес эл почты
 * @returns {UserProfileDTO}
 */
function createProfile(userId, lastName, firstName, patronymic, email) {
    let profile = new UserProfileDTO();
    if (lastName.length === 0) {
        lastName = null;
    }
    if (firstName.length === 0) {
        firstName = null;
    }
    if (patronymic.length === 0) {
        patronymic = null;
    }
    profile.build(userId, lastName, firstName, patronymic, email);
    return profile;
}

/**
 * Сохранить пользователя
 *
 * @param user {UserDTO}
 */
function saveUser(user) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: OperationEnum.properties[OperationEnum.SAVE].url,
        data: JSON.stringify(user),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            if (data.status === 412) {
                $('#user-form-modal').find('#loginError').html(data.error).addClass('d-block')
            } else {
                $('#user-form-modal').modal('hide')
                showPopup(data.message);
                clearUserForm()
            }
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error);
        }
    });
}

/**
 * Удалить пользователя
 *
 * @param userId id пользователя
 */
function deleteUser(userId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    let userDTO = {
        id: userId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: OperationEnum.properties[OperationEnum.DELETE].url,
        data: JSON.stringify(userDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message)
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error)
        }
    });
}

/**
 * Деактивировать пользователя
 *
 * @param userId id пользователя
 */
function deactivate(userId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    let userDTO = {
        id: userId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: OperationEnum.properties[OperationEnum.DEACTIVATE].url,
        data: JSON.stringify(userDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message)
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error)
        }
    });
}

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

/**
 * Показать сообщений
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
 * Очистить форму пользователей от данных
 */
function clearUserForm() {
    let userModalForm = $('#user-form-modal')
    userModalForm.find('#lastName').val('')
    userModalForm.find('#firstName').val('')
    userModalForm.find('#patronymic').val('')
    userModalForm.find('#user-login').val('')
    userModalForm.find('#email').val('')
    userModalForm.find('#roles').prop('selectedIndex', -1)
    userModalForm.find('#saleChanel').prop('selectedIndex', -1)
    userModalForm.find('#kins').prop('selectedIndex', -1)
}

/**
 * Отобразить форму подтверждения
 *
 * @param title {String} заголовок формы
 * @param message {String} сообщение
 * @param objectId {String} идентификатор объекта
 * @param action {OperationEnum} действие
 */
function showConfirmForm(title, message, objectId, action) {
    confirmForm.find('#title').html(title)
    confirmForm.find('#message').html(message)
    confirmForm.find('#accept').attr('data-object-id', objectId)
    confirmForm.find('#accept').attr('data-action', action)
    confirmForm.modal('show')
}

/**
 * Получить пользователя по id
 *
 * @param userId id пользователя
 */
function getUser(userId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader();
    let userDTO = {
        id: userId
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "find",
        data: JSON.stringify(userDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showUpdateUserForm(data)
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error)
        }
    });
}

/**
 * Показать форму для изменения пользователя
 *
 * @param data
 */
function showUpdateUserForm(data) {
    let userDTO = new UserDTO()
    userDTO.build(data.id, data.login, data.role, data.partnerId, data.kin)
    userDTO.profile = data.profile
    let userForm = $('#user-form-modal')
    userForm.find('#id').val(userDTO.id)
    userForm.find('#edit').val(true)
    userForm.find('#user-login').val(userDTO.login)
    userForm.find('#email').val(userDTO.profile.email)
    bindRoles(userDTO.role)
    bindPartner(userDTO.partnerId)
    bindKin(userDTO.kin)
    userForm.find('#lastName').val(userDTO.profile.lastName)
    userForm.find('#firstName').val(userDTO.profile.firstName)
    userForm.find('#patronymic').val(userDTO.profile.patronymic)
    userForm.find('#action').attr("data-action", OperationEnum.UPDATE)
    userForm.modal('show')
}

/**
 * Преобразовать список ролей пользователя в выделенные элементы выпадающего списка
 *
 * @param role {RoleDTO}
 */
function bindRoles(role) {
    let userForm = $('#user-form-modal');
    $.each(userForm.find('#roles option'), function (ind, el) {
        if (el.value === (role.id + '')) {
            $(el).attr('selected', 'selected')
        }
    })
    userForm.find('#roles').selectpicker('refresh')
}

/**
 * Преобразовать id партнёра в выделенный элемент выпадающего списка
 *
 * @param partnerId id партнёра
 */
function bindPartner(partnerId) {
    let userForm = $('#user-form-modal');
    $.each(userForm.find('#saleChanel option'), function (ind, el) {
        if (el.value === (partnerId + '')) {
            $(el).attr('selected', 'selected')
        }
    })
    userForm.find('#saleChanel').selectpicker('refresh')
}

/**
 * Преобразовать степень родства в выделенный элемент выпадающего списка
 *
 * @param kin
 */
function bindKin(kin) {
    let userForm = $('#user-form-modal');
    $.each(userForm.find('#kins option'), function (ind, el) {
        if (el.value === (kin + '')) {
            $(el).attr('selected', 'selected')
        }
    })
    userForm.find('#kins').selectpicker('refresh')
}
