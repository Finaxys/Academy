package com.finaxys.slackbot.DAL;

import java.io.Serializable;

public class Message implements Serializable{
    
	private static final long serialVersionUID = 1L;
	private String text;
    private String response_type;

    public Message(String text) {
        this.text 			= text;
        this.response_type 	="ephemeral";
    }

    public Message(String text, String response_type) {
        this.text 			= text;
        this.response_type 	= response_type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }
}
