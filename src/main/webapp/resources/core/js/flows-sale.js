var filters = [];

var max;
var min;

jQuery(document).ready(function ($) {

    blockUnblockDropdownMenus('block', false);
    blockUnblockDivide();
    getFiltersFromLS((window.location.pathname + '').split("/")[1]);
    $('#msg-modal').on('shown.bs.modal', function () {
        // if data-timer attribute is set use that, otherwise use default (7000)
        var timer = 3000;
        $(this).delay(timer).fadeOut(200, function () {
            $(this).modal('hide');
        });
    });

    $(document).on('click', '#unblock_operations', function () {
        let tableId = $(this).data('table-id');
        if ($(this).text() === 'Разблокировать операции') {
            releaseOperations(tableId, 'unblock');
        } else {
            releaseOperations(tableId, 'block');
        }
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

    $(document).on('click', '#liDivide', function (e) {
        e.preventDefault();
        if (linkHasClass($(this))) return false;
        let divideModal = $('#divideModal');
        let flowId = $(this).parent().parent().parent().parent().attr('id');
        let flowMaxSum = $(this).parent().parent().parent().parent().find('> td:eq(9)').data('givedCash');
        divideModal.modal('show');
        divideModal.find('#divideId').val(flowId);
        divideModal.find('#flowMaxSum').val(flowMaxSum);
    });

    $(document).on('click', '#cancelDivide', function (e) {
        e.preventDefault();
        $('#divideModal').modal('hide');
    });

    $(document).on('submit', '#divideData', function (e) {
        e.preventDefault();
        let divideModal = $('#divideModal');
        let flowId = parseFloat(divideModal.find('#divideId').val());
        let divideSum = parseFloat(divideModal.find('#divideCash').val());
        let divideCashErr = $('#divideCashErr');
        let flowMaxSum = parseFloat(divideModal.find('#flowMaxSum').val());
        if (divideSum <= 0 || divideSum > flowMaxSum) {
            divideCashErr.show();
            return false;
        } else {
            divideCashErr.hide();
        }
        divideCash(flowId, divideSum);
    });

    $(document).on('change', ':checkbox', function () {
        var id = $(this).attr('id');
        var noDivide;
        noDivide = $(this).closest('tr').find('> td:eq(1)').text().length > 0 && $(this).prop('checked') === true;
        if (typeof id === 'undefined') {
            var cnt = checkChecked();
            if (cnt > 0) {
                blockUnblockDropdownMenus('unblock', noDivide);
            } else {
                blockUnblockDropdownMenus('block', noDivide);
            }
        }
    });

    $('#reinvestAll').addClass('disabled');
    max = findMinMaxDate('#invFlowsSale tbody', 5, "max");
    min = findMinMaxDate('#invFlowsSale tbody', 5, "min");
    populateStorageUnderFacilities('uFacilities');
    populateStorageRooms();
    $("#search-form").submit(function (event) {
        enableSearchButton(false);
        event.preventDefault();
        searchFlows("allInvFlows");
    });

    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 0;
        $('#pageSize').val(pageSize);
        $('#filter-form').submit();
    });

    $("#filter-form").submit(function () {
        enableSearchButton(false);
        populateFilters((window.location.pathname + '').split("/")[1]);
        enableSearchButton(true);
    });

    $('table#invFlowsSale').find('> tbody').find('> tr').each(function (i) {
        $(this).data('passed', true);
    });

    $('#reinvestAll').on('click', function (event) {
        event.preventDefault();
        if (linkHasClass($('#reinvestAll'))) return false;
        $('#reInvestModal').modal({
            show: true
        })
    });

    $(document).on('click', 'a#cancelReinvest', function (event) {
        event.preventDefault();
        $('#reInvestModal').modal("hide");
    });

    $(document).on('change', '#checkIt', function () {
        var checked = $('#checkIt').prop('checked');
        if (!checked) {
            blockUnblockDropdownMenus('block', false);
            $('table#invFlowsSale').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:not(:disabled)').prop('checked', false);
            });
        } else {
            $('table#invFlowsSale').find('> tbody').find('> tr').each(function () {
                if (!$(this).data('passed')) {
                    $(this).find(':checkbox:not(:disabled)').prop('checked', false);
                } else {
                    $(this).find(':checkbox:not(:disabled)').prop('checked', function () {
                        return checked;
                    });
                }
            });
            blockUnblockDropdownMenus('unblock', false);
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
        let facility = $(this).find('option:selected').attr('id');
        getUFFromLS(facility, 'uFacilities');
    });

    $(document).on('change', '#srcFacilities', function () {
        var facility = $(this).val();
        getUnderFacilitiesFromLocalStorage(facility, 'srcUnderFacilities');
    });

    $(document).on('change', '#srcUnderFacilities', function () {
        var room = $(this).find('option:selected').attr('id');
        getRoomsFromLocalStorage(room);
    });

    $(document).on('click', '#loadInvFlowsSaleAjax', function (event) {
        event.preventDefault();
        loadFlowsAjax("loadFlowsSaleAjax");
    });

    $('#deleteAll').on('click', function (event) {
        event.preventDefault();
        if (linkHasClass($('#deleteAll'))) return false;
        showLoader();
        var cashIdList = [];
        $('table#invFlowsSale').find('> tbody').find('> tr').each(function () {
            $(this).find(':checkbox:checked').not(':disabled').each(function () {
                cashIdList.push($(this).closest('tr').attr('id'));
                $(this).closest('tr').remove();
            })
        });
        deleteCash(cashIdList);
    });

    $('#divideAll').on('click', function (event) {
        event.preventDefault();
        if (linkHasClass($('#divideAll'))) return false;
        var chk = $('table#investorsCash').find('> tbody').find('> tr').find(':checkbox:checked:not(:disabled)');
        var facilityId = chk.closest('td').parent().find('td:eq(0)').attr('data-facility-id');

        getUnderFacilitiesFromLocalStorage(
            facilityId,
            'underFacilities');
        getUnderFacilitiesFromLocalStorage(
            facilityId,
            'underFacilitiesList');
        $('#underFacilitiesList').find('option:contains(Выберите подобъект)').remove();
        $('#divideModal').modal({
            show: true
        });
    });
});

function show(data) {
    var json = JSON.stringify(data);
    $('#cash-details').html(json);
}

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

function display(data, tableForSearch) {
    var json = JSON.stringify(data, null, 4);
    $('#' + tableForSearch).html(json);
}

function loadFlowsAjax(action) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let data = new FormData();
    let fileBuckets = [];
    $.each($('#file')[0].files, function (k, value) {
        data.append(k, value);
        fileBuckets.push(k, value);
    });
    if (fileBuckets.length === 0) {
        return;
    }

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
            let $message = $('#msg');
            let error = data.error;
            if (error === null) {
                $message.css("color", "black")
                showPopup(data.message);
            } else {
                $message.css("color", "red")
                showPopup(data.error);w
            }
            closeLoader();
        },
        error: function(error) {
            showPopup(error.responseText);
            closeLoader();
        }
    });

}

function prepareFilter() {
    let facility = $('#fFacilities').find(':selected').text();
    let underFacility = $('#uFacilities').find(':selected').text();
    let investor = $('#investors').find(':selected').text();
    let dateBegin = $('#beginPeriod').val();
    let dateEnd = $('#endPeriod').val();
    dateBegin = dateBegin + '';
    dateEnd = dateEnd + '';

    if (facility !== 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlowsSale tbody', 1, facility);
        apply_filter('#invFlowsSale tbody', 11, underFacility);
        apply_filter('#invFlowsSale tbody', 2, investor);
    } else if (facility !== 'Выберите объект' && underFacility === 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlowsSale tbody', 1, facility);
    } else if (facility === 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlowsSale tbody', 11, underFacility);
    } else if (facility === 'Выберите объект' && underFacility === 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlowsSale tbody', 2, investor);
    } else if (facility === 'Выберите объект' && underFacility === 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlowsSale tbody', 1, 'any');
    } else if (facility !== 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlowsSale tbody', 1, facility);
        apply_filter('#invFlowsSale tbody', 11, underFacility);
    } else if (facility !== 'Выберите объект' && underFacility === 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlowsSale tbody', 1, facility);
        apply_filter('#invFlowsSale tbody', 2, investor);
    } else if (facility === 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#invFlowsSale tbody', 11, underFacility);
        apply_filter('#invFlowsSale tbody', 2, investor);
    }

    if (dateBegin === '' && dateEnd === '') {
        filters = [];
        apply_date_filter('#invFlowsSale tbody', 12, min, max, "any");
    } else {
        filters = [];
        apply_date_filter('#invFlowsSale tbody', 12, dateBegin, dateEnd, "not");
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
    let err = false;
    let cashes = [];
    let cash;
    let givedCash;
    let dateGived;
    let facility;
    let investor;
    let newCashDetails = null;
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
        name: $('#srcFacilities').find('option:selected').text()
    };

    underFacility = {
        id: reinvestData.find('#srcUnderFacilities').children(":selected").attr("id"),
        name: $('#srcUnderFacilities').find('option:selected').text()
    };

    if (facility.name.indexOf('Выберите объект') >= 0) {
        $('#facilityErr').css('display', 'block');
        err = true;
    } else {
        $('#facilityErr').css('display', 'none');
        err = false;
    }

    if (underFacility.name.indexOf('Выберите подобъект') >= 0) {
        $('#underFacilityErr').css('display', 'block');
        err = true;
    } else {
        $('#underFacilityErr').css('display', 'none');
        err = false;
    }

    shareKind = $('#shareTypeName').find('option:selected').text();

    if (shareKind.indexOf('Не определена') >= 0) {
        $('#shareKindErr').css('display', 'block');
        err = true;
    } else {
        $('#shareKindErr').css('display', 'none');
        err = false;
    }

    switch (shareKind) {
        case 'Основная':
            shareKind = 'MAIN'
            break
        case 'Сверхдоля':
            shareKind = 'OVER'
    }

    if (err) {
        closeLoader();
        return false;
    }
    var current;

    $('#reInvestModal').modal('hide');
    $('#reinvestAll').prop('disabled', true);
    $('table#invFlowsSale').find('> tbody').find('> tr').each(function () {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            $(this).closest('tr').find('input:checkbox').prop('disabled', true);
            current = $(this).closest('tr');
            sourceFlowsId = current.attr('id');
            reinvestIdList.push(current.attr('id'));
            $('#' + current.attr('id') + ' input:checkbox').prop('disabled', true);

            sourceFacility = {
                id: current.children('td:eq(0)').attr('data-facility-id'),
                name: current.children('td:eq(0)').text()
            };

            sourceUnderFacility = {
                id: current.children('td:eq(10)').attr('data-under-facility-id'),
                name: current.children('td:eq(10)').text()
            };

            room = null;

            dateReport = current.children('td:eq(11)').attr('data-date-sale');

            investor = {
                id: current.children('td:eq(1)').attr('data-investor-id'),
                login: current.children('td:eq(1)').text()
            };

            givedCash = current.children('td:eq(9)').attr('data-gived-cash');

            cash = {
                id: null,
                givedCash: givedCash,
                dateGivedCash: dateGived,
                facility: facility,
                investor: investor,
                newCashDetail: newCashDetails,
                underFacility: underFacility,
                dateClosingInvest: null,
                typeClosing: null,
                shareType: shareKind,
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
        what: "sale"
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
    return $('table#invFlowsSale').find('[type="checkbox"]:checked:not(:disabled)').length;
}

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

function blockUnblockDropdownMenus(blockUnblock, noDivide) {
    var reinvest = $('#reinvest');
    switch (blockUnblock) {
        case 'block':
            reinvest.find('> li').each(function () {
                $(this).closest('li').removeClass('active').addClass('disabled');
            });
            break;
        case 'unblock':
            reinvest.find('> li').each(function () {
                if ($(this).find($(":first-child")).text() === 'Массовое разделение сумм' && noDivide) {
                    $(this).closest('li').removeClass('active').addClass('disabled');
                } else {
                    $(this).closest('li').removeClass('disabled').addClass('active');
                }
            });
            break;
    }
}

function deleteCash(cashIdList) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
        cashIdList: cashIdList,
        what: "sale"
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

function linkHasClass(link) {
    if (link.hasClass('disabled')) return true;
}

function divideCash(flowId, divideSum) {
    showLoader();
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({
        divideSumId: flowId,
        divideSum: divideSum
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/divideFlows",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            closeLoader();
            let sum = parseInt(data.message, 10);
            if (sum === 0)  $('#divideModal').modal('hide');
            $('#flowMaxSum').val(data.message);
            $('#divideCash').val(data.message);
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Закончили!');
        });
}

function blockUnblockDivide() {
    $('table#invFlowsSale').find('> tbody').find('> tr').each(function () {
        if ($(this).find(':checkbox').attr('checked') === 'checked') {
            $(this).find('td:last-child').find('ul.dropdown-menu').find('#liDivide').removeClass('active').addClass('disabled');
        } else {
            $(this).find('td:last-child').find('ul.dropdown-menu').find('#liDivide').removeClass('disabled').addClass('active');
        }
    });
}


function getUFFromLS(facilityId, uFacilitiesId) {
    let underFacilities;
    underFacilities = JSON.parse(localStorage.getItem('uf'));
    let option;

    let options;
    if (underFacilities === null) populateStorageUnderFacilities(uFacilitiesId);
    if (parseInt(facilityId) === 0) {
        options = underFacilities.map(function (item) {

            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parent-id', item.facilityId);
            option.setAttribute('value', item.underFacility);
            option.innerText = item.underFacility;

            return option;

        });
    } else {
        options = underFacilities.filter(function (item) {
            return item.facilityId === parseInt(facilityId);
        }).map(function (item) {

            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parent-id', item.facilityId);
            option.setAttribute('value', item.underFacility);
            option.innerText = item.underFacility;

            return option;

        });

        option = document.createElement('option');
        option.setAttribute('id', "0");
        option.setAttribute('value', 'Без подобъекта');
        option.innerText = 'Без подобъекта';
        options.unshift(option);

    }

    $('#' + uFacilitiesId)
        .find('option')
        .remove()
        .end()
        .append(options)
        .selectpicker('refresh');
}
