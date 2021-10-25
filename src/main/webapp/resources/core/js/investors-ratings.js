jQuery(document).ready(function ($) {
    onCalculateRatingClick()
})

function onCalculateRatingClick() {
    $('#calculate').on('click', function (event) {
        event.preventDefault()
        if (checkRatingDates()) {
            calculateRatings()
        }
    })
}

function checkRatingDates() {
    let firstRatingDate = $('#first-rating-date').val()
    if (firstRatingDate.length !== 10) {
        showPopup("Укажите дату первого рейтинга")
        return false
    }
    let isNotDate = isNaN(Date.parse(firstRatingDate))
    if (isNotDate) {
        showPopup("Укажите дату первого рейтинга")
        return false
    }
    let secondRatingDate = $('#second-rating-date').val()
    if (secondRatingDate.length !== 10) {
        showPopup("Укажите дату второго рейтинга")
        return false
    }
    isNotDate = isNaN(Date.parse(firstRatingDate))
    if (isNotDate) {
        showPopup("Укажите дату второго рейтинга")
        return false
    }
    return true
}

function calculateRatings() {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    showLoader()
    let ratingFilter = {
        firstRatingDate: Date.parse($('#first-rating-date').val()),
        secondRatingDate: Date.parse($('#second-rating-date').val())
    }

    $.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "rating/calculate",
        data: JSON.stringify(ratingFilter),
        dataType: 'json',
        timeout: 0,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            closeLoader()
            showPopup(data.message);
        },
        error: function (jqXHR) {
            closeLoader()
            showPopup(jqXHR.responseText)
        }
    });
}
