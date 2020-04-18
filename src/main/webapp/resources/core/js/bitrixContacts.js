jQuery(document).ready(function ($) {
    $('#updateBitrixContact').click(function (event) {
        event.preventDefault();
        updateContacts();
    });
});

function updateContacts() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/bitrix/merge",
        data: '',
        dataType: 'json',
        timeout: 120000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showModal(data);
        },
        error: function (e) {
            showModal(e.error);
        }
    });
}

function showModal(message) {
    let msgModal = $('#msg-modal');
    msgModal.find('#msg').val('');
    msgModal.find('#msg').append(message);
    msgModal.modal('show');
}
