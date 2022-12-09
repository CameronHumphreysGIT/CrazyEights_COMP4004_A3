package com;

import java.util.ArrayList;

import static com.Config.MAX_DRAW;

public class Player {

    private String name;
    private int number;
    private ArrayList<String> cards = new ArrayList<>();
    //communicate to the player the maximum cards they can draw in a turn
    private int draw = MAX_DRAW;

    public Player(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public void discard(String card) {
        cards.remove(card);
    }

    public void deal(String c) {
        cards.add(c);
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getDraw() {
        return draw;
    }

    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }
}
