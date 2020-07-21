jQuery(document).ready(function ($) {
    getFiltersFromLS((window.location.pathname + '').split("/")[1]);

    $(document).on('click', 'a[name*="page_"]', function (e) {
        e.preventDefault();
        $('#pageNumber').val(parseInt($(this).attr('id')) - 1);
        let pageSize = 100;
        if ($('#all').prop('checked')) pageSize = 0;
        $('#pageSize').val(pageSize);
        $('#filter-form').submit();
    });

    $("#filter-form").submit(function () {
        enableSearchButton(false);
        populateFilters((window.location.pathname + '').split("/")[1]);
        enableSearchButton(true);
    });

    $(document).on('click', '#bth-clear', function (e) {
        e.preventDefault();
        $('#investors').find('option:eq(0)').prop('selected', true);
        $('#partners').find('option:eq(0)').prop('selected', true);
        $('#kins').find('option:eq(0)').prop('selected', true);
        $('.selectpicker').selectpicker('refresh');
        $('#beginPeriod').val('');
        $('#endPeriod').val('');
        populateFilters((window.location.pathname + '').split("/")[1]);
        window.location.href = window.location.pathname;
    });
});