package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.NonAlphabeticMessagePattern;
import com.finaxys.slackbot.BUL.Patterns.RegularMessagePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RealMessageMatcher {
	
    private List<Pattern> unrealMessagePatterns;

    public RealMessageMatcher() {
    	
        unrealMessagePatterns = new ArrayList<>();
        
        unrealMessagePatterns.add(RegularMessagePattern
        					 .getPattern());
        
        unrealMessagePatterns.add(NonAlphabeticMessagePattern
        					 .getPattern());
    }

    public boolean isRealMessage(String message) {
    	
        for (Pattern pattern : unrealMessagePatterns)
            if (pattern.matcher(message).matches()) return false;
        
        return true;
    }
}
