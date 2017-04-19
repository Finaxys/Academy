package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class AddEventScoreArgumentsPattern {
	
    private static 	Pattern pattern;
    final 			String 	eventScoreArgumentsPattern;

    private AddEventScoreArgumentsPattern() {
    	
        eventScoreArgumentsPattern 	= ".*<@\\w{9}|.*>.*\\d{1,3}.*\\s\\w*\\sevent.*";
        pattern 						= Pattern.compile(eventScoreArgumentsPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new AddEventScoreArgumentsPattern();
        return pattern;
    }
}
