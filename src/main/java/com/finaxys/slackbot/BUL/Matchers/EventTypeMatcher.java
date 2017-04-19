package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.EventTypePattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EventTypeMatcher {
	
    private List<Pattern> eventTypeMatcher;

    public EventTypeMatcher() {
    	
        eventTypeMatcher = new ArrayList<>();
        eventTypeMatcher.add(EventTypePattern.getPattern());
    }

    public boolean match(String message) {
    	
        for (Pattern pattern : eventTypeMatcher)
            if (!pattern.matcher(message).matches()) return false;
        
        return true;
    }
}
