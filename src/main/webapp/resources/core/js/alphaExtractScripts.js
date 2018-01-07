jQuery(document).ready(function ($) {
    $('#facility').change(function () {
        var facility = $(this).val();
        appendRentors(facility);
        var rentor = $('#rentor').val();
        appendCorrectTags(facility, rentor);
    });
    $('#rentor').change(function () {
        var facility = $('#facility').val();
        var rentor = $(this).val();
        appendCorrectTags(facility, rentor);
    })
});

function appendRentors(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"facility" : facility});

    showLoader();
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "findrentors",
        data : JSON.stringify(search),
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

function appendCorrectTags(facility, rentor) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"facility" : facility, "rentor" : rentor});

    showLoader();
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "findcorrecttags",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#correctTags')
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

