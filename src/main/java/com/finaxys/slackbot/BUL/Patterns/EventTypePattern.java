package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class EventTypePattern {
	
    private static 	Pattern pattern;
    private 		String 	regex;

    private EventTypePattern() {
    	
        regex 	= "\\s*(group|individual)\\s*";
        pattern = Pattern.compile(regex);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new EventTypePattern();
        return pattern;
    }
}
