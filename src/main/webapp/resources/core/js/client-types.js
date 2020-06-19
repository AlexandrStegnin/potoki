let ClientType = function () {
}

ClientType.prototype = {
    id: 0,
    title: '',
    modifiedBy: '',
    build: function (id, title) {
        this.id = id;
        this.title = title;
    }
}

jQuery(document).ready(function ($) {
    let clientTypeTable = $('#client-type-table');
    $('#save-client-type').on('click', function (e) {
        let clientTypeTitle = $('#client-type-title').val().trim();
        if (clientTypeTitle === '') {
            return false;
        }
        let clientTypeId = $('#client-type-id').val();
        if (clientTypeId === '') {
            saveClientType('create');
            $('#create-client-type').modal('hide');
        } else {
            saveClientType('update');
            $('#create-client-type').modal('hide');
        }
    });
    clientTypeTable.on('click', '.update-client-type', function () {
        fillModalForm($(this).attr('data-client-type-id'), $(this).attr('data-client-type-title'));
    });
    clientTypeTable.on('click', '.delete-client-type', function () {
        let clientTypeId = $(this).attr('data-client-type-id');
        let clientTypeTitle = $(this).attr('data-client-type-title')
        deleteClientType(clientTypeId, clientTypeTitle);
        $(this).closest('tr').remove();
    });
});

function saveClientType(operation) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let clientType = new ClientType();

    let id = $('#client-type-id').val();
    let title = $('#client-type-title').val().trim();

    clientType.build(id, title);

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "types/" + operation,
        data: JSON.stringify(clientType),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            updateRow(data);
            fillModalForm('', '');
            showPopup(operation, data.title);
        })
        .fail(function (e) {
            console.log(e);
        });
}

function deleteClientType(clientTypeId, clientTypeTitle) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let clientType = new ClientType();

    clientType.build(clientTypeId, clientTypeTitle);

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "types/delete",
        data: JSON.stringify(clientType),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            showPopup(data);
        })
        .fail(function (e) {
            console.log(e);
        });
}

function updateRow(clientType) {
    let row = $('tr#' + clientType.id + '');
    if (row.length > 0) {
        row.find('td:eq(1)').text(clientType.title);
        row.find('td:eq(2)').text(clientType.modifiedBy);
    } else {
        let newRow = createNewRow(clientType);
        $('#client-type-table').find('tr:last').after(newRow);
    }
}

function createNewRow(clientType) {
    return '<tr id="' + clientType.id + '">' +
        '<td>' + clientType.id + '</td>' +
        '<td>' + clientType.title + '</td>' +
        '<td>' + clientType.modifiedBy + '</td>' +
        '<td>' +
            '<button type="button" class="btn btn-sm btn-success update-client-type" data-toggle="modal"' +
                ' data-target="#create-client-type" data-client-type-id="' + clientType.id + '"' +
                ' data-client-type-title="' + clientType.title + '">' +
                ' <i class="far fa-edit"></i>' +
            '</button>' +
            '<button type="button" class="btn btn-sm btn-danger delete-client-type" data-type-id="' + clientType.id + '"' +
                ' data-client-type-title="' + clientType.title + '">' +
                '<i class="far fa-trash-alt"></i>' +
            '</button>' +
        '</td>' +
        '</tr>'
}

function fillModalForm(clientTypeId, clientTypeTitle) {
    $('#client-type-id').val(clientTypeId);
    $('#client-type-title').val(clientTypeTitle);
}

function showPopup(message, title) {
    if (message === 'create') {
        message = 'Вид клиента ' + title + ' успешно создан';
    } else if (message === 'update') {
        message = 'Вид клиента ' + title + ' успешно обновлён';
    }
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
