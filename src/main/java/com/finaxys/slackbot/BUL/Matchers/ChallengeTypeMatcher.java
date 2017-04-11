package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.ChallengeTypePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ChallengeTypeMatcher {
	
    private List<Pattern> challengeTypeMatcher;

    public ChallengeTypeMatcher() {
    	
        challengeTypeMatcher = new ArrayList<>();
        challengeTypeMatcher.add(ChallengeTypePattern.getPattern());
    }

    public boolean match(String message) {
    	
        for (Pattern pattern : challengeTypeMatcher)
            if (!pattern.matcher(message).matches()) return false;
        
        return true;
    }
}
