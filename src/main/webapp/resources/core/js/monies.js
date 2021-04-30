let filters = [];

let OperationEnum = {
    CREATE: 'CREATE',
    UPDATE: 'UPDATE',
    CLOSE: 'CLOSE',
    RESALE: 'RESALE',
    CASHING: 'CASHING',
    REINVEST: 'REINVEST',
    DOUBLE: 'DOUBLE',
    DELETE: 'DELETE',
    properties: {
        CREATE: {
            url: 'create'
        },
        UPDATE: {
            url: '../update'
        },
        CLOSE: {
            url: '../close/one'
        },
        RESALE: {
            url: '../close/resale'
        },
        CASHING: {
            url: '../close/cashing/one'
        },
        REINVEST: {
            url: '../close/reinvest/one'
        },
        DELETE: {
            url: '../delete/list'
        }
    }
}

Object.freeze(OperationEnum)

jQuery(document).ready(function ($) {

    let operation = $('#operation').val();

    $('#all').on('change', function () {
        $('#bth-search').click()
    })

    $('#not-accepted').on('change', function () {
        $('#accepted').val(!$(this).attr('checked'))
        $('#bth-search').click()
    })

    $('#submit').on('click', function (e) {
        if (operation !== OperationEnum.DOUBLE) {
            e.preventDefault()
            save(operation)
        }
    })

    $('#accept-all').on('click', function (e) {
        e.preventDefault()
        let acceptedIds = []
        let checked = $('table#monies').find('> tbody').find('> tr').find(':checkbox:checked:not(:disabled)');
        checked.map(function () {
            acceptedIds.push($(this).data('money-id'))
        })
        let acceptedMoneyDTO = new AcceptedMoneyDTO()
        acceptedMoneyDTO.build(acceptedIds)
        acceptMonies(acceptedMoneyDTO)
    })

    blockActions();
    $(document).on('mousedown', '#underFacilitiesList > option', function (e) {
        e.preventDefault();
        this.selected = !this.selected;
    });

    $(document).on('mousedown', '#underFacilities > option', function (e) {
        e.preventDefault();
        this.selected = !this.selected;
    });

    $(document).on('click', '#unblock_operations', function () {
        let tableId = $(this).data('table-id');
        if ($(this).text() === 'Разблокировать операции') {
            releaseOperations(tableId, 'unblock');
        } else {
            releaseOperations(tableId, 'block');
        }
    });

    $(document).on('click', '#bth-clear', function (e) {
        e.preventDefault();
        $('#fFacilities').selectpicker('deselectAll');
        $('#uFacilities').selectpicker('deselectAll');
        $('#investors').selectpicker('deselectAll');
        $('.selectpicker').selectpicker('refresh');
        $('#beginPeriod').val('');
        $('#endPeriod').val('');
        $('#bth-search').click()
    });

    $(document).on('click', '.disabled', function (e) {
        e.preventDefault();
    });

    let newCash = $('#newCash').val();
    let edit = $('#edit').val();
    let doubleCash = $('#doubleCash').val();
    let closeCash = $('#closeCash').val();
    let what;
    if (newCash === 'true') {
        what = 'newCash'
    } else if (edit === 'true') {
        what = 'edit'
    } else if (doubleCash === 'true') {
        what = 'doubleCash'
    } else if (closeCash === 'true') {
        what = 'closeCash'
    } else {
        what = null
    }

    populateStorageUnderFacilities('uFacilities');
    blockMenus();

    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 1;
        $('#pageSize').val(pageSize);
        $('#search-form').submit();
    });

    $('#investors').selectpicker({
        actionsBox: true,
        selectAllText: 'Выбрать всё',
        deselectAllText: 'Очистить'
    }).on('changed.bs.select', function () {
        let options = $("#investors option");
        options.sort(function (a, b) {
            if ($(a).attr('data-lastName') > $(b).attr('data-lastName')) return 1;
            else if ($(a).attr('data-lastName') < $(b).attr('data-lastName')) return -1;
            else return 0;
        });
        $("#investors").empty().append(options);
        $('#investors option:selected').prependTo('#investors');
        $(this).selectpicker('refresh');
    });

    $('#fFacilities').selectpicker({
        actionsBox: true,
        selectAllText: 'Выбрать всё',
        deselectAllText: 'Очистить'
    }).on('changed.bs.select', function () {
        let options = $("#fFacilities option");
        $("#fFacilities").empty().append(options);
        $('#fFacilities option:selected').prependTo('#fFacilities');
        $(this).selectpicker('refresh');
    });

    /**

     МАССОВОЕ РЕИНВЕСТИРОВАНИЕ НАЧАЛО

     **/

    blockUnblockDropdownMenus('block', false);

    $(document).on('change', ':checkbox', function () {
        let id = $(this).attr('id');
        let noDivide;
        noDivide = $(this).closest('tr').find('> td:eq(1)').text().length > 0 && $(this).prop('checked') === true &&
            ($(this).closest('tr').find('> td:eq(1)').text().indexOf('_Целиком') < 0);
        if (typeof id === 'undefined') {
            let cnt = checkChecked();
            if (cnt > 0) {
                blockUnblockDropdownMenus('unblock', noDivide);
            } else {
                blockUnblockDropdownMenus('block', noDivide);
            }
        }
    });

    $('table#monies').find('> tbody').find('> tr').each(function () {
        $(this).data('passed', true);
    });

    $('#reinvestAll').on('click', function (event) {
        let allModalForm = $('#all-modal');
        event.preventDefault();
        if (linkHasClass($('#reinvestAll'))) return false;
        allModalForm.modal({
            show: true
        });
        allModalForm.find('input#action').attr('data-action', 'reinvest');
        allModalForm.find('input#action').val('Реинвестировать')
        $('#dateClosingRow').removeClass('d-none');
        $('#facilitiesRow').removeClass('d-none');
        $('#underFacilityRow').removeClass('d-none');
        $('#shareTypeNameRow').removeClass('d-none');

        $('#underFacilitiesListRow').addClass('d-none');
        $('#real-date').addClass('d-none');
        $('#typeClosingRow').addClass('d-none');
        $('#buyerRow').addClass('d-none');
    });

    $('#closeAll').on('click', function (event) {
        let allModalForm = $('#all-modal');
        event.preventDefault();
        if (linkHasClass($('#closeAll'))) return false;
        allModalForm.modal({
            show: true
        });
        allModalForm.find('input#action').attr('data-action', 'close');
        allModalForm.find('input#action').val('Закрыть')
        $('#dateClosingRow').removeClass('d-none');
        $('#real-date').removeClass('d-none');
        $('#typeClosingRow').removeClass('d-none');
        let typeClosing = $('#typeClosing').find('option:selected').text();
        if (typeClosing === 'Перепродажа доли') {
            $('#buyerRow').removeClass('d-none');
        } else {
            $('#buyerRow').addClass('d-none');
        }
        $('#shareTypeNameRow').addClass('d-none');
        $('#facilitiesRow').addClass('d-none');
        $('#underFacilityRow').addClass('d-none');
        $('#underFacilitiesListRow').addClass('d-none');
        allModalForm.find('#typeClosing').find('option:contains(Реинвестирование)').remove()
        allModalForm.find('#typeClosing').find('option:contains(Вывод_комиссия)').remove()
        $('#typeClosing').selectpicker('refresh')
    });


    $('#divideAll').on('click', function (event) {
        let allModalForm = $('#all-modal');
        event.preventDefault();
        if (linkHasClass($('#divideAll'))) return false;
        let chk = $('table#monies').find('> tbody').find('> tr').find(':checkbox:checked:not(:disabled)');
        let facilityId = chk.closest('td').parent().find('td:eq(0)').attr('data-facility-id');

        getUnderFacilitiesFromLocalStorage(
            facilityId,
            'srcUnderFacilities');
        getUnderFacilitiesFromLocalStorage(
            facilityId,
            'underFacilitiesList');
        let underFacilitiesList = $('#underFacilitiesList')
        underFacilitiesList.find('option:contains(Выберите подобъект)').remove();
        underFacilitiesList.selectpicker('refresh')
        allModalForm.modal({
            show: true
        });
        allModalForm.find('input#action').attr('data-action', 'divide')
        allModalForm.find('input#action').val('Разделить')
        $('#underFacilityRow').removeClass('d-none');
        $('#underFacilitiesListRow').removeClass('d-none');
        $('#dateClosingRow').addClass('d-none');
        $('#real-date').addClass('d-none');
        $('#typeClosingRow').addClass('d-none');
        $('#facilitiesRow').addClass('d-none');
        $('#shareTypeNameRow').addClass('d-none');
        $('#buyerRow').addClass('d-none');
    });

    $('#deleteAll').on('click', function (event) {
        event.preventDefault();
        $('#confirm-delete').modal('show');
    })
    $('#accept-delete').on('click', function (event) {
        $('#confirm-delete').modal('hide');
        if (linkHasClass($('#deleteAll'))) return false;
        let cashIdList = [];
        $('table#monies').find('> tbody').find('> tr').each(function () {
            $(this).find(':checkbox:checked').not(':disabled').each(function () {
                cashIdList.push($(this).closest('tr').attr('id'));
                $(this).closest('tr').remove();
            })
        });
        $('#msg').html('Начинаем удаление денег...');
        $('#msg-modal').modal('show');
        connect();
        deleteCash(cashIdList);
    });

    $(document).on('change', '#checkIt', function () {
        const checked = $('#checkIt').prop('checked');
        let noDivide = false;
        if (!checked) {
            blockUnblockDropdownMenus('block', noDivide);
            $('table#monies').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:not(:disabled)').prop('checked', false);
            });
        } else {
            $('table#monies').find('> tbody').find('> tr').each(function () {
                if (!$(this).data('passed')) {
                    $(this).find(':checkbox:not(:disabled)').prop('checked', false);
                } else {
                    if ($(this).find('td:eq(8)').text() === '') {
                        $(this).find(':checkbox:not(:disabled)').prop('checked', function () {
                            if (!noDivide) {
                                noDivide = ($(this).closest('tr').find('> td:eq(1)').text().length > 0 &&
                                    ($(this).closest('tr').find('> td:eq(1)').text().indexOf('_Целиком') < 0));
                            }
                            return checked;
                        });
                    }
                }
            });
            blockUnblockDropdownMenus('unblock', !noDivide);
        }
    });

    $('#action').on('click', function (event) {
        event.preventDefault();
        let action = $('input#action').attr('data-action');
        switch (action) {
            case 'reinvest':
                prepareReinvestCash();
                break
            case 'divide':
                prepareDivideCash();
                break
            case 'close':
                if ($('#typeClosing').find(':selected').text() === 'Перепродажа доли') {
                    hasError.errors = false;
                    hasError.saleShareFunc();
                }
                if (!hasError.errors) {
                    prepareCloseCash();
                }
                return false;
        }
    });

    $(document).on('submit', '#divideData', function (event) {
        event.preventDefault();
        prepareDivideCash();
    });

    $(document).on('submit', '#closeData', function (event) {
        event.preventDefault();
        if ($('#typeClosing').find(':selected').text() === 'Перепродажа доли') {
            hasError.errors = false;
            hasError.saleShareFunc();
        }

        if (!hasError.errors) {
            prepareCloseCash();
        }
        return false;
    });
    /**

     МАССОВОЕ РЕИНВЕСТИРОВАНИЕ КОНЕЦ

     **/
    if (newCash === 'true') {
        moveFields('newCash');
    } else if (edit === 'true') {
        moveFields('edit');
    } else if (doubleCash === 'true') {
        moveFields('doubleCash');
    } else if (closeCash === 'true') {
        moveFields('closeCash');
    }

    let url = window.location.href;
    if (url.indexOf('money/create') >= 0 || url.indexOf('money/edit') >= 0 || url.indexOf('double-cash') >= 0 || url.indexOf('close-cash') >= 0) {
        populateStorageUnderFacilities('underFacilities');
    } else {
        populateStorageUnderFacilities('uFacilities');
    }
    if (url.indexOf('double-cash') >= 0) {
        $('#doubleCash').val('true');
    }

    disableFields(operation);

    $('.investorPicker').change(function () {
        let selectedText = $(this).find('option:selected').map(function () {
            return $(this).text();
        }).get().join(',');
        let invArray = selectedText.split(",");
        if (invArray.length === 1) {
            $('#cashingSubmit').removeAttr('disabled');
        } else {
            $('#cashingSubmit').attr('disabled', 'true');
        }
    });

    let hasError = {
        'saleShareFunc': function () {
            let dateClosingInfo = $('#dateCloseErr');
            let typeClosingInfo = $('#buyerErr');
            let investorBuyer = $('#buyer');
            if (investorBuyer.css('display') === 'block' && investorBuyer.find(':selected').text() === 'Выберите инвестора') {
                hasError.errors = true;
                typeClosingInfo.html('Необходимо выбрать инвестора').show();
            } else {
                typeClosingInfo.html('').hide();
            }
            if ($('#dateClose').val().length < 10) {
                hasError.errors = true;
                dateClosingInfo.html('Необходимо указать дату').show();
            } else {
                dateClosingInfo.html('').hide();
            }
        },
        'investorBuyerFunc': function () {
            let dateClosingInfo = $('#dateCloseError');
            let typeClosingInfo = $('#investorBuyerErr');
            let investorBuyer = $('#investorBuyer');
            if (investorBuyer.css('display') === 'block' && investorBuyer.find(':selected').text() === 'Выберите инвестора') {
                hasError.errors = true;
                typeClosingInfo.html('Необходимо выбрать инвестора').show();
            } else {
                typeClosingInfo.html('').hide();
            }
            if ($('#dateCloseInv').val().length < 10) {
                hasError.errors = true;
                dateClosingInfo.html('Необходимо указать дату').show();
            } else {
                dateClosingInfo.html('').hide();
            }
        },
        'reFacilityFunc': function () {

            let reFacilityInfo = $('#reFacilityErr');
            let reFacility = $('#reFacility');
            if (reFacility.css('display') === 'block' && reFacility.find(':selected').text() === 'Выберите объект') {
                hasError.errors = true;
                reFacilityInfo.html('Необходимо выбрать объект').show();
            } else {
                reFacilityInfo.html('').hide();
            }
        },
        'reInvestDateFunc': function () {

            let reInvestDateInfo = $('#dateCloseError');
            let reInvestDate = $('#dateCloseInv');
            let reInvDate = reInvestDate.text();
            if (reInvDate === '' && $('#typeClosing').find(':selected').text() === 'Реинвестирование') {
                hasError.errors = true;
                reInvestDateInfo.html('Необходимо указать дату').show();
            } else {
                reInvestDateInfo.html('').hide();
            }
        }
    };

    $('#facilities').change(function () {
        let facilities = [];
        let facility = $(this).val();
        facilities.push(facility);
        getMultipleUFFromLSSelectPicker(facilities, 'underFacilities');
    });

    $('#fFacilities').change(function () {
        let facilitiesList = [];
        $.each($(this).find(':selected'), function(ind, el) {
            facilitiesList.push(el.value);
        });
        getMultipleUFFromLS(facilitiesList, 'uFacilities');
        $('#uFacilities').selectpicker('refresh');
    });

    $('#srcFacilities').change(function () {
        let facility = $(this).find('option:selected').val();
        getUnderFacilitiesFromLocalStorage(facility, 'srcUnderFacilities');
    });

    $('#sourceFacilities').change(function () {
        let facility = $(this).find('option:selected').val();
        getUnderFacilitiesFromLocalStorage(facility, 'sourceUnderFacilities');
    })

    $(document).on("change", "#typeClosing", function () {
        let typeClosing = $('#typeClosingRow');
        let reFacility = $('#sourceFacility');
        let reUnderFacility = $('#sourceUnderFacility');

        if ($(this).find(':selected').text() === 'Реинвестирование') {
            reFacility.removeClass('d-none');
            reUnderFacility.removeClass('d-none');
            $("#dateRepRow").removeClass('d-none');
            $('#shareType').find('option:contains(Основная)').attr('selected', 'selected');
            reFacility.insertAfter(typeClosing);
            reUnderFacility.insertAfter(reFacility);
            $('#investorBuyerRow').addClass('d-none');
        } else if ($(this).find(':selected').text() === 'Перепродажа доли') {
            reFacility.addClass('d-none');
            reUnderFacility.addClass('d-none');
            $("#dateRepRow").addClass('d-none');
            $('#investorBuyerRow').removeClass('d-none');
            if ($("#all-modal").hasClass('show')) {
                $('#buyerRow').removeClass('d-none')
            } else {
                $('#buyerRow').addClass('d-none')
            }
        } else {
            reFacility.addClass('d-none');
            reUnderFacility.addClass('d-none');
            $("#dateRepRow").addClass('d-none');
            $('#investorBuyerRow').addClass('d-none');
            if ($(this).find(':selected').text() === 'Выберите вид закрытия') {
                $('#dateCloseInv').val('');
            }
        }
    });

    $(document).on("change", "#cashDetail", function () {

        let cashDetail = $('#cashDetailRow');
        let dateRep = $('#dateRepRow');
        let reFacility = $('#reFacility');
        let reUnderFacility = $('#reUnderFacility');

        if ($(this).find(':selected').text() === 'Реинвестирование с аренды') {
            dateRep.insertAfter(cashDetail);
            reFacility.insertAfter(dateRep);
            reUnderFacility.insertAfter(reFacility);
            reFacility.css("display", "block");
            reUnderFacility.css("display", "block");
            dateRep.css("display", "block");
            $('#shareType').find('option:contains(Основная)').attr('selected', 'selected');
        } else if ($(this).find(':selected').text() === 'Реинвестирование с продажи') {
            reFacility.insertAfter(cashDetail);
            reUnderFacility.insertAfter(reFacility);
            reFacility.css("display", "block");
            reUnderFacility.css("display", "block");
            dateRep.css("display", "none");
            $('#shareType').find('option:contains(Основная)').attr('selected', 'selected');
        } else {
            reFacility.css("display", "none");
            reUnderFacility.css("display", "none");
            dateRep.css("display", "none");
        }
    });

    $("#reFacilities").change(function () {
        let reFacility = $(this).val();
        getUnderFacilitiesFromLocalStorage(reFacility, 'reUnderFacilities');
    });
    $('.selectpicker').selectpicker('refresh');

    $('#accept-create').on('click', function () {
        $('#createAccepted').val('true')
        $('#confirm-create').modal('hide')
        save(OperationEnum.CREATE)
    })
});

let User = function () {
}

User.prototype = {
    id: 0,
    login: '',
    build: function (id, login) {
        this.id = id;
        this.login = login;
    }
}

let UnderFacility = function () {
}

UnderFacility.prototype = {
    id: 0,
    name: '',
    build: function (id, name) {
        this.id = id;
        this.name = name;
    }
}

function disableFields(operation) {
    let facilitiesRow = $('#facilitiesRow');
    let underFacilitiesRow = $('#underFacilitiesRow');
    let investorRow = $('#investorRow');
    let cashRow = $('#cashRow');
    let dateGivenRow = $('#dateGivenCashRow');
    let cashSrcRow = $('#cashSrcRow');
    let cashDetailRow = $('#cashDetailRow');
    let dateCloseInvRow = $('#dateCloseInvRow');
    let reFacilityRow = $('#sourceFacility');
    let reUnderFacilityRow = $('#sourceUnderFacility');
    let dateRepRow = $('#dateRepRow');
    let typeCloseRow = $('#typeClosingRow');
    let buyerRow = $('#investorBuyerRow');
    let realDateGivenRow = $('#realDateGivenRow')

    let facilities = $('#facilities')
    let underFacilities = $('#underFacilities');
    let investor = $('#investor');
    let cash = $('#cash');
    let dateGiven = $('#dateGivenCash');
    let cashSrc = $('#cashSrc');
    let cashDetail = $('#cashDetail');
    let dateReport = $('#dateRep');
    let shareType = $('#shareType');

    let dateClose = $('#dateCloseInv');
    let typeClose = $('#typeClosing');
    let reFacility = $('#sourceFacilities');
    let reUnderFacility = $('#sourceUnderFacilities');
    let realDateGiven = $('#realDateGiven')

    switch (operation) {
        case OperationEnum.CREATE:
            facilitiesRow.removeClass('d-none')
            underFacilitiesRow.removeClass('d-none')
            investorRow.removeClass('d-none')
            cashRow.removeClass('d-none')
            dateGivenRow.removeClass('d-none')
            cashSrcRow.removeClass('d-none')
            cashDetailRow.removeClass('d-none')
            realDateGivenRow.removeClass('d-none')

            dateCloseInvRow.addClass('d-none')
            typeCloseRow.addClass('d-none')
            reFacilityRow.addClass('d-none')
            reUnderFacilityRow.addClass('d-none')
            dateRepRow.addClass('d-none')
            buyerRow.addClass('d-none')

            facilities.prop('disabled', false)
            underFacilities.prop('disabled', false)
            investor.prop('disabled', false)
            cash.prop('disabled', false)
            dateGiven.prop('disabled', false)
            cashSrc.prop('disabled', false)
            cashDetail.prop('disabled', false)
            dateReport.prop('disabled', false)
            shareType.prop('disabled', false)
            realDateGiven.prop('disabled', false)

            dateClose.prop('disabled', true)
            typeClose.prop('disabled', true)
            reFacility.prop('disabled', true)
            reUnderFacility.prop('disabled', true)

            break
        case OperationEnum.UPDATE:
            facilitiesRow.removeClass('d-none')
            underFacilitiesRow.removeClass('d-none')
            investorRow.removeClass('d-none')
            cashRow.removeClass('d-none')
            dateGivenRow.removeClass('d-none')
            cashSrcRow.removeClass('d-none')
            cashDetailRow.removeClass('d-none')
            realDateGivenRow.removeClass('d-none')
            let cashDetailVal = $('#cashDetail:selected').text()
            if (cashDetailVal === 'Реинвестирование с продажи') {
                reFacilityRow.removeClass('d-none')
                reUnderFacilityRow.removeClass('d-none')
                dateRepRow.addClass('d-none')
            } else if (cashDetailVal === 'Реинвестирование с аренды') {
                reFacilityRow.removeClass('d-none')
                reUnderFacilityRow.removeClass('d-none')
                dateRepRow.removeClass('d-none')
            } else {
                reFacilityRow.addClass('d-none')
                reUnderFacilityRow.addClass('d-none')
                dateRepRow.addClass('d-none')
            }
            dateCloseInvRow.addClass('d-none')
            typeCloseRow.addClass('d-none')
            reFacilityRow.addClass('d-none')
            reUnderFacilityRow.addClass('d-none')
            dateRepRow.addClass('d-none')
            buyerRow.addClass('d-none')
            break
        case OperationEnum.CLOSE:
            dateCloseInvRow.removeClass('d-none');
            typeCloseRow.removeClass('d-none');
            realDateGivenRow.addClass('d-none')
            dateClose.prop('disabled', false)
            typeClose.prop('disabled', false)
            reFacility.prop('disabled', false)
            reUnderFacility.prop('disabled', false)

            facilities.prop('disabled', true);
            underFacilities.prop('disabled', true);
            investor.prop('disabled', true);
            cash.prop('disabled', true);
            dateGiven.prop('disabled', true);
            cashSrc.prop('disabled', true);
            cashDetail.prop('disabled', true);
            dateReport.prop('disabled', true);
            shareType.prop('disabled', true);
            break
    }

    if ($('#doubleCash').val() === 'true') {
        $('#facilities').prop('disabled', true);
        $('#underFacilities').prop('disabled', false);
        $('#investor').prop('disabled', true);
        $('#cash').prop('disabled', false);
        $('#dateGivenCash').prop('disabled', true);
        $('#cashSrc').prop('disabled', true);
        $('#cashDetail').prop('disabled', true);
        $('#dateCloseInv').prop('disabled', true);
        $('#dateCloseInvRow').css('display', 'none');
        $('#typeClosing').prop('disabled', true);
        $('#typeClosingRow').css('display', 'none');
        $('#reFacilities').prop('disabled', true);
        $('#reUnderFacilities').prop('disabled', true);
        $('#dateRep').prop('disabled', true);
        $('#shareType').prop('disabled', true);
    }
}

function enableFields() {
    $('#facilities').prop('disabled', false);
    $('#underFacilities').prop('disabled', false);
    $('#investor').prop('disabled', false);
    $('#cash').prop('disabled', false);
    $('#dateGivenCash').prop('disabled', false);
    $('#cashSrc').prop('disabled', false);
    $('#cashDetail').prop('disabled', false);
    $('#dateCloseInv').prop('disabled', false);
    $('#typeClosing').prop('disabled', false);
    $('#reFacilities').prop('disabled', false);
    $('#reUnderFacilities').prop('disabled', false);
    $('#dateRep').prop('disabled', false);
}

function moveFields(mAttribute) {

    let facilities = $('#facilitiesRow');
    let underFacilities = $('#underFacilitiesRow');
    let investor = $('#investorRow');
    let cash = $('#cashRow');
    let dateGivenCash = $('#dateGivenCashRow');
    let cashSrc = $('#cashSrcRow');
    let cashTyp = $('#cashTypRow');
    let cashDetail = $('#cashDetailRow');
    let invType = $('#invTypeRow');
    let dateCloseInv = $('#dateCloseInvRow');
    let typeClosing = $('#typeClosingRow');
    let dateRep = $('#dateRepRow');
    let reFacility = $('#reFacility');
    let reUnderFacility = $('#reUnderFacility');
    let shareTypeName = $('#shareTypeNameRow');
    let realDateGiven = $('#realDateGivenRow');

    switch (mAttribute) {
        case "newCash":
            underFacilities.insertAfter(facilities);
            investor.insertAfter(underFacilities);
            cash.insertAfter(investor);
            dateGivenCash.insertAfter(cash);
            cashSrc.insertAfter(dateGivenCash);
            cashDetail.insertAfter(cashTyp);
            shareTypeName.insertAfter(invType);
            dateRep.insertAfter(shareTypeName);
            dateCloseInv.insertAfter(dateRep);
            typeClosing.insertAfter(dateCloseInv);
            reFacility.insertAfter(dateRep);
            reUnderFacility.insertAfter(reFacility);
            break;
        case "edit":
            underFacilities.insertAfter(facilities);
            investor.insertAfter(underFacilities);
            cash.insertAfter(investor);
            dateGivenCash.insertAfter(cash);
            cashSrc.insertAfter(dateGivenCash);
            cashTyp.insertAfter(cashSrc);
            cashDetail.insertAfter(cashTyp);
            dateRep.insertAfter(cashDetail);
            reFacility.insertAfter(dateRep);
            reUnderFacility.insertAfter(reFacility);
            invType.insertAfter(reUnderFacility);
            shareTypeName.insertAfter(invType);
            dateCloseInv.insertAfter(shareTypeName);
            typeClosing.insertAfter(dateCloseInv);

            break;

        case "doubleCash":
        case "closeCash":
            underFacilities.insertAfter(facilities);
            investor.insertAfter(underFacilities);
            cash.insertAfter(investor);
            dateGivenCash.insertAfter(cash);
            cashSrc.insertAfter(dateGivenCash);
            cashDetail.insertAfter(cashTyp);
            shareTypeName.insertAfter(invType);
            dateRep.insertAfter(shareTypeName);
            dateCloseInv.insertAfter(dateRep);
            typeClosing.insertAfter(dateCloseInv);
            reFacility.insertAfter(dateRep);
            reUnderFacility.insertAfter(reFacility);
            realDateGiven.insertAfter(typeClosing);
            if (mAttribute === 'closeCash') {
                realDateGiven.attr('display', 'block');
            } else {
                realDateGiven.attr('display', 'none');
            }
            break;
    }
}

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

function display(data) {
    let json = JSON.stringify(data, null, 4);
    $('#investorsCash').html(json);
}

function checkChecked() {
    return $('table#monies').find('[type="checkbox"]:checked:not(:disabled)').length;
}

function prepareReinvestCash() {
    let cashes = [];
    let dateGiven;
    let dateClose;
    let shareTypeId;

    dateClose = $('#dateClose').val();
    if (dateClose.length < 10) {
        $('#dateCloseErr').css('display', 'block');
    } else {
        $('#dateCloseErr').css('display', 'none');
    }
    dateGiven = new Date(dateClose).getTime();
    dateClose = dateGiven;

    let facilitiesSelect = $('#srcFacilities').find('option:selected');
    let facilityToReinvestId = facilitiesSelect.val();

    let underFacilitiesSelect = $('#srcUnderFacilities').find('option:selected');
    let underFacilityToReinvestId = underFacilitiesSelect.attr('id');

    if (facilityToReinvestId === 0) {
        $('#facilityErr').css('display', 'block');
        return false;
    } else {
        $('#facilityErr').css('display', 'none');
    }

    shareTypeId = $('#shareType').find('option:selected').val()

    if (shareTypeId === 0) {
        $('#shareTypeErr').css('display', 'block');
        return false;
    } else {
        $('#shareTypeErr').css('display', 'none');
    }

    showLoader();
    let current;

    $('#all-modal').modal('hide');
    $('#reinvestAll').prop('disabled', true);
    $('table#monies').find('> tbody').find('> tr').each(function (i) {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            $(this).closest('tr').find('input:checkbox').prop('disabled', true);
            current = $(this).closest('tr');
            $('#' + current.attr('id') + ' input:checkbox').prop('disabled', true);
            cashes.push(current.attr('id'));
        });
    });
    reinvestCash(facilityToReinvestId, underFacilityToReinvestId, shareTypeId, cashes, dateClose);
}

function reinvestCash(facilityToReinvestId, underFacilityToReinvestId, shareTypeId, cashes, dateClose) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let reinvestCashDTO = ({
        facilityToReinvestId: facilityToReinvestId,
        underFacilityToReinvestId: underFacilityToReinvestId,
        investorCashIdList: cashes,
        dateClose: dateClose,
        shareTypeId: shareTypeId
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: 'reinvest/save',
        data: JSON.stringify(reinvestCashDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message);
        },
        error: function (e) {
            showPopup('Что-то пошло не так = [' + e.error + "]");
        },
        done: function (e) {
            enableSearchButton(true);
        }
    });
}

function prepareDivideCash() {
    let err;
    let cashes = [];
    let id;
    let reUnderFacilitiesList = [];
    let excludedUnderFacilities = [];

    $('#underFacilitiesList').find('option:selected').each(function () {
        excludedUnderFacilities.push($(this).attr('id'));
    });
    $('#srcUnderFacilities').find('option:selected').each(function (ind, el) {
        reUnderFacilitiesList.push(el.id);
    });

    if (reUnderFacilitiesList.length === 0 || reUnderFacilitiesList[0] === 0) {
        $('#underFacilityErr').css('display', 'block');
        err = true;
    } else {
        $('#underFacilityErr').css('display', 'none');
        err = false;
    }

    if (err) {
        return false;
    }
    let current;

    $('#all-modal').modal('hide');
    blockUnblockDropdownMenus('block');
    $('table#monies').find('> tbody').find('> tr').each(function (i) {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            $(this).closest('tr').find('input:checkbox').prop('disabled', true);
            current = $(this).closest('tr');
            $('#' + current.attr('id') + ' input:checkbox').prop('disabled', true);
            id = current.attr('id');
            cashes.push(id);
        });
    });
    $('#msg').html('Начинаем разделение денег...');
    $('#msg-modal').modal('show');
    connect();
    if (reUnderFacilitiesList.length > 1) {
        divideMultiple(cashes, reUnderFacilitiesList, excludedUnderFacilities);
    } else {
        saveDivideCash(cashes, reUnderFacilitiesList[0], excludedUnderFacilities);
    }
}

function saveDivideCash(cashes, reUnderFacility, excludedUnderFacilities) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let divideCashDTO = {
        investorCashList: cashes,
        reUnderFacilityId: reUnderFacility,
        excludedUnderFacilitiesIdList: excludedUnderFacilities
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/divide-cash",
        data: JSON.stringify(divideCashDTO),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
        },
        error: function (e) {
            showPopup('Что-то пошло не так [' + e + ']');
        },
        done: function (e) {
            enableSearchButton(true);
        }
    });
}

function divideMultiple(cashes, reUnderFacilitiesList, excludedUnderFacilities) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let divideCashDTO = ({
        investorCashList: cashes,
        reUnderFacilitiesIdList: reUnderFacilitiesList,
        excludedUnderFacilitiesIdList: excludedUnderFacilities
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "divide-multiple",
        data: JSON.stringify(divideCashDTO),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            showPopup(data.message);
        },
        error: function (e) {
            showPopup('Что-то пошло не так [' + e.error + ']');
        },
        done: function (e) {
            enableSearchButton(true);
        }
    });
}

function deleteCash(cashIdList) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let deleteMoneyDTO = ({
        moneyIds: cashIdList
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "delete/list",
        data: JSON.stringify(deleteMoneyDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            showPopup(data.message);
        })
        .fail(function (jqXHR) {
            $('#content').addClass('bg-warning')
            showPopup(jqXHR.responseText);
        })
        .always(function () {
            console.log('Закончили!');
        });

}

function prepareCloseCash() {
    let what = "closeCash";
    let invBuyer = $('#buyer');
    let investorBuyerId = invBuyer.find(':selected').val();
    let investorBuyer = {
        id: invBuyer.find(':selected').val(),
        login: invBuyer.find(':selected').text()
    };

    if (investorBuyer.id === '0') {
        investorBuyer = null;
    }

    let dateClosingInvest = new Date($('#dateClose').val()).getTime();
    let realDateGiven = new Date($('#realDateGiven').val()).getTime();

    let cashIdList = [];
    $('table#monies').find('> tbody').find('> tr').each(function () {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            cashIdList.push($(this).closest('tr').attr('id'));
        })
    });
    $('#all-modal').modal('hide')
    closeCash(cashIdList, investorBuyerId, dateClosingInvest, what, realDateGiven);
}

function closeCash(cashIdList, invBuyer, dateClosingInvest, what, realDateGiven) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let closeCashDTO = ({
        "investorCashIdList": cashIdList,
        "investorBuyerId": invBuyer,
        "dateReinvest": dateClosingInvest,
        "operation": what,
        "realDateGiven": realDateGiven
    });

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "close",
        data: JSON.stringify(closeCashDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message);
        },
        error: function (e) {
            showPopup(e.toLocaleString());
        }
    });
}

function blockMenus() {
    let current;
    $('table#monies').find('> tbody').find('> tr').each(function (i) {
        current = $(this).closest('tr');
        if (current.children('td:eq(7)').text() === 'Реинвестирование с аренды') {
            current.find('#aEdit').addClass('disabled');
            current.find('#aDivide').addClass('disabled');
        }
    });
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

function blockActions() {
    $('table#monies').find('> tbody').find('> tr').each(function () {
        $(this).find(':checkbox:disabled').each(function () {
            $(this).closest('tr').find('#aDivide').addClass('disabled');
            $(this).closest('tr').find('#aDouble').addClass('disabled');
            $(this).closest('tr').find('#aEdit').addClass('disabled');
        })
    });
}

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

function linkHasClass(link) {
    if (link.hasClass('disabled')) return true;
}

/***
 *
 * Новая версия
 */

let MoneyDTO = function () {}

MoneyDTO.prototype = {
    id: null,
    facilityId: null,
    underFacilityId: null,
    investorId: null,
    cash: 0.0,
    dateGiven: null,
    cashSourceId: null,
    newCashDetailId: null,
    shareTypeId: 0,
    dateReport: null,
    buyerId: null,
    dateClose: null,
    typeCloseId: null,
    realDateGiven: null,
    operation: null,
    reFacilityId: null,
    reUnderFacilityId: null,
    sourceFacilityId: null,
    sourceUnderFacilityId: null,
    createAccepted: false,
    build: function (facilityId, underFacilityId, investorId, cash, dateGiven, cashSourceId,
                     newCashDetailId, shareTypeId, createAccepted) {
        this.facilityId = facilityId;
        this.underFacilityId = underFacilityId;
        this.investorId = investorId;
        this.cash = cash;
        this.dateGiven = dateGiven;
        this.cashSourceId = cashSourceId;
        this.newCashDetailId = newCashDetailId;
        this.shareTypeId = shareTypeId;
        this.createAccepted = createAccepted;
    },
    setId: function (cashId) {
        this.id = cashId;
    },
    setDateReport: function (dateReport) {
        this.dateReport = dateReport;
    },
    resale: function (id, buyerId, dateClose, typeCloseId, realDateGiven) {
        this.id = id;
        this.buyerId = buyerId;
        this.dateClose = dateClose;
        this.typeCloseId = typeCloseId;
        this.realDateGiven = realDateGiven;
        this.operation = OperationEnum.RESALE
    },
    cashing: function (id, dateClose) {
        this.id = id;
        this.dateClose = dateClose;
        this.operation = OperationEnum.CASHING
    },
    reinvest: function (id, dateClose, reFacilityId, reUnderFacilityId, sourceFacilityId, sourceUnderFacilityId) {
        this.id = id;
        this.dateClose = dateClose;
        this.reFacilityId = reFacilityId
        this.reUnderFacilityId = reUnderFacilityId
        this.sourceFacilityId = sourceFacilityId
        this.sourceUnderFacilityId = sourceUnderFacilityId
        this.operation = OperationEnum.REINVEST
    },
    update: function (id, facilityId, underFacilityId, investorId, cash, dateGiven, cashSourceId,
                      newCashDetailId, shareTypeId, realDateGiven) {
        this.id = id;
        this.facilityId = facilityId;
        this.underFacilityId = underFacilityId;
        this.investorId = investorId;
        this.cash = cash;
        this.dateGiven = dateGiven;
        this.cashSourceId = cashSourceId;
        this.newCashDetailId = newCashDetailId;
        this.shareTypeId = shareTypeId;
        this.realDateGiven = realDateGiven;
        this.operation = OperationEnum.UPDATE
    }
}

/**
 * Сохранить сумму инвестора
 *
 * @param {MoneyDTO} moneyDTO DTO суммы
 */
function saveMoney(moneyDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader();
    let url = OperationEnum.properties[moneyDTO.operation].url
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: url,
        data: JSON.stringify(moneyDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            let status = data.status;
            switch (moneyDTO.operation) {
                case OperationEnum.CREATE:
                    if (status === 200) {
                        showPopup(data.message);
                        clearMoneyForm()
                    } else {
                        $('#confirm-create').modal('show');
                    }
                    break
                case OperationEnum.UPDATE:
                case OperationEnum.RESALE:
                case OperationEnum.CASHING:
                case OperationEnum.REINVEST:
                    showPopup(data.message);
                    window.location.href = '../list'
                    break
            }

        },
        error: function (e) {
            closeLoader()
            showPopup(e.error);
        }
    });
}

/**
 * Очистить поля формы создания/изменения/разделения/закрытия денег
 *
 */
function clearMoneyForm() {
    $('#facilities').val('0')
    $('#underFacilities').val('0')
    $('#investor').val('0')
    $('#cash').val('')
    $('#dateGivenCash').val('')
    $('#cashSrc').val('0')
    $('#cashDetail').val('0')
    $('#shareType').val('0')
    $('.selectpicker').selectpicker('refresh')
}

/**
 * Проверить и сохранить сумму инвестора
 * @param {OperationEnum} operation вид операции
 */
function save(operation) {
    if (check(operation)) {
        let moneyDTO = getMoneyDTO(operation)
        saveMoney(moneyDTO);
    }
}

/**
 * Проверить поля формы
 *
 * @param {OperationEnum} operation вид операции
 * @returns {boolean} результат проверки
 */
function check(operation) {
    switch (operation) {
        case OperationEnum.CREATE:
        case OperationEnum.UPDATE:
            let facilitySelect = $('#facilities');
            let facilityId = facilitySelect.find(':selected').val();
            if (facilityId === '0') {
                $('#facilityError').removeClass('d-none')
                return false
            } else {
                $('#facilityError').addClass('d-none')
            }
            let underFacilitySelect = $('#underFacilities');
            let underFacilityId = underFacilitySelect.find(':selected').attr('id');
            if (underFacilityId === '0') {
                $('#underFacilityError').removeClass('d-none')
                return false
            } else {
                $('#underFacilityError').addClass('d-none')
            }
            let investorSelect = $('#investor');
            let investorId = investorSelect.find(':selected').val();
            if (investorId === '0') {
                $('#investorError').removeClass('d-none')
                return false
            } else {
                $('#investorError').addClass('d-none')
            }
            let cashSum = $('#cash').val()
            if (cashSum.length === 0) {
                $('#cashError').removeClass('d-none')
                return false
            } else {
                $('#cashError').addClass('d-none')
            }
            let dateGiven = $('#dateGivenCash').val()
            if (dateGiven.length === 0 || dateGiven.length > 10) {
                $('#dateGivenError').removeClass('d-none')
                return false
            } else {
                $('#dateGivenError').addClass('d-none')
            }
            let shareTypeSelect = $('#shareType')
            let shareTypeId = shareTypeSelect.find(':selected').val()
            if (shareTypeId === '0') {
                $('#shareTypeError').removeClass('d-none')
                return false
            } else {
                $('#shareTypeError').addClass('d-none')
            }
            break
        case OperationEnum.CLOSE:
            let dateClose = $('#dateCloseInv').val();
            if (dateClose.length === 0 || dateClose.length > 10) {
                $('#dateCloseError').removeClass('d-none')
                return false
            } else {
                $('#dateCloseError').addClass('d-none')
            }
            let typeClosing = $('#typeClosing').find(':selected').text()
            if (typeClosing === 'Выберите вид закрытия') {
                $('#typeClosingError').removeClass('d-none')
                return false
            } else {
                if (typeClosing === 'Перепродажа доли') {
                    let buyer = $('#investorBuyer').find(':selected').val()
                    if (buyer === '0') {
                        $('#investorBuyerError').removeClass('d-none')
                        return false
                    } else {
                        $('#investorBuyerError').addClass('d-none')
                    }
                }
                $('#typeClosingError').addClass('d-none')
            }
            break
    }
    return true
}

/**
 * Подготовить DTO для создания/обновления/закрытия/разделения денег
 *
 * @param {OperationEnum} operation
 * @returns {MoneyDTO} подготовленный DTO
 */
function getMoneyDTO(operation) {
    let cashId = $('#id').val()
    let facilityId = $('#facilities').find(':selected').val()
    let underFacilityId = $('#underFacilities').find(':selected').attr('id')
    let investorId = $('#investor').find(':selected').val()
    let cashSum = $('#cash').val()
    let dateGiven = $('#dateGivenCash').val()
    let cashSourceId = $('#cashSrc').find(':selected').val()
    let newCashDetailId = $('#cashDetail').find(':selected').val()
    let shareTypeId = $('#shareType').find(':selected').val()
    let dateReport = $('#dateRep').val()
    let realDateGiven = $('#realDateGiven').val()
    let moneyDTO = new MoneyDTO()
    let createAccepted = $('#createAccepted').val()
    switch (operation) {
        case OperationEnum.CREATE:
            moneyDTO.build(facilityId, underFacilityId, investorId, cashSum, dateGiven,
                cashSourceId, newCashDetailId, shareTypeId, createAccepted)
            moneyDTO.operation = OperationEnum.CREATE
            return moneyDTO
        case OperationEnum.UPDATE:
            moneyDTO.update(cashId, facilityId, underFacilityId, investorId, cashSum, dateGiven,
                cashSourceId, newCashDetailId, shareTypeId, realDateGiven)
            return moneyDTO
        case OperationEnum.CLOSE:
            let buyerId = $('#investorBuyer').find(':selected').val()
            let dateClose = $('#dateCloseInv').val()
            let typeClosing = $('#typeClosing');
            let typeCloseId = typeClosing.find(':selected').val()
            let typeClose = typeClosing.find(':selected').text()
            if (typeClose === 'Перепродажа доли') {
                moneyDTO.resale(cashId, buyerId, dateClose, typeCloseId, realDateGiven)
            } else if (typeClose === 'Вывод') {
                moneyDTO.cashing(cashId, dateClose)
            } else if (typeClose === 'Реинвестирование') {
                let reFacilityId = $('#sourceFacilities').val()
                let reUnderFacilityId = $('#sourceUnderFacilities').val()
                moneyDTO.reinvest(cashId, dateClose, reFacilityId, reUnderFacilityId, facilityId, underFacilityId)
            }
            return moneyDTO
    }
}

let AcceptedMoneyDTO = function () {
}

AcceptedMoneyDTO.prototype = {
    acceptedMoneyIds: [],
    build: function (acceptedMoneyIds) {
        this.acceptedMoneyIds = acceptedMoneyIds;
    }
}

/**
 * Согласовать выбранные суммы
 *
 * @param acceptedMoneyDTO {AcceptedMoneyDTO}
 */
function acceptMonies(acceptedMoneyDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: 'accept',
        data: JSON.stringify(acceptedMoneyDTO),
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
            closeLoader()
            showPopup(e.error);
        }
    });
}
