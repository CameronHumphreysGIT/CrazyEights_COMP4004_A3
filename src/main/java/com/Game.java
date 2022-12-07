package com;

import com.beust.ah.A;

import java.util.ArrayList;

public class Game {
    ArrayList<Player> playerList = new ArrayList<>();
    Deck deck = new Deck();
    String topCard;
    int currentTurn;
    int round = 0;
    public final int MAX_HAND = 5;

    public Game() {
    }

    public void startRound() {
        round++;
        currentTurn = round;
        //deal cards to players
        for (int i =0; i < MAX_HAND; i++) {
            for (int j =0; j < playerList.size(); j++) {
                playerList.get(j).deal(deck.dealCard());
            }
        }
        topCard = deck.dealCard();
    }

    public void addPlayer(Player p) {
        playerList.add(p);
    }

    public int playerCount() {
        return playerList.size();
    }

    public String getTopCard() {
        return topCard;
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

    public int deckCount() {
        return deck.getCount();
    }
}
