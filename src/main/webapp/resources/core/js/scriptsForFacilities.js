jQuery(document).ready(function ($) {
    $('a#delete').click(function (event) {
        event.preventDefault();
        enableButton(false);
        var rowId = $(this).attr('name');
        $('#tblFacilities').find('tr#' + rowId).remove();
        deleteFacility(rowId);
    })
});

function deleteFacility(facilityId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility" : facilityId})

    showLoader();

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "delete",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#popup_modal_form').find('#message').append(data.message);
            closeLoader();
            showPopup();
            closePopup();
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            showPopup();
            closePopup();
        },
        done : function() {
            enableButton(true);
        }
    });
}

function enableButton(flag) {
    $("a#delete").prop("disabled", flag);
}
