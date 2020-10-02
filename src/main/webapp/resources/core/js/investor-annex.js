jQuery(document).ready(function ($) {
    // connect();
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
    let progressBar = $('#progressBar');
    // Ajax call for file uploading
    let ajaxReq = $.ajax({
        url: 'annexes/upload',
        type: 'POST',
        enctype: "multipart/form-data",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        xhr: function () {
            //Get XmlHttpRequest object
            let xhr = $.ajaxSettings.xhr();

            //Set onprogress event handler
            xhr.upload.onprogress = function (event) {
                let percent = Math.round((event.loaded / event.total) * 100);
                progressBar.text(percent + '%');
                progressBar.css('width', percent + '%');
            };
            return xhr;
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
            //Reset alert message and progress bar
            $('#pb').addClass('d-block')
            progressBar.text('');
            progressBar.css('width', '0%');
        }
    });

    // Called on success of file upload
    ajaxReq.done(function (msg) {
        showPopup(msg.message);
        $('#file').val('');
        $('#filter-btn').prop('disabled', false).click();
    });

    // Called on failure of file upload
    ajaxReq.fail(function (jqXHR) {
        showPopup(jqXHR.responseText + '(' + jqXHR.status + ' - ' + jqXHR.statusText + ')');
        $('#filter-btn').prop('disabled', false);
    });
}

function deleteAnnexList(annexIdList) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    let annexModel = {
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
            closeLoader()
            showPopup(data.message);
            $('#filter-btn').click()
        },
        error: function (e) {
            closeLoader()
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
    $('#file').bind('change', function () {
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
