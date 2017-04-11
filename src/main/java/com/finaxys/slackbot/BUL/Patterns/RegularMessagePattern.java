package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class RegularMessagePattern {
	
    private static 	Pattern pattern;
    final 			String 	regularMessagePattern;

    private RegularMessagePattern() {
    	
        regularMessagePattern 	= "([a-zA-Z0-9])\\1{2,}";
        pattern 				= Pattern.compile(regularMessagePattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new RegularMessagePattern();
        return pattern;
    }
}
