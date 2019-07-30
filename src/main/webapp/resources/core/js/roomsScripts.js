jQuery(document).ready(function ($) {
    let sold = $('#sold').val() === 'Да' ? 'block' : 'none';
    $('#dateSaleRow').css('display', sold);
    $('#dateSaleErr').css('display', 'none');

    $(document).on('change', '#sold', function () {
        if ($(this).find('option:selected').val() === 'Нет') {
            $('#soldHid').prop('checked', false);
            $('#dateSaleRow').attr('style', 'display: none');
        } else {
            $('#soldHid').prop('checked', true);
            $('#dateSaleRow').removeAttr('style');
        }
    });

    $(document).on('submit', '#rooms', function (e) {
        if ($('#soldHid').prop('checked') && $('#dateSaleRow').find('#dateSale').val().length < 10) {
            e.preventDefault();
            $('#dateSaleErr').css('display', 'block');
        } else {
            $('#dateSaleErr').css('display', 'none');
        }
    })
});