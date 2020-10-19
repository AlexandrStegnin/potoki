jQuery(document).ready(function ($) {

    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 0;
        $('#pageSize').val(pageSize);
        $('#filter-form').submit();
    });

    $(document).on('click', '#btn-clear', function (e) {
        e.preventDefault();
        $('#investors').find('option:eq(0)').prop('selected', true);
        $('#partners').find('option:eq(0)').prop('selected', true);
        $('#kins').find('option:eq(0)').prop('selected', true);
        $('.selectpicker').selectpicker('refresh');
        $('#beginPeriod').val('');
        $('#endPeriod').val('');
        $('#btn-search').click()
    });

    toggleAllRows()

});

function toggleAllRows() {
    $('#all').on('change', function () {
        $('#btn-search').click()
    })
}
