let filters = [];

jQuery(document).ready(function ($) {

    blockActions();
    blockDeleteLink();
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
        $('#investors').selectpicker('deselectAll'); //find('option:eq(0)').prop('selected', true);
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
        if ($('#all').prop('checked')) pageSize = 0;
        $('#pageSize').val(pageSize);
        $('#search-form').submit();
    });

    $('#investors').selectpicker({
        actionsBox: true,
        selectAllText: 'Выбрать всё',
        deselectAllText: 'Сбросить всё'
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
        deselectAllText: 'Сбросить всё'
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

    // $('#msg-modal').on('shown.bs.modal', function () {
    //     // if data-timer attribute is set use that, otherwise use default (7000)
    //     var timer = 3000;
    //     $(this).delay(timer).fadeOut(200, function () {
    //         $(this).modal('hide');
    //     });
    // });

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
        // showLoader();
        let cashIdList = [];
        let sourceIdList = [];
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
        $('#msg').html('Начинаем удаление денег...');
        $('#msg-modal').modal('show');
        connect();
        deleteCash(cashIdList);
    });

    $('a#del').on('click', function (event) {
        event.preventDefault();
        if (linkHasClass($(this).parent())) return false;
        // showLoader();
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
        $('#msg').html('Начинаем удаление денег...');
        $('#msg-modal').modal('show');
        connect();
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
        const checked = $('#checkIt').prop('checked');
        let noDivide = false;
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
            blockUnblockDropdownMenus('unblock', !noDivide);
        }
    });

    $(document).on('submit', '#reInvestData', function (event) {
        event.preventDefault();
        populateFilters((window.location.pathname + '').split("/")[1]);
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

    let url = window.location.href;
    if (url.indexOf('newinvestorscash') >= 0 || url.indexOf('edit-cash') >= 0 || url.indexOf('double-cash') >= 0 || url.indexOf('close-cash') >= 0) {
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
        // Disable the search button
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
            if ($(this).find(':selected').text() === 'Выберите вид закрытия') {
                $('#dateCloseInv').val('');
            }
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
    underFacility: '',
    build: function (id, underFacility) {
        this.id = id;
        this.underFacility = underFacility;
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

    // return false;
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
                id: investor.find("option:selected").val(),
                login: investor.find("option:selected").text()
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
            showPopup(data.message);
            window.location.href = '/investorscash';
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
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({
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
        $('#dateCloseInv').removeAttr('disabled');
        $('#dateCloseInvRow').removeAttr('disabled');
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

function moveFields(mAttribute) {

    let facilities = $('#facilitiesRow');
    let underFacilities = $('#underFacilitiesRow');
    let investor = $('#investorRow');
    let cash = $('#cashRow');
    let dateGivedCash = $('#dateGivedCashRow');
    let cashSrc = $('#cashSrcRow');
    let cashTyp = $('#cashTypRow');
    let cashDetail = $('#cashDetailRow');
    let invType = $('#invTypeRow');
    let dateCloseInv = $('#dateCloseInvRow');
    let typeClosing = $('#typeClosingRow');
    let dateRep = $('#dateRepRow');
    let reFacility = $('#reFacility');
    let reUnderFacility = $('#reUnderFacility');
    let shareKindName = $('#shareKindNameRow');
    let realDateGiven = $('#realDateGivenRow');

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
    var json = JSON.stringify(data, null, 4);
    $('#investorsCash').html(json);
}

function checkChecked() {
    return $('table#investorsCash').find('[type="checkbox"]:checked:not(:disabled)').length;
}

function prepareSaveCash() {
    let cashes = [];
    let cash;
    let givedCash;
    let dateGived;
    let facility;
    let underFacility;
    let investor;
    let investorsType;
    let dateReport;
    let dateClose;
    let shareKind;
    let sourceFacility;
    let sourceUnderFacility;
    let reinvestData = $('form#reInvestData');
    let reinvestIdList = [];
    let sourceId;

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
        id: reinvestData.find('#srcUnderFacilities option:selected').attr('id'),
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
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({
        investorsCashList: cashes,
        reinvestIdList: reinvestIdList,
        dateClose: dateClose
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: '/saveReInvCash',
        data: JSON.stringify(search),
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
            showPopup('Что-то пошло не так = [' + e.toLocaleString() + "]");
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
                id: investor.find("option:selected").val(),
                login: investor.find("option:selected").text()
            },
            dateGivedCash: $('#dateGivedCash').val(),
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
                window.location.href = '/investorscash';
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
    let divideData = $('form#divideData');
    let cash;
    let cashes = [];
    let id;
    let facility;
    let underFacility;
    let investor;
    let givedCash;
    let dateGivedCash;
    let cashSource;
    let cashType;
    let newCashDetails;
    let investorsType;
    let dateClosingInvest;
    let typeClosingInvest;
    let shareKind;
    let dateReport;
    let sourceFacility;
    let sourceUnderFacility;
    let room;
    let reUnderFacility;
    let reUnderFacilitiesList = [];
    let excludedUnderFacilities = [];

    $('#underFacilitiesList').find('option:selected').each(function () {
        let underFacility = {
            id: $(this).attr('id'),
            underFacility: $(this).text()
        };
        excludedUnderFacilities.push(underFacility);
    });
    divideData.find('#underFacilities').find('option:selected').each(function (ind, el) {
        reUnderFacility = {
            id: el.id,
            underFacility: el.value
        };
        reUnderFacilitiesList.push(reUnderFacility);
    });

    if (reUnderFacilitiesList.length === 0 || reUnderFacilitiesList[0].underFacility.indexOf('Выберите подобъект') >= 0) {
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
    let search = ({
        investorsCashList: cashes,
        reUnderFacility: reUnderFacility,
        underFacilitiesList: excludedUnderFacilities
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "saveDivideCash",
        data: JSON.stringify(search),
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
    let search = ({
        investorsCashList: cashes,
        reUnderFacilitiesList: reUnderFacilitiesList,
        underFacilitiesList: excludedUnderFacilities
    });

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "divide-multiple",
        data: JSON.stringify(search),
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
            // closeLoader();
            showPopup(data.message);
        })
        .fail(function (e) {
            showPopup('Что-то пошло не так [' + e.toLocaleString() + ']');
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

    let dateClosingInvest = new Date($('#dateClosing').val()).getTime();
    let realDateGiven = new Date($('#realDateGiven').val()).getTime();
    let typeClosingInvests = $('#typeClosing');
    let typeClosingInvest = {
        id: typeClosingInvests.find(':selected').val(),
        typeClosingInvest: typeClosingInvests.find(':selected').text()
    };
    if (typeClosingInvest.id === '0') {
        typeClosingInvest = null;
    }

    let cashIdList = [];
    $('table#investorsCash').find('> tbody').find('> tr').each(function () {
        $(this).find(':checkbox:checked').not(':disabled').each(function () {
            cashIdList.push($(this).closest('tr').attr('id'));
            $(this).closest('tr').remove();
        })
    });

    closeCash(cashIdList, investorBuyer, dateClosingInvest, what, realDateGiven);
}

function closeCash(cashIdList, invBuyer, dateClosingInvest, what, realDateGiven) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    let search = ({
        "cashIdList": cashIdList,
        "user": invBuyer,
        "dateReinvest": dateClosingInvest,
        "what": what,
        "realDateGiven": realDateGiven
    });

    showLoader();

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/closeCash",
        data: JSON.stringify(search),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            $('#closeModal').modal('hide');
            closeLoader();
            showPopup(data.message);
        },
        error: function (e) {
            showPopup(e.toLocaleString());
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
        else current.find('#liDelete').removeClass('disabled').addClass('active').find('a#del').css('cssText', 'color: #ff0000 !important');
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

function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}

// function slideBox(message) {
//     $('#slideBox').find('h4').html(message);
//     setTimeout(function () {
//         $('#slideBox').animate({'right': '52px'}, 500);
//     }, 500);
//     setTimeout(function () {
//         $('#slideBox').animate({'right': '-300px'}, 500);
//     }, 4000);
// }

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
        url: "/investorsCash",
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
