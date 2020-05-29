let stompClient = null;


function connect() {
    let socket = new SockJS('/status');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/progress/status', function (status) {
            showStatus(status.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function send() {
    stompClient.send("/task/status", {}, $("#name").val());
}

function showStatus(message) {
    $("#msg").html(message);
    if (message === 'OK') {
        setTimeout(function () {
            $('#msg-modal').modal('hide');
        }, 3000);
        disconnect();
    }
}
