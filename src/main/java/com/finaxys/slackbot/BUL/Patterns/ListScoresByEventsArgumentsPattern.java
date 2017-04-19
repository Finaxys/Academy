package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class ListScoresByEventsArgumentsPattern {
	
    private static 	Pattern pattern;
    final 			String 	eventScoreArgumentsPattern;

    private ListScoresByEventsArgumentsPattern() {
    	
        eventScoreArgumentsPattern 	= ".*\\w*\\sevent.*";
        pattern 						= Pattern.compile(eventScoreArgumentsPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new ListScoresByChallengsArgumentsPattern();
        return pattern;
    }
}
