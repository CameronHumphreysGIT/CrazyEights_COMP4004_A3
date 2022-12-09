package com;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    ArrayList<String> cards = new ArrayList<>();
    public Deck() {
        for (int i = 1; i <= 13; i++) {
            String prefix = "" + i;
            switch(i) {
                case 1:
                    prefix = "A";
                    break;
                case 11:
                    prefix = "J";
                    break;
                case 12:
                    prefix = "Q";
                    break;
                case 13:
                    prefix = "K";
                    break;
            }
            cards.add(prefix + "D");//diamonds
            cards.add(prefix + "H");//hearts
            cards.add(prefix + "C");//clubs
            cards.add(prefix + "S");//spades
        }
        //all in, shuffle
        shuffle();
    }

    public void setTop(String c) {
        cards.set(0, c);
    }

    public String dealCard() {
        String out = cards.get(0);
        cards.remove(0);
        return out;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int getCount() {
        return cards.size();
    }
}
