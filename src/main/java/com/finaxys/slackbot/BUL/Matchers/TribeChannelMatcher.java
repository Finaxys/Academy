package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.TribeChannelPattern;

import java.util.regex.Matcher;

public class TribeChannelMatcher {
	
    private Matcher matcher;

    public TribeChannelMatcher() {
    }

    public boolean isNotTribe(String channelName) {
    	
        return !TribeChannelPattern	.getTribeChannelPattern()
        							.matcher(channelName)
        							.matches();
    }
}