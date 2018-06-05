package com.finaxys.slackbot.BUL.Listeners;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.DebugModeService;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.HelpService;
import com.finaxys.slackbot.interfaces.SlackUserService;

import allbegray.slack.rtm.EventListener;

@Component
public class MessageListener implements EventListener {

	@Autowired
	private NewTribeJoinedService newTribeJoinedService;
	@Autowired
	private InnovateService innovateService;
	@Autowired
	private ChannelLeftService channelLeftService;
	@Autowired
	private HelpService helpService;
	@Autowired
	private EventService eventService;
	@Autowired
	private SlackUserService slackUserService;
	@Autowired
	private DebugModeService debugModeService;

	public MessageListener() {
	}

	private void analyseMessage(JsonNode jsonNode) {
		String message = jsonNode.get("text").asText().trim();
		String[] command = message.split(" ");
		String channelId = jsonNode.get("channel").asText().trim();
		String userId = jsonNode.get("user").asText().trim();
		String response = "";
		SlackBotTimer timer = new SlackBotTimer();
		
		
		if (debugModeService.isOnDebugMode())
			timer.capture();
		
		switch (command[0]) {

		case "fx_help":
			if (command.length == 1)
				response =  helpService.fx_help(slackUserService.isAdmin(userId));
			else
				response =  "fx_help doesn't take arguments.";
			break;
		
		case "fxadmin_add":
			if (command.length == 2)
				response = slackUserService.addUserAsAdmin(userId, command[1]);
			else
				response = "fxadmin_add takes 1 argument : the user you want to add, ex: @atef";
			break;
			
		case "fxadmin_remove":
			if (command.length == 2) {
				if (slackUserService.isAdmin(userId))
					response = slackUserService.removeAdmin(command[1]);
				else
					response =  "You need to be administrator to run the command " + command[0];				
			}				
			else
				response = "fxadmin_remove takes 1 argument : admin's name";
			break;
			
		case "fxadmin_list":
			if (command.length == 1) {
				if (slackUserService.isAdmin(userId))
					response = slackUserService.listAllAdmins();
				else
					response =  "You need to be administrator to run the command " + command[0];				
			}				
			else
				response = "fxadmin_list takes 0 arguments !";
			break;
						
		case "fxadmin_enable_debug":
			if (command.length == 1) {
				if (slackUserService.isAdmin(userId))
					response =  debugModeService.enableDebugMode();
				else
					response =  "You need to be administrator to run the command " + command[0];				
			}
			else
				response =  "fxadmin_enable_debug does not take arguments";
			break;
			
		case "fxadmin_disable_debug":
			if (command.length == 1)
				response =  debugModeService.disableDebugMode();
			else
				response =  "fxadmin_disable_debug does not take arguments";
			break;
		
		case "fx_events_by_date":
			if (command.length == 2)
				response =  eventService.getEventsByDate(command[1]);
			else
				response =  "fx_events_by_date takes 1 argument : date of the events.";
			break;

		case "fx_events_by_type":
			if (command.length == 2)
				response =  eventService.getEventsByType(command[1]);
			else
				response = "fx_events_by_type takes 1 argument : type of event (group or individual).";
			break;
	
		case "fx_action_add":
			if (command.length == 5)
				response = eventService.addEventAction(command[1], command[2], command[3], Integer.parseInt(command[4]));
			else
				response = "fx_event_action_add takes 4 arguments : name of event, name of action, description of action, number of points.";
			break;
	
		case "fx_action_remove":
			if (command.length == 3)
				response = eventService.removeEventAction(userId, command[1], command[2]);
			else
				response = "fx_action_remove takes 2 arguments : name of event, name of action.";
			break;
		
		case "fx_leaderboard":
			if (command.length == 2)
				response = listScores(command[1]);
			else if(command.length == 1)
				response = listScores("");
			else
				response = "fx_leaderboard take 0 or 1 argument : the number of rows you want to display or an event name.";
			break;

		case "fx_events_list":
			if (command.length == 1) {
				List<Event> events = eventService.getAll();
				if (events.isEmpty()) {					
					response = "There is no previous events! Come on create one !";
				}
				else {
					response = eventService.getStringFromList(events);
				}
			}
			else {
				response = "fx_event_list doesn't take arguments";
			}
			break;

		case "fx_event_add":
			if (command.length == 4 && (command[3].equals("group") || command[3].equals("individual")))
				response = eventService.addEvent(command, jsonNode) ;
			else
				response = "fx_event_add takes 3 arguments : name of event, description of event, type of event (group|individual).";
			break;
		
		case "fx_event_details":
			if (command.length == 2) {
				Event event = eventService.getEventByName(command[1]);
				response = event == null ? "The event " + command[1]  + " does not exist !" : event.toString();				
			}
			else
				response = "fx_event_details takes 1 argument: name of event";
			break;
	
		case "fx_event_remove":
			if (command.length == 2)
				response = eventService.removeEvent(userId, command[1]);
			else
				response = "fx_event_remove takes 1 argument: name of event.";
			break;
			
		case "fx_action_performed":
			if (command.length == 4) {
				response = eventService.addActionToSlackuser(command[1], command[2], command[3]);
			} else
				response = "fx_event_action_add_to_user takes 3 arguments: name of event, code of action, name of user.";
			break;
			
		case "fx_manager_add":
			if (command.length == 3) {
				response = eventService.addManager(userId, command[1], command[2]);
			} else
				response = "fx_manager_add takes 2 arguments: name of user, name of event";
			break;
			
		case "fx_manager_remove":
			if (command.length == 3) {
				response = eventService.removeManager(userId, command[1], command[2]);
			} else
				response = "fx_manager_remove takes 2 arguments: name of user, name of event";
			break;
			
		case "fx_score":
			if (command.length == 2) {
				response = slackUserService.displayScoreUser(command[1]);
			} else
				response = "fx_score takes 1 argument: name of user.";
			break;
			
		default:
			//response =  "hi " + message;
			break;
		}
		if (debugModeService.isOnDebugMode())
			timer.capture();
		
		if (!response.equals(""))
			SlackBot.postMessage(channelId, response + (debugModeService.isOnDebugMode() ? timer : ""),debugModeService.isOnDebugMode());
	}

	

	public static boolean isInteger(String s) {
		try {	
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String listScores(String parameter) {
		String messageText = "";
		if(isInteger(parameter) || parameter.equals("")) {
			int size = parameter.isEmpty() ? -1 : Integer.parseInt(parameter);
			if (size == 0)
				return messageText + "You must enter a positive integer.";	

			List<SlackUser> users = slackUserService.getAllOrderedByScore(size);
			for (SlackUser profile : users)
				messageText += "<@" + profile.getSlackUserId() + "> : " + profile.calculateScore() + "\n";
		}
		else {
			Event event = eventService.getEventByName(parameter.trim());

			if (event == null)
				return "No such event named "+ parameter +" ! Check the event name";
			messageText = "Leaderboard of " + event.getName() + " :" + " \n ";
			for (SlackUser slackUser : slackUserService.getAll()) {
				if (slackUser.getActions() == null) {
					slackUser.setActions(new HashSet<>());
				}
				if (slackUser.getActions().size() != 0)
					messageText += "<@" + slackUser.getSlackUserId() + "> : "
							+ slackUser.calculateScore(eventService.getEventByName(parameter.trim()))
							+ "\n";
			}
		}
		return messageText;
	}

	
	public void handleMessage(JsonNode jsonNode) {
		if (!jsonNode.has("subtype")) {
			//realMessageReward.rewardReadMessage(jsonNode);

			System.out.println("Hello user " + jsonNode.get("user").asText());

			analyseMessage(jsonNode);

		} else {
			String messageSubtype = jsonNode.get("subtype").asText();

			if (messageSubtype.equals("channel_join"))
				newTribeJoinedService.onNewTribeJoined(jsonNode);

			else if (messageSubtype.equals("file_share"))
				innovateService.rewardFileShared(jsonNode);

			else if (messageSubtype.equals("channel_leave"))
				channelLeftService.onChannelLeaveMessage(jsonNode);
		}
	}
}
