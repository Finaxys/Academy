package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class ContestAddPattern {
	
    private static 	Pattern pattern;
    final 			String  ContestAddArgumentPattern;

    public static Pattern getPattern() {
    	
        if (pattern == null) new ContestAddPattern();
        return pattern;
    }
    private ContestAddPattern() {
    	
        ContestAddArgumentPattern = "^\".+\".+[1-9][0-9]*.*";
        pattern 				= Pattern.compile( ContestAddArgumentPattern);
    }

}
