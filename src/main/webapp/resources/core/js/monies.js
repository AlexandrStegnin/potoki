let filters = [];

jQuery(document).ready(function ($) {

    $('#money-table').on('submit', function (e) {
        e.preventDefault()
        let operation = $('#operation').val();
        switch (operation) {
            case "CREATE":
                checkAndCreate();
                break
        }
    })

    blockActions();
    getFiltersFromLS((window.location.pathname + '').split("/")[1]);
    $(document).on('mousedown', '#underFacilitiesList > option', function (e) {
        e.preventDefault();
        this.selected = !this.selected;
    });

    $(document).on('mousedown', '#underFacilities > option', function (e) {
        e.preventDefault();
        this.selected = !this.selected;
    });

    $('#cashingSubmit').on('click', function (e) {
        e.preventDefault();
        let investors = $('#investor').val();
        let investorId = 0;
        if ($.isArray(investors)) {
            investorId = investors[0];
        }
        cashingMoney(investorId);
    })

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
        populateFilters((window.location.pathname + '').split("/")[1]);
        window.location.href = window.location.pathname;
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

    $(document).on('change', '#facilities', function () {
        hasError.errors = false;
        hasError.allMoneyCashingFunc();
    });

    $('#investor').on('change', function () {
        hasError.errors = false;
        hasError.allMoneyCashingFunc();
    });

    $('#cashing').on('keyup click', function () {
        hasError.errors = false;
        hasError.allMoneyCashingFunc();
    });

    $('#commissionNoMore').on('keyup click', function () {
        hasError.errors = false;
        hasError.allMoneyCashingFunc();
    });

    $('#allMoneyCashing').on('click', function () {
        allMoneyCashing();
    });

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
        let sourceIdList = [];
        $('table#monies').find('> tbody').find('> tr').each(function () {
            $(this).find(':checkbox:checked').not(':disabled').each(function () {
                cashIdList.push($(this).closest('tr').attr('id'));
                sourceIdList.push($(this).closest('tr').find('> td:last').data('source'));
                $(this).closest('tr').remove();
            })
        });
        if (sourceIdList.indexOf('|') >= 0) {
            $.each(sourceIdList, function (ind, el) {
                let tmp = el.split('|');
                if (tmp.length > 0) {
                    $.each(tmp, function (i, elem) {
                        $('table#monies').find('> tbody').find('> tr').each(function () {
                            if ($(this).attr('id') === elem) {
                                $(this).find(':checkbox').prop('disabled', false);
                                $(this).find(':checkbox').prop('checked', false);
                            }
                        })
                    })
                }
            });
        } else {
            $('table#monies').find('> tbody').find('> tr').each(function () {
                if ($(this).attr('id') === sourceIdList[0]) {
                    $(this).find(':checkbox').prop('disabled', false);
                    $(this).find(':checkbox').prop('checked', false);
                }
            })
        }
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
                populateFilters((window.location.pathname + '').split("/")[1]);
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
    if (url.indexOf('money/create') >= 0 || url.indexOf('edit-cash') >= 0 || url.indexOf('double-cash') >= 0 || url.indexOf('close-cash') >= 0) {
        populateStorageUnderFacilities('underFacilities');
    } else {
        populateStorageUnderFacilities('uFacilities');
    }
    if (url.indexOf('double-cash') >= 0) {
        $('#doubleCash').val('true');
    }

    disableFields();

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
            let dateClosingInfo = $('#reInvestDateErr');
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

            let reInvestDateInfo = $('#reInvestDateErr');
            let reInvestDate = $('#dateCloseInv');
            let reInvDate = reInvestDate.text();
            if (reInvDate === '' && $('#typeClosing').find(':selected').text() === 'Реинвестирование') {
                hasError.errors = true;
                reInvestDateInfo.html('Необходимо указать дату').show();
            } else {
                reInvestDateInfo.html('').hide();
            }
        },
        'allMoneyCashingFunc': function () {

            let facility = $('#facilities').find(':selected').text();
            let investor = $('#investor').find(':selected').text();
            let cashing = $('#cashing').val();
            let dateCashing = $('#dateGivenCash').val();
            if (facility === 'Выберите объект' || investor === 'Выберите инвесторов...' || investor === '' || cashing === '' || dateCashing === '') {
                hasError.errors = true;
                $('#allMoneyCashing').hide();
            } else {
                $('#allMoneyCashing').show();
            }
        },
        'sendIt': function () {
            if (!hasError.errors) {
                prepareCashSave(what);
            }
        }
    };

    $("#search-form").submit(function () {
        enableSearchButton(false);
        populateFilters((window.location.pathname + '').split('/')[1]);
        enableSearchButton(true);
    });

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

    $(document).on("change", "#typeClosing", function () {
        let typeClosing = $('#typeClosingRow');
        let reFacility = $('#sourceFacility');
        let reUnderFacility = $('#sourceUnderFacility');

        if ($(this).find(':selected').text() === 'Реинвестирование') {
            reFacility.css("display", "block");
            $("#dateRepRow").css("display", "block");
            $('#shareTypeName').find('option:contains(Основная)').attr('selected', 'selected');
            reFacility.insertAfter(typeClosing);
            reUnderFacility.insertAfter(reFacility);
            $('#investorBuyerRow').addClass('d-none');
        } else if ($(this).find(':selected').text() === 'Перепродажа доли') {
            reFacility.css("display", "none");
            reUnderFacility.css("display", "none");
            $("#dateRepRow").addClass('d-none');
            $('#investorBuyerRow').removeClass('d-none');
        } else {
            reFacility.css("display", "none");
            reUnderFacility.css("display", "none");
            $("#dateRepRow").addClass('d-none');
            $('#investorBuyerRow').addClass('d-none');
            if ($(this).find(':selected').text() === 'Выберите вид закрытия') {
                $('#dateCloseInv').val('');
            }
        }
    });

    $(document).on("change", "#typeClosing", function () {
        if ($(this).find(':selected').text() === 'Перепродажа доли') {
            $('#buyerRow').removeClass('d-none');
        } else {
            $('#buyerRow').addClass('d-none');
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
            $('#shareTypeName').find('option:contains(Основная)').attr('selected', 'selected');
        } else if ($(this).find(':selected').text() === 'Реинвестирование с продажи') {
            reFacility.insertAfter(cashDetail);
            reUnderFacility.insertAfter(reFacility);
            reFacility.css("display", "block");
            reUnderFacility.css("display", "block");
            dateRep.css("display", "none");
            $('#shareTypeName').find('option:contains(Основная)').attr('selected', 'selected');
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

function allMoneyCashing() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let underFacility = $("#underFacilities").find("option:selected").text();
    if (underFacility === 'Выберите подобъект') {
        underFacility = null;
    }

    let investor = $("#investor");
    let investors = [];
    investor.find('option:selected').each(function () {
        let user = new User();
        user.build($(this).val(), $(this).text());
        investors.push(user);
    });

    let search = ({
        investorsList: investors,
        user: {
            id: investor.find("option:selected").val(),
            login: investor.find("option:selected").text()
        },
        facilities: {
            id: $("#facilities").find("option:selected").val()
        },
        underFacilities: {
            underFacility: underFacility
        },
        commission: $('#cashing').val(),
        commissionNoMore: $('#commissionNoMore').val(),
        monies: {
            facility: {
                id: $("#facilities").find("option:selected").val(),
                name: $("#facilities").find("option:selected").text()
            },
            underFacility: {
                name: $("#underFacilities").find("option:selected").text()
            },
            investor: {
                id: investor.find("option:selected").val(),
                login: investor.find("option:selected").text()
            },
            dateGivedCash: $('#dateGivenCash').val(),
            givedCash: $('#cash').val()
        }
    });
    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "allMoneyCashing",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            showPopup(data.message);
            window.location.href = '/money/list';
        },
        error: function (e) {
            closeLoader();
            showPopup('Что-то пошло не так = [' + e.error + "]");
        },
        done: function (e) {
            enableSearchButton(true);
        }
    });
}

function prepareCashSave(what) {
    let invBuyer = $('#investorBuyer');
    let investorBuyer = {
        id: invBuyer.find(':selected').val(),
        login: invBuyer.find(':selected').text()
    };

    if (investorBuyer.id === '0') {
        investorBuyer = null;
    }

    let cashId = $('#id').val();

    let facilities = $('#facilities');
    let facility = {
        id: facilities.find(':selected').val(),
        name: facilities.find(':selected').text()
    };

    let underFacilities = $('#underFacilities');
    let underFacility = {
        id: underFacilities.find(':selected').attr('id'),
        name: underFacilities.find(':selected').text()
    };
    if (underFacility.id === '0') {
        underFacility = null;
    }

    let investors = $('#investor');
    let investor = {id: investors.find(':selected').val(), login: investors.find(':selected').text()};

    let givedCash = $('#cash').val();

    let dateGivenCash = new Date($('#dateGivenCash').val()).getTime();

    let cashSources = $('#cashSrc');
    let cashSource = {id: cashSources.find(':selected').val(), cashSource: cashSources.find(':selected').text()};
    if (cashSource.id === '0') {
        cashSource = null;
    }

    let newCashDetails = $('#cashDetail');
    let newCashDetail = {
        id: newCashDetails.find(':selected').val(),
        name: newCashDetails.find(':selected').text()
    };
    if (newCashDetail.id === '0') {
        newCashDetail = null;
    }

    let dateClosingInvest = new Date($('#dateCloseInv').val()).getTime();

    let dateReport = new Date($('#dateRep').val()).getTime();

    let typeClosingInvests = $('#typeClosing');
    let typeClosingInvest = {
        id: typeClosingInvests.find(':selected').val(),
        name: typeClosingInvests.find(':selected').text()
    };
    if (typeClosingInvest.id === '0') {
        typeClosingInvest = null;
    }

    let reFacilities = $('#sourceFacilities');
    let reFacility = {id: reFacilities.find(':selected').val(), name: reFacilities.find(':selected').text()};
    if (reFacility.id === '0') {
        reFacility = null;
    }

    let reUnderFacilities = $('#sourceUnderFacilities');
    let reUnderFacility = {
        id: reUnderFacilities.find(':selected').attr('id'),
        name: reUnderFacilities.find(':selected').text()
    };
    if (reUnderFacility.id === '0') {
        reUnderFacility = null;
    }

    let dateReinvest = dateClosingInvest;

    let shareTypes = $('#shareTypeName');
    let shareType = shareTypes.find(':selected').text();
    if (shareType === 'Не определена') {
        shareType = null;
    }

    let source = $('#source').val();
    if (source.length === 0) {
        source = null;
    }

    let cash = {
        id: cashId,
        facility: facility,
        underFacility: underFacility,
        investor: investor,
        givedCash: givedCash,
        dateGivedCash: dateGivenCash,
        cashSource: cashSource,
        newCashDetail: newCashDetail,
        dateClosingInvest: dateClosingInvest,
        typeClosing: typeClosingInvest,
        shareType: shareType,
        dateReport: dateReport,
        sourceFacility: reFacility,
        source: source,
        sourceUnderFacility: reUnderFacility
    };

    let flag = true;
    if (cashId === '') {
        flag = false;
    }

    saveCash(cash, reFacility, reUnderFacility, dateReinvest, flag, investorBuyer, what);
}

function saveCash(cash, reFacility, reUnderFacility, dateReinvest, flag, invBuyer, what) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({
        "monies": cash,
        "reFacility": reFacility,
        "reUnderFacility": reUnderFacility,
        "dateReinvest": dateReinvest,
        "user": invBuyer,
        "what": what
    });

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "saveCash",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            $('#popup_modal_form').find('#message').append(data.message);
            showPopup();
            closePopup();
            if (flag) {
                window.location.href = '/money/list';
            }

            $('#facilities').prop('selectedIndex', 0);
            $('#underFacilities').prop('selectedIndex', 0);
            $('#investor').prop('selectedIndex', 0);
            $('#cash').val(0);
            $('#dateGivenCash').val('');
            $('#cashSrc').prop('selectedIndex', 0);
            $('#cashTyp').prop('selectedIndex', 0);
            $('#cashDetail').prop('selectedIndex', 0);
            $('#invType').prop('selectedIndex', 0);
            $('#dateCloseInv').val('');
            $('#typeClosing').prop('selectedIndex', 0);
            $('#reFacilities').prop('selectedIndex', 0);
            $('#reUnderFacilities').prop('selectedIndex', 0);
            $('#dateRep').val('');
            if (what !== null && what === 'doubleCash') {
                window.location.href = '/double-cash-' + cash.id
            }
        },
        error: function (e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            showPopup();
            closePopup();
        }
    });
}

function disableFields() {
    if ($('#closeCash').val() === 'true') {
        $('#facilities').prop('disabled', true);
        $('#underFacilities').prop('disabled', true);
        $('#investor').prop('disabled', true);
        $('#cash').prop('disabled', true);
        $('#dateGivenCash').prop('disabled', true);
        $('#cashSrc').prop('disabled', true);
        $('#cashDetail').prop('disabled', true);
        $('#dateCloseInv').prop('disabled', false);
        $('#typeClosing').prop('disabled', false);
        $('#reFacilities').prop('disabled', false);
        $('#reUnderFacilities').prop('disabled', false);
        $('#dateRep').prop('disabled', true);
        $('#shareTypeName').prop('disabled', true);
    } else if ($('#doubleCash').val() === 'true') {
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
        $('#shareTypeName').prop('disabled', true);
    } else if ($('#edit').val() === 'true') {
        let cashDetail = $('#cashDetail');
        let reFacility = $('#sourceFacility');
        let reUnderFacility = $('#sourceUnderFacility');
        let dateRep = $('#dateRepRow');

        $('#facilities').prop('disabled', false);
        $('#underFacilities').prop('disabled', false);
        $('#investor').prop('disabled', false);
        $('#cash').prop('disabled', false);
        $('#dateGivenCash').prop('disabled', false);
        $('#cashSrc').prop('disabled', false);

        cashDetail.prop('disabled', false);
        if (cashDetail.text() === 'Реинвестирование с продажи') {
            reFacility.css("display", "block");
            reUnderFacility.css("display", "block");
            dateRep.css("display", "none");
        } else if (cashDetail.text() === 'Реинвестирование с аренды') {
            reFacility.css("display", "block");
            reUnderFacility.css("display", "block");
            dateRep.css("display", "block");
        } else {
            reFacility.css("display", "none");
            reUnderFacility.css("display", "none");
            dateRep.css("display", "none");
        }
        $('#dateCloseInv').removeAttr('disabled');
        $('#dateCloseInvRow').removeAttr('disabled');
        $('#typeClosing').prop('disabled', false);
        $('#reFacilities').prop('disabled', false);
        $('#reUnderFacilities').prop('disabled', false);
        $('#dateRep').prop('disabled', false);
    } else if ($('#newCash').val() === 'true') {
        $('#facilities').removeClass('d-none');
        $('#underFacilities').removeClass('d-none');
        $('#investor').removeClass('d-none');
        $('#cash').removeClass('d-none');
        $('#dateGivenCash').removeClass('d-none');
        $('#cashSrc').removeClass('d-none');
        $('#cashDetail').removeClass('d-none');
        $('#dateCloseInvRow').addClass('d-none');
        $('#typeClosingRow').addClass('d-none');
        $('#reFacilities').addClass('d-none');
        $('#reUnderFacilities').addClass('d-none');
        $('#dateRepRow').addClass('d-none');
        $('#investorBuyerRow').addClass('d-none');
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

    shareTypeId = $('#shareTypeName').find('option:selected').val()

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

function cashingMoney() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let underFacility = $("#underFacilities").find("option:selected").text();
    if (underFacility === 'Выберите подобъект') {
        underFacility = null;
    }

    let underFacilities = $('#underFacilities');
    let underFacilitiesList = [];
    underFacilities.find('option:selected').each(function (ind, el) {
        let uf = new UnderFacility();
        uf.build(el.id, el.value);
        underFacilitiesList.push(uf);
    });

    let investor = $("#investor");
    let investors = [];
    investor.find('option:selected').each(function () {
        let user = new User();
        user.build($(this).val(), $(this).text());
        investors.push(user);
    });

    let search = ({
        investorsList: investors,
        user: {
            id: investor.find("option:selected").val(),
            login: investor.find("option:selected").text()
        },
        facilities: {
            id: $("#facilities").find("option:selected").val()
        },
        underFacilities: {
            underFacility: underFacility
        },
        underFacilitiesList: underFacilitiesList,
        commission: $('#cashing').val(),
        commissionNoMore: $('#commissionNoMore').val(),
        monies: {
            facility: {
                id: $("#facilities").find("option:selected").val(),
                name: $("#facilities").find("option:selected").text()
            },
            underFacility: {
                name: $("#underFacilities").find("option:selected").text()
            },
            investor: {
                id: investor.find("option:selected").val(),
                login: investor.find("option:selected").text()
            },
            dateGivedCash: $('#dateGivenCash').val(),
            givedCash: $('#cash').val()
        }
    });
    showLoader();
    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "cashing-money",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            if (data === '') {
                showPopup("Деньги успешно выведены");
                window.location.href = '/money/list';
            } else {
                $('#toBigSumForCashing').html(data);
            }
        },
        error: function (e) {
            closeLoader();
            showPopup('Что-то пошло не так = [' + e.toLocaleString() + "]");
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
        url: "divide-cash",
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
    let search = ({
        cashIdList: cashIdList
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/deleteCashList",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            showPopup(data.message);
        })
        .fail(function (e) {
            showPopup('Что-то пошло не так [' + e.error + ']');
        })
        .always(function () {
            console.log('Закончили!');
        });

}

function prepareCloseCash() {
    let what = "closeCash";
    let invBuyer = $('#buyer');
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
    closeCash(cashIdList, investorBuyer, dateClosingInvest, what, realDateGiven);
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

function checkAndCreate() {
    let facilitySelect = $('#facilities');
    if (facilitySelect.find(':selected').val() === '0') {
        $('#facilityError').removeClass('d-none')
        return false
    } else {
        $('#facilityError').addClass('d-none')
    }
    let underFacilitySelect = $('#underFacilities');
    if (underFacilitySelect.find(':selected').attr('id') === '0') {
        $('#underFacilityError').removeClass('d-none')
        return false
    } else {
        $('#underFacilityError').addClass('d-none')
    }
    let investorSelect = $('#investor');
    if (investorSelect.find(':selected').val() === '0') {
        $('#investorError').removeClass('d-none')
        return false
    } else {
        $('#investorError').addClass('d-none')
    }
    // $('#cash').removeClass('d-none');
    // $('#dateGivedCash').removeClass('d-none');
    // $('#cashSrc').removeClass('d-none');
    // $('#cashDetail').removeClass('d-none');
    // $('#dateCloseInvRow').addClass('d-none');
    // $('#typeClosingRow').addClass('d-none');
    // $('#reFacilities').addClass('d-none');
    // $('#reUnderFacilities').addClass('d-none');
    // $('#dateRepRow').addClass('d-none');
    // $('#investorBuyerRow').addClass('d-none');
}
