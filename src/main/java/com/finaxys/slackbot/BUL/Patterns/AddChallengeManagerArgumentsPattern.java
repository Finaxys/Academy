package com.finaxys.slackbot.BUL.Patterns;

import java.util.regex.Pattern;

public class AddChallengeManagerArgumentsPattern {
	
    private static 	Pattern pattern;
    final 			String 	challengeManagerArgumentsPattern;

    private AddChallengeManagerArgumentsPattern() {
    	
        challengeManagerArgumentsPattern 	= ".+(<@\\w{9}|.*>){1}";
        pattern 							= Pattern.compile(challengeManagerArgumentsPattern);
        
    }

    public static Pattern getPattern() {
    	
        if (pattern == null) new AddChallengeManagerArgumentsPattern();
        return pattern;
    }
}
