jQuery(document).ready(function ($) {

    /* МОДАЛЬНОЕ ОКНО */

    $(document).on('click', 'a#updateInvestorDemo', function (e) {
        e.preventDefault();
        updateInvestorDemo();
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

function updateInvestorDemo() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "../updateInvestorDemo",
        data: "",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            slideBox(data.message);
        },
        error: function (e) {
            console.log("ERROR: ", e);
            closeLoader();
        }
    });
}

function updateMarketingTree() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "../updateMarketingTree",
        data: "",
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            slideBox(data.message);
        },
        error: function (e) {
            slideBox(e);
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

function slideBox(message) {
    $('#slideBox').find('h4').html(message);
    setTimeout(function () {
        $('#slideBox').animate({'right': '52px'}, 500);
    }, 500);
    setTimeout(function () {
        $('#slideBox').animate({'right': '-300px'}, 500);
    }, 4000);
}

function populateFilters(pageName) {
    let filters = [];
    let facilityId;
    let underFacilityId;
    let investorsId = [];
    let startDate;
    let endDate;

    let fFacilities;
    let uFacilities;
    let investors;
    let beginPeriod;
    let endPeriod;

    let elDateClose;
    let elSrcFacilities;
    let elSrcUnderFacilities;
    let elShareKindName;

    let dateClose;
    let srcFacility;
    let srcUnderFacility;
    let shareKindName;
    let reInvestData;

    let allRows;

    fFacilities = $('#fFacilities');
    uFacilities = $('#uFacilities');
    investors = $('#investors');
    beginPeriod = $('#beginPeriod');
    endPeriod = $('#endPeriod');
    allRows = $('#all').attr('checked');

    switch (pageName) {
        case "investorscash":
        case "paysToInv":
            //Search form


            facilityId = fFacilities.find('option:selected').attr('id');
            underFacilityId = uFacilities.find('option:selected').attr('id');
            investors.find('option:selected').each(function () {
                investorsId.push($(this).attr('id'));
            });
            startDate = beginPeriod.val();
            endDate = endPeriod.val();

            //ReInvest form
            reInvestData = $('#reInvestData');
            elDateClose = reInvestData.find('#dateClose');
            elSrcFacilities = reInvestData.find('#srcFacilities');
            elSrcUnderFacilities = reInvestData.find('#srcUnderFacilities');
            elShareKindName = reInvestData.find('#shareKindName');

            dateClose = elDateClose.val();
            srcFacility = elSrcFacilities.find('option:selected').val();
            srcUnderFacility = elSrcUnderFacilities.find('option:selected').val();
            shareKindName = elShareKindName.find('option:selected').val();

            break;
        case "flowsSale":
            facilityId = fFacilities.find('option:selected').val();
            underFacilityId = uFacilities.find('option:selected').val();
            investorsId = investors.find('option:selected').val();
            startDate = beginPeriod.val();
            endDate = endPeriod.val();
            break;
    }
    let filter = {
        facilityId: facilityId,
        underFacilityId: underFacilityId,
        investorId: investorsId,
        startDateVal: startDate,
        endDateVal: endDate,
        allRows: allRows
    };
    filters.push(filter);
    filter = {
        dateClose: dateClose,
        srcFacility: srcFacility,
        srcUnderFacility: srcUnderFacility,
        shareKindName: shareKindName
    };
    filters.push(filter);

    localStorage.setItem(pageName + 'Filters', JSON.stringify(filters));
}

function getFiltersFromLS(pageName) {
    let lastFilters = JSON.parse(localStorage.getItem(pageName + 'Filters'));
    if (lastFilters != null && (lastFilters.length > 0)) {
        let beginPeriod = $('#beginPeriod');
        let endPeriod = $('#endPeriod');
        let allRows = $('#all');
        switch (pageName) {
            case "paysToInv":
                getFilters("paysToInv", lastFilters);
                break;
            case "investorscash":
                getFilters("investorscash", lastFilters);
                break;
            case "flowsSale":
                $('#fFacilities option[value="' + lastFilters[0].facilityId + '"]').attr('selected', 'selected');
                $('#uFacilities option[value="' + lastFilters[0].underFacilityId + '"]').attr('selected', 'selected');
                $('#investors option[value="' + lastFilters[0].investorId + '"]').attr('selected', 'selected');
                beginPeriod.val(lastFilters[0].startDateVal);
                endPeriod.val(lastFilters[0].endDateVal);
                allRows.prop('checked', !!lastFilters[0].allRows);
                break;
        }
    }
}

function getFilters(pageName, lastFilters) {
    let beginPeriod = $('#beginPeriod');
    let endPeriod = $('#endPeriod');
    let investors;
    $('#fFacilities option[id=' + lastFilters[0].facilityId + ']').attr('selected', 'selected');
    investors = lastFilters[0].investorId;
    $.each(investors, function (ind, el) {
        $('#investors option[id=' + el + ']').attr('selected', 'selected');
    });

    beginPeriod.text(lastFilters[0].startDateVal);
    endPeriod.text(lastFilters[0].endDateVal);

    $('#dateClose').val(lastFilters[1].dateClose);
    $('#srcFacilities').val(lastFilters[1].srcFacility);
    $('#srcUnderFacilities').val(lastFilters[1].srcUnderFacility);
    $('#shareKindName').val(lastFilters[1].shareKindName);
    if (lastFilters[0].facilityId !== 0) getUnderFacilitiesFromLocalStorage(lastFilters[0].facilityId, 'uFacilities');
    $('#uFacilities option[id=' + lastFilters[0].underFacilityId + ']').attr('selected', 'selected');
}