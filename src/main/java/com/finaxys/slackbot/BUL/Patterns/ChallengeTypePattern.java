package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class ChallengeTypePattern {
	
    private static 	Pattern pattern;
    private 		String 	regex;

    private ChallengeTypePattern() {
    	
        regex 	= "\\s*(group|individual)\\s*";
        pattern = Pattern.compile(regex);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new ChallengeTypePattern();
        return pattern;
    }
}
