package com;

import java.util.ArrayList;

public class Player {

    private String name;
    private int number;
    private ArrayList<String> cards = new ArrayList<>();

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

    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }
}
