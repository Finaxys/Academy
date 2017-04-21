package com.finaxys.slackbot.WebServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.ActionService;
import com.finaxys.slackbot.interfaces.SlackUserService;

@RestController
@RequestMapping("/actions")
public class ActionWebService extends BaseWebService {
	
	@Autowired
	SlackUserService slackUserService;
	
	@Autowired
	ActionService actionService;
	
    @RequestMapping(value = "/fx_action_add", method = RequestMethod.POST)
    public ResponseEntity<JsonNode> addAction(@RequestParam("user_id") String userId,
    										  @RequestParam("text")    String arguments) {
    	
    	SlackBotTimer timer = new SlackBotTimer();
    	
    	ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_action_add");
    	
    	if(!slackUserService.isAdmin(userId))
    		return newResponseEntity("/fx_action_add " + arguments + " \n " + "You are not an admin!" + timer,true);
    	
    	int code = Integer.parseInt(argumentsSplitter.getIntegers(0));
    	int points = Integer.parseInt(argumentsSplitter.getIntegers(1));
    	String actionName = argumentsSplitter.getString(0);
    	
    	Action action = new Action(code, actionName, points);
    	
    	new Thread(()->{actionService.save(action);}).start();

        return newResponseEntity("Action successfully added " + timer,true);
    }
    
    @RequestMapping(value = "/fx_event_action_add", method = RequestMethod.POST)
    public ResponseEntity<JsonNode> addEventAction(@RequestParam("user_id") String userId,
    										  	   @RequestParam("text")    String arguments) {
    	
    	SlackBotTimer timer = new SlackBotTimer();
    	
    	ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_event_action_add");
    	
    	//TODO isEventManager
    	if(!slackUserService.isAdmin(userId))
    		return newResponseEntity("/fx_event_action_add " + arguments + " \n " + "You are not an admin!" + timer,true);
    	
    	int code = Integer.parseInt(argumentsSplitter.getIntegers(0));
    	String eventName = argumentsSplitter.getString(0);
    	
    	Event event = eventService.getEventByName(eventName);
    	
    	if (event == null)
    		return newResponseEntity("Event does not exist " + timer, true);
    	
    	eventService.addAction(event, code);

        return newResponseEntity("Action successfully added to the event " + timer,true);
    }
    
    @RequestMapping(value = "/fx_action_score_add", method = RequestMethod.POST)
    public ResponseEntity<JsonNode> addActionScore(@RequestParam("user_id") String userId,
    										  	   @RequestParam("text")    String arguments) {
    	
    	SlackBotTimer timer = new SlackBotTimer();
    	
    	ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_action_score_add");
    	
    	//TODO isEventManager
    	if(!slackUserService.isAdmin(userId))
    		return newResponseEntity("/fx_action_score_add " + arguments + " \n " + "You are not an admin!" + timer,true);
    	
    	int code = Integer.parseInt(argumentsSplitter.getIntegers(0));
    	String eventName = argumentsSplitter.getString(0);
    	String slackUserId = argumentsSplitter.getUserId();
    	
    	Event event = eventService.getEventByName(eventName);
    	
    	if (event == null)
    		return newResponseEntity("Event does not exist " + timer, true);
    	
    	Action action = actionService.get(code);
		
		if (action == null)
			return newResponseEntity("Action does not exist " + timer,true);
    	
    	eventService.addScore(event, slackUserId, action);

        return newResponseEntity("Score successfully added for the action " + timer,true);
    }
    
}
