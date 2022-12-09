package com;

import com.beust.ah.A;

import java.util.ArrayList;

public class Game {
    ArrayList<Player> playerList = new ArrayList<>();
    Deck deck = new Deck();
    String topCard;
    int currentTurn;
    int round = 0;
    boolean isLeft = true;
    public final int MAX_HAND = 5;

    public Game() {
    }

    public void startRound() {
        round++;
        currentTurn = round;
        //empty every player's hand
        for (Player p : playerList) {
            p.setCards(new ArrayList<String>());
        }
        //deal cards to players
        for (int i =0; i < MAX_HAND; i++) {
            for (int j =0; j < playerList.size(); j++) {
                playerList.get(j).deal(deck.dealCard());
            }
        }
        topCard = deck.dealCard();
    }

    public boolean isPlayable(String card) {
        //check if the card is currently playable
        if (card.charAt(0) == '8') {
            return true;
        }
        //same suit, use card.length since sometimes we have 10's
        return (card.charAt(card.length() - 1) == topCard.charAt(topCard.length() - 1));
    }

    public void play(String card, int player) {
        //player played a card.
        topCard = card;
        //take it out of their hand.
        playerList.get(player - 1).discard(card);
        //aces got some special stuff.
        if (card.charAt(0) == ('A')) {
            //played an ace, flip order go to previous player
            isLeft = false;
        }
        currentTurn = nextTurn();
    }

    public void addPlayer(Player p) {
        playerList.add(p);
    }

    public int playerCount() {
        return playerList.size();
    }

    public int nextTurn() {
        if (isLeft) {
            if (currentTurn == playerCount()) {
                //back to first player
                return 1;
            }
            return currentTurn + 1;
        }else {
            if (currentTurn == 1) {
                //back to last player
                return playerCount();
            }
            return currentTurn - 1;
        }
    }

    public String getTopCard() {
        return topCard;
    }

    public void setTopCard(String card) {
        topCard = card;
    }

    public void setCards(ArrayList<String> cards, int player) {
        //set that player's cards
        playerList.get(player - 1).setCards(cards);
    }

    public int deckCount() {
        return deck.getCount();
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getRound() {
        return round;
    }

    public Player getPlayer(int pid) {
        return playerList.get(pid - 1);
    }


    public boolean isLeft() {
        return isLeft;
    }
}
