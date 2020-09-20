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
    getFiltersFromLS((window.location.pathname + '').split("/")[1]);

    $(document).on('click', '#unblock_operations', function () {
        let tableId = $(this).data('table-id');
        if ($(this).text() === 'Разблокировать операции') {
            releaseOperations(tableId, 'unblock');
        } else {
            releaseOperations(tableId, 'block');
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
        prepareSaveInvestorsCash();
    });

    $(document).on('click', '#bth-clear', function (e) {
        e.preventDefault();
        $('#fFacilities').find('option:eq(0)').prop('selected', true);
        $('#uFacilities').find('option:eq(0)').prop('selected', true);
        $('#investors').find('option:eq(0)').prop('selected', true);
        $('#beginPeriod').val('');
        $('#endPeriod').val('');
        populateFilters((window.location.pathname + '').split("/")[1]);
        window.location.href = window.location.pathname;
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
            $('#deleteAll').addClass('disabled');
            $('table#rentPayments').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:not(:disabled)').prop('checked', false);
            });
        } else {
            $('#reinvestAll').removeClass('disabled');
            $('#deleteAll').removeClass('disabled');
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

    $(document).on('click', '#loadInvFlowsAjax', function (event) {
        event.preventDefault();
        loadFlowsAjax("loadFlowsAjax");
    });

    $('[data-toggle="tooltip"]').tooltip();

    $('#deleteAll').on('click', function (event) {
        showLoader();
        event.preventDefault();
        let cashIdList = [];
        $('table#rentPayments').find('> tbody').find('> tr').each(function () {
            $(this).find(':checkbox:checked').not(':disabled').each(function () {
                cashIdList.push($(this).closest('tr').attr('id'));
                $(this).closest('tr').remove();
            })
        });
        deleteCash(cashIdList);
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

function loadFlowsAjax(action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let form = $('#rentPayments')[0];
    let data = new FormData();
    let fileBuckets = [];
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

function prepareSaveInvestorsCash() {
    showLoader();
    let dateGiven;
    let facilityId;
    let dateReport;
    let shareType;
    let reinvestData = $('form#reInvestData');
    let reinvestIdList = [];

    dateGiven = $('#dateGiv').val();

    if (dateGiven.length === 0) {
        $('#dateGivenErr').css('display', 'block');
        return false;
    } else {
        $('#dateGivenErr').css('display', 'none');
        dateGiven = new Date(dateGiven).getTime();
    }

    facilityId = reinvestData.find('#srcFacility').val()

    if (facilityId === 0) {
        $('#facilityErr').css('display', 'block');
        return false;
    } else {
        $('#facilityErr').css('display', 'none');
        err = false;
    }

    shareType = reinvestData.find('#shareKindName').val()

    if (shareType.indexOf('Выберите вид доли') >= 0) {
        $('#shareTypeErr').css('display', 'block');
        err = true;
    } else {
        $('#shareTypeErr').css('display', 'none');
        err = false;
    }

    if (err) {
        closeLoader();
        return false;
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

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "reinvest",
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

function deleteCash(cashIdList) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({
        cashIdList: cashIdList,
        what: ""
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/deleteFlowsList",
        data: JSON.stringify(search),
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

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
