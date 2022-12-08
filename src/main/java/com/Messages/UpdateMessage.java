package com.Messages;
import com.*;

import java.util.ArrayList;

public class UpdateMessage extends GameMessage{
    //similar to GameMessage
    String playable = "";

    public UpdateMessage(Game g, int player) {
        super(g, player);
        for (int i =0; i < cards.length; i++) {
            if (g.isPlayable(cards[i])) {
                playable += "" + i;
            }
        }
    }

    public String getPlayable() {
        return playable;
    }

    public void setPlayable(String playable) {
        this.playable = playable;
    }
}
