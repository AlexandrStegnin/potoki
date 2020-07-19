jQuery(document).ready(function ($) {

    $('a#delete').click(function (event) {
        event.preventDefault();
        let sourceId = $(this).attr('name');

        deleteSource(sourceId);
        $('#tblCashSrc').find('tr#' + sourceId).remove();
    });

    $("#cashSrc").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        let modelAttributeValue = $('#edit').val();
        if (modelAttributeValue === "true") {
            updateSource();
        } else {
            saveSource();
        }
    });
});

function saveSource() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let cashSource = {
        "name": $("#source").val()
    };
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "create",
        data: JSON.stringify(cashSource),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
            $('#source').val('');
        },
        error: function (e) {
            console.log(e.error)
        }
    });

}

function updateSource() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let cashSource = ({
        "name": $("#source").val(),
        "id": $("#id").val()
    });
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "edit",
        data: JSON.stringify(cashSource),
        dataType: 'text',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            let message = JSON.parse(data).message
            showPopup(message);
            $('#source').val('');
        },
        error: function (e) {
            console.log(e.error)
        }
    });

}

function deleteSource(sourceId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let cashSource = ({"id": sourceId});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "delete",
        data: JSON.stringify(cashSource),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
        },
        error: function (e) {
            console.log(e.error)
        }
    });
}

function showPopup(message) {
    closeLoader();
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
