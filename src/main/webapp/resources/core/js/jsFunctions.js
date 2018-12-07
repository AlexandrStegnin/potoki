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
            option.innerText = item.underFacility;

            return option;

        });

        option = document.createElement('option');
        option.setAttribute('id', "0");
        option.innerText = "Выберите подобъект";
        options.unshift(option);
    }

    $('#' + uFacilitiesId)
        .find('option')
        .remove()
        .end()
        .append(options)
        .prop('selected', 'Выберите подобъект');
}

function getRoomsFromLocalStorage(underFacilityId) {
    let rooms = JSON.parse(localStorage.getItem('rooms'));
    let option;

    let options;

    if (parseInt(underFacilityId) === 0) {
        options = rooms.map(function (item) {

            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parrent-id', item.underFacilityId);
            option.innerText = item.room;

            return option;

        });
    } else {
        options = rooms.filter(function (item) {
            return item.underFacilityId === parseInt(underFacilityId);
        }).map(function (item) {

            option = document.createElement('option');
            option.setAttribute('id', item.id);
            option.setAttribute('data-parrent-id', item.underFacilityId);
            option.innerText = item.room;

            return option;

        });

        option = document.createElement('option');
        option.setAttribute('id', "0");
        option.innerText = "Выберите помещение";
        options.unshift(option);
    }

    $('#rooms')
        .find('option')
        .remove()
        .end()
        .append(options)
        .prop('selected', 'Выберите помещение');
}