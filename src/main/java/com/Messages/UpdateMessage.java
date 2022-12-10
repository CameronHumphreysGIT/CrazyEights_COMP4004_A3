package com.Messages;
import com.*;

import java.util.ArrayList;

public class UpdateMessage extends GameMessage{
    //similar to GameMessage
    String playable = "";
    boolean enableDraw;

    public UpdateMessage(Game g, int player) {
        super(g, player);
        enableDraw = g.canDraw(player);
        for (int i =0; i < cards.length; i++) {
            if (g.isPlayable(cards[i])) {
                playable += "" + i;
            }
        }
    }

    public String getPlayable() {
        return playable;
    }

    public boolean getEnableDraw() {
        return enableDraw;
    }

    public void setPlayable(String playable) {
        this.playable = playable;
    }

    public void setEnableDraw(boolean enableDraw) {
        this.enableDraw = enableDraw;
    }
}
