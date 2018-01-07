jQuery(document).ready(function ($) {
    var stompClient;
    var socket = new SockJS('/turn');
    stompClient = Stomp.over(socket);

    check();

    $('#someSwitchOptionWarning').change(function () {
        turnOff(this.checked ? "1" : "0");
    })
});



function turnOff(activate) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"switchSite" : activate});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "switch",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            if(data.message === "Сайт отключен"){
                sendMessage();
            }
        },
        error : function(e) {
            $('#popup_modal_form').find('#message').append(e.error);
            showPopup();
            closePopup();
        }
    });
}

function check() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var search = ({"switchSite" : ""});

    $.ajax({
        type : "POST",
        contentType : "application/json;charset=utf-8",
        url : "checkservice",
        data : JSON.stringify(search),
        dataType : 'json',
        timeout : 100000,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            $('#someSwitchOptionWarning').prop('checked', data.message === "1");
        },
        error : function(e) {
        }
    });
}

function sendMessage() {
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/support/messages', function(greeting){
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
    stompClient.send("/server/turn", {}, JSON.stringify({ 'name': 'Сайт будет отключен' }));
}