let filters = [];

let max;
let min;

jQuery(document).ready(function ($) {

    blockDeleteLink();
    blockActions();
    $(document).on('mousedown', '#underFacilitiesList > option', function (e) {
        e.preventDefault();
        this.selected = !this.selected;
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
    /**

     МАССОВОЕ РЕИНВЕСТИРОВАНИЕ НАЧАЛО

     **/

    blockUnblockDropdownMenus('block', false);

    $('#msg-modal').on('shown.bs.modal', function () {
        // if data-timer attribute is set use that, otherwise use default (7000)
        var timer = 3000;
        $(this).delay(timer).fadeOut(200, function () {
            $(this).modal('hide');
        });
    });

    $(document).on('change', ':checkbox', function () {
        var id = $(this).attr('id');
        var noDivide;
        noDivide = $(this).closest('tr').find('> td:eq(1)').text().length > 0 && $(this).prop('checked') === true &&
            ($(this).closest('tr').find('> td:eq(1)').text().indexOf('_Целиком') < 0);
        if (typeof id === 'undefined') {
            var cnt = checkChecked();
            if (cnt > 0) {
                blockUnblockDropdownMenus('unblock', noDivide);
            } else {
                blockUnblockDropdownMenus('block', noDivide);
            }
        }
    });
    $('table#investorsCash').find('> tbody').find('> tr').each(function () {
        $(this).data('passed', true);
    });

    $(document).on('click', '#reinvestAll', function (event) {
        event.preventDefault();
        if (linkHasClass($('#reinvestAll'))) return false;
        $('#reInvestModal').modal({
            show: true
        });
    });

    $('#closeAll').find('> a').on('click', function (event) {
        event.preventDefault();
        if (linkHasClass($('#closeAll'))) return false;
        $('#closeModal').modal({
            show: true
        })
    });

    $('#deleteAll').on('click', function (event) {
        event.preventDefault();
        if (linkHasClass($('#deleteAll'))) return false;
        showLoader();
        var cashIdList = [];
        var sourceIdList = [];
        $('table#investorsCash').find('> tbody').find('> tr').each(function () {
            $(this).find(':checkbox:checked').not(':disabled').each(function () {
                cashIdList.push($(this).closest('tr').attr('id'));
                sourceIdList.push($(this).closest('tr').find('> td:last').data('source'));
                $(this).closest('tr').remove();
            })
        });
        if (sourceIdList.indexOf('|') >= 0) {
            $.each(sourceIdList, function (ind, el) {
                var tmp = el.split('|');
                if (tmp.length > 0) {
                    $.each(tmp, function (i, elem) {
                        $('table#investorsCash').find('> tbody').find('> tr').each(function () {
                            if ($(this).attr('id') === elem) {
                                $(this).find(':checkbox').prop('disabled', false);
                                $(this).find(':checkbox').prop('checked', false);
                            }
                        })
                    })
                }
            });
        } else {
            $('table#investorsCash').find('> tbody').find('> tr').each(function () {
                if ($(this).attr('id') === sourceIdList[0]) {
                    $(this).find(':checkbox').prop('disabled', false);
                    $(this).find(':checkbox').prop('checked', false);
                }
            })
        }
        deleteCash(cashIdList);
    });

    $('a#del').on('click', function (event) {
        event.preventDefault();
        console.log($(this).parent());
        if (linkHasClass($(this).parent())) return false;
        showLoader();
        var cashIdList = [];
        var sourceIdList = [];
        cashIdList.push($(this).data('delete'));
        sourceIdList.push($(this).parent().parent().parent().parent().parent().find('> td:last').data('source'));
        if (sourceIdList.indexOf('|') >= 0) {
            $.each(sourceIdList, function (ind, el) {
                var tmp = el.split('|');
                if (tmp.length > 0) {
                    $.each(tmp, function (i, elem) {
                        $('table#investorsCash').find('> tbody').find('> tr').each(function () {
                            if ($(this).attr('id') === elem) {
                                $(this).find(':checkbox').prop('disabled', false);
                                $(this).find(':checkbox').prop('checked', false);
                            }
                        })
                    })
                }
            });
        } else {
            $('table#investorsCash').find('> tbody').find('> tr').each(function () {
                if ($(this).attr('id') === sourceIdList[0]) {
                    $(this).find(':checkbox').prop('disabled', false);
                    $(this).find(':checkbox').prop('checked', false);
                }
            })
        }

        $(this).closest('tr').remove();
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

    $(document).on('click', 'a#cancelReinvest', function (event) {
        event.preventDefault();
        $('#reInvestModal').modal("hide");
    });

    $(document).on('click', 'a#cancelClose', function (event) {
        event.preventDefault();
        $('#closeModal').modal("hide");
    });

    $(document).on('click', 'a#cancelDivide', function (event) {
        event.preventDefault();
        $('#divideModal').modal("hide");
    });

    $(document).on('change', '#checkIt', function () {
        var checked = $('#checkIt').prop('checked');
        var noDivide = false;
        if (!checked) {
            blockUnblockDropdownMenus('block', noDivide);
            $('table#investorsCash').find('> tbody').find('> tr').each(function () {
                $(this).find(':checkbox:not(:disabled)').prop('checked', false);
            });
        } else {
            $('table#investorsCash').find('> tbody').find('> tr').each(function () {
                if (!$(this).data('passed')) {
                    $(this).find(':checkbox:not(:disabled)').prop('checked', false);
                } else {
                    if ($(this).find('td:eq(9)').text() === '') {
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
            blockUnblockDropdownMenus('unblock', noDivide);
        }

        /*
        var cnt = checkChecked();
        console.log(cnt);
        */
    });

    $(document).on('submit', '#reInvestData', function (event) {
        event.preventDefault();
        prepareSaveCash();
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

    var url = window.location.href;
    if (url.indexOf('newinvestorscash') >= 0 || url.indexOf('edit-cash') >= 0 || url.indexOf('double-cash') >= 0 || url.indexOf('close-cash') >= 0) {
        populateStorageUnderFacilities('underFacilities');
    } else {
        populateStorageUnderFacilities('uFacilities');
    }
    if (url.indexOf('double-cash') >= 0) {
        $('#doubleCash').val('true');
    }

    disableFields();

    max = findMinMaxDate('#investorsCash tbody', 6, "max");
    min = findMinMaxDate('#investorsCash tbody', 6, "min");

    var hasError = {
        'saleShareFunc': function () {
            var dateClosingInfo = $('#dateCloseErr');
            var typeClosingInfo = $('#buyerErr');
            var investorBuyer = $('#buyer');
            if (investorBuyer.css('display') === 'block' && investorBuyer.find(':selected').text() === 'Выберите инвестора') {
                hasError.errors = true;
                typeClosingInfo.html('Необходимо выбрать инвестора').show();
            } else {
                typeClosingInfo.html('').hide();
            }
            if ($('#dateClosing').val().length < 10) {
                hasError.errors = true;
                dateClosingInfo.html('Необходимо указать дату').show();
            } else {
                dateClosingInfo.html('').hide();
            }
        },
        'investorBuyerFunc': function () {
            var dateClosingInfo = $('#reInvestDateErr');
            var typeClosingInfo = $('#investorBuyerErr');
            var investorBuyer = $('#investorBuyer');
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

            var reFacilityInfo = $('#reFacilityErr');
            var reFacility = $('#reFacility');
            if (reFacility.css('display') === 'block' && reFacility.find(':selected').text() === 'Выберите объект') {
                hasError.errors = true;
                reFacilityInfo.html('Необходимо выбрать объект').show();
            } else {
                reFacilityInfo.html('').hide();
            }
        },
        'reInvestDateFunc': function () {

            var reInvestDateInfo = $('#reInvestDateErr');
            var reInvestDate = $('#dateCloseInv');
            var reInvDate = reInvestDate.text();
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
            let dateCashing = $('#dateGivedCash').val();
            if (facility === 'Выберите объект' || investor === 'Выберите инвестора' || cashing === '' || dateCashing === '') {
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

    $(document).on('click', '[id^="page_"]', function (e) {
        $('#pageNumber').val(this.id.split('_')[1]);
        e.preventDefault();
        $("#search-form").submit();
    });

    $("#search-form").submit(function (event) {
        let facility, underFacility, investor, dateBegin, dateEnd;
        facility = $('#fFacilities').find(":selected").val();
        underFacility = $('#uFacilities').find(":selected").val();
        investor = $('#investors').find(":selected").val();
        dateBegin = $('#beginPeriod').val();
        dateEnd = $('#endPeriod').val();

        let filters = [
            {
                "fFacilities": facility
            },
            {
                "uFacilities": underFacility
            },
            {
                "investors": investor
            },
            {
                "beginPeriod": dateBegin
            },
            {
                "endPeriod": dateEnd
            }
        ];

        sessionStorage.setItem("filters", JSON.stringify(filters));

    });

    $('#facilities').change(function () {
        var facility = $(this).val();
        getUnderFacilitiesFromLocalStorage(facility, 'underFacilities');
    });

    $('#fFacilities').change(function () {
        var facility = $(this).val();
        getUnderFacilitiesFromLocalStorage(facility, 'uFacilities');
    });

    $(document).on("change", "#typeClosing", function () {

        var typeClosing = $('#typeClosingRow');
        var reFacility = $('#sourceFacility');
        var reUnderFacility = $('#sourceUnderFacility');


        if ($(this).find(':selected').text() === 'Реинвестирование') {
            reFacility.css("display", "block");
            $("#dateRepRow").css("display", "block");
            $('#shareKindName').find('option:contains(Основная доля)').attr('selected', 'selected');
            reFacility.insertAfter(typeClosing);
            reUnderFacility.insertAfter(reFacility);
            $('#investorBuyerRow').css('display', 'none');
        } else if ($(this).find(':selected').text() === 'Перепродажа доли') {
            reFacility.css("display", "none");
            reUnderFacility.css("display", "none");
            $("#dateRepRow").css("display", "none");
            $('#investorBuyerRow').css('display', 'block');
        } else {
            reFacility.css("display", "none");
            reUnderFacility.css("display", "none");
            $("#dateRepRow").css("display", "none");
            $('#investorBuyerRow').css('display', 'none');
        }
    });

    $(document).on("change", "#typeClosing", function () {
        if ($(this).find(':selected').text() === 'Перепродажа доли') {
            $('#buyerRow').css('display', 'block');
        } else {
            $('#buyerRow').css('display', 'none');
        }
    });

    $(document).on("change", "#cashDetail", function () {

        var cashDetail = $('#cashDetailRow');
        var dateRep = $('#dateRepRow');
        var reFacility = $('#reFacility');
        var reUnderFacility = $('#reUnderFacility');

        if ($(this).find(':selected').text() === 'Реинвестирование с аренды') {
            dateRep.insertAfter(cashDetail);
            reFacility.insertAfter(dateRep);
            reUnderFacility.insertAfter(reFacility);
            reFacility.css("display", "block");
            reUnderFacility.css("display", "block");
            dateRep.css("display", "block");
            $('#shareKindName').find('option:contains(Основная доля)').attr('selected', 'selected');
        } else if ($(this).find(':selected').text() === 'Реинвестирование с продажи') {
            reFacility.insertAfter(cashDetail);
            reUnderFacility.insertAfter(reFacility);
            reFacility.css("display", "block");
            reUnderFacility.css("display", "block");
            dateRep.css("display", "none");
            $('#shareKindName').find('option:contains(Основная доля)').attr('selected', 'selected');
        } else {
            reFacility.css("display", "none");
            reUnderFacility.css("display", "none");
            dateRep.css("display", "none");
        }
    });

    $("#reFacilities").change(function () {
        var reFacility = $(this).val();
        getUnderFacilitiesFromLocalStorage(reFacility, 'reUnderFacilities');
    });

    let filters = JSON.parse(sessionStorage.getItem("filters"));

    $('#fFacilities option:eq(' + filters[0].fFacilities + ')').attr('selected', 'selected');
    getUnderFacilitiesFromLocalStorage(filters[0].fFacilities, 'uFacilities');
    $('#investors').val(filters[2].investors).change();
    $('#beginPeriod').val(filters[3].beginPeriod);
    $('#endPeriod').val(filters[4].endPeriod);
    $('#uFacilities option:contains(' + filters[1].uFacilities + ')').attr('selected', 'selected');
});

function allMoneyCashing() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let underFacility = $("#underFacilities").find("option:selected").text();
    if (underFacility === 'Выберите подобъект') {
        underFacility = null;
    }

    let search = ({
        user: {
            id: $("#investor").find("option:selected").val(),
            login: $("#investor").find("option:selected").text()
        },
        facilities: {
            id: $("#facilities").find("option:selected").val()
        },
        underFacilities: {
            underFacility: underFacility
        },
        commission: $('#cashing').val(),
        commissionNoMore: $('#commissionNoMore').val(),
        investorsCash: {
            facility: {
                id: $("#facilities").find("option:selected").val(),
                facility: $("#facilities").find("option:selected").text()
            },
            underFacility: {
                // id: $("#underFacilities").find("option:selected").val(),
                underFacility: $("#underFacilities").find("option:selected").text()
            },
            investor: {
                id: $("#investor").find("option:selected").val(),
                login: $("#investor").find("option:selected").text()
            },
            dateGivedCash: $('#dateGivedCash').val(),
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
            slideBox(data.message);

            $('#facilities').prop('selectedIndex', 0);
            $('#underFacilities').prop('selectedIndex', 0);
            $('#investor').prop('selectedIndex', 0);
            $('#cash').val('');
            $('#dateGivedCash').val('');
            $('#cashing').val('');
            $('#commissionNoMore').val('');


        },
        error: function (e) {
            closeLoader();
            display(e);
        },
        done: function (e) {
            enableSearchButton(true);
        }
    });
}

function prepareCashSave(what) {
    var invBuyer = $('#investorBuyer');
    var investorBuyer = {
        id: invBuyer.find(':selected').val(),
        login: invBuyer.find(':selected').text()
    };

    if (investorBuyer.id === '0') {
        investorBuyer = null;
    }

    var cashId = $('#id').val();

    var facilities = $('#facilities');
    var facility = {id: facilities.find(':selected').val(), facility: facilities.find(':selected').text()};

    var underFacilities = $('#underFacilities');
    var underFacility = {
        id: underFacilities.find(':selected').attr('id'),
        underFacility: underFacilities.find(':selected').text()
    };
    if (underFacility.id === '0') {
        underFacility = null;
    }

    var investors = $('#investor');
    var investor = {id: investors.find(':selected').val(), login: investors.find(':selected').text()};

    var givedCash = $('#cash').val();

    var dateGivedCash = new Date($('#dateGivedCash').val()).getTime();

    var cashSources = $('#cashSrc');
    var cashSource = {id: cashSources.find(':selected').val(), cashSource: cashSources.find(':selected').text()};
    if (cashSource.id === '0') {
        cashSource = null;
    }

    var cashTypes = $('#cashTyp');
    var cashType = {id: cashTypes.find(':selected').val(), cashType: cashTypes.find(':selected').text()};
    if (cashType.id === '0') {
        cashType = null;
    }

    var newCashDetails = $('#cashDetail');
    var newCashDetail = {
        id: newCashDetails.find(':selected').val(),
        newCashDetail: newCashDetails.find(':selected').text()
    };
    if (newCashDetail.id === '0') {
        newCashDetail = null;
    }

    var investorsTypes = $('#invType');
    var investorsType = {
        id: investorsTypes.find(':selected').val(),
        investorsType: investorsTypes.find(':selected').text()
    };
    if (investorsType.id === '0') {
        investorsType = null;
    }

    var dateClosingInvest = new Date($('#dateCloseInv').val()).getTime();

    var dateReport = new Date($('#dateRep').val()).getTime();

    var typeClosingInvests = $('#typeClosing');
    var typeClosingInvest = {
        id: typeClosingInvests.find(':selected').val(),
        typeClosingInvest: typeClosingInvests.find(':selected').text()
    };
    if (typeClosingInvest.id === '0') {
        typeClosingInvest = null;
    }

    var reFacilities = $('#sourceFacilities');
    var reFacility = {id: reFacilities.find(':selected').val(), facility: reFacilities.find(':selected').text()};
    if (reFacility.id === '0') {
        reFacility = null;
    }

    var reUnderFacilities = $('#sourceUnderFacilities');
    var reUnderFacility = {
        id: reUnderFacilities.find(':selected').attr('id'),
        underFacility: reUnderFacilities.find(':selected').text()
    };
    if (reUnderFacility.id === '0') {
        reUnderFacility = null;
    }

    var dateReinvest = dateClosingInvest;

    var shareKinds = $('#shareKindName');
    var shareKind = {id: shareKinds.find(':selected').val(), shareKind: shareKinds.find(':selected').text()};
    if (shareKind.id === '0') {
        shareKind = null;
    }

    var source = $('#source').val();
    if (source.length === 0) {
        source = null;
    }

    var cash = {
        id: cashId,
        facility: facility,
        underFacility: underFacility,
        investor: investor,
        givedCash: givedCash,
        dateGivedCash: dateGivedCash,
        cashSource: cashSource,
        cashType: cashType,
        newCashDetails: newCashDetail,
        investorsType: investorsType,
        dateClosingInvest: dateClosingInvest,
        typeClosingInvest: typeClosingInvest,
        shareKind: shareKind,
        dateReport: dateReport,
        sourceFacility: reFacility,
        source: source,
        sourceUnderFacility: reUnderFacility
    };

    var flag = true;
    if (cashId === '') {
        flag = false;
    }

    saveCash(cash, reFacility, reUnderFacility, dateReinvest, flag, investorBuyer, what);
}

function saveCash(cash, reFacility, reUnderFacility, dateReinvest, flag, invBuyer, what) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({
        "investorsCash": cash,
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
                window.location.href = '/investorscash';
            }

            $('#facilities').prop('selectedIndex', 0);
            $('#underFacilities').prop('selectedIndex', 0);
            $('#investor').prop('selectedIndex', 0);
            $('#cash').val(0);
            $('#dateGivedCash').val('');
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
        $('#dateGivedCash').prop('disabled', true);
        $('#cashSrc').prop('disabled', true);
        $('#cashTyp').prop('disabled', true);
        $('#cashDetail').prop('disabled', true);
        $('#invType').prop('disabled', true);
        $('#dateCloseInv').prop('disabled', false);
        $('#typeClosing').prop('disabled', false);
        $('#reFacilities').prop('disabled', false);
        $('#reUnderFacilities').prop('disabled', false);
        $('#dateRep').prop('disabled', true);
        $('#shareKindName').prop('disabled', true);
    } else if ($('#doubleCash').val() === 'true') {
        $('#facilities').prop('disabled', true);
        $('#underFacilities').prop('disabled', false);
        $('#investor').prop('disabled', true);
        $('#cash').prop('disabled', false);
        $('#dateGivedCash').prop('disabled', true);
        $('#cashSrc').prop('disabled', true);
        $('#cashTyp').prop('disabled', true);
        $('#cashDetail').prop('disabled', true);
        $('#invType').prop('disabled', true);
        $('#dateCloseInv').prop('disabled', true);
        $('#dateCloseInvRow').css('display', 'none');
        $('#typeClosing').prop('disabled', true);
        $('#typeClosingRow').css('display', 'none');
        $('#reFacilities').prop('disabled', true);
        $('#reUnderFacilities').prop('disabled', true);
        $('#dateRep').prop('disabled', true);
        $('#shareKindName').prop('disabled', true);
    } else if ($('#edit').val() === 'true') {
        var cashDetail = $('#cashDetail');
        var reFacility = $('#sourceFacility');
        var reUnderFacility = $('#sourceUnderFacility');
        var dateRep = $('#dateRepRow');

        $('#facilities').prop('disabled', false);
        $('#underFacilities').prop('disabled', false);
        $('#investor').prop('disabled', false);
        $('#cash').prop('disabled', false);
        $('#dateGivedCash').prop('disabled', false);
        $('#cashSrc').prop('disabled', false);
        $('#cashTyp').prop('disabled', false);

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
        $('#invType').prop('disabled', false);
        $('#dateCloseInv').prop('disabled', false);
        $('#typeClosing').prop('disabled', false);
        $('#reFacilities').prop('disabled', false);
        $('#reUnderFacilities').prop('disabled', false);
        $('#dateRep').prop('disabled', false);
    } else if ($('#newCash').val() === 'true') {
        $('#facilities').prop('disabled', false);
        $('#underFacilities').prop('disabled', false);
        $('#investor').prop('disabled', false);
        $('#cash').prop('disabled', false);
        $('#dateGivedCash').prop('disabled', false);
        $('#cashSrc').prop('disabled', false);
        $('#cashTyp').prop('disabled', false);
        $('#cashDetail').prop('disabled', false);
        $('#invType').prop('disabled', false);
        $('#dateCloseInv').prop('disabled', true);
        $('#dateCloseInvRow').css('display', 'none');
        $('#typeClosing').prop('disabled', true);
        $('#typeClosingRow').css('display', 'none');
        $('#reFacilities').prop('disabled', false);
        $('#reUnderFacilities').prop('disabled', false);
        $('#dateRep').prop('disabled', false);
    }
}

function enableFields() {
    $('#facilities').prop('disabled', false);
    $('#underFacilities').prop('disabled', false);
    $('#investor').prop('disabled', false);
    $('#cash').prop('disabled', false);
    $('#dateGivedCash').prop('disabled', false);
    $('#cashSrc').prop('disabled', false);
    $('#cashTyp').prop('disabled', false);
    $('#cashDetail').prop('disabled', false);
    $('#invType').prop('disabled', false);
    $('#dateCloseInv').prop('disabled', false);
    $('#typeClosing').prop('disabled', false);
    $('#reFacilities').prop('disabled', false);
    $('#reUnderFacilities').prop('disabled', false);
    $('#dateRep').prop('disabled', false);
}

function prepareFilteredCashed() {
    var facility = $('#fFacilities').find(':selected').text();
    var underFacility = $('#uFacilities').find(':selected').text();
    var investor = $('#investors').find(':selected').text();
    var dateBegin = $('#beginPeriod').val();
    var dateEnd = $('#endPeriod').val();
    var url = window.location.href;
    var pageNumber = url.split('investorscash/')[1];
    filteredInvestorsCash(facility, underFacility, investor, dateBegin, dateEnd, pageNumber);
}

function prepareFilter() {
    var facility = $('#fFacilities').find(':selected').text();
    var underFacility = $('#uFacilities').find(':selected').text();
    var investor = $('#investors').find(':selected').text();
    var dateBegin = $('#beginPeriod').val();
    var dateEnd = $('#endPeriod').val();

    if (facility !== 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#investorsCash tbody', 1, facility);
        apply_filter('#investorsCash tbody', 2, underFacility);
        apply_filter('#investorsCash tbody', 3, investor);
    } else if (facility !== 'Выберите объект' && underFacility === 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#investorsCash tbody', 1, facility);
    } else if (facility === 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#investorsCash tbody', 2, underFacility);
    } else if (facility === 'Выберите объект' && underFacility === 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#investorsCash tbody', 3, investor);
    } else if (facility === 'Выберите объект' && underFacility === 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#investorsCash tbody', 1, 'any');
    } else if (facility !== 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor === 'Выберите инвестора') {
        filters = [];
        apply_filter('#investorsCash tbody', 1, facility);
        apply_filter('#investorsCash tbody', 2, underFacility);
    } else if (facility !== 'Выберите объект' && underFacility === 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#investorsCash tbody', 1, facility);
        apply_filter('#investorsCash tbody', 3, investor);
    } else if (facility === 'Выберите объект' && underFacility !== 'Выберите подобъект' && investor !== 'Выберите инвестора') {
        filters = [];
        apply_filter('#investorsCash tbody', 2, underFacility);
        apply_filter('#investorsCash tbody', 3, investor);
    }
    if (dateBegin === '' && dateEnd === '') {
        filters = [];
        apply_date_filter('#investorsCash tbody', 5, min, max, "any");
    } else {
        filters = [];
        apply_date_filter('#investorsCash tbody', 5, dateBegin, dateEnd, "not");
    }
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

function moveFields(mAttribute) {

    var facilities = $('#facilitiesRow');
    var underFacilities = $('#underFacilitiesRow');
    var investor = $('#investorRow');
    var cash = $('#cashRow');
    var dateGivedCash = $('#dateGivedCashRow');
    var cashSrc = $('#cashSrcRow');
    var cashTyp = $('#cashTypRow');
    var cashDetail = $('#cashDetailRow');
    var invType = $('#invTypeRow');
    var dateCloseInv = $('#dateCloseInvRow');
    var typeClosing = $('#typeClosingRow');
    var dateRep = $('#dateRepRow');
    var reFacility = $('#reFacility');
    var reUnderFacility = $('#reUnderFacility');
    var shareKindName = $('#shareKindNameRow');

    switch (mAttribute) {
        case "newCash":
            underFacilities.insertAfter(facilities);
            investor.insertAfter(underFacilities);
            cash.insertAfter(investor);
            dateGivedCash.insertAfter(cash);
            cashSrc.insertAfter(dateGivedCash);
            cashTyp.insertAfter(cashSrc);
            cashDetail.insertAfter(cashTyp);
            invType.insertAfter(cashDetail);
            shareKindName.insertAfter(invType);
            dateRep.insertAfter(shareKindName);
            dateCloseInv.insertAfter(dateRep);
            typeClosing.insertAfter(dateCloseInv);
            reFacility.insertAfter(dateRep);
            reUnderFacility.insertAfter(reFacility);
            break;
        case "edit":
            underFacilities.insertAfter(facilities);
            investor.insertAfter(underFacilities);
            cash.insertAfter(investor);
            dateGivedCash.insertAfter(cash);
            cashSrc.insertAfter(dateGivedCash);
            cashTyp.insertAfter(cashSrc);
            cashDetail.insertAfter(cashTyp);
            dateRep.insertAfter(cashDetail);
            reFacility.insertAfter(dateRep);
            reUnderFacility.insertAfter(reFacility);
            invType.insertAfter(reUnderFacility);
            shareKindName.insertAfter(invType);
            dateCloseInv.insertAfter(shareKindName);
            typeClosing.insertAfter(dateCloseInv);

            break;

        case "doubleCash":
        case "closeCash":
            underFacilities.insertAfter(facilities);
            investor.insertAfter(underFacilities);
            cash.insertAfter(investor);
            dateGivedCash.insertAfter(cash);
            cashSrc.insertAfter(dateGivedCash);
            cashTyp.insertAfter(cashSrc);
            cashDetail.insertAfter(cashTyp);
            invType.insertAfter(cashDetail);
            shareKindName.insertAfter(invType);
            dateRep.insertAfter(shareKindName);
            dateCloseInv.insertAfter(dateRep);
            typeClosing.insertAfter(dateCloseInv);
            reFacility.insertAfter(dateRep);
            reUnderFacility.insertAfter(reFacility);
            break;
    }
}

function enableSearchButton(flag) {
    $("#btn-search").prop("disabled", flag);
}

function display(data) {
    var json = JSON.stringify(data, null, 4);
    $('#investorsCash').html(json);
}

function checkChecked() {
    return $('table#investorsCash').find('[type="checkbox"]:checked:not(:disabled)').length;
}

function prepareSaveCash() {
    var cashes = [];
    var cash;
    var givedCash;
    var dateGived;
    var facility;
    var underFacility;
    var investor;
    var investorsType;
    var dateReport;
    var dateClose;
    var shareKind;
    var sourceFacility;
    var sourceUnderFacility;
    var reinvestData = $('form#reInvestData');
    var reinvestIdList = [];
    var sourceId;

    dateClose = $('#dateClose').val();
    dateGived = new Date(dateClose).getTime();
    dateClose = dateGived;
    if (dateClose.length < 10) {
        $('#dateCloseErr').css('display', 'block');
    } else {
        $('#dateCloseErr').css('display', 'none');
    }

    facility = {
        id: reinvestData.find('#srcFacilities').val(),
        facility: $('#srcFacilities').find('option:selected').text()
    };

    underFacility = {
        id: reinvestData.find('#srcUnderFacilities').val(),
        underFacility: $('#srcUnderFacilities').find('option:selected').text()
    };

    if (facility.facility.indexOf('Выберите объект') >= 0) {
        $('#facilityErr').css('display', 'block');
        return false;
    } else {
        $('#facilityErr').css('display', 'none');
    }

    if (underFacility.underFacility.indexOf('Выберите подобъект') >= 0) {
        $('#underFacilityErr').css('display', 'block');
        return false;
    } else {
        $('#underFacilityErr').css('display', 'none');
    }

    shareKind = {
        id: reinvestData.find('#shareKindName').val(),
        shareKind: $('#shareKindName').find('option:selected').text()
    };

    if (shareKind.shareKind.indexOf('Выберите вид доли') >= 0) {
        $('#shareKindErr').css('display', 'block');
        return false;
    } else {
        $('#shareKindErr').css('display', 'none');
    }

    showLoader();
    var current;

    $('#reInvestModal').modal('hide');
    $('#reinvestAll').prop('disabled', true);
    $('table#investorsCash').find('> tbody').find('> tr').each(function (i) {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            $(this).closest('tr').find('input:checkbox').prop('disabled', true);
            current = $(this).closest('tr');
            reinvestIdList.push(current.attr('id'));
            $('#' + current.attr('id') + ' input:checkbox').prop('disabled', true);

            sourceId = current.attr('id');

            sourceFacility = {
                id: current.children('td:eq(0)').attr('data-facility-id'),
                facility: current.children('td:eq(0)').text()
            };

            sourceUnderFacility = {
                id: current.children('td:eq(1)').attr('data-under-facility-id'),
                underFacility: current.children('td:eq(1)').text()
            };

            if (sourceUnderFacility.underFacility.length === 0) {
                sourceUnderFacility = null;
            }

            dateReport = current.children('td:eq(4)').attr('data-report-date');
            //tmpDate = dateReport.split(".");
            //dateReport = new Date(parseInt(tmpDate[2]), parseInt(tmpDate[1]) - 1, parseInt(tmpDate[0])).getTime();

            investor = {
                id: current.children('td:eq(2)').attr('data-investor-id'),
                login: current.children('td:eq(2)').text()
            };

            givedCash = current.children('td:eq(3)').attr('data-gived-cash');

            cash = {
                id: null,
                givedCash: givedCash,
                dateGivedCash: dateGived,
                facility: facility,
                investor: investor,
                cashSource: null,
                cashType: null,
                newCashDetails: null,
                investorsType: investorsType,
                underFacility: underFacility,
                dateClosingInvest: null,
                typeClosingInvest: null,
                shareKind: shareKind,
                sourceId: sourceId,
                source: sourceId.toString(),
                dateReport: dateReport,
                sourceFacility: sourceFacility,
                sourceUnderFacility: sourceUnderFacility,
                sourceFlowsId: null,
                room: null
            };

            cashes.push(cash);

        });
    });

    saveReCash(cashes, reinvestIdList, dateClose);

}

function saveReCash(cashes, reinvestIdList, dateClose) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
        investorsCashList: cashes,
        reinvestIdList: reinvestIdList,
        dateClose: dateClose
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: '../saveReInvCash',
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
            console.log(e);
        },
        done: function (e) {
            enableSearchButton(true);
        }
    });
}

function prepareDivideCash() {
    showLoader();
    var err = false;

    var divideData = $('form#divideData');
    var cash;
    var cashes = [];
    var id;
    var facility;
    var underFacility;
    var investor;
    var givedCash;
    var dateGivedCash;
    var cashSource;
    var cashType;
    var newCashDetails;
    var investorsType;
    var dateClosingInvest;
    var typeClosingInvest;
    var shareKind;
    var dateReport;
    var sourceFacility;
    var sourceUnderFacility;
    var room;
    var reUnderFacility;
    var excludedUnderFacilities = [];

    $('#underFacilitiesList').find('option:selected').each(function () {
        var underFacility = {
            id: $(this).attr('id'),
            underFacility: $(this).text()
        };
        excludedUnderFacilities.push(underFacility);
    });

    reUnderFacility = {
        id: divideData.find('#underFacilities').find('option:selected').attr('id'),
        underFacility: divideData.find('#underFacilities').find('option:selected').text()
    };

    if (reUnderFacility.underFacility.indexOf('Выберите подобъект') >= 0) {
        $('#underFacilityErr').css('display', 'block');
        err = true;
    } else {
        $('#underFacilityErr').css('display', 'none');
        err = false;
    }

    if (err) {
        closeLoader();
        return false;
    }
    var current;

    $('#divideModal').modal('hide');
    blockUnblockDropdownMenus('block');
    $('table#investorsCash').find('> tbody').find('> tr').each(function (i) {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            $(this).closest('tr').find('input:checkbox').prop('disabled', true);
            current = $(this).closest('tr');

            $('#' + current.attr('id') + ' input:checkbox').prop('disabled', true);

            id = current.attr('id');
            facility = {
                id: current.children('td:eq(0)').attr('data-facility-id'),
                facility: current.children('td:eq(0)').text()
            };

            underFacility = {
                id: current.children('td:eq(1)').attr('data-under-facility-id'),
                underFacility: current.children('td:eq(1)').text()
            };
            if (underFacility.underFacility.length === 0) {
                underFacility = null;
            }

            investor = {
                id: current.children('td:eq(2)').attr('data-investor-id'),
                login: current.children('td:eq(2)').text()
            };

            givedCash = current.children('td:eq(3)').attr('data-gived-cash');
            dateGivedCash = current.children('td:eq(4)').attr('data-report-date');
            //dateGivedCash = new Date(dateGivedCash).getTime();

            cashSource = {
                id: current.children('td:eq(5)').attr('data-cash-source-id'),
                cashSource: current.children('td:eq(5)').text()
            };
            if (cashSource.cashSource.length === 0) {
                cashSource = null;
            }
            cashType = {
                id: current.children('td:eq(6)').attr('data-cash-type-id'),
                cashType: current.children('td:eq(6)').text()
            };
            if (cashType.cashType.length === 0) {
                cashType = null;
            }
            newCashDetails = {
                id: current.children('td:eq(7)').attr('data-cash-details-id'),
                newCashDetail: current.children('td:eq(7)').text()
            };
            if (newCashDetails.newCashDetail.length === 0) {
                newCashDetails = null;
            }
            investorsType = {
                id: current.children('td:eq(8)').attr('data-investors-type-id'),
                investorsType: current.children('td:eq(8)').text()
            };
            if (investorsType.investorsType.length === 0) {
                investorsType = null;
            }

            shareKind = {
                id: current.children('td:eq(11)').attr('data-share-kind-id'),
                shareKind: current.children('td:eq(11)').text()
            };
            if (shareKind.shareKind.length === 0) {
                shareKind = null;
            }
            dateReport = current.children('td:eq(12)').attr('data-date-report');
            if (dateReport.length === 0) {
                dateReport = null;
            }

            sourceFacility = {
                id: current.children('td:eq(13)').attr('data-source-facility-id'),
                facility: current.children('td:eq(13)').text()
            };
            if (sourceFacility.facility.length === 0) {
                sourceFacility = null;
            }
            sourceUnderFacility = {
                id: current.children('td:eq(14)').attr('data-source-under-id'),
                underFacility: current.children('td:eq(14)').text()
            };
            if (sourceUnderFacility.underFacility.length === 0) {
                sourceUnderFacility = null;
            }
            room = {
                id: current.children('td:eq(15)').attr('data-room-id'),
                room: current.children('td:eq(15)').text()
            };
            if (room.room.length === 0) {
                room = null;
            }

            cash = {
                id: id,
                facility: facility,
                underFacility: underFacility,
                investor: investor,
                givedCash: givedCash,
                dateGivedCash: dateGivedCash,
                cashSource: cashSource,
                cashType: cashType,
                newCashDetails: newCashDetails,
                investorsType: investorsType,
                dateClosingInvest: dateClosingInvest,
                typeClosingInvest: typeClosingInvest,
                shareKind: shareKind,
                dateReport: dateReport,
                sourceFacility: sourceFacility,
                sourceUnderFacility: sourceUnderFacility,
                sourceFlowsId: null,
                room: room,
                sourceId: null
            };

            cashes.push(cash);

        });
    });

    saveDivideCash(cashes, reUnderFacility, excludedUnderFacilities);
}

function saveDivideCash(cashes, reUnderFacility, excludedUnderFacilities) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
        investorsCashList: cashes,
        reUnderFacility: reUnderFacility,
        underFacilitiesList: excludedUnderFacilities
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "../saveDivideCash",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 1000000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            $('#msg').html(data.message);
            $('#msg-modal').modal('show');
        },
        error: function (e) {
            alert(e);
            console.log(e);
        },
        done: function (e) {
            enableSearchButton(true);
        }
    });
}


function deleteCash(cashIdList) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var search = ({
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
            closeLoader();
            slideBox(data.message);
            //$('#msg').html(data.message);
            //$('#msg-modal').modal('show');
        })
        .fail(function (e) {
            console.log(e);
        })
        .always(function () {
            console.log('Закончили!');
        });

}

function prepareCloseCash() {

    var what = "closeCash";
    var invBuyer = $('#buyer');
    var investorBuyer = {
        id: invBuyer.find(':selected').val(),
        login: invBuyer.find(':selected').text()
    };

    if (investorBuyer.id === '0') {
        investorBuyer = null;
    }

    var dateClosingInvest = new Date($('#dateClosing').val()).getTime();

    var typeClosingInvests = $('#typeClosing');
    var typeClosingInvest = {
        id: typeClosingInvests.find(':selected').val(),
        typeClosingInvest: typeClosingInvests.find(':selected').text()
    };
    if (typeClosingInvest.id === '0') {
        typeClosingInvest = null;
    }

    var cashIdList = [];
    $('table#investorsCash').find('> tbody').find('> tr').each(function () {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            cashIdList.push($(this).closest('tr').attr('id'));
            $(this).closest('tr').remove();
        })
    });

    closeCash(cashIdList, investorBuyer, dateClosingInvest, what);
}

function closeCash(cashIdList, invBuyer, dateClosingInvest, what) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({
        "cashIdList": cashIdList,
        "user": invBuyer,
        "dateReinvest": dateClosingInvest,
        "what": what
    });

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "../closeCash",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#closeModal').modal('hide');
            closeLoader();
            slideBox(data.message);

        },
        error: function (e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            slideBox(data.message);
        }
    });
}

function blockMenus() {
    var current;
    $('table#investorsCash').find('> tbody').find('> tr').each(function (i) {
        current = $(this).closest('tr');
        if (current.children('td:eq(7)').text() === 'Реинвестирование с аренды') {
            current.find('#liEdit').addClass('disabled');
            current.find('#liDivide').addClass('disabled');
        }
    });
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

function blockDeleteLink() {
    let current;
    $('table#investorsCash tbody tr td:contains("Вывод")').each(function () {
        current = $(this).closest('tr');
        if ($(this).text() === 'Вывод') current.find('#liDelete').addClass('disabled').find('a#del').css('color', '');
    });
}

function blockActions() {
    $('table#investorsCash').find('> tbody').find('> tr').each(function () {
        $(this).find(':checkbox:disabled').each(function () {
            $(this).closest('tr').find('#liDivide').addClass('disabled');
            $(this).closest('tr').find('#liDouble').addClass('disabled');
            $(this).closest('tr').find('#liEdit').addClass('disabled');
            $(this).closest('tr').find('#liDelete').addClass('disabled').find('a#del').css('color', '');
        })
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

function filteredInvestorsCash(facility, underFacility, investor, dateBegin, dateEnd, pageNumber) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({
        "facility": facility,
        "underFacility": underFacility,
        "investor": investor,
        "dateStart": dateBegin,
        "dateEnd": dateEnd,
        "pageNumber": pageNumber
    });

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "../investorsCash",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader();
            // $('#popup_modal_form').find('#message').append(data.message);
            // showPopup();
            // closePopup();
        },
        error: function (e) {
            $('#popup_modal_form').find('#message').append(e.error);
            closeLoader();
            // showPopup();
            // closePopup();
        }
    });
}

function linkHasClass(link) {
    if (link.hasClass('disabled')) return true;
}