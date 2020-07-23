jQuery(document).ready(function ($) {

    $('a#delete').click(function (event) {
        event.preventDefault();
        let newCashDetailId = $(this).attr('data-cash-detail-id');

        deleteDetail(newCashDetailId);
        $('#tblNewCashDetails').find('tr#' + newCashDetailId).remove();
    });

    $("#newCashDetails").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        let modelAttributeValue = $('#edit').val();
        if(modelAttributeValue === "true"){
            update();
        }else{
            create();
        }
    });
});

function create() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let detailDTO = {
        "name" : $("#newDetail").val()
    };
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "create",
        data : JSON.stringify(detailDTO),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            showPopup(data.message)
            $('#newDetail').val('');
        },
        error: function(e) {
            console.log(e.error)
        }
    });

}

function update() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let detailDTO = {
        "id" : $("#id").val(),
        "name" : $("#newDetail").val()
    };
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "" + detailDTO.id,
        data : JSON.stringify(detailDTO),
        dataType : 'text',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            showPopup(data.message);
            window.location.href = "/new-cash-details/list";
        },
        error : function(e) {
            console.log(e.error)
        }
    });

}

function deleteDetail(newCashDetailId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let detailDTO = {
        "id" : newCashDetailId
    };

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "delete",
        data : JSON.stringify(detailDTO),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            showPopup(data.message);
        },
        error: function(e) {
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
