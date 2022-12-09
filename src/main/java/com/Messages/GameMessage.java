package com.Messages;

import com.Game;

import java.util.ArrayList;

public abstract class GameMessage {
    //framework for all messages passed to users during the game
    String[] cards = new String[com.Config.MAX_HAND];
    int cardCount = 0;

    public GameMessage(Game g, int player) {
        ArrayList<String> c = g.getPlayer(player).getCards();
        for (int i =0; i < c.size(); i++) {
            cards[i] = c.get(i);
            cardCount++;
        }
    }

    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }
}
