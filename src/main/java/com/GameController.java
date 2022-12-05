package com;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.Messages.*;

@Controller
public class GameController {
    @MessageMapping("/hello")
    @SendTo("/topic/welcome")
    public Greeting greeting(HelloMessage hello) throws Exception {
        //delay so we can get to the page
        Thread.sleep(2000);
        System.out.println("sending greeting");
        return new Greeting(hello.getName(), 1);
    }
}