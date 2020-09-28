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
    roles: [],
    partnerId: 0,
    profile: null,
    kin: null,
    build: function (id, login, roles, partnerId, kin) {
        this.id = id;
        this.login = login;
        this.roles = roles;
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

jQuery(document).ready(function ($) {

    let userForm = $('#user-form-modal');

    $('#create-user').on('click', function (e) {
        e.preventDefault()
        userForm.find('#title').html('Создание пользователя')
        userForm.find('#accept').html('Создать')
        userForm.modal('show')
    })

    userForm.find('#accept').on('click', function () {
        let userDTO = getUserDTO()
        checkUserDTO(userDTO)
        saveUser(userDTO)
    })

    $('#inactive').on('change', function () {
        $('#deactivated').val(!$(this).prop('checked'))
        $('#search-form').submit()
    })

    $('#confirm').on('change', function () {
        $('#confirmed').val(!$(this).prop('checked'))
        $('#search-form').submit()
    })

    let confirmForm = $('#confirm-form');

    $('.deactivate').on('click', function (e) {
        e.preventDefault()
        let userId = $(this).data('user-id')
        confirmForm.find('#title').html('Деактивация пользователя')
        confirmForm.find('#message').html('Действительно хотите деактивировать пользователя?')
        confirmForm.find('#accept').attr('data-object-id', userId)
        confirmForm.modal('show')
    })

    $('#accept').on('click', function (e) {
        e.preventDefault()
        let userId = $(this).data('object-id')
        confirmForm.modal('hide')
        deactivate(userId)
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
        deleteUser(userId);
        $('#tblUsers').find('tr#' + userId).remove();
    });
});

/**
 * Проверить правильность заполения формы
 *
 * @param userDTO {UserDTO}
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
    if (userDTO.roles.length === 0) {
        rolesError.addClass('d-block')
        return false
    } else {
        rolesError.removeClass('d-block')
    }
}

function getUserDTO() {
    let login = $('#user-login').val()
    let roles = getRoles();
    let partner = getPartner();
    let kin = $('#kins').val();
    let userId = $('#id').val();

    let userDTO = new UserDTO()
    userDTO.build(userId, login, roles, partner.id, kin)
    userDTO.profile = createProfile(userId, $('#lastName').val(), $('#firstName').val(), $('#patronymic').val(), $('#email').val());

    return userDTO
}

function prepareUserSave() {
    let userDTO = getUserDTO()
    saveUser(userDTO);
}

function getRoles() {
    let roles = [];
    $('#roles').find(':selected').each(function (i, selected) {
        let roleDTO = new RoleDTO();
        roleDTO.build($(selected).val(), $(selected).text());
        roles.push(roleDTO);
    });
    return roles;
}

function getPartner() {
    let saleChanel = $('#saleChanel');
    let partnerDTO = new UserDTO();
    partnerDTO.buildPartner(saleChanel.find(':selected').val(), saleChanel.find(':selected').text());
    if (partnerDTO.partnerId === 0) {
        partnerDTO.partnerId = null;
    }
    return partnerDTO;
}

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

    let url = window.location.href;
    let actionUrl;
    if (url.indexOf('edit') >= 0) {
        actionUrl = "users/save"
    } else {
        actionUrl = "save"
    }
    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: actionUrl,
        data: JSON.stringify(user),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            if (url.indexOf('edit') >= 0) {
                window.location.href = 'list';
            }
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
        url: "users/delete",
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

function prepareUsersFilter() {
    let status = $('#userStatus').find(':selected').text();

    if (status === 'Все') {
        filters = [];
        apply_filter('#tblUsers tbody', 4, 'any');
    } else if (status === 'Не подтверждён') {
        filters = [];
        apply_filter('#tblUsers tbody', 4, 'Нет');
    } else if (status === 'Подтверждён') {
        filters = [];
        apply_filter('#tblUsers tbody', 4, 'Да');
    } else {
        filters = [];
        apply_filter('#tblUsers tbody', 5, status);
    }
}

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
        url: "users/deactivate",
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

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

function clearUserForm() {
    $('#lastName').val('')
    $('#firstName').val('')
    $('#patronymic').val('')
    $('#user-login').val('')
    $('#email').val('')
    $('#roles').prop('selectedIndex', -1)
    $('#saleChanel').prop('selectedIndex', -1)
    $('#kins').prop('selectedIndex', -1)
}
