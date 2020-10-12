let AccountTxDTO = function () {}

AccountTxDTO.prototype = {
    id: null,
    facilityId: null,
    underFacilityId: null,
    dateReinvest: null,
    shareType: null,
    build: function (id, facilityId, underFacilityId, dateReinvest, shareType) {
        this.id = id
        this.facilityId = facilityId
        this.underFacilityId = underFacilityId
        this.dateReinvest = dateReinvest
        this.shareType = shareType
    }
}

jQuery(document).ready(function ($) {
    showPageableResult()
})

function showPageableResult() {
    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 1;
        $('#pageSize').val(pageSize);
        $('#filter-form').submit();
    });
}

/**
 * Показать сообщение
 *
 * @param message {String}
 */
function showPopup(message) {
    $('#msg').html(message);
    $('#msg-modal').modal('show');
    setTimeout(function () {
        $('#msg-modal').modal('hide');
    }, 3000);
}
