jQuery(document).ready(function ($) {

});

function sendMail() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var form = $('#sendMail')[0];
    var data = new FormData(/*form*/);
    var fileBuckets = [];
    $.each($('#file')[0].files, function(k, value)
    {
        data.append(k, value);
        fileBuckets.push(k, value);
    });

    var users = [];
    $('#users').find(':selected').each(function(i, selected){
        users.push({id: $(selected).val()});
    });

    var mailingGroups = [];
    $('#mGroups').find(':selected').each(function(i, selected){
        mailingGroups.push({id: $(selected).val()});
    });

    var subject = $('#mSubj').val();
    var body = $('#mBody').val();

    var sendingMail = ({"mailingGroups" : mailingGroups, "users" : users,
        "subject" : subject, "body" : body});

    data.append("sendingMail", JSON.stringify(sendingMail));

    showLoader();
    $.ajax({
        type : "POST",
        enctype: "multipart/form-data",
        processData: false,
        contentType : false, //"application/json;charset=utf-8",
        cache: false,
        url : "createmail",
        data : data, //JSON.stringify(sendingMail),
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
        }
    });

}