let filters = [];

let max;
let min;

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
    max = findMinMaxDate('#invFlows tbody', 1, "max");
    min = findMinMaxDate('#invFlows tbody', 1, "min");
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

    $('table#invFlows').find('> tbody').find('> tr').each(function (i) {
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
            $('table#invFlows').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:not(:disabled)').prop('checked', false);
            });
        } else {
            $('#reinvestAll').removeClass('disabled');
            $('#deleteAll').removeClass('disabled');
            $('table#invFlows').find('> tbody').find('> tr').each(function () {
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
        $('table#invFlows').find('> tbody').find('> tr').each(function () {
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

    let form = $('#invFlows')[0];
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
    let err = false;
    let cashes = [];
    let cash;
    let givedCash;
    let dateGived;
    let facility;
    let investor;
    let cashType = null;
    let newCashDetails = null;
    let investorsType;
    let underFacility;
    let dateReport;
    let shareKind;
    let sourceFacility;
    let sourceUnderFacility;
    let reinvestData = $('form#reInvestData');
    let reinvestIdList = [];
    let sourceFlowsId;
    let room;

    dateGived = $('#dateGiv').val();

    if (dateGived.length === 0) {
        $('#dateRepErr').css('display', 'block');
        err = true;
    } else {
        $('#dateRepErr').css('display', 'none');
        err = false;
        dateGived = new Date(dateGived).getTime();
    }

    facility = {
        id: reinvestData.find('#srcFacilities').val(),
        facility: $('#srcFacilities').find('option:selected').text()
    };

    if (facility.facility.indexOf('Выберите объект') >= 0) {
        $('#facilityErr').css('display', 'block');
        err = true;
    } else {
        $('#facilityErr').css('display', 'none');
        err = false;
    }

    investorsType = {
        id: reinvestData.find('#invType').val(),
        investorsType: $('#invType').find('option:selected').text()
    };

    if (investorsType.investorsType.indexOf('Выберите вид инвестора') >= 0) {
        $('#invTypeErr').css('display', 'block');
        err = true;
    } else {
        $('#invTypeErr').css('display', 'none');
        err = false;
    }

    shareKind = {
        id: reinvestData.find('#shareKindName').val(),
        shareKind: $('#shareKindName').find('option:selected').text()
    };

    if (shareKind.shareKind.indexOf('Выберите вид доли') >= 0) {
        $('#shareKindErr').css('display', 'block');
        err = true;
    } else {
        $('#shareKindErr').css('display', 'none');
        err = false;
    }

    if (err) {
        closeLoader();
        return false;
    }
    let tmpDate;
    let current;

    $('#reInvestModal').modal('hide');
    $('#reinvestAll').prop('disabled', true);
    $('table#invFlows').find('> tbody').find('> tr').each(function (i) {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            $(this).closest('tr').find('input:checkbox').prop('disabled', true);
            current = $(this).closest('tr');
            sourceFlowsId = current.attr('id');
            reinvestIdList.push(current.attr('id'));
            $('#' + current.attr('id') + ' input:checkbox').prop('disabled', true);

            sourceFacility = {
                id: current.children('td:eq(1)').attr('data-facility-id'),
                facility: current.children('td:eq(1)').text()
            };

            sourceUnderFacility = {
                id: current.children('td:eq(2)').attr('data-under-facility-id'),
                underFacility: current.children('td:eq(2)').text()
            };

            room = {
                id: current.children('td:eq(3)').attr('data-room-id'),
                room: current.children('td:eq(3)').text()
            };

            if (room.room.length === 0) {
                room = null;
            }

            dateReport = current.children('td:eq(0)').attr('data-report-date');
            //tmpDate = dateReport.split(".");
            //dateReport = new Date(parseInt(tmpDate[2]), parseInt(tmpDate[1]) - 1, parseInt(tmpDate[0])).getTime();

            investor = {
                id: current.children('td:eq(4)').attr('data-investor-id'),
                login: current.children('td:eq(4)').text()
            };

            givedCash = current.children('td:eq(16)').attr('data-gived-cash');

            cash = {
                id: null,
                givedCash: givedCash,
                dateGivedCash: dateGived,
                facility: facility,
                investor: investor,
                cashSource: null,
                cashType: cashType,
                newCashDetails: newCashDetails,
                investorsType: investorsType,
                underFacility: null,
                dateClosingInvest: null,
                typeClosingInvest: null,
                shareKind: shareKind,
                dateReport: dateReport,
                sourceFacility: sourceFacility,
                sourceUnderFacility: sourceUnderFacility,
                sourceFlowsId: sourceFlowsId,
                room: room
            };

            cashes.push(cash);

        });
    });

    saveReinvestCash(cashes, reinvestIdList);

}

function saveReinvestCash(cashes, reinvestIdList) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({
        investorsCashList: cashes,
        reinvestIdList: reinvestIdList,
        what: ""
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "saveReCash",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            $('#msg').html(data.message);
            $('#msg-modal').modal('show');
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
    return $('table#invflows').find('[type="checkbox"]:checked:not(:disabled)').length;
}

function showPopup() {
    $('#popup_modal_form')
        .css('display', 'block') // убирaем у мoдaльнoгo oкнa display: none;
        .animate({opacity: 1, top: '50%'}, 200); // плaвнo прибaвляем прoзрaчнoсть oднoвременнo сo съезжaнием вниз
}

function closePopup() {
    setTimeout(function () {
        $('#popup_modal_form')
            .animate({opacity: 0, top: '45%'}, 200,  // плaвнo меняем прoзрaчнoсть нa 0 и oднoвременнo двигaем oкнo вверх
                function () { // пoсле aнимaции
                    $(this).css('display', 'none'); // делaем ему display: none;
                }
            )
            .find('#message').html('');
    }, 3000);
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
