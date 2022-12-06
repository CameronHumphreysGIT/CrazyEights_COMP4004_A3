package com;

import java.util.ArrayList;

public class Game {
    ArrayList<Player> playerList = new ArrayList<>();
    public Game() {
    }
    public void addPlayer(Player p) {
        playerList.add(p);
    }
    public int playerCount() {
        return playerList.size();
    }
}
