let filters = [];

let max;
let min;

let SalePaymentDTO = function () {}

SalePaymentDTO.prototype = {
    dateGiven: null,
    facilityId: 0,
    underFacilityId: 0,
    shareType: null,
    salePaymentsId: [],
    build: function (dateGiven, facilityId, underFacilityId, shareType, salePaymentsId) {
        this.dateGiven = dateGiven;
        this.facilityId = facilityId;
        this.underFacilityId = underFacilityId;
        this.shareType = shareType;
        this.salePaymentsId = salePaymentsId;
    }
}

let SalePaymentDivideDTO = function () {}

SalePaymentDivideDTO.prototype = {
    salePaymentId: 0,
    extractedSum: 0.0,
    build: function (salePaymentId, extractedSum) {
        this.salePaymentId = salePaymentId;
        this.extractedSum = extractedSum;
    }
}

jQuery(document).ready(function ($) {

    blockUnblockDropdownMenus('block', false);
    blockUnblockDivide();

    $('#delete-list').on('click', function (event) {
        let confirm = $('#confirm-delete');
        confirm.modal('show')
        confirm.find('#action').attr('data-action', 'delete-list')
    });

    $(document).on('click', '#accept-delete', function (event) {
        let confirm = $('#confirm-delete');
        confirm.modal('hide')
        let action = confirm.find('#action').attr('data-action')
        if (action === 'delete-list') {
            showLoader();
            event.preventDefault();
            let cashIdList = [];
            $('table#salePayments').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:checked').not(':disabled').each(function () {
                    cashIdList.push($(this).closest('tr').attr('id'));
                    $(this).closest('tr').remove();
                })
            });
            deleteList(cashIdList);
        } else {
            deleteAll()
        }
    })

    $(document).on('click', '#upload', function (event) {
        event.preventDefault();
        upload();
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
        prepareReinvestSale();
    });

    $(document).on('click', '#bth-clear', function (e) {
        e.preventDefault();
        $('#fFacilities').find('option:eq(0)').prop('selected', true);
        $('#uFacilities').find('option:eq(0)').prop('selected', true);
        $('#investors').find('option:eq(0)').prop('selected', true);
        $('#beginPeriod').val('');
        $('#endPeriod').val('');
        window.location.href = window.location.pathname;
    });

    $(document).on('click', '#aDivide', function (e) {
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
        let divideSum = parseFloat(divideModal.find('#divideCash').val()) || 0;
        let divideCashErr = $('#divideCashErr');
        let flowMaxSum = parseFloat(divideModal.find('#flowMaxSum').val()) || 0;
        if (divideSum <= 0 || divideSum > flowMaxSum) {
            divideCashErr.addClass('d-block');
            return false;
        } else {
            divideCashErr.removeClass('d-block');
        }
        divideCash(flowId, divideSum);
    });

    $(document).on('change', ':checkbox', function () {
        let id = $(this).attr('id');
        let noDivide;
        noDivide = $(this).closest('tr').find('> td:eq(1)').text().length > 0 && $(this).prop('checked') === true;
        if (typeof id === 'undefined') {
            let cnt = checkChecked();
            if (cnt > 0) {
                blockUnblockDropdownMenus('unblock', noDivide);
            } else {
                blockUnblockDropdownMenus('block', noDivide);
            }
        }
    });

    $('#reinvestAll').addClass('disabled');
    max = findMinMaxDate('#salePayments tbody', 5, "max");
    min = findMinMaxDate('#salePayments tbody', 5, "min");
    populateStorageUnderFacilities('uFacilities');
    populateStorageRooms();

    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 0;
        $('#pageSize').val(pageSize);
        $('#filter-form').submit();
    });

    $('table#salePayments').find('> tbody').find('> tr').each(function (i) {
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

    $(document).on('click', '#checkIt', function () {
        let checkIt = $('#checkIt');
        let checked = checkIt.attr('data-checked');
        if (checked === 'true') {
            checkIt.attr('data-checked', 'false');
            checkIt.text('Выделить всё')
            checkIt.removeClass('btn-danger').addClass('btn-primary')
            blockUnblockDropdownMenus('block', false);
            $('table#salePayments').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:not(:disabled)').prop('checked', false);
            });
        } else {
            checkIt.attr('data-checked', 'true');
            checkIt.text('Очистить выбор')
            checkIt.removeClass('btn-primary').addClass('btn-danger')
            $('table#salePayments').find('> tbody').find('> tr').each(function () {
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

    $('#fFacilities').change(function () {
        let facility = $(this).find('option:selected').attr('id');
        getUFFromLS(facility, 'uFacilities');
    });

    $(document).on('change', '#srcFacility', function () {
        let facility = $(this).val();
        getUnderFacilitiesFromLocalStorage(facility, 'srcUnderFacility');
    });

    $(document).on('change', '#srcUnderFacility', function () {
        let room = $(this).find('option:selected').attr('id');
        getRoomsFromLocalStorage(room);
    });

    $('#deleteAll').on('click', function (event) {
        event.preventDefault();
        if (linkHasClass($('#deleteAll'))) return false;
        showLoader();
        let cashIdList = [];
        $('table#salePayments').find('> tbody').find('> tr').each(function () {
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
        let chk = $('table#investorsCash').find('> tbody').find('> tr').find(':checkbox:checked:not(:disabled)');
        let facilityId = chk.closest('td').parent().find('td:eq(0)').attr('data-facility-id');

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

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
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

function prepareReinvestSale() {
    let dateGiven;
    let facilityId;
    let underFacilityId;
    let shareType;
    let reinvestIdList = [];

    dateGiven = $('#dateGiven').val();

    if (dateGiven.length === 0) {
        $('#dateGivenErr').addClass('d-block');
        return false;
    } else {
        $('#dateGivenErr').removeClass('d-block');
        dateGiven = new Date(dateGiven).getTime();
    }

    facilityId = $('#srcFacility').find('option:selected').val()

    if (facilityId === "0") {
        $('#facilityErr').addClass('d-block');
        return false;
    } else {
        $('#facilityErr').removeClass('d-block');
    }

    underFacilityId = $('#srcUnderFacility').find('option:selected').val()

    if (underFacilityId === "0" || underFacilityId === 'Без подобъекта') {
        $('#underFacilityErr').addClass('d-block');
        return false;
    } else {
        $('#underFacilityErr').removeClass('d-block');
    }

    shareType = $('#shareTypeName').find('option:selected').val();
    let shareTypeErr = $('#shareTypeErr');
    if (shareType.indexOf('Не определена') >= 0) {
        shareTypeErr.addClass('d-block');
        return false;
    } else {
        shareTypeErr.removeClass('d-block');
    }

    switch (shareType) {
        case 'Основная доля':
            shareType = 'MAIN'
            break
        case 'Сверхдоля':
            shareType = 'OVER'
            break
        default:
            shareTypeErr.addClass('d-block');
            return false;
    }

    let current;

    $('#reInvestModal').modal('hide');
    $('#reinvestAll').prop('disabled', true);
    $('table#salePayments').find('> tbody').find('> tr').each(function () {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            $(this).closest('tr').find('input:checkbox').prop('disabled', true);
            current = $(this).closest('tr');
            reinvestIdList.push(current.attr('id'));
            $('#' + current.attr('id') + ' input:checkbox').prop('disabled', true);
        });
    });
    let salePaymentDTO = new SalePaymentDTO();
    salePaymentDTO.build(dateGiven, facilityId, underFacilityId, shareType, reinvestIdList);
    reinvestSalePayments(salePaymentDTO);
}

function reinvestSalePayments(salePaymentDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "./sale/reinvest",
        data: JSON.stringify(salePaymentDTO),
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
            showPopup(e.error)
            console.log(e.error);
        },
        done: function (e) {
            closeLoader();
            enableSearchButton(true);
        }
    });
}

function checkChecked() {
    return $('table#salePayments').find('[type="checkbox"]:checked:not(:disabled)').length;
}

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

function blockUnblockDropdownMenus(blockUnblock, noDivide) {
    let reinvest = $('#reinvest');
    switch (blockUnblock) {
        case 'block':
            reinvest.find('> li').each(function () {
                $(this).closest('li').removeClass('active').addClass('disabled');
            });
            break;
        case 'unblock':
            reinvest.find('> a').each(function () {
                if ($(this).text() === 'Массовое разделение сумм' && noDivide) {
                    $(this).addClass('disabled');
                } else {
                    $(this).removeClass('disabled');
                }
            });
            break;
    }
}

function deleteList(cashIdList) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let salePaymentDTO = ({
        salePaymentsId: cashIdList
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "sale/delete/checked",
        data: JSON.stringify(salePaymentDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            closeLoader();
            showPopup(data.message);
            $('#bth-search').click()
        })
        .fail(function (e) {
            closeLoader();
            showPopup(e.error);
            console.log(e.error);
        })
        .always(function () {
            closeLoader();
            console.log('Закончили!');
        });
}

function linkHasClass(link) {
    if (link.hasClass('disabled')) return true;
}

function divideCash(salePaymentId, extractedSum) {
    showLoader();
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let divideSaleDTO = new SalePaymentDivideDTO()
    divideSaleDTO.build(salePaymentId, extractedSum)

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "sale/divide",
        data: JSON.stringify(divideSaleDTO),
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
    $('table#salePayments').find('> tbody').find('> tr').each(function () {
        if ($(this).find(':checkbox').attr('checked') === 'checked') {
            $(this).find('td:last-child').find('div.dropdown-menu').find('#aDivide').addClass('disabled');
        } else {
            $(this).find('td:last-child').find('div.dropdown-menu').find('#aDivide').removeClass('disabled');
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
        url: "sale/upload",
        data: data,
        dataType: 'json',
        timeout: 1000000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message);
            if (data.status !== 412) {
                $('#bth-search').click()
            } else {
                $('#file').val('')
            }
        },
        error: function (e) {
            closeLoader();
            showPopup(e.error);
            disconnect();
        }
    });
}

function releaseOperations(tableId, what) {
    switch (what) {
        case 'block':
            $('#bth-search').click();
            break;
        case 'unblock':
            $('table#' + tableId).find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:disabled').each(function () {
                    $(this).closest('tr').find('#aDivide').removeClass('disabled');
                    $(this).removeAttr('checked').removeAttr('disabled');
                })
            });
            $('#unblock_operations').removeClass('btn-danger').addClass('btn-success').text('Заблокировать операции');
            break;
    }
}
