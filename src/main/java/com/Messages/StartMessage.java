package com.Messages;

import com.Game;

import java.util.ArrayList;

public class StartMessage extends GameMessage {
    String deckCount;
    String topCard;
    int twos;

    public StartMessage(Game g, int player) {
        super(g, player);
        deckCount = "" + g.deckCount();
        topCard = g.getTopCard();
        twos = g.getTwos();
    }

    public String getDeckCount() {
        return deckCount;
    }

    public void setDeckCount(String deckCount) {
        this.deckCount = deckCount;
    }

    public String getTopCard() {
        return topCard;
    }

    public void setTopCard(String topCard) {
        this.topCard = topCard;
    }

    public int getTwos() {
        return twos;
    }

    public void setTwos(int twos) {
        this.twos = twos;
    }

}
