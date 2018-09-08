jQuery(document).ready(function ($) {

});

function check() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    //var search = ({"check" : 0});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "checkservice",
        data : null,
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#rentor')
                .find('option')
                .remove()
                .end()
                .attr('disabled', false)
                .append(data.message);
            closeLoader();
        },
        error : function(e) {
            closeLoader();
        }
    });
}