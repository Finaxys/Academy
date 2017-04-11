package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class ListScoresByChallengsArgumentsPattern {
	
    private static 	Pattern pattern;
    final 			String 	challengeScoreArgumentsPattern;

    private ListScoresByChallengsArgumentsPattern() {
    	
        challengeScoreArgumentsPattern 	= ".*\\w*\\schallenge.*";
        pattern 						= Pattern.compile(challengeScoreArgumentsPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new ListScoresByChallengsArgumentsPattern();
        return pattern;
    }
}
