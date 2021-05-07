jQuery(document).ready(function ($) {

    /* МОДАЛЬНОЕ ОКНО */

    $(document).on('click', '#clearLS', function (e) {
        e.preventDefault();
        if (window.localStorage) localStorage.clear();
        $('#msg').html('Локальное хранилище очищено');
        $('#msg-modal').modal('show');
    });

    $(document).on('click', 'a#updateMarketingTree', function (e) {
        e.preventDefault();
        updateMarketingTree();
    });

    $('a#go').click(function (event) { // лoвим клик пo ссылки с id="go"

        var facility = $(this).attr('name');
        showDetails(facility);

        event.preventDefault(); // выключaем стaндaртную рoль элементa
        $('#overlay').fadeIn(400, // снaчaлa плaвнo пoкaзывaем темную пoдлoжку
            function () { // пoсле выпoлнения предъидущей aнимaции
                $('#modal_form')
                    .css('display', 'block') // убирaем у мoдaльнoгo oкнa display: none;
                    .animate({opacity: 1, top: '50%'}, 200); // плaвнo прибaвляем прoзрaчнoсть oднoвременнo сo съезжaнием вниз
            });
    });
    /* Зaкрытие мoдaльнoгo oкнa, тут делaем тo же сaмoе нo в oбрaтнoм пoрядке */

    $('#modal_close, #overlay').click(function () { // лoвим клик пo крестику или пoдлoжке
        $('#sum-details tbody > tr').remove();
        $('#modal_form')
            .animate({opacity: 0, top: '45%'}, 200,  // плaвнo меняем прoзрaчнoсть нa 0 и oднoвременнo двигaем oкнo вверх
                function () { // пoсле aнимaции
                    $(this).css('display', 'none'); // делaем ему display: none;
                    $('#overlay').fadeOut(400); // скрывaем пoдлoжку
                }
            );
    });

    /* МОДАЛЬНОЕ ОКНО */

    /* ВОССТАНОВЛЕНИЕ ПАРОЛЯ */
    $('#reset').click(function (event) {
        event.preventDefault();
        let email = $('#email').val();
        let login = $('#login').val();
        resetPass(email, login);
    });

    $("#changePassForm").submit(function (event) {
        event.preventDefault();
        savePass();
    });

    $("#matchPassword").keyup(function () {
        if ($("#password").val() !== $("#matchPassword").val()) {
            $("#message").show().html('<b>Пароли не совпадают</b>');
        } else {
            $("#message").hide().html("");
        }
    });

    /* ВОССТАНОВЛЕНИЕ ПАРОЛЯ */


});

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

function display(data) {
    var json = JSON.stringify(data, null, 4);
    $('#investorsSummary').html(json);
}

/**/
function showDetails(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility": facility});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "sumdetails",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            console.log("SUCCESS: ", data);
            show(data);
        },
        error: function (e) {
            console.log("ERROR: ", e);
            show(e);
        }
    });

}

function show(data) {
    var json = JSON.stringify(data, null, 4);
    $('#sum-details').append(json);
}

function resetPass(email, login) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = (
        {
            "email": email,
            "login": login
        }
    );

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "resetPassword",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            if (data.message !== null) {
                var msg = data.message;
                $('#message').html(data.message);
                $('#message').removeClass("alert alert-danger").addClass("alert alert-success");
                $('#message').show();
                $('#welcome_msg').hide();
                $('#email').val('');
                $('#login').val('');
                closeLoader();
                time(msg);
                setInterval(time, 1000, msg);
            } else {
                closeLoader();
                $('#email').val('');
                $('#login').val('');
                $('#message').html(data.error);
                $('#message').removeClass("alert alert-success").addClass("alert alert-danger");
                $('#message').show();
                $('#welcome_msg').hide();

            }

        },
        error: function (e) {
            closeLoader();
            $('#message').append(e);
        }
    });
}

function savePass() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"password": $('#password').val()});

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "savePassword",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            if (data.message !== null) {
                var msg = data.message;
                $('#message').html(data.message);
                $('#message').removeClass("alert alert-danger").addClass("alert alert-success");
                $('#message').show();
                time(msg);
                setInterval(time, 1000, msg);
            } else {
                $('#message').html(data.error);
                $('#message').removeClass("alert alert-success").addClass("alert alert-danger");
                $('#message').show();
            }
        },
        error: function (e) {
            console.log("ERROR: ", e);
            show(e);
        }
    });


}

function updateMarketingTree() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "marketing-tree/update",
        data: "",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader()
            showPopup(data.message)
        },
        error: function (e) {
            showPopup(e)
            closeLoader()
        },
        always: function () {
            closeLoader()
        }
    });
}

let i = 5;//время в сек.
function time(message) {
    $("#message").html('<p>' + message +
        'Через <b>' + i + '</b> секунд Вы будете переадресованы на страницу входа</p>');//визуальный счетчик
    i--;//уменьшение счетчика
    if (i < 0) window.location.href = "/login"; //редирект
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
