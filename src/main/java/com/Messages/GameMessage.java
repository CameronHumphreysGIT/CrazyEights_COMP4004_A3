package com.Messages;

import com.Game;

import java.util.ArrayList;

public class GameMessage {
    String[] cards = new String[7];
    String deckCount;
    String topCard;

    public GameMessage(Game g, int player) {
        ArrayList<String> c = g.getPlayer(player).getCards();
        for (int i =0; i < c.size(); i++) {
            cards[i] = c.get(i);
        }
        deckCount = "" + g.deckCount();
        topCard = g.getTopCard();
    }
    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
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
