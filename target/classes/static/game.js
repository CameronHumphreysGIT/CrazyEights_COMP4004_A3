$(document).ready(main);
var stompClient = null;
var number = null;
var response = false;

function main() {
   var socket = new SockJS('/websocket');
   stompClient = Stomp.over(socket);
   stompClient.connect({}, function (frame) {
       console.log('Connected: ' + frame);
       stompClient.subscribe('/topic/welcome', function (greeting) {
           showWelcome(JSON.parse(greeting.body));
       }, {id: "welcome"});
   });
}

function showWelcome(message) {
    number = message.number;
    $("#welcome").html("Welcome " + message.name + " to Crazy Eights, you are Player" + number);
    $("#status").html("Waiting...0 other players");
    //got welcome, resubscribe to our personal message board
    stompClient.unsubscribe("welcome");
    stompClient.send("/app/lobby", {}, JSON.stringify({}));
    stompClient.subscribe('/topic/lobby', function (message) {
       showStatus(JSON.parse(message.body));
    });
}

function showStatus(message) {
    if (message.content === "game"){
        $("#status").html("In Game");
    } else if (message.players === 2 && number === 1 && !(response)) {
        $("#status").html("Three total players, would you like to wait for another?");
        //add yes and no buttons
        $("#form").append('<button id="Yes" class="btn btn-default" type="submit">Yes</button>');
        $("#form").append('<button id="No" class="btn btn-default" type="submit">No</button>');
        //set click functions
        $("#Yes").click(function() {
            //send response
            stompClient.send("/app/host", {}, JSON.stringify({"response": "Yes"}));
            //set status
            $("#status").html("Waiting..." + message.players + " other players");
            //delete buttons
            $("#Yes").remove();
            $("#No").remove();
        });
        $("#No").click(function() {
            stompClient.send("/app/host", {}, JSON.stringify({"response": "No"}));
            $("#status").html("In Game");
            $("#Yes").remove();
            $("#No").remove();
        });
        //do this so we don't ask again
        response = true;
    }else {
        $("#status").html("Waiting..." + message.players + " other players");
    }
}

