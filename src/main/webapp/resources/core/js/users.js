let UserDTO = function () {}

let RoleDTO = function () {}

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
    build: function (id, login, roles, partnerId) {
        this.id = id;
        this.login = login;
        this.roles = roles;
        this.partnerId = partnerId;
    },
    buildPartner: function (id, login) {
        this.id = id;
        this.login = login;
    }
}

jQuery(document).ready(function ($) {
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
        let userId = $(this).attr('name');
        deleteUser(userId);
        $('#tblUsers').find('tr#' + userId).remove();
    });

    $('#bth-search').click(function (event) {
        event.preventDefault();
        prepareUsersFilter();
    });
});

function prepareUserSave() {
    let roles = getRoles();
    let partner = getPartner();
    let kin = $('#kins').val();
    let userId = $('#id').val();

    let userDTO = {
        id: userId,
        lastName: $('#last_name').val(),
        firstName: $('#first_name').val(),
        middleName: $('#middle_name').val(),
        login: $('#login').val(),
        email: $('#email').val(),
        roles: roles,
        kin: kin,
        partnerId: partner.id
    };
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

function saveUser(user) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let url = window.location.href;
    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "users/update",
        data: JSON.stringify(user),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            if (url.indexOf('edit') >= 0) {
                window.location.href = '/admin';
            }
            showPopup(data.message);
            $('#last_name').val('');
            $('#first_name').val('');
            $('#middle_name').val('');
            $('#login').val('');
            $('#email').val('');
            $('#roles').prop('selectedIndex', -1);
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

    let search = ({"rentor": userId});

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "deleteuser",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            $('#popup_modal_form').find('#message').append(data.message);
            closeLoader();
            showPopup();
            closePopup();
        },
        error: function (e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            showPopup();
            closePopup();
        }
    });
}

function prepareUsersFilter() {
    let stuff = $('#srchStuff').find(':selected').text();

    if (stuff !== 'Все' && stuff !== 'Не подтверждён') {
        filters = [];
        apply_filter('#tblUsers tbody', 4, stuff);
    } else if (stuff === 'Не подтверждён') {
        filters = [];
        apply_filter('#tblUsers tbody', 4, "Нет");
    } else {
        filters = [];
        apply_filter('#tblUsers tbody', 4, 'any');
    }
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
