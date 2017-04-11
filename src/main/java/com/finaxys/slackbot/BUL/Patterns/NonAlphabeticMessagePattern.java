package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class NonAlphabeticMessagePattern {
	
    private static 	Pattern pattern;
    final 			String 	nonAlphabeticMessagePattern;

    private NonAlphabeticMessagePattern() {
    	
        nonAlphabeticMessagePattern = "\\W*";
        pattern 					= Pattern.compile(nonAlphabeticMessagePattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new NonAlphabeticMessagePattern();
        return pattern;
    }
}
