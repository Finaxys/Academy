package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.AddEventManagerArgumentsPattern;
import com.finaxys.slackbot.BUL.Patterns.UserIdArgumentPattern;
import com.finaxys.slackbot.BUL.Patterns.UserNameArgumentPattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventManagerArgumentsMatcher {
	
    private Matcher matcher;
    private Pattern addEventManagerArgumentsPattern;
    private Pattern userIdArgumentPattern;
    private Pattern userNameArgumentPattern;

    public EventManagerArgumentsMatcher() {
    	
        addEventManagerArgumentsPattern = AddEventManagerArgumentsPattern.getPattern();
        userIdArgumentPattern 				= UserIdArgumentPattern.getPattern();
        userNameArgumentPattern 			= UserNameArgumentPattern.getPattern();
    }

    public boolean isCorrect(String message) {

        if (!addEventManagerArgumentsPattern.matcher(message).matches()) return false;
        
        return true;
    }

    public String getEventName(String message) {
    	
        String eventName = message.substring(0,message.indexOf('<'));
        
        return eventName.trim();
    }

    public String getUserName(String message) {
    	
        String  userName = message.substring(message.indexOf('<'),message.length());
        
        return  userName.trim();
    }
    public String getUserIdArgument(String message) {
    	
        matcher = userIdArgumentPattern.matcher(message);
        
        return matcher.find() ? matcher.group().substring(1) : "";
    }

    public String getUserNameArgument(String message) {
    	
        matcher = userNameArgumentPattern.matcher(message);
        
        return matcher.find() ? matcher.group().substring(0) : "";
    }

}

