jQuery(document).ready(function ($) {

    $('a#delete').click(function (event) {
        event.preventDefault();
        let typeClosingInvestId = $(this).attr('data-type-closing-id');
        $('#tblTypeClosInv').find('tr#' + typeClosingInvestId).remove();
        deleteTypeClosing(typeClosingInvestId);
    });

    $("#typeClosing").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        let modelAttributeValue = $('#edit').val();
        if (modelAttributeValue === "true") {
            updateTypeClosing();
        } else {
            saveTypeClosing();
        }
    });
});

function saveTypeClosing() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let typeClosingDTO = {
        "name": $("#typeClosInvest").val()
    };
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "create",
        data: JSON.stringify(typeClosingDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message)
            $('#typeClosInvest').val('');
        },
        error: function (e) {
            console.log(e.error)
        }
    });

}

function updateTypeClosing() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let typeClosingDTO = ({
        "name": $("#typeClosInvest").val(),
        "id": $("#id").val()
    });
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "" + typeClosingDTO.id,
        data: JSON.stringify(typeClosingDTO),
        dataType: 'text',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message)
            window.location.href = "/type-closing/list";
        },
        error: function (e) {
            console.log(e.error)
        }
    });

}

function deleteTypeClosing(typeClosingInvestId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let typeClosingDTO = {
        "id": typeClosingInvestId
    };

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "delete",
        data: JSON.stringify(typeClosingDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message)
        },
        error: function (e) {
            console.log(e.error)
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
