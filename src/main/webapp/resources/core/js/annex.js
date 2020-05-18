jQuery(document).ready(function ($) {
    blurElement($('.out'), 4);
    $('#readAnnex').css('z-index', '1000001');
    $('#look').attr('disabled', false);
    disableScroll(true);
});

function blurElement(element, size) {
    let filterVal = 'blur(' + size + 'px)';
    $(element).css({
        'filter': filterVal,
        'webkitFilter': filterVal,
        'mozFilter': filterVal,
        'oFilter': filterVal,
        'msFilter': filterVal,
        'transition': 'all 0.5s ease-out',
        '-webkit-transition': 'all 0.5s ease-out',
        '-moz-transition': 'all 0.5s ease-out',
        '-o-transition': 'all 0.5s ease-out'
    });
}

function disableScroll(disable) {
    let body = $('body');
    if (disable === true) {
        body.addClass('stop-scrolling');
        body.bind('touchmove', function(e){e.preventDefault()});
    } else {
        body.removeClass('stop-scrolling');
        body.unbind('touchmove');
    }

}
