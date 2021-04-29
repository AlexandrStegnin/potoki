let filters = [];

let max;
let min;

let RentPaymentDTO = function () {}

RentPaymentDTO.prototype = {
    dateGiven: null,
    facilityId: 0,
    shareType: null,
    rentPaymentsId: [],
    build: function (dateGiven, facilityId, shareType, rentPaymentsId) {
        this.dateGiven = dateGiven;
        this.facilityId = facilityId;
        this.shareType = shareType;
        this.rentPaymentsId = rentPaymentsId;
    }
}

jQuery(document).ready(function ($) {

    blockUnblockDropdownMenus('block');

    $(document).on('click', '#unblock_operations', function () {
        let tableId = $(this).data('table-id');
        if ($(this).text() === 'Разблокировать операции') {
            releaseOperations(tableId, 'unblock');
        } else {
            $('#btn-search').click()
        }
    });

    $('#msg-modal').on('shown.bs.modal', function () {
        // if data-timer attribute is set use that, otherwise use default (7000)
        let timer = 3000;
        $(this).delay(timer).fadeOut(200, function () {
            $(this).modal('hide');
        });
    });

    $(document).on('submit', '#reInvestData', function (event) {
        event.preventDefault();
        prepareReinvestRent();
    });

    $(document).on('click', '#accept-delete', function (event) {
        let confirm = $('#confirm-delete');
        confirm.modal('hide')
        let action = confirm.find('#action').attr('data-action')
        if (action === 'delete-list') {
            showLoader();
            event.preventDefault();
            let cashIdList = [];
            $('table#rentPayments').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:checked').not(':disabled').each(function () {
                    cashIdList.push($(this).closest('tr').attr('id'));
                    $(this).closest('tr').remove();
                })
            });
            deleteList(cashIdList);
        }
    })

    $(document).on('click', '#btn-clear', function (e) {
        e.preventDefault();
        $('#fFacilities').find('option:eq(0)').prop('selected', true);
        $('#uFacilities').find('option:eq(0)').prop('selected', true);
        $('#investors').find('option:eq(0)').prop('selected', true);
        $('#beginPeriod').val('');
        $('#endPeriod').val('');
        $('.selectpicker').selectpicker('refresh')
        $('#btn-search').click()
    });

    $('#reinvestAll').prop('disabled', true);
    max = findMinMaxDate('#rentPayments tbody', 1, "max");
    min = findMinMaxDate('#rentPayments tbody', 1, "min");
    populateStorageUnderFacilities('uFacilities');
    populateStorageRooms();

    $(document).on('change', ':checkbox', function () {
        let id = $(this).attr('id');

        if (typeof id === 'undefined') {
            let cnt = checkChecked();
            if (cnt > 0) {
                blockUnblockDropdownMenus('unblock');
            } else {
                blockUnblockDropdownMenus('block');
            }
        }
    });

    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 1;
        $('#pageSize').val(pageSize);
        $('#filter-form').submit();
    });

    $('table#rentPayments').find('> tbody').find('> tr').each(function (i) {
        $(this).data('passed', true);
    });

    $('#reinvestAll').on('click', function (event) {
        event.preventDefault();
        $('#reInvestModal').modal({
            show: true
        })
    });

    $(document).on('click', 'a#cancelReinvest', function (event) {
        event.preventDefault();
        $('#reInvestModal').modal("hide");
    });

    $(document).on('change', '#checkAll', function () {
        let checked = $('#checkAll').prop('checked');
        if (!checked) {
            $('#reinvestAll').addClass('disabled');
            $('#delete-list').addClass('disabled');
            $('table#rentPayments').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:not(:disabled)').prop('checked', false);
            });
        } else {
            $('#reinvestAll').removeClass('disabled');
            $('#delete-list').removeClass('disabled');
            $('table#rentPayments').find('> tbody').find('> tr').each(function () {
                if (!$(this).data('passed')) {
                    $(this).find(':checkbox:not(:disabled)').prop('checked', false);
                } else {
                    $(this).find(':checkbox:not(:disabled)').prop('checked', checked);
                }
            });
        }
    });

    $('#fFacilities').change(function () {
        let facility = $(this).find('option:selected').attr('id');
        //appendUnderFacilities(facility, "uFacilities");
        getUnderFacilitiesFromLocalStorage(facility, 'uFacilities');
    });

    $(document).on('change', '#srcFacilities', function (event) {
        let facility = $(this).val();
        //appendUnderFacilities(facility, "uFacilities");
        getUnderFacilitiesFromLocalStorage(facility, 'srcUnderFacilities');
    });

    $(document).on('change', '#srcUnderFacilities', function () {

        let room = $(this).find('option:selected').attr('id');
        //appendUnderFacilities(facility, "uFacilities");
        getRoomsFromLocalStorage(room);
    });

    $(document).on('click', '#upload', function (event) {
        event.preventDefault();
        upload();
    });

    $('[data-toggle="tooltip"]').tooltip();

    $('#delete-list').on('click', function (event) {
        let confirm = $('#confirm-delete');
        confirm.modal('show')
        confirm.find('#action').attr('data-action', 'delete-list')
    });

});

function show(data) {
    let json = JSON.stringify(data);
    $('#cash-details').html(json);
}

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

function display(data, tableForSearch) {
    let json = JSON.stringify(data, null, 4);
    $('#' + tableForSearch).html(json);
}

function upload() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader();
    let data = new FormData();
    $.each($('#file')[0].files, function (k, value) {
        data.append(k, value);
    });
    $.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        processData: false,
        contentType: false,
        cache: false,
        url: "rent/upload",
        data: data,
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message);
            window.location.href = 'rent'
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error);
            disconnect();
        }
    });
}

function findMinMaxDate(table, col, maxOrMin) {
    let max, min;
    let data = [];
    let cDate;
    $(table).find('tr td:nth-child(' + col + ')').each(function (i) {
        let checkDate = $(this).text();
        let parts = checkDate.split(".");
        cDate = new Date(parseInt(parts[2]), parseInt(parts[1]) - 1, parseInt(parts[0]));
        data.push(cDate);
    });
    switch (maxOrMin) {
        case "max":
            max = new Date(Math.max.apply(null, data));
            return max;
        case "min":
            min = new Date(Math.min.apply(null, data));
            return min;
    }
}

function prepareReinvestRent() {
    let dateGiven;
    let facilityId;
    let shareType;
    let reinvestData = $('form#reInvestData');
    let reinvestIdList = [];

    dateGiven = $('#dateGiven').val();

    if (dateGiven.length === 0) {
        $('#dateGivenErr').addClass('d-block');
        return false;
    } else {
        $('#dateGivenErr').removeClass('d-block');
        dateGiven = new Date(dateGiven).getTime();
    }

    facilityId = reinvestData.find('#srcFacility').val()

    if (facilityId === "0") {
        $('#facilityErr').addClass('d-block');
        return false;
    } else {
        $('#facilityErr').removeClass('d-block');
    }

    shareType = reinvestData.find('#shareTypeName').find('option:selected').val()

    if (shareType.indexOf('Не определена') >= 0) {
        $('#shareTypeErr').addClass('d-block');
        return false;
    } else {
        $('#shareTypeErr').removeClass('d-block');
    }

    let current;

    $('#reInvestModal').modal('hide');
    $('#reinvestAll').prop('disabled', true);
    $('table#rentPayments').find('> tbody').find('> tr').each(function (i) {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            $(this).closest('tr').find('input:checkbox').prop('disabled', true);
            current = $(this).closest('tr');
            reinvestIdList.push(current.attr('id'));
            $('#' + current.attr('id') + ' input:checkbox').prop('disabled', true);
        });
    });
    let rentPaymentDTO = new RentPaymentDTO();
    rentPaymentDTO.build(dateGiven, facilityId, shareType, reinvestIdList);
    reinvestRentPayments(rentPaymentDTO);

}

/**
 * Реивестировать выплаты с аренды
 *
 * @param rentPaymentDTO {RentPaymentDTO} DTO для реинвестирования
 */
function reinvestRentPayments(rentPaymentDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "./rent/reinvest",
        data: JSON.stringify(rentPaymentDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message)
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        },
        done: function (e) {
            closeLoader();
            enableSearchButton(true);
        }
    });
}

function checkChecked() {
    return $('table#rentPayments').find('[type="checkbox"]:checked:not(:disabled)').length;
}

function blockUnblockDropdownMenus(blockUnblock) {
    let reinvest = $('#reinvest');
    switch (blockUnblock) {
        case 'block':
            reinvest.find('> li').each(function () {
                $(this).closest('li').removeClass('active').addClass('disabled');
            });
            break;
        case 'unblock':
            reinvest.find('> li').each(function () {
                $(this).closest('li').removeClass('disabled').addClass('active');
            });
            break;
    }
}

function deleteList(cashIdList) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let rentPaymentDTO = ({
        rentPaymentsId: cashIdList
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "rent/delete/checked",
        data: JSON.stringify(rentPaymentDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            closeLoader();
            showPopup(data.message);
            window.location.href = 'rent'
        })
        .fail(function (e) {
            closeLoader();
            console.log(e);
        })
        .always(function () {
            closeLoader();
            console.log('Закончили!');
        });
}

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
