$(document).ready(main);
var stompClient = null;
var number = null;
var response = false;
var connectionStage = 0;
var next;
const lastMessage = {to:"someone", content:"something"};

function main() {
   var socket = new SockJS('/websocket');
   stompClient = Stomp.over(socket);
   stompClient.connect({}, function (frame) {
       console.log('Connected: ' + frame);
       stompClient.subscribe('/topic/welcome', function (greeting) {
           showWelcome(JSON.parse(greeting.body));
       }, {id: "welcome"});
       stompClient.subscribe('/topic/lobby', function (message) {
              showStatus(JSON.parse(message.body));
       }, {id: "lobby"});
   });
   //set the click func for draw here.
   $("#draw").click(function() {
       console.log("clicked the draw button");
       //send response
       stompClient.send("/app/" + number, {}, JSON.stringify({"response": "draw"}));
   });
}

function waitNextStage(expected) {
    //console.log("didn't get a server reply, sending again");
    //function to repeatedly send the last response until we get a reply.
    //wait a second
    //if (expected === connectionStage) {
        //we waited and didn't get a reply, let's send again
    //    stompClient.send(lastMessage.to, {}, JSON.stringify(lastMessage.content))
    //}
}

function showWelcome(message) {
    connectionStage = 1;
    number = message.number;
    $("#welcome").html("Welcome " + message.name + " to Crazy Eights, you are Player" + number);
    $("#status").html("Waiting...0 other players");
    //got welcome, resubscribe to our personal message board
    stompClient.unsubscribe("welcome");
    stompClient.send("/app/lobby", {}, JSON.stringify({}));
    lastMessage.to = "/app/lobby";
    lastMessage.content = {};
    //this will, wait 300 milis, then call waitNextStage
    setTimeout(waitNextStage(connectionStage), 300);
    stompClient.subscribe('/topic/' + number, function (message) {
      showGame(JSON.parse(message.body));
    }, {id: "justMe"});
}

function showStatus(message) {
    connectionStage = 2;
    if (message.content !== "") {
        connectionStage = 3;
        $("#status").html("In Game, Round" + message.round + ", Player" + message.content + "'s turn " + "turn order:" + message.dir + ", next: " + message.next);
        if (next && (message.content != number)) {
            alert("Previous player played a queen or Ace, you were Skipped");
        }
         next = (number == message.next);

        stompClient.send("/app/" + number, {}, JSON.stringify({"response":""}));
        lastMessage.to = "/app/" + number;
        lastMessage.content = {"response":""};
        //this will, wait 300 milis, then call waitNextStage
        setTimeout(waitNextStage(connectionStage), 2000);
        response = false;
    } else if (message.players === 2 && number === 1 && !(response)) {
        $("#status").html("Three total players, would you like to wait for another?");
        //add yes and no buttons
        $("#form").append('<button id="Yes" class="btn btn-default" type="submit">Yes</button>');
        $("#form").append('<button id="No" class="btn btn-default" type="submit">No</button>');
        //set click functions
        $("#Yes").click(function() {
            //send response
            stompClient.send("/app/host", {}, JSON.stringify({"response": "Yes"}));
            lastMessage.to = "/app/host";
            lastMessage.content = {"response":"Yes"};
            //set status
            $("#status").html("Waiting..." + message.players + " other players");
            //delete buttons
            $("#Yes").remove();
            $("#No").remove();
        });
        $("#No").click(function() {
            stompClient.send("/app/host", {}, JSON.stringify({"response": "No"}));
            lastMessage.to = "/app/host";
            lastMessage.content = {"response":"No"};
            $("#status").html("In Game");
            $("#Yes").remove();
            $("#No").remove();
        });
        //do this so we don't ask again
        response = true;
        //this will, wait 300 milis, then call waitNextStage
        setTimeout(waitNextStage(connectionStage), 2000);
    }else {
        $("#status").html("Waiting..." + message.players + " other players");
    }
}

function showGame(message) {
    connectionStage = 4;
    //if we are the current turn
    var status = $("#status").text();
    if (status[23] === ("" + number)) {
        //my turn
        if (!response) {
            stompClient.send("/app/" + number, {}, JSON.stringify({"response":"update"}));
            lastMessage.to = "/app/" + number;
            lastMessage.content = {"response":"update"};
            //this will, wait 300 milis, then call waitNextStage
            setTimeout(waitNextStage(connectionStage), 2000);
            response = true;
        } else {
            connectionStage = 5;
            console.log("constructing buttons...");
            $("#cards").empty();
            var hasPlayable = false;
            for (var i=0; i < message.cardCount; i++) {
                if (message.playable.includes(i)) {
                    hasPlayable = true;
                    //this means this card is playable, make it a button
                    $("#cards").append('<button id="card' + (i+1) + '" class="btn btn-default" type="submit">' + message.cards[i] + '</button>');
                    //set click function
                    $("#card" + (i+1)).click({id: (i+1)}, playCard);
                }else {
                    $("#cards").append('<p id="card' + (i+1) + '" style="display:inline">' + message.cards[i] + ' </p>');
                }
            }
            //create the draw button, maybe
            if (!$( "#draw" ).prop("disabled") && hasPlayable) {
                //means we have already drawn, and we must play the first playable card
                $( "#draw" ).prop( "disabled", true);
            }else {
                $( "#draw" ).prop( "disabled", false);
            }
            //we don't want to reset all that hard work
            return;
        }
    }
    setGameInfo(message);
}

function setGameInfo(message) {
    console.log("default, setting cards, topdeck and deck");
    $("#cards").empty();
    for (var i=0; i < message.cardCount; i++) {
        $("#cards").append('<p id="card' + (i+1) + '" style="display:inline">' + message.cards[i] + ' </p>');
    }
    $("#deck").html(message.deckCount);
    $("#topCard").html(message.topCard);
}

function playCard(event) {
    var response = $("#card" + (event.data.id)).text();
    if (response[0] == '8') {
        //prompt for a suit
        //add yes and no buttons
        $("#form").append('<button id="S" class="btn btn-default" type="submit">S</button>');
        $("#form").append('<button id="C" class="btn btn-default" type="submit">C</button>');
        $("#form").append('<button id="H" class="btn btn-default" type="submit">H</button>');
        $("#form").append('<button id="D" class="btn btn-default" type="submit">D</button>');
        //set click functions
        $("#S").click(function() {
            //send response
            stompClient.send("/app/play/" + number, {}, JSON.stringify({"response": "8S"}));
            //delete buttons
            $("#S").remove();
            $("#C").remove();
            $("#H").remove();
            $("#D").remove();
        });
        $("#C").click(function() {
            //send response
            stompClient.send("/app/play/" + number, {}, JSON.stringify({"response": "8C"}));
            //delete buttons
            $("#S").remove();
            $("#C").remove();
            $("#H").remove();
            $("#D").remove();
        });
        $("#H").click(function() {
            //send response
            stompClient.send("/app/play/" + number, {}, JSON.stringify({"response": "8H"}));
            //delete buttons
            $("#S").remove();
            $("#C").remove();
            $("#H").remove();
            $("#D").remove();
        });
        $("#D").click(function() {
            //send response
            stompClient.send("/app/play/" + number, {}, JSON.stringify({"response": "8D"}));
            //delete buttons
            $("#S").remove();
            $("#C").remove();
            $("#H").remove();
            $("#D").remove();
        });
        //don't want to send a response anyways...
        return;
    }
    console.log("sending: " + "#card" + (event.data.id)  + " res: "+ response);
    //send the response as the card string
    stompClient.send("/app/play/" + number, {}, JSON.stringify({"response": ("" + response)}));
    lastMessage.to = "/app/play/" + number;
    //delete these if they exist
    $("#S").remove();
    $("#C").remove();
    $("#H").remove();
    $("#D").remove();
    lastMessage.content = {"response":response};
    //this will, wait 300 milis, then call waitNextStage
    setTimeout(waitNextStage(connectionStage), 2000);
}

