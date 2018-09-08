jQuery(document).ready(function ($) {

    $('a#delete').click(function (event) {
        event.preventDefault();
        var typeId = $(this).attr('name');

        deleteType(typeId);
        $('#tblCashType').find('tr#' + typeId).remove();
    });

    $("#cashType").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        var modelAttributeValue = $('#edit').val();
        if(modelAttributeValue === "true"){
            updateType();
        }else{
            saveType();
        }
    });
});

function saveType() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"cashType" : $("#cType").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "savecashtype",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#cType').val('');

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

function updateType() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"cashType" : $("#cType").val(),
        "cashTypeId" : $("#id").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "editcashtype",
        data : JSON.stringify(search),
        dataType : 'text',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#cType').val('');
            showPopup();
            closePopup();
            window.location.href = "/viewcashtypes";
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });

}

function deleteType(typeId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"cashTypeId" : typeId});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "deletetype",
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