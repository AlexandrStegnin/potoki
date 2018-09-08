jQuery(document).ready(function ($) {

    $(document).on('click', '#loadSaleOfFacilitiesAjax', function (event) {
        event.preventDefault();
        loadSaleOfFacilitiesAjax("loadSaleOfFacilitiesAjax");
    });

    $('#deleteSale').on('click', function (event) {
        event.preventDefault();
        deleteSaleOfFacilities();
    })
});

function deleteSaleOfFacilities() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/deleteSaleOfFacilities",
        data: '',
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            closeLoader();
            slideBox(data.message);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Закончили!');
        });
}

function loadSaleOfFacilitiesAjax(action) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var form = $('#invFlows')[0];
    var data = new FormData();
    var fileBuckets = [];
    $.each($('#file')[0].files, function (k, value) {
        data.append(k, value);
        fileBuckets.push(k, value);
    });

    showLoader();
    $.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        processData: false,
        contentType: false,
        cache: false,
        url: action,
        data: data,
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#popup_modal_form').find('#message').append(data.message);
            closeLoader();
            showPopup();
            closePopup();
        },
        error: function (e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            showPopup();
            closePopup();
        }
    });

}