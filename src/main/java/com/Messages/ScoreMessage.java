package com.Messages;

import com.Game;

public class ScoreMessage {
    int players;
    int round;
    int player1;
    int player2;
    int player3;
    int player4;

    public ScoreMessage(Game g) {
        players = g.playerCount();
        round = (g.getRound() - 1);
        player1 = g.getScores()[0];
        player2 = g.getScores()[1];
        player3 = g.getScores()[2];
        player4 = g.getScores()[3];
    }

    public int getPlayers() {
        return players;
    }

    public int getRound() {
        return round;
    }

    public int getPlayer1() {
        return player1;
    }

    public int getPlayer2() {
        return player2;
    }

    public int getPlayer3() {
        return player3;
    }

    public int getPlayer4() {
        return player4;
    }
}
