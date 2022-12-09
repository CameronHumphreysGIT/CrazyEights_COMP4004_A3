package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import com.Messages.*;

import java.util.ArrayList;

@Controller
public class GameController {

    private static SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public GameController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    Game g = new Game();
    @MessageMapping("/hello")
    @SendTo("/topic/welcome")
    public Player player(ResponseMessage hello) throws Exception {
        //delay so we can get to the page
        Thread.sleep(2000);
        //TODO make it so if someone joins while someone else is joining we don't have fucky shit
        //TODO watch out for fourth player joins without permission
        System.out.println("sending greeting to: " + (g.playerCount() + 1));
        Player p = new Player(hello.getResponse(), g.playerCount() + 1);
        g.addPlayer(p);
        return p;
    }

    @MessageMapping("/host")
    @SendTo("/topic/lobby")
    public StatusMessage status1(ResponseMessage rm) throws Exception{
        System.out.println("got response from p1");
        if (rm.getResponse().equals("No")) {
            g.startRound();
            return new StatusMessage(g);
        }else {
            return new StatusMessage(g.playerCount() - 1);
        }
    }

    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public StatusMessage status2() throws Exception{
        System.out.println("sending status to a player");
        if (g.playerCount() == 4) {
            g.startRound();
            return new StatusMessage(g);
        }
        return new StatusMessage(g.playerCount() - 1);
    }

    @MessageMapping("/{playerId}")
    @SendTo("/topic/{playerId}")
    public GameMessage game1(@DestinationVariable("playerId") int pId, ResponseMessage rm) throws Exception {
        System.out.println("Got card request from player" + pId);
        if (!rm.getResponse().equals("")) {
            //player wants an update or to draw a card
            if (rm.getResponse().equals("draw")) {
                System.out.println("player is drawing card" + pId);
                g.drawCard(pId);
            }
            //player specifically asking for an update, it's this players turn
            System.out.println("sending buttons to player" + pId);
            return new UpdateMessage(g, pId);
        }
        //give it the game object, it'll set everything else
        System.out.println("sending regular cards to" + pId);
        return new StartMessage(g, pId);
    }
    @MessageMapping("/play/{playerId}")
    @SendTo("/topic/lobby")
    public StatusMessage play(@DestinationVariable("playerId") int pId, ResponseMessage rm) throws Exception {
        //player played a card, put it in the Game
        g.play(rm.getResponse(), pId);
        //send new status message
        return new StatusMessage(g);
    }
    public void reset() {
        this.g = new Game();
    }

    public void setCards(ArrayList<String> cards, int player) {
        g.setCards(cards, player);
    }
    public void setTopCard(String card) {
        g.setTopCard(card);
    }
    public void setDraw(String card) {g.setDraw(card);}
    public void refresh() {
        //go through and send a refresh to each player.
        for (int i = 1; i <= g.playerCount(); i++) {
            //send an update message to whoever is current turn
            if (i == g.getCurrentTurn()) {
                GameController.messagingTemplate.convertAndSend(("/topic/" + i), new UpdateMessage(g, i));
            }else {
                GameController.messagingTemplate.convertAndSend(("/topic/" + i), new StartMessage(g, i));
            }
        }
    }
}