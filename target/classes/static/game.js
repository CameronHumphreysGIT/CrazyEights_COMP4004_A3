$(document).ready(main);
var stompClient = null;

function main() {
   var socket = new SockJS('/websocket');
   stompClient = Stomp.over(socket);
   stompClient.connect({}, function (frame) {
       console.log('Connected: ' + frame);
       stompClient.subscribe('/topic/welcome', function (greeting) {
           showWelcome(JSON.parse(greeting.body).content);
       });
   });
}



function showWelcome(message) {
    console.log("setting html");
    $("#welcome").html(message);
}

