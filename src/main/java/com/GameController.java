package com;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.Messages.*;

@Controller
public class GameController {
    Game g = new Game();
    @MessageMapping("/hello")
    @SendTo("/topic/welcome")
    public Player player(ResponseMessage hello) throws Exception {
        //delay so we can get to the page
        Thread.sleep(2000);
        System.out.println("sending greeting");
        Player p = new Player(hello.getResponse(), g.playerCount() + 1);
        g.addPlayer(p);
        return p;
    }

    @MessageMapping("/host")
    @SendTo("/topic/lobby")
    public StatusMessage status1(ResponseMessage rm) throws Exception{
        Thread.sleep(500);
        System.out.println("got response from p1");
        if (rm.getResponse().equals("No")) {
            g.startRound();
            return new StatusMessage("1", g.playerCount() - 1);
        }else {
            return new StatusMessage("", g.playerCount() - 1);
        }
    }

    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public StatusMessage status2() throws Exception{
        Thread.sleep(500);
        System.out.println("sending status to a player");
        if (g.playerCount() == 4) {
            g.startRound();
            return new StatusMessage("1", g.playerCount() - 1);
        }
        return new StatusMessage("", g.playerCount() - 1);
    }

    @MessageMapping("/{playerId}")
    @SendTo("/topic/{playerId}")
    public GameMessage game1(@DestinationVariable("playerId") int pId, ResponseMessage rm) throws Exception {
        //give it the game object, it'll set everything else
        return new GameMessage(g, pId);
    }

    public void reset()
    {
        this.g = new Game();
    }
}