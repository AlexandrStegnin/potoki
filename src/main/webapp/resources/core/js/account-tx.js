let AccountTxDTO = function () {
}

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
    subscribeCheckAllClick()
})

/**
 * Показать результаты при клике на номер страницы
 */
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
 * Выделить/снять выделение со всех чекбоксов на странице
 */
function subscribeCheckAllClick() {
    $(document).on('click', '#checkIt', function () {
        let checkItBtn = $('#checkIt');
        const checked = checkItBtn.data('checked');
        $('table#transactions').find('> tbody').find('> tr').each(function () {
            $(this).find(':checkbox:not(:disabled)').prop('checked', !checked);
        });
        checkItBtn.data('checked', !checked)
    })
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
