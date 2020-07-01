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
    let roles = [];
    $('#roles').find(':selected').each(function (i, selected) {
        roles.push({
            id: $(selected).val(),
            role: $(selected).text()
        });
    });

    let facilities = [];
    $('#facility').find(':selected').each(function (i, selected) {
        facilities.push({id: $(selected).val()});
    });

    let stuffs = $('#stuffs');
    let stuff = {id: stuffs.find(':selected').val(), stuff: stuffs.find(':selected').text()};

    let sChanel = $('#sChanel');
    let partner = {
        id: sChanel.find(':selected').val(),
        login: sChanel.find(':selected').text()
    };

    let kin = $('#kins').val();

    let salesChanel = {
        "partnerId": partner.id,
        "kin": kin
    };

    let userId = $('#id').val();

    let user = {
        id: userId,
        lastName: $('#last_name').val(),
        first_name: $('#first_name').val(),
        middle_name: $('#middle_name').val(),
        login: $('#login').val(),
        password: null,
        email: $('#email').val(),
        state: $('#state').val(),
        userStuff: stuff,
        roles: roles,
        kin: kin,
        partnerId: salesChanel.partnerId
    };
    saveUser(user, facilities);
}

function saveUser(user, facilities) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let search = ({"user": user, "facilityList": facilities});
    let url = window.location.href;
    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "saveuser",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {

            closeLoader();
            showPopup();
            closePopup();
            if (url.indexOf('edit') >= 0) {
                window.location.href = '/admin';
            }
            $('#popup_modal_form').find('#message').append(data.message);
            $('#last_name').val('');
            $('#first_name').val('');
            $('#middle_name').val('');
            $('#login').val('');
            $('#email').val('');
            $('#stuffs').prop('selectedIndex', 0);
            $('#facility').prop('selectedIndex', -1);
            $('#roles').prop('selectedIndex', -1);
        },
        error: function (e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            showPopup();
            closePopup();
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

function searchUsers() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({"searchStuff": $("#srchStuff").find("option:selected").val()});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "usersByStuff",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            display(data);
        },
        error: function (e) {
            display(e);
        },
        done: function (e) {
            enableSearchButton(true);
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
        apply_filter('#tblUsers tbody', 5, "Нет");
    } else {
        filters = [];
        apply_filter('#tblUsers tbody', 4, 'any');
    }
}

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

function display(data) {
    let json = JSON.stringify(data, null, 4);
    $('#tblUsers').html(json);
}
