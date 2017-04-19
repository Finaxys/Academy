package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class AddEventManagerArgumentsPattern {
	
    private static 	Pattern pattern;
    final 			String 	eventManagerArgumentsPattern;

    private AddEventManagerArgumentsPattern() {
    	
        eventManagerArgumentsPattern 	= ".+(<@\\w{9}|.*>){1}";
        pattern 							= Pattern.compile(eventManagerArgumentsPattern);
        
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new AddEventManagerArgumentsPattern();
        return pattern;
    }
}
