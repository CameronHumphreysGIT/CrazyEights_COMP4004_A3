package com.Messages;

public class StatusMessage {
    //simple Message class to parse JSOn properly
    private String content;
    private int players;

    public StatusMessage() {
    }

    public StatusMessage(String content, int players) {
        this.content = content;
        this.players = players;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }
}
