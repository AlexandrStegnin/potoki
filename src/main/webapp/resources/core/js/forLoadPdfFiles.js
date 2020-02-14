jQuery(document).ready(function($) {
    $(document).on('click', 'a#loadPdf', function (event) {
        event.preventDefault();
        $('#pdfForm').modal({
            show: true,
            backdrop: 'static',
            keyboard: false
        });
    });

    $(document).on('click', '#closeLoadPdf', function () {
        $('#pdfForm').modal('hide');
        window.location.href = "/profile";
    });

    $(document).on('click', '#closeAllPdf', function () {
        $('#allPdf').modal('hide');
    });


    $('#tblAnnex').dataTable({
        "paging": false,
        "ordering": false,
        "info": false,
        "bFilter": false,
        language: {
            "processing": "Подождите...",
            "search": "Поиск:",
            "lengthMenu": "Показать _MENU_ записей",
            "info": "Записи с _START_ до _END_ из _TOTAL_ записей",
            "infoEmpty": "Записи с 0 до 0 из 0 записей",
            "infoFiltered": "(отфильтровано из _MAX_ записей)",
            "infoPostFix": "",
            "loadingRecords": "Загрузка записей...",
            "zeroRecords": "Записи отсутствуют.",
            "emptyTable": "В таблице отсутствуют данные",
            "paginate": {
                "first": "Первая",
                "previous": "Предыдущая",
                "next": "Следующая",
                "last": "Последняя"
            },
            "aria": {
                "sortAscending": ": активировать для сортировки столбца по возрастанию",
                "sortDescending": ": активировать для сортировки столбца по убыванию"
            }
        }
    });


    $(document).on('click', '#save', function (event) {
        event.preventDefault();
        if ($('#investor').val() === "0") {
            $('#chooseInvestorMsg').css("display", "block").text("Необходимо выбрать инвестора");
            return false;
        } else {
            $('#chooseInvestorMsg').css("display", "none").text("");
        }
        savePdf();
    });

    $(document).on('click', '#unread', function (event) {
        event.preventDefault();
        $('#allPdf').modal({
            show: true
        });

    });

    $(document).on('click', 'a#delete', function (event) {
        event.preventDefault();
        var annexToContracts = {
            id : $(this).data('id'),
            annexName : $(this).attr('name')
        };
        $('#tblAnnex').find('tr#' + $(this).data('row-id')).remove();
        deleteAnnex(annexToContracts);
    })

});

function appendRowAfterSaveFile(annexId, annexName, userId, link, read, dateRead, action) {
    var row =
        "<tr id='" + annexId + "'>" +
            "<td style='text-align: center'>" + annexId + "</td>" +
            "<td style='text-align: center'>" + annexName + "</td>" +
            "<td style='text-align: center'><a href='/annexToContract/" + annexName + "'/> target='_blank'>Посмотреть</a></td>" +
            "<td style='text-align: center'>" +
                "<label for='annexId" + annexId + "'>Ознакомлен</label>" +
                "<input type='checkbox' id='annexId" + annexId + "' " +
                        "data-user-id='" + userId + "' " +
                        "data-annex-id='" + annexId + "'/>" +
            "</td>" +
            "<td style='text-align: center' id='annexDate-" + annexId + "'></td>" +
            "<td style='text-align: center'>" +
                "<a href='/deleteAnnex' /> id='delete' name='" + annexName + "' " +
                    "data-id='" + annexId + "' " +
                    "data-row-id='" + annexId + "' " +
                    "class='btn btn-danger custom-width'>Удалить</a></td>" +
        "</tr>";

    $('#tblAnnex').find('tr:last').append(row);

}

function deleteAnnex(annexToContracts) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({
        "annexToContracts" : annexToContracts
    });
    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "deleteAnnex",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            updateUnreadAnnexes();
            $('#successDelete').text(data.message);
            console.log(data.message);
        },
        error : function(e) {
            console.log('Произошла ошибка - ' + e.toString());
        }
    });
}

function savePdf() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var data = new FormData();
    $.each($('#file')[0].files, function(k, value) {
        data.append(k, value);
    });

    data.append("investorId", $('#investor').val());

    $.ajax({
        type : "POST",
        enctype: "multipart/form-data",
        processData: false,
        contentType : false,
        cache: false,
        url : "savePdf",
        data : data,
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#successLoad').text(data.message);
            $('#pdfForm').modal('hide');
            updateUnreadAnnexes();
        },
        error : function(e) {
            console.log(e.error);
        }
    });

}
