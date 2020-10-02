jQuery(document).ready(function ($) {
    connect();
    toggleTooltip()
    checkFilesSize()
    $('#upload').on('click', function (e) {
        e.preventDefault();
        uploadAnnexes();
    });
    $('#check-all').on('click', function () {
        $('input:checkbox').not(this).prop('checked', this.checked);
    });
    $('#delete-annex-list').on('click', function () {
        let annexIdList = [];
        $.each($('input:checkbox:checked').not('#check-all'), function (ind, el) {
            annexIdList.push(el.id);
        });
        deleteAnnexList(annexIdList);
    })
});

function uploadAnnexes() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let data = new FormData();
    $.each($('#file')[0].files, function (k, value) {
        data.append(k, value);
    });
    $('#msg').html('Начинаем загрузку файлов...');
    $('#msg-modal').modal('show');
    // connect();
    $.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        processData: false,
        contentType: false,
        cache: false,
        url: "annexes/upload",
        data: data,
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
        },
        error: function (e) {
            showPopup(e.error);
            disconnect();
        }
    });
}

function deleteAnnex(annexId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let annex = ({
        id: annexId
    });
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "annexes/delete",
        data: JSON.stringify(annex),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
        },
        error: function (e) {
            showPopup('Что-то пошло не так [' + e.error + ']');
        }
    });
}

function deleteAnnexList(annexIdList) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let  annexModel = {
        annexIdList: annexIdList
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "annexes/delete/list",
        data: JSON.stringify(annexModel),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
        },
        error: function (e) {
            showPopup('Что-то пошло не так [' + e.error + ']');
        }
    });
}

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

function toggleTooltip() {
    $(function () {
        $('[data-toggle="tooltip"]').tooltip('disable')
    })
}

function checkFilesSize() {
    $('#file').bind('change', function() {
        let tooltip = $('[data-toggle="tooltip"]');
        let files = this.files
        let totalSize = 0
        $.each(files, function () {
            totalSize += parseInt(this.size)
        })
        if (totalSize > 20971520) {
            tooltip.tooltip('enable')
            tooltip.tooltip('show')
        } else {
            $('#upload').removeClass('disabled')
        }
        if (files.length === 0) {
            $('#upload').addClass('disabled')
            tooltip.tooltip('hide')
            tooltip.tooltip('disable')
        }
    });
}
