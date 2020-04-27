jQuery(document).ready(function ($) {
    $('#investors').selectpicker().on('changed.bs.select', function () {
        let options = $("#investors option");
        options.sort(function (a, b) {
            if ($(a).attr('data-lastName') > $(b).attr('data-lastName')) return 1;
            else if ($(a).attr('data-lastName') < $(b).attr('data-lastName')) return -1;
            else return 0;
        });
        if ($('#investors option:selected').text() === "Выберите инвестора") {
            $('#viewInvestorData').attr('disabled', 'true');
        } else {
            $('#viewInvestorData').removeAttr('disabled');
        }
    });
});
