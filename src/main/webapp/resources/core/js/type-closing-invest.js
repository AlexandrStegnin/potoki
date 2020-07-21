jQuery(document).ready(function ($) {

    $('a#delete').click(function (event) {
        event.preventDefault();
        var typeClosingInvestId = $(this).attr('name');
        $('#tblTypeClosInv').find('tr#' + typeClosingInvestId).remove();
        deleteTypeClosing(typeClosingInvestId);
    });

    $("#typeClosingInvest").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        var modelAttributeValue = $('#edit').val();
        if(modelAttributeValue === "true"){
            updateTypeClosingInvest();
        }else{
            saveTypeClosingInvest();
        }
    });
});

function saveTypeClosingInvest() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"typeClosingInvest" : $("#typeClosInvest").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "saveTypeClosing",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#typeClosInvest').val('');

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

function updateTypeClosingInvest() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"typeClosingInvest" : $("#typeClosInvest").val(),
        "typeClosingInvestId" : $("#id").val()});
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "editTypeClosing",
        data : JSON.stringify(search),
        dataType : 'text',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            $('#typeClosInvest').val('');
            showPopup();
            closePopup();
            window.location.href = "/viewTypesClosingInvest";
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });

}

function deleteTypeClosing(typeClosingInvestId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"typeClosingInvestId" : typeClosingInvestId});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "deleteTypeClosing",
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