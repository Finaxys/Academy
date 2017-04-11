package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class UserNameArgumentPattern {
	
    private static 	Pattern pattern;
    final 			String 	userNameArgumentPattern;

    private UserNameArgumentPattern() {
    	
        userNameArgumentPattern = "|\\w*";
        pattern 				= Pattern.compile(userNameArgumentPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new UserNameArgumentPattern();
        return pattern;
    }
}

