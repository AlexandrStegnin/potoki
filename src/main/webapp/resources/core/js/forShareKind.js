jQuery(document).ready(function ($) {
    var modal = $("#bs-modal-shareKind");

    $("#shareKind").keydown(function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            return false;
        }
    });

    $('a#delete').click(function (event) {
        event.preventDefault();
        var shareKindId = $(this).attr('name');
        $('#tblShareKinds').find('tr#' + shareKindId).remove();
        deleteShareKind(shareKindId);
    });

    $("a#newShareKind").click(function (event) {
        event.preventDefault();
        modal.modal({
            show : true,
            backdrop : 'static',
            keyboard : false
        });
        modal.find($("#formTitle")).text("Добавление вида доли");
    });

    $(document).on("click", "a#saveEdit", function (event) {
        event.preventDefault();
        var modelAttributeValue = $(this).text();
        if(modelAttributeValue === "Обновить"){
            updateShareKind();
        }else{
            saveShareKind();
        }
    });

    $(document).on("click", "a#editShare", function (event) {
        event.preventDefault();
        modal.modal({
            show : true,
            backdrop : 'static',
            keyboard : false
        });

        var shareId = $(this).attr("name");
        var shareName = $(this).data("toggle");
        modal.find("#id").val(shareId);
        modal.find("#shareKindName").val(shareName);
        modal.find("#saveEdit").text("Обновить");
    });

});

function saveShareKind() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"shareKind" : $("#shareKindName").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "saveShareKind",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#shareKindName').val('');

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

function updateShareKind() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"shareKind" : $("#shareKindName").val(),
        "shareKindId" : $("#id").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "editShareKind",
        data : JSON.stringify(search),
        dataType : 'text',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#shareKindName').val('');
            showPopup();
            closePopup();
            window.location.href = "/viewShareKind";
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });

}

function deleteShareKind(shareKindId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"shareKindId" : shareKindId});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "deleteShareKind",
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