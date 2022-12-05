package com;

public class Greeting {
    private String content;

    public Greeting(String name, int number) {
        content = "Welcome " + name + " to Crazy Eights, you are Player" + number;
    }
    public String getContent() {
        return content;
    }
}
