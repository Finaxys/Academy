package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class OneUsernameArgumentPattern {
	
    private static 	Pattern pattern;
    final 			String 	oneUsernameArgumentPattern;

    private OneUsernameArgumentPattern() {
    	
        oneUsernameArgumentPattern 	= ".*(<@\\w{9}|.*>){1}.*";
        pattern 					= Pattern.compile(oneUsernameArgumentPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new OneUsernameArgumentPattern();
        return pattern;
    }
}
