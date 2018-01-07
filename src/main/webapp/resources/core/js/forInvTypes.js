jQuery(document).ready(function ($) {

    $('a#delete').click(function (event) {
        event.preventDefault();
        var invTypeId = $(this).attr('name');

        deleteInvType(invTypeId);
        $('#tblInvType').find('tr#' + invTypeId).remove();
    });

    $("#invTypes").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        var modelAttributeValue = $('#edit').val();
        if(modelAttributeValue === "true"){
            updateInvType();
        }else{
            saveInvType();
        }
    });
});

function saveInvType() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"invType" : $("#invType").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "saveinvtype",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#invType').val('');

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

function updateInvType() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"invType" : $("#invType").val(),
        "invTypeId" : $("#id").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "editinvtype",
        data : JSON.stringify(search),
        dataType : 'text',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#invType').val('');
            showPopup();
            closePopup();
            window.location.href = "/viewinvestorstypes";
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });

}

function deleteInvType(invTypeId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"invTypeId" : invTypeId});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "deleteinvtype",
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