jQuery(document).ready(function ($) {

    $('a#delete').click(function (event) {
        event.preventDefault();
        var sourceId = $(this).attr('name');

        deleteSource(sourceId);
        $('#tblCashSrc').find('tr#' + sourceId).remove();
    });

    $("#cashSrc").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        var modelAttributeValue = $('#edit').val();
        if(modelAttributeValue === "true"){
            updateSource();
        }else{
            saveSource();
        }
    });
});

function saveSource() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"cashSource" : $("#source").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "savecashsource",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#source').val('');

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

function updateSource() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"cashSource" : $("#source").val(),
                    "cashSourceId" : $("#id").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "editcashsource",
        data : JSON.stringify(search),
        dataType : 'text',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#source').val('');
            showPopup();
            closePopup();
            window.location.href = "/viewcashsources";
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });

}

function deleteSource(sourceId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"cashSourceId" : sourceId});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "deletesource",
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