$(document).ready(function() {
    $('.hideShow').hide();
    $('#uf').css('color', 'rgb(0, 0, 0)');
    $('#pay').css('color', 'rgb(0, 0, 0)');
    $(document).on('change', '[data-toggle="toggle"]', function (event) {
        event.preventDefault();
        $(this).parents().next('.hideShow').toggle();
        var labelText = $('label[for='+  this.id  +']').text();
        var show = false;

        if(labelText === 'Показать'){
            $('label[for='+  this.id  +']').text('Скрыть');
        }else{
            $('label[for='+  this.id  +']').text('Показать');
        }

        $('label:contains("Скрыть")').each(function(i, el){
            show = true;
        });

        if(show){
            $('#uf').css('color', 'rgb(0, 0, 0)');
        }else{
            $('#uf').css('color', 'rgb(255, 255, 255)');
        }
    });

    $(document).on('change', '[data-toggle="toggleUnd"]', function (event) {
        event.preventDefault();
        $(this).parents().next('.hideShowUnd').toggle();
        var labelText = $('label[for='+  this.id  +']').text();
        var show = false;

        if(labelText === 'Показать'){
            $('label[for='+  this.id  +']').text('Скрыть');
        }else{
            $('label[for='+  this.id  +']').text('Показать');
        }

        $('label:contains("Скрыть")').each(function(i, el){
            show = true;
        });

        if(show){
            $('#ufUnd').css('color', 'rgb(0, 0, 0)');
        }else{
            $('#ufUnd').css('color', 'rgb(255, 255, 255)');
        }
    });

    $(document).on('click', '.levelUnderFacilities', function(){

        var showMore = false;
        $(this).nextUntil('.levelUnderFacilities').toggle();
        var tdText = $(this).find('td:eq(4)').text();
        if(tdText === 'Показать'){
            $(this).find('td:eq(4)').text('Скрыть');
        }else{
            $(this).find('td:eq(4)').text('Показать');
        }

        $('td.more:contains("Скрыть")').each(function(i, el){
            showMore = true;
        });
        if(showMore){
            $('#pay').css('color', 'rgb(0, 0, 0)');
        }else{
            $('#pay').css('color', 'rgb(255, 255, 255)');
        }
    });
});
