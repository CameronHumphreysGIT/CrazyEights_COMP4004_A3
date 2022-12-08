package com.Messages;

import com.Game;

import java.util.ArrayList;

public class StartMessage extends GameMessage {
    String deckCount;
    String topCard;

    public StartMessage(Game g, int player) {
        super(g, player);
        deckCount = "" + g.deckCount();
        topCard = g.getTopCard();
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
}
