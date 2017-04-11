package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class ChallengeNameArgumentPattern {
	
    private static 	Pattern pattern;
    final 			String 	challengeNameArgumentPattern;

    private ChallengeNameArgumentPattern() {
    	
        challengeNameArgumentPattern 	= "\\w*\\schallenge";
        pattern 						= Pattern.compile(challengeNameArgumentPattern);
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new ChallengeNameArgumentPattern();
        return pattern;
    }
}
