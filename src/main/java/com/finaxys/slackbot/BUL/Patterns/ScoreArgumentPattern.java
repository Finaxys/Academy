package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class ScoreArgumentPattern {
	
    private static Pattern 	pattern;
    final String 			scoreArgumentPattern;

    private ScoreArgumentPattern() {
    	
        scoreArgumentPattern 	= "\\d{1,3}\\spoints";
        pattern 				= Pattern.compile(scoreArgumentPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new ScoreArgumentPattern();
        return pattern;
    }
}
