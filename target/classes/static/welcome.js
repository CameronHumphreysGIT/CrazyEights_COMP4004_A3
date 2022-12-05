var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        send();
    });
}

function send() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
    location.href='game.html';
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#join" ).click(function() { connect(); });
});