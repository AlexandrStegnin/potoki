let Facility = function () {}

Facility.prototype = {
    id: 0,
    name: '',
    fullName: '',
    city: '',
    build: function (id, name, fullName, city) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.city = city;
    }
}

jQuery(document).ready(function ($) {
    $('a#delete').click(function (event) {
        event.preventDefault();
        enableButton(false);
        let rowId = $(this).closest('tr').attr('id');
        $('#tblFacilities').find('tr#' + rowId).remove();
        deleteFacility(rowId);
    });
    $('#create').click(function (event) {
        showLoader();
        let action = $(this).data('action')
        if (action === 'create') {
            event.preventDefault();
            create();
        }
        closeLoader();
    })
});

function create() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let facility = createFacility(null);

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "create",
        data : JSON.stringify(facility),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success: function(data) {
            showPopup(data.message);
            if (data.status === 200) {
                clearForm();
            }
        },
        error: function(request, status, error){
            console.log(request.responseText);
            console.log(status);
            console.log(error);
        },
        always: function() {
            enableButton(true);
            closeLoader();
        }
    });
}

function deleteFacility(facilityId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({"facilityStr" : facilityId});

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
            showPopup(data.message);
        },
        error : function(e) {
            console.log(e)
        },
        done : function() {
            enableButton(true);
        }
    });
}

function enableButton(flag) {
    $("a#delete").prop("disabled", flag);
}

function createFacility(facilityId) {
    let facility = new Facility();
    let name = $('#f_name').val();
    let fullName = $('#full_name').val();
    let city = $('#city').val();
    facility.build(facilityId, name, fullName, city);
    return facility;
}

function clearForm() {
    $('#f_name').val('');
    $('#full_name').val('');
    $('#city').val('');
}

function showPopup(message) {
    closeLoader();
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
