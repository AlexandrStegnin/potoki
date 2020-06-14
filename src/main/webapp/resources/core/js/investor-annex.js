jQuery(document).ready(function ($) {
    $('#search-annex').on('keyup', function () {
        filterByAnnexName();
    })
    $('.delete-annex').click(function (e) {
        e.preventDefault();
        let annexId = $(this).attr('data-annex-id');
        deleteAnnex(annexId);
        $(this).closest('tr').remove();
    });
    $('#upload').on('click', function (e) {
        e.preventDefault();
        uploadAnnexes();
    })
});

function uploadAnnexes() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let data = new FormData();
    $.each($('#file')[0].files, function (k, value) {
        data.append(k, value);
    });
    $('#msg').html('Начинаем загрузку файлов...');
    $('#msg-modal').modal('show');
    connect();
    $.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        processData: false,
        contentType: false,
        cache: false,
        url: "annexes/upload",
        data: data,
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
            disconnect();
        },
        error: function (e) {
            showPopup(e.toString());
            disconnect();
        }
    });
}

function deleteAnnex(annexId) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let annex = ({
        id: annexId
    });
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "annexes/delete",
        data: JSON.stringify(annex),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
        },
        error: function (e) {
            showPopup('Что-то пошло не так [' + e.toString() + ']');
        }
    });
}

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

function filterByAnnexName() {
    let input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("search-annex");
    filter = input.value.toUpperCase();
    table = document.getElementById("annex-table");
    tr = table.getElementsByTagName("tr");
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}
