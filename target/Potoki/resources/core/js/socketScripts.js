var stompClient = null;
jQuery(document).ready(function ($) {
    $('#tmr').css('display', 'none');
    var socket = new SockJS('/turn');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/support/messages', function(){
            showGreeting(/*JSON.parse(greeting.body).content*/);
        });
    });
});

/*
function connect() {
    var socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function(greeting){
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}


function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
}
 */

function sendName() {
    var name = document.getElementById('name').value;
    stompClient.send("/server/turn", {}, JSON.stringify({ 'name': name }));
}

function showGreeting() {
    /*
    parseTime_bv(remain_bv);
    setInterval(parseTime_bv, 1000, remain_bv);
    */
    /*
    timeSock(message);
    setInterval(timeSock, 1000, message);
    */
    $('#tmr').css('display', 'block');
    var messages = "До отключения сайта осталось ";
    timeSockets(messages);

    setInterval(timeSockets, 1000, messages);


    /*
    var response = document.getElementById('tmr');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.html(p);
    */

}
/*
var i = 5;//время в сек.
function timeSock(message){
    $("#tmr").html('<p>' + message +
        ' через <b>' + i + '</b> секунд</p>');//визуальный счетчик
    i--;//уменьшение счетчика
    if (i < 0) window.location.href = "/welcome"; //редирект
}
*/

var timestamp = 300;
function timeSockets(message){
    if (timestamp < 0) timestamp = 0;

    var strMin = "минут";
    var strSecs = "секунд";
    //var day = Math.floor((timestamp/60/60) / 24);
    var hour = Math.floor(timestamp/60/60);
    var min = Math.floor((timestamp - hour*60*60)/60);
    var secs = Math.floor(timestamp - hour*60*60 - min*60);
    //var left_hour = Math.floor( (timestamp - day*24*60*60) / 60 / 60 );
    switch (min){
        case 1:
            strMin = "минута";
            break;
        case 2:
        case 3:
        case 4:
            strMin = "минуты";
            break;
        case 5:
            strMin = "минут";
            break;
    }

    var secSwitch = String(secs).substring(String(secs).length - 1);
    switch (secSwitch){
        case "1":
            strSecs = "секунда";
            break;
        case "2":
        case "3":
        case "4":
            strSecs = "секунды";
            break;
        case "5":
            strSecs = "секунд";
            break;
    }

    if(String(min).length <= 1)
        min = "0" + min;

    if(String(secs).length <= 1)
        secs = "0" + secs;

    $('#tmr').html(message + "<b>" + min + " " + strMin + " " + secs + " " + strSecs + "</b> ");
    timestamp--;
    if (timestamp < 0) window.location.href = "/welcome"; //редирект
}