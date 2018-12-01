var filters = [];

var max;
var min;

jQuery(document).ready(function ($) {

    blockUnblockDropdownMenus('block');

    $('#msg-modal').on('shown.bs.modal', function () {
        // if data-timer attribute is set use that, otherwise use default (7000)
        var timer = 3000;
        $(this).delay(timer).fadeOut(200, function () {
            $(this).modal('hide');
        });
    });

    $(document).on('submit', '#reInvestData', function (event) {
        event.preventDefault();
        prepareSaveInvestorsCash();
    });


    $('#reinvestAll').prop('disabled', true);
    max = findMinMaxDate('#invFlows tbody', 1, "max");
    min = findMinMaxDate('#invFlows tbody', 1, "min");
    populateStorageUnderFacilities('uFacilities');
    populateStorageRooms();
    $("#search-form").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        searchFlows("allInvFlows");
    });

    $("#filter-form").submit(function (event) {

        // Disble the search button
        enableSearchButton(false);
        let fFacility = $('#fFacilities');
        let uFacility = $('#uFacilities');
        let inv = $('#investors');

        let facility = fFacility.find('option:selected').val();
        let underFacility = uFacility.find('option:selected').val();
        let investor = inv.find('option:selected').val();

        let dateFrom = $('#beginPeriod').val() + '';
        let dateTo = $('#endPeriod').val() + '';

        if (dateFrom.length === 0) dateFrom = null;
        if (dateTo.length === 0) dateTo = null;

        let facilityParam = facility === 'Выберите объект' ? '' : '?facility=' + facility;
        let uFacilityParam = underFacility === 'Выберите подобъект' ? facilityParam + '' : facilityParam.indexOf('?') === 0 ?
            facilityParam + '&underFacility=' + underFacility : '?underFacility=' + underFacility;
        let investorParam = investor === 'Выберите инвестора' ? uFacilityParam + '' : uFacilityParam.indexOf('?') === 0 ?
            uFacilityParam + '&investor=' + investor : '?investor=' + investor;
        let startDateParam = (dateFrom === null || dateFrom.length === 0) ? investorParam + '' : investorParam.indexOf('?') === 0 ? investorParam + '&startDate=' + dateFrom :
            '?startDate=' + dateFrom;
        let endDateParam = (dateTo === null || dateTo.length === 0) ? startDateParam + '' : startDateParam.indexOf('?') === 0 ? startDateParam + '&endDate=' + dateTo :
            '?endDate=' + dateTo;

        let params = endDateParam.indexOf('?') === 0 ? endDateParam : '';
        // Prevent the form from submitting via the browser.
        event.preventDefault();
        window.location.href = window.location.pathname + params;

        // getByPageNumber(facility, underFacility, investor, dateFrom, dateTo);

        // prepareFilter();
        enableSearchButton(true);
        //searchCash();


    });

    $(document).on('change', ':checkbox', function () {
        var id = $(this).attr('id');

        if (typeof id === 'undefined') {
            var cnt = checkChecked();
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
        var checked = $('#checkIt').prop('checked');
        if (!checked) {
            $('#reinvestAll').addClass('disabled');
            $('table#invFlows').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:not(:disabled)').prop('checked', false);
            });
        } else {
            $('#reinvestAll').removeClass('disabled');
            $('table#invFlows').find('> tbody').find('> tr').each(function () {
                if (!$(this).data('passed')) {
                    $(this).find(':checkbox:not(:disabled)').prop('checked', false);
                } else {
                    $(this).find(':checkbox:not(:disabled)').prop('checked', checked);
                }
            });
        }
    });

    $("#searchFacility").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        searchByMonths("invPaysMonth");
        searchFlows("invPaysAll");
    });

    $("#search-form-months").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        searchByMonths("invPaysMonth");
    });

    $('#fFacilities').change(function () {
        var facility = $(this).val();
        //appendUnderFacilities(facility, "uFacilities");
        getUnderFacilitiesFromLocalStorage(facility, 'uFacilities');
    });

    $(document).on('change', '#srcFacilities', function (event) {
        var facility = $(this).val();
        //appendUnderFacilities(facility, "uFacilities");
        getUnderFacilitiesFromLocalStorage(facility, 'srcUnderFacilities');
    });

    $(document).on('change', '#srcUnderFacilities', function () {

        var room = $(this).find('option:selected').attr('id');
        //appendUnderFacilities(facility, "uFacilities");
        getRoomsFromLocalStorage(room);
    });

    $(document).on('click', '#loadInvFlowsAjax', function (event) {
        event.preventDefault();
        loadFlowsAjax("loadFlowsAjax");
    });

    /* МОДАЛЬНОЕ ОКНО */
    $(document).on('click', 'a#goCash, a#goPays, a#goPaysAll, a#goArendaMonth, a#goArendaAll, a#goRashodiMonth, a#goRashodiAll', function (event) {

        if ($(this).attr('name') === "monthPays") {
            showLoader();
            showPaysMonths($("#facilities").find("option:selected").text());
        } else if ($(this).attr('name') === "allPays") {
            showLoader();
            showPaysAll($("#facilities").find("option:selected").text());
        } else if ($(this).attr('name') === "monthArenda") {
            showLoader();
            showArendaMonths($("#facilities").find("option:selected").text());
        } else if ($(this).attr('name') === "allArenda") {
            showLoader();
            showArendaAll($("#facilities").find("option:selected").text());
        } else if ($(this).attr('name') === "monthRashodi") {
            showLoader();
            showRashodiMonth($("#facilities").find("option:selected").text());
        } else if ($(this).attr('name') === "allRashodi") {
            showLoader();
            showRashodiAll($("#facilities").find("option:selected").text());
        } else {
            var facility = $(this).attr('name');
            showDetails(facility);
        }

        event.preventDefault(); // выключaем стaндaртную рoль элементa
    });

    $('[data-toggle="tooltip"]').tooltip();

    $(document).on('click', '#howCalculate', function (event) {
        event.preventDefault();
        $('#bs-modal').modal('hide');
        showLoader();
        var period = $("#monthAndYear").text();
        var facility = $(this).attr('name');
        var table = $(this).data('table');
        howCalculate(period, facility, table);
    });

    $(document).on('click', '#close-modal-calc', function (event) {
        event.preventDefault();
        $('#bs-modal').modal('show');
        $('#bs-modal-calc').modal('hide');
    });


    $('#deleteAll').on('click', function (event) {
        showLoader();
        event.preventDefault();
        var cashIdList = [];
        $('table#invFlows').find('> tbody').find('> tr').each(function () {
            $(this).find(':checkbox:checked').not(':disabled').each(function () {
                cashIdList.push($(this).closest('tr').attr('id'));
                $(this).closest('tr').remove();
            })
        });
        deleteCash(cashIdList);
    });

});

function howCalculate(period, facility, table) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
        "facility": facility,
        "period": period,
        "tableForSearch": table
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "showCalculating",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showBsModalCalc(data);
            $('#close-modal-calc').text('Закрыть');
            $('#bs-modal-calc').modal({
                show: true,
                backdrop: 'static',
                keyboard: false
            });
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        }
    });
}

function searchByMonths(tableForSearch) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
        //"pay" : $("#paysMonth").find("option:selected").text(),
        //"underFacility" : $("#underFacilitiesMonth").find("option:selected").text(),
        "facility": $("#facilities").find("option:selected").text(),
        "tableForSearch": tableForSearch
    });
    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "mainFlowsInv",
        data: JSON.stringify(search),
        dataType: 'text',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            display(data, tableForSearch);
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

function searchFlows(tableForSearch) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
        //"iYear" : $("#years").find("option:selected").text(),
        //"iMonth" : $("#months").find("option:selected").text(),
        //"pay" : $("#pays").find("option:selected").text(),
        //"underFacility" : $("#underFacilities").find("option:selected").text(),
        "facility": $("#facilities").find("option:selected").text(),
        "tableForSearch": tableForSearch
    });
    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "mainFlowsInv",
        data: JSON.stringify(search),
        dataType: 'text',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            display(data, tableForSearch);
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

function showDetails(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility": facility});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "sumdetails",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showBsModal(data);
            $('#bs-modal').modal('show');
        },
        error: function (e) {
            console.log(e);
        }
    });

}

function showPaysMonths(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility": facility});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "goPays",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showBsModal(data);
            $('#bs-modal').modal('show');
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        }
    });
}

function showPaysAll(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility": facility});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "goPaysAll",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showBsModal(data);
            $('#bs-modal').modal('show');
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        }
    });

}

function showArendaMonths(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility": facility});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "goArendaMonth",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showBsModal(data);
            $('#bs-modal').modal('show');
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        }
    });

}

function showArendaAll(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility": facility});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "goArendaAll",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showBsModal(data);
            $('#bs-modal').modal('show');
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        }
    });

}

function showRashodiMonth(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility": facility});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "goRashodiMonth",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showBsModal(data);
            $('#bs-modal').modal('show');
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        }
    });

}

function showRashodiAll(facility) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({"facility": facility});

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "goRashodiAll",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showBsModal(data);
            $('#bs-modal').modal('show');
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        }
    });

}

function show(data) {
    var json = JSON.stringify(data);
    $('#cash-details').html(json);
}

function showBsModal(data) {
    var json = JSON.stringify(data);
    $('#detailsModal').html(json);
}

function showBsModalCalc(data) {
    var json = JSON.stringify(data);
    $('#detailsModalCalc').html(json);
}

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

function display(data, tableForSearch) {
    var json = JSON.stringify(data, null, 4);
    $('#' + tableForSearch).html(json);
}

function loadFlowsAjax(action) {
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

function prepareFilter() {
    var facility = $('#fFacilities').find(':selected').text();
    var underFacility = $('#uFacilities').find(':selected').text();
    var investor = $('#investors').find(':selected').text();
    var dateBegin = $('#beginPeriod').val();
    var dateEnd = $('#endPeriod').val();
    dateBegin = dateBegin + '';
    dateEnd = dateEnd + '';

    if (facility !== 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlows tbody', 2, facility);
        apply_filter('#invFlows tbody', 3, underFacility);
        apply_filter('#invFlows tbody', 5, investor);
    } else if (facility !== 'Выберите объект' && underFacility === 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlows tbody', 2, facility);
    } else if (facility === 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlows tbody', 3, underFacility);
    } else if (facility === 'Выберите объект' && underFacility === 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlows tbody', 5, investor);
    } else if (facility === 'Выберите объект' && underFacility === 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlows tbody', 1, 'any');
    } else if (facility !== 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlows tbody', 2, facility);
        apply_filter('#invFlows tbody', 3, underFacility);
    } else if (facility !== 'Выберите объект' && underFacility === 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlows tbody', 2, facility);
        apply_filter('#invFlows tbody', 5, investor);
    } else if (facility === 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlows tbody', 3, underFacility);
        apply_filter('#invFlows tbody', 5, investor);
    }

    if (dateBegin === '' && dateEnd === '') {
        filters = [];
        apply_date_filter('#invFlows tbody', 1, min, max, "any");
    } else {
        filters = [];
        apply_date_filter('#invFlows tbody', 1, dateBegin, dateEnd, "not");
    }
}

function apply_date_filter(table, col, dateFrom, dateTo, any) {
    var fDate, tDate, cDate;
    var parts;
    dateFrom = dateFrom + '';
    dateTo = dateTo + '';
    if (dateFrom.length === 10) {
        parts = dateFrom.split("-");
        fDate = new Date(parseInt(parts[0]), parseInt(parts[1]) - 1, parseInt(parts[2]));
    } else {
        fDate = null;
    }
    if (dateTo.length === 10) {
        parts = dateTo.split("-");
        tDate = new Date(parseInt(parts[0]), parseInt(parts[1]) - 1, parseInt(parts[2]));
    } else {
        tDate = null;
    }

    filters[col] = any;

    if (filters[col] !== 'any') {
        $(table).find('tr td:nth-child(' + col + ')').each(function (i) {
            var checkDate = $(this).text();
            parts = checkDate.split(".");
            cDate = new Date(parseInt(parts[2]), parseInt(parts[1]) - 1, parseInt(parts[0]));
            if (fDate === null && tDate !== null) {
                if (cDate <= tDate && $(this).parent().data('passed')) {
                    $(this).parent().data('passed', true);
                } else {
                    $(this).parent().data('passed', false);
                }
            } else if (fDate !== null && tDate === null) {
                if (cDate >= fDate && $(this).parent().data('passed')) {
                    $(this).parent().data('passed', true);
                } else {
                    $(this).parent().data('passed', false);
                }
            } else {
                if ((cDate <= tDate && cDate >= fDate) && $(this).parent().data('passed')) {
                    $(this).parent().data('passed', true);
                } else {
                    $(this).parent().data('passed', false);
                }
            }
        });
    }

    $(table).find('tr').each(function (i) {
        if (!$(this).data('passed')) {
            $(this).hide();
        } else {
            $(this).show();
        }
    });
}

function findMinMaxDate(table, col, maxOrMin) {
    var max, min;
    var data = [];
    var cDate;
    $(table).find('tr td:nth-child(' + col + ')').each(function (i) {
        var checkDate = $(this).text();
        var parts = checkDate.split(".");
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
    var err = false;
    var cashes = [];
    var cash;
    var givedCash;
    var dateGived;
    var facility;
    var investor;
    var cashType = null;
    var newCashDetails = null;
    var investorsType;
    var underFacility;
    var dateReport;
    var shareKind;
    var sourceFacility;
    var sourceUnderFacility;
    var reinvestData = $('form#reInvestData');
    var reinvestIdList = [];
    var sourceFlowsId;
    var room;

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
    var tmpDate;
    var current;

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
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
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
    var reinvest = $('#reinvest');
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
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
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

function slideBox(message) {
    $('#slideBox').find('h4').html(message);
    setTimeout(function () {
        $('#slideBox').animate({'right': '52px'}, 500);
    }, 500);
    setTimeout(function () {
        $('#slideBox').animate({'right': '-300px'}, 500);
    }, 4000);
}

function getByPageNumber(facility, underFacility, investor, dateFrom, dateTo) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({
        "facilities": facility,
        "underFacilities": underFacility,
        "user": investor,
        "dateStart": dateFrom,
        "dateEnd": dateTo
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/paysToInv/1",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            closeLoader();
        },
        error: function (e) {
            closeLoader();
            console.log(e);
        }
    });
}