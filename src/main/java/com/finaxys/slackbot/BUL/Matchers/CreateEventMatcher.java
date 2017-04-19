package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.CreateEventPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CreateEventMatcher {
	
    private List<Pattern> createEventPatterns;

    public CreateEventMatcher() {
    	
        createEventPatterns = new ArrayList<>();
        createEventPatterns.add(CreateEventPattern.getPattern());
    }

    public boolean match(String message) {
    	
        for (Pattern pattern : createEventPatterns)
            if (!pattern.matcher(message).matches()) return false;
        
        return true;
    }
}
