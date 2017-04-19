package com.finaxys.slackbot.BUL.Matchers;

import com.finaxys.slackbot.BUL.Patterns.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventScoreArgumentsMatcher {
	
    private Matcher 		matcher;
    private List<Pattern> 	addEventScoreArgumentsPatterns;
    private Pattern 		listEventScoreArgumentsPatterns;
    private Pattern 		eventNameArgument;
    private Pattern 		scoreArgument;
    private Pattern 		slackUserIdArgument;

    public EventScoreArgumentsMatcher() {
    	
        addEventScoreArgumentsPatterns = new ArrayList<>();
        addEventScoreArgumentsPatterns.add(AddEventScoreArgumentsPattern.getPattern());
        listEventScoreArgumentsPatterns = ListScoresByEventsArgumentsPattern.getPattern();
        eventNameArgument 				= EventNameArgumentPattern.getPattern();
        scoreArgument 						= ScoreArgumentPattern.getPattern();
        slackUserIdArgument 			= UserIdArgumentPattern.getPattern();
        
    }

    public boolean isCorrect(String message) {
    	
        for (Pattern pattern : addEventScoreArgumentsPatterns)
            if (!pattern.matcher(message).matches()) return false;
        
        return true;
    }

    public boolean isCorrectListRequest(String message) {
    	
        return listEventScoreArgumentsPatterns.matcher(message).matches(); 
    }

    public String getEventName(String message) {
    	
        matcher = eventNameArgument.matcher(message);
        return matcher.find() ? matcher.group().substring(0, matcher.group().indexOf(" ")) : "";    
    }

    public String getScore(String message) {
    	
        matcher = scoreArgument.matcher(message);
        return matcher.find() ? matcher.group().substring(0, matcher.group().indexOf(" ")) : "";
    }

    public String getFinaxysProfileId(String message) {
    	
        matcher = slackUserIdArgument.matcher(message);
        return matcher.find() ? matcher.group().substring(1) : "";
    }


}
