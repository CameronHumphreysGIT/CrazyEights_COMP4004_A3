package com;

import com.beust.ah.A;

import java.util.ArrayList;

import static com.Config.MAX_HAND;
import static com.Config.START_HAND;

public class Game {
    ArrayList<Player> playerList = new ArrayList<>();
    //list of all cards that have been drawn by a player, if it's their turn.
    String drawn;
    Deck deck = new Deck();
    String topCard;
    int currentTurn;
    int round = 0;
    boolean isLeft = true;

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
        for (int i =0; i < START_HAND; i++) {
            for (int j =0; j < playerList.size(); j++) {
                playerList.get(j).deal(deck.dealCard());
            }
        }
        topCard = deck.dealCard();
    }

    public boolean isPlayable(String card) {
        if (card == null) {
            return false;
        }
        if (drawn != null && !drawn.equals(card)) {
            //only the most recently drawn card is playable if the player chooses to draw
            return false;
        }
        //check if the card is currently playable
        //this works as my 1's are A instead of 10
        if (card.charAt(0) == '8' || card.charAt(0) == topCard.charAt(0)) {
            return true;
        }
        //same suit, use card.length since sometimes we have 10's
        return (card.charAt(card.length() - 1) == topCard.charAt(topCard.length() - 1));
    }

    public void play(String card, int player) {
        //maybe they are just ending their turn
        if (!card.equals("end")) {
            //player played a card.
            topCard = card;
            //take it out of their hand.
            playerList.get(player - 1).discard(card);
        }
        //aces got some special stuff.
        if (card.charAt(0) == ('A')) {
            //played an ace, flip order go to previous player
            isLeft = false;
        }
        currentTurn = nextTurn();
        if (card.charAt(0) == 'Q') {
            //skip them
            currentTurn = nextTurn();
        }
        //always recent the drawn card.
        drawn = null;
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

    public void drawCard(int player) {
        drawn = deck.dealCard();
        //dealem
        playerList.get(player - 1).deal(drawn);
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

    public void setDraw(String c) {
        //set the next card to be drawn
        deck.setTop(c);
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
