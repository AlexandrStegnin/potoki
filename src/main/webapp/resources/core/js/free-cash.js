let AccountSummaryDTO = function () {
}

AccountSummaryDTO.prototype = {
    accountId: null,
    build: function (accountId) {
        this.accountId = accountId
    }
}

jQuery(document).ready(function ($) {
    showPageableResult()
    subscribeCheckAllClick()
    subscribeCheckboxChange()
    clearFilters()
    toggleAllRows()
    subscribeTxShowClick()
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
            toggleStateReinvestButton(!checked)
        });
        checkItBtn.data('checked', !checked)
    })
}

/**
 * Получить детализацию по счёту
 *
 * @param accSummaryDTO {AccountSummaryDTO} DTO для удаления сумм
 */
function getDetails(accSummaryDTO) {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "details",
        data: JSON.stringify(accSummaryDTO),
        dataType: 'json',
        timeout: 100000,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        }
    })
        .done(function (data) {
            console.log(data)
        })
        .fail(function (e) {
            showPopup('Что-то пошло не так [' + e.message + ']');
        })
        .always(function () {
            console.log('Закончили!');
        });

}

/**
 * Очистить фильтры
 */
function clearFilters() {
    $(document).on('click', '#btn-clear',function (e) {
        e.preventDefault()
        $('#owners').prop('selectedIndex', -1)
        $('.selectpicker').selectpicker('refresh')
        $('#all').prop('checked', false)
        $('#bth-search').click()
    })
}

/**
 * Отображать на 1 странице или постранично
 */
function toggleAllRows() {
    $('#all').on('change', function () {
        $('#bth-search').click()
    })
}

/**
 * Изменение состояния чекбоксов
 */
function subscribeCheckboxChange() {
    $(document).on('change', 'input:checkbox', function () {
        if ($(this).prop('checked')) {
            toggleStateReinvestButton(true)
        } else {
            let count = countChecked()
            toggleStateReinvestButton(count > 0)
        }
    })
}

/**
 * Посчитать кол-во отмеченных чекбоксов
 * @return {Number} кол-во отмеченных чекбоксов
 */
function countChecked() {
    return $('table#transactions').find('input:checkbox:checked').length
}

/**
 * Блокировать/разблокировать кнопку "Реинвестировать"
 *
 * @param enable {boolean} признак блокировать или разблокировать
 */
function toggleStateReinvestButton(enable) {
    if (enable) {
        $('#reinvest').removeClass('disabled')
    } else {
        $('#reinvest').addClass('disabled')
    }
}

/**
 * Нажатие кнопки "Просмотреть"
 */
function subscribeTxShowClick() {
    $('.tx-show').on('click', function () {
        let txId = $(this).data('tx-id')
        let accSummaryDTO = new AccountSummaryDTO()
        accSummaryDTO.build(txId)
        getDetails(accSummaryDTO)
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
