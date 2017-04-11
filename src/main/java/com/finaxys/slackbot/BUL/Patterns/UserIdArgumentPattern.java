package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class UserIdArgumentPattern {
	
    private static 	Pattern pattern;
    final 			String 	userIdArgumentPattern;

    private UserIdArgumentPattern() {
    	
        userIdArgumentPattern 	= "@\\w{9}";
        pattern 				= Pattern.compile(userIdArgumentPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new UserIdArgumentPattern();
        return pattern;
    }
}
