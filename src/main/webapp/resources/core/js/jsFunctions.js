const CHOOSE_ROOM = 'Выберите помещение';

function apply_filter(table, col, text) {
    filters[col] = text;

    $(table).find('tr').each(function (i) {
        $(this).data('passed', true);
    });

    for (let index in filters) {
        if (filters[index] !== 'any') {
            $(table).find('tr td:nth-child(' + index + ')').each(function () {
                if ($(this).text() === filters[index] && $(this).parent().data('passed')) {
                    $(this).parent().data('passed', true);
                } else {
                    $(this).parent().data('passed', false);
                }
            });
        }
    }

    $(table).find('tr').each(function (i) {
        if (!$(this).data('passed')) {
            $(this).hide();
        } else {
            $(this).show();
        }
    });
}

function populateStorageUnderFacilities(uFacilitiesId) {
    let underFacilities = [];
    $('#' + uFacilitiesId).find('option').each(function (i) {
        underFacilities.push({
            id: $(this).attr('id'),
            facilityId: $(this).data('parentId'),
            underFacility: $(this).text()
        })
    });
    let oldArray = JSON.parse(localStorage.getItem('uf'));
    if (oldArray === null || (oldArray.length <= underFacilities.length)) {
        localStorage.setItem('uf', JSON.stringify(underFacilities));
    }
}

function populateStorageRooms() {
    let rooms = [];
    $('#rooms').find('option').each(function (i) {
        rooms.push({
            id: $(this).attr('id'),
            underFacilityId: $(this).data('parentId'),
            room: $(this).text()
        })
    });
    localStorage.setItem('rooms', JSON.stringify(rooms));
}

function getUnderFacilitiesFromLocalStorage(facilityId, uFacilitiesId) {
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
            option.setAttribute('value', item.id);
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
            option.setAttribute('value', item.id);
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

function getMultipleUFFromLS(facilitiesList, uFacilitiesId) {
    let underFacilities;
    underFacilities = JSON.parse(localStorage.getItem('uf'));
    let option;

    let options;
    let finalOptions = [];
    if (underFacilities === null) populateStorageUnderFacilities(uFacilitiesId);
    $.each(facilitiesList, function (ind, el) {
        if (facilitiesList.length <= 1) {
            options = [];
        }
        options = underFacilities.filter(function (item) {
            return item.facilityId === parseInt(el);
        }).map(function (item) {
            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parent-id', item.facilityId);
            option.setAttribute('value', item.id);
            option.innerText = item.underFacility;
            return option;
        });

        $.each(options, function (ind, el) {
            finalOptions.push(el);
        });
    });
    option = document.createElement('option');
    option.setAttribute('id', "0");
    option.setAttribute('value', 'Без подобъекта');
    option.innerText = 'Без подобъекта';
    finalOptions.unshift(option);
    $('#' + uFacilitiesId)
        .find('option')
        .remove()
        .end()
        .append(finalOptions);
}

function getMultipleUFFromLSSelectPicker(facilitiesList, uFacilitiesId) {
    let underFacilities;
    underFacilities = JSON.parse(localStorage.getItem('uf'));
    let option;

    let options;
    let finalOptions = [];
    if (underFacilities === null) populateStorageUnderFacilities(uFacilitiesId);
    $.each(facilitiesList, function (ind, el) {
        if (facilitiesList.length <= 1) {
            options = [];
        }
        options = underFacilities.filter(function (item) {
            return item.facilityId === parseInt(el);
        }).map(function (item) {
            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parent-id', item.facilityId);
            option.setAttribute('value', item.underFacility);
            option.innerText = item.underFacility;
            return option;
        });

        $.each(options, function (ind, el) {
            finalOptions.push(el);
        });
    });
    option = document.createElement('option');
    option.setAttribute('id', "0");
    option.setAttribute('value', 'Без подобъекта');
    option.innerText = 'Без подобъекта';
    finalOptions.unshift(option);
    $('#' + uFacilitiesId).empty()
        .append(finalOptions)
        .selectpicker('refresh');
}

function getRoomsFromLocalStorage(underFacilityId) {
    let rooms = JSON.parse(localStorage.getItem('rooms'));
    let option;

    let options;

    if (parseInt(underFacilityId) === 0) {
        options = rooms.map(function (item) {
            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parent-id', item.underFacilityId);
            option.setAttribute('value', item.id);
            option.innerText = item.room;
            return option;

        });
    } else {
        options = rooms.filter(function (item) {
            return item.underFacilityId === parseInt(underFacilityId);
        }).map(function (item) {
            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parent-id', item.underFacilityId);
            option.setAttribute('value', item.id);
            option.innerText = item.room;
            return option;

        });

        option = document.createElement('option');
        option.setAttribute('id', "0");
        option.setAttribute('value', "0");
        option.innerText = CHOOSE_ROOM;
        options.unshift(option);
    }

    $('#rooms')
        .find('option')
        .remove()
        .end()
        .append(options)
        .prop('selected', CHOOSE_ROOM);
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
                    $(this).closest('tr').find('#aDouble').removeClass('disabled');
                    $(this).closest('tr').find('#aEdit').removeClass('disabled');
                    $(this).closest('tr').find('#aDelete').removeClass('disabled').find('a#del').css('cssText', 'color: #ff0000 !important');
                    $(this).removeAttr('checked').removeAttr('disabled');
                })
            });
            $('#unblock_operations').removeClass('btn-danger').addClass('btn-success').text('Заблокировать операции');
            break;
    }
}
