jQuery(document).ready(function ($) {

    $('a#delete').click(function (event) {
        event.preventDefault();
        var newCashDetailId = $(this).attr('name');

        deleteDetail(newCashDetailId);
        $('#tblNewCashDetails').find('tr#' + newCashDetailId).remove();
    });

    $("#newCashDetails").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        var modelAttributeValue = $('#edit').val();
        if(modelAttributeValue === "true"){
            updateDetail();
        }else{
            saveDetail();
        }
    });
});

function saveDetail() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"newCashDetail" : $("#newDetail").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "savenewcashdetail",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#newDetail').val('');

            showPopup();
            closePopup();
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });

}

function updateDetail() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"newCashDetail" : $("#newDetail").val(),
        "newCashDetailId" : $("#id").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "editnewcashdetail",
        data : JSON.stringify(search),
        dataType : 'text',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#newDetail').val('');
            showPopup();
            closePopup();
            window.location.href = "/viewnewcashdetails";
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });

}

function deleteDetail(newCashDetailId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"newCashDetailId" : newCashDetailId});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "deletenewcashdetail",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            showPopup();
            closePopup();
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });
}