package com.Messages;

import com.Game;

public class StatusMessage {
    //simple Message class to parse JSOn properly
    private String content;
    private int round;
    private String dir;
    private String next;
    private int players;

    public StatusMessage(String g, int r) {
        //round over status.
        content = g;
        //r - 1 is the previous round (round has already incremented)
        round = r - 1;
    }

    public StatusMessage(int p) {
        content = "";
        players = p;
    }

    public StatusMessage(Game g) {
        content = "" + g.getCurrentTurn();
        players = g.playerCount();
        //alternative constructor for in game.
        //content will be current player
        round = g.getRound();
        if (g.isLeft()) {
            dir = "left(incrementing)";
        }else {
            dir = "right(decrementing)";
        }
        next = "" + g.nextTurn();
    }


    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }
}
