package com;

import com.beust.ah.A;

import java.util.ArrayList;

import static com.Config.*;

public class Game {
    ArrayList<Player> playerList = new ArrayList<>();
    //list of all cards that have been drawn by a player, if it's their turn.
    String drawn = "";
    int draws = 0;
    Deck deck = new Deck();
    String topCard;
    int currentTurn;
    int round = 0;
    boolean isLeft = true;
    int twos = 0;
    ArrayList<String> sequence = new ArrayList<>();

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
        if (sequence.contains(card)) {
            return true;
        }
        if (card == null) {
            return false;
        }
        if (!drawn.equals("") && !drawn.equals(card)) {
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
        sequence.remove(card);
        //maybe they are just ending their turn
        if (!card.equals("end")) {
            //player played a card.
            topCard = card;
            //take it out of their hand.
            playerList.get(player - 1).discard(card);
        }
        //set twos as the amount of twos played in a row.
        if (card.charAt(0) == '2') {
            twos++;
        }else if (twos != 0) {
            //means we played a non two on top of a two, reset chain.
            twos = 0;
        }
        //always reset the drawn card.
        drawn = "";
        if (!sequence.isEmpty()) {
            //basically, if we are doing a sequence, it's still our turn
            return;
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
    }

    public boolean playableSequence(int player) {
        Player p = playerList.get(player - 1);
        //if this player has any 2s, we're good.
        if (p.getCards().contains("2S") || p.getCards().contains("2C") || p.getCards().contains("2H") || p.getCards().contains("2D")) {
            int index;
            String[] possibilities = new String[]{"2S", "2C", "2H", "2D"};
            for (String card : possibilities) {
                index = p.getCards().indexOf(card);
                if (index != -1) {
                    sequence.add(p.getCards().get(index));
                    return true;
                }
            }
        }
        //if they have less cards than required
        if (!(p.getCards().size() >= 2*twos)) {
            return false;
        }
        ArrayList<String> compatible = new ArrayList<>();
        //okay, now check how many cards are compatible with the topCard.
        for (String card : p.getCards()) {
            if (isPlayable(card)) {
                compatible.add(card);
            }
        }
        if (compatible.size() >= 2*twos) {
            //good, just return true with an empty sequence?
            sequence.addAll(compatible);
            return true;
        }
        return false;
    }

    public boolean canDraw(int player) {
        //basically figuring out whether or not a player can draw:
        //scenarios:
        //it is that player's turn
        if (currentTurn == player) {
            if (twos > 0) {
                if (!(sequence.isEmpty()) || playableSequence(player)) {
                    System.out.println("heyoooahhhhhhhhh" + sequence);
                    return false;
                }
            } else if (drawn != "" && isPlayable(drawn)) {
                //last card drawn is playable, stop drawing cards.
                draws = 0;
                return false;
            }
            if (draws == ((2*twos) + MAX_DRAW)) {
                draws = 0;
                return false;
            }
            return true;
        }else {
            return false;
        }
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
        draws++;
        String deal = deck.dealCard();
        if (topCard.charAt(0) == '2') {
            if (draws < (2*twos)) {
                //the current player must draw.
                drawn = "drawing";
            }else {
                //we have finished drawing...
                drawn = "";
            }
        } else {
            drawn = deal;
        }
        //dealem
        playerList.get(player - 1).deal(deal);
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

    public int getTwos() {
        return twos;
    }
}
