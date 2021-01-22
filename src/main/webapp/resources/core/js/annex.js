let Annex = function () {
}

Annex.prototype = {
    id: 0,
    annexName: '',
    annexRead: 0,
    userId: 0,
    dateRead: new Date(),
    build: function (id, annexName, annexRead, userId, dateRead) {
        this.id = id;
        this.annexName = annexName;
        this.annexRead = annexRead;
        this.userId = userId;
        this.dateRead = dateRead;
    }
}

jQuery(document).ready(function ($) {
    blurElement($('.out'), 4);
    checkAnnexes(null);
    $('#look').on('click', function () {
        $('#readAnnex').css('display', 'none');
    });

    $('#closed-projects').on('click', function (e) {
        e.preventDefault()
        getAnnexes(null)
        $('#readAnnexTable').modal('show');
    });

    $("#readAnnexTable").on("hidden.bs.modal", function () {
        if ($('#readAnnex').css('z-index') !== '-1000001') {
            $('#readAnnex').css('display', 'block');
        }
    });
    $(document).on('change', 'input[type="checkbox"]:checked', function () {
        let userId = $(this).data('user-id');
        let annex = {
            id: $(this).data('annex-id'),
            annexName: $(this).data('annex-name')
        };
        $(this).attr('disabled', 'disabled');
        let d = new Date();
        let output = d.toLocaleDateString();
        $('#annexDate-' + annex.id).html(output);
        let trId = $(this).closest('tr').attr('id');
        markAnnexRead(trId, userId, annex, 1);
    });
});

/**
 * Скрыть/отобразить окно с приложениями и необходимостью прочтения
 *
 * @param have
 */
function toggleAnnexModal(have) {
    showLoader();
    let readAnnex = $('#readAnnex');
    if (have === true) {
        getAnnexes(null);
        blurElement($('.out'), 4);
        readAnnex.css('z-index', '1000001');
        readAnnex.css('display', 'block');
        $('#look').attr('disabled', false);
        disableScroll(true);
    } else {
        $("#readAnnexTable").modal('hide');
        blurElement($('.out'), 0);
        $('div.out').css('filter', '');
        readAnnex.css('z-index', '-1000001');
        readAnnex.css('display', 'none');
        disableScroll(false);
    }
    closeLoader();
}

/**
 * Проверить есть ли непрочитанные приложения к договорам инвестора
 *
 * @param login
 */
function checkAnnexes(login) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let user = {
        login: login
    };

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "have-unread",
        data: JSON.stringify(user),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (have) {
            toggleAnnexModal(have);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function (e) {
            closeLoader();
        })
}

/**
 * Получить приложения к договорам инвестора
 *
 * @param login
 */
function getAnnexes(login) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let user = {
        login: login
    };

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "get-annexes",
        data: JSON.stringify(user),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            createAnnexesTbl(data);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function (e) {
            closeLoader();
        })
}

/**
 * Создать таблицу с приложениями
 *
 * @param annexes
 */
function createAnnexesTbl(annexes) {

    let body = $('table#tblAnnex').find('tbody');
    let tr, td, a, label, input, strDate;
    body.empty();

    $.each(annexes, function (i, el) {
        let annex = new Annex();
        annex.build(el.id, el.annex.annexName, el.annexRead, el.userId, el.dateRead);
        tr = $('<tr></tr>');
        tr.attr('id', annex.id);
        tr.appendTo(body);

        td = $('<td></td>');
        td.data('annexName', annex.annexName);
        a = $('<a>' + annex.annexName + '</a>');
        a.attr('href', '/annexes/' + annex.id)
            .attr('target', '_blank');
        a.appendTo(td);
        td.appendTo(tr);

        label = $('<label></label>');
        label.attr('htmlFor', 'annexId' + annex.id);
        input = $('<input type="checkbox">');
        input.attr('checked', annex.annexRead === 1);
        input.attr('disabled', annex.annexRead === 1);
        input.data('userId', annex.userId);
        input.data('annexId', annex.id);
        input.data('annexName', annex.annexName);
        td = $('<td style="text-align: center"></td>');
        label.appendTo(td);
        input.appendTo(td);
        td.appendTo(tr);

        strDate = annex.dateRead === null ? '' : new Date(annex.dateRead).toLocaleDateString();

        td = $('<td>' + strDate + '</td>');
        td.attr('id', 'annexDate-' + annex.id);
        td.appendTo(tr);
    });
}

/**
 * Отметить приложение к договору, как прочитанное
 *
 * @param id
 */
function markAnnexRead(id) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let annex = {
        id: id
    };
    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "mark-read-annex",
        data: JSON.stringify(annex),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            closeLoader();
            updateUnreadAnnexes();
        },
        error: function (e) {
            closeLoader();
            console.log('Произошла ошибка - ' + e.toString());
        },
        always: function () {
            closeLoader()
        }
    });
}

/**
 * Проверить остались и непрочитанные приложения
 *
 */
function updateUnreadAnnexes() {
    let unchecked = checkUnreadAnnex();
    if (unchecked === 0) {
        toggleAnnexModal(false);
    }
}

function checkUnreadAnnex() {
    return $('#tblAnnex').find('tr').find('input[type="checkbox"]').not(':checked').length;
}

function blurElement(element, size) {
    let filterVal = 'blur(' + size + 'px)';
    $(element).css({
        'filter': filterVal,
        'webkitFilter': filterVal,
        'mozFilter': filterVal,
        'oFilter': filterVal,
        'msFilter': filterVal,
        'transition': 'all 0.5s ease-out',
        '-webkit-transition': 'all 0.5s ease-out',
        '-moz-transition': 'all 0.5s ease-out',
        '-o-transition': 'all 0.5s ease-out'
    });
}

function disableScroll(disable) {
    let body = $('body');
    if (disable === true) {
        body.addClass('stop-scrolling');
        body.bind('touchmove', function(e){e.preventDefault()});
    } else {
        body.removeClass('stop-scrolling');
        body.unbind('touchmove');
    }
}
