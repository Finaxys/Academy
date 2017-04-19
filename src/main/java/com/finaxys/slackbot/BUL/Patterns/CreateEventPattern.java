package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class CreateEventPattern {
	
    private static 	Pattern pattern;
    private 		String 	regex;

    private CreateEventPattern() {
    	
        regex 	= ".+,(group|individual),.+";
        pattern = Pattern.compile(regex);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new CreateEventPattern();
        return pattern;
    }
}
