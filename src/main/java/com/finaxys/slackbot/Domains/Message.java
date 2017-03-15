package com.finaxys.slackbot.Domains;

/**
 * Created by Bannou on 15/03/2017.
 */
public class Message {
    private String text;
    private String response_type;

    public Message(String text) {
        this.text = text;
    }

    public Message(String text, String response_type) {
        this.text = text;
        this.response_type = response_type;
    }
}
