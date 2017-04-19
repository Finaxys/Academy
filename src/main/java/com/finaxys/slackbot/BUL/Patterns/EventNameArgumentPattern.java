package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class EventNameArgumentPattern {
	
    private static 	Pattern pattern;
    final 			String 	eventNameArgumentPattern;

    private EventNameArgumentPattern() {
    	
        eventNameArgumentPattern 	= "\\w*\\sevent";
        pattern 						= Pattern.compile(eventNameArgumentPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new EventNameArgumentPattern();
        return pattern;
    }
}
