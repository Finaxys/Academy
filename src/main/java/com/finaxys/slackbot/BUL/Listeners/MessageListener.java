package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.DebugMode;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.HelpService;
import com.finaxys.slackbot.interfaces.RoleService;
import com.finaxys.slackbot.interfaces.ActionService;
import com.finaxys.slackbot.interfaces.DebugModeService;
import com.finaxys.slackbot.interfaces.SlackUserService;

@Component
public class MessageListener implements EventListener {

	@Autowired
	private NewTribeJoinedService newTribeJoinedService;

	@Autowired
	private RealMessageReward realMessageReward;

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
	private RoleService roleService;
	
	@Autowired
	private ActionService actionService;
	
	
	private DebugMode flagDebug;
	
	public MessageListener() {
	}
	
	public MessageListener(DebugMode flag) {
		flagDebug = flag;
	}

	private void analyseMessage(JsonNode jsonNode) {
		String message = jsonNode.get("text").asText().trim();
		String[] command = message.split(" ");
		String channelId = jsonNode.get("channel").asText().trim();
		
		System.out.println("**********");
		System.out.println(flagDebug.isOnDebugMode());
		
		switch (command[0]) {
		case "fx_help":
			if(command.length == 1) {
				SlackBot.postMessage(channelId, helpService.fx_help(), flagDebug.isOnDebugMode());
				//SlackBot.postMessage(channelId,helpService.fx_help());
			}
			else
				SlackBot.postMessage(channelId, "fx_help doesn't take arguments ", flagDebug.isOnDebugMode());
			
			break;
			
		case "fx_event_list":
			if(command.length == 1) {
				SlackBotTimer timer = new SlackBotTimer();

				timer.capture();

				List<Event> events = eventService.getAll();

				timer.capture();

				if (events.isEmpty())
					SlackBot.postMessage(channelId,
							"/fx_event SlackBot_list" + " \n " + "There is no previous events! Come on create one!" + timer, flagDebug.isOnDebugMode());

				else
					SlackBot.postMessage(channelId, eventService.getStringFromList(events) + timer, flagDebug.isOnDebugMode());
			}
			else
			{
				SlackBot.postMessage(channelId,"fx_event_list doesn't take arguments", flagDebug.isOnDebugMode());

			}
			break;

		case "fx_event_add":
			if (command.length == 4 && (command[3].equals("group") || command[3].equals("individual")))
				SlackBot.postMessage(channelId,addEvent(command, jsonNode), flagDebug.isOnDebugMode());
			else
				SlackBot.postMessage(channelId,"fx_event_add takes 3 arguments : [event name] [description] [group|individual]", flagDebug.isOnDebugMode());
			break;

		case "fx_event_named" : 
			if(command.length == 2) 
				SlackBot.postMessage(channelId,getEventByName(command[1]), flagDebug.isOnDebugMode());
			else 
				SlackBot.postMessage(channelId,"fx_event_named takes only one argument: event_name", flagDebug.isOnDebugMode());
			break;

		case "fx_event_del" : 
			if(command.length == 2) 
				SlackBot.postMessage(channelId,removeEventByName(command[1]), flagDebug.isOnDebugMode());
			else 
				SlackBot.postMessage(channelId,"fx_event_del takes only one argument: event_name", flagDebug.isOnDebugMode());
			break;

		case "fx_action_add" : 
			if(command.length == 4)
				SlackBot.postMessage(channelId,addAction(command), flagDebug.isOnDebugMode());
			else 
				SlackBot.postMessage(channelId,"fx_action_add takes 3 arguments: [Code] [ActionName] [points]", flagDebug.isOnDebugMode());
			break;
		
		case "fx_manager_del" :
			if (command.length == 3)
				SlackBot.postMessage(channelId,removeManager(command), flagDebug.isOnDebugMode());
			else
				SlackBot.postMessage(channelId,"/fx_manager_del takes 2 arguments : eventName username", flagDebug.isOnDebugMode());
				
		default:
			break;
		}
	}
	
	public String addEvent(String[] command, JsonNode jsonNode) {
		SlackBotTimer timer = new SlackBotTimer();
		timer.capture();

		Event event = new Event(command[1], command[2], command[3]);
		if (eventService.getEventByName(command[1]) != null)
			return "/fx_event_add " + command[1] + " " + command[2] + " " + command[3] + " :  This event already exists ! " + timer;
		else 
		{
			new Thread(() -> {
				eventService.save(event);
				SlackUser user = slackUserService.get(jsonNode.get("UserId").asText().trim());
				Role role = new Role("event_manager", user, event);
				roleService.save(role);
			}).start();
			timer.capture();
			return command[1] + " has been successfully added ! " + timer;
		}
	}

	public String getEventByName(String arguments) {
		SlackBotTimer timer = new SlackBotTimer();
		String fxevent = "";

		Event event = eventService.getEventByName(arguments);

		timer.capture();

		if (event == null) 
			return "fx_event_named " + arguments + "\n" + "Nonexistent event." + timer;
		else
			return event.toString() + timer; 
	}
	
	public String removeEventByName(String arguments) {
		SlackBotTimer timer = new SlackBotTimer();
		String fxevent = "";

		Event event = eventService.getEventByName(arguments);

		timer.capture();

		if (event == null) 
			return "Error, can't find the event: " + arguments + ".\n" + timer;
		else
		{
			eventService.remove(event);
			return arguments + " has been removed ! " + timer; 
		}
	}
	
	public String addAction(String[] commands)
	{
		SlackBotTimer timer = new SlackBotTimer();
		timer.capture();

		try
		{
			int code = Integer.parseInt(commands[1]);
			int points = Integer.parseInt(commands[3]);
			
			Action action = new Action(code, commands[2], points);

			if (actionService.get(code) != null)
				return "/fx_action_add " + code + " " + commands[2] + " " + points + " \n " + " :  This action already exists ! " + timer;
			else 
			{
				new Thread(() -> {
					actionService.save(action);
				}).start();
				timer.capture();
				return "The action named " + commands[2] + " has been successfully added ! " + timer;
			}
		}
		catch (NumberFormatException e)
		{
			return "fx_action_add failed. Please, check the arguments types. " + timer;
		}
	}
	
	public String removeManager(String[] commands) {
		SlackBotTimer timer = new SlackBotTimer();
		String fxevent = "";
		String eventName = commands[1];
		String userName = commands[2];

		timer.capture();

		List<Event> events = eventService.getAll();
		List<SlackUser> users = slackUserService.getAll();
		List<Role> roles = roleService.getAll();
		
		Event event = null;
		SlackUser user = null;
		
		for (Event e: events)
			if (e.getName().equals(eventName))
				event = e;
		for (SlackUser u: users)
			if (u.getName().equals(userName))
				user = u;
		for (Role role: roles)
			if (role.getEvent().getEventId().equals(event.getEventId())
					&& role.getSlackUser().getSlackUserId().equals(user.getSlackUserId()))
			{
				roleService.remove(role);
				return  userName + " has been removed from " + eventName + " ! \n" + timer;
			}
		return  "Error: The user " + userName + " wasn't found for the event " + eventName + " ! \n" + timer;
	}
	
	private static String getHelpCommands() {
		String fxCommands = "*/fx_event_list* \n List all the events. \n \n"
				+ "*/fx_event_add* [event name] [description] [group|individual] \n Adds a new event. \n \n"
				+ "*/fx_event_named* [event name] \n Gives an event details. \n \n"
				+ "*/fx_events_by_date* [yyyy-mm-dd] \n List of events by date \n \n"
				+ "*/fx_events_by_type* [group|individual] \n List of events by type \n \n"
				+ "*/fx_event_score_list*  [eventName] event \n Gives the score list of a given event. \n \n"
				+ "*/fx_event_del* [event name] \n Removes an event! \n \n"
				+ "*/fx_event_score_add* @username [points] [name] \n Adds a score to an event attendee. \n \n"
				+ "*/fx_action_add* \n [Code] [ActionName] [Points] \n \n"
				+ "*/fx_action_score_add* \n [EventName] [UserName] [ActionCode] \n \n"
				+ "*/fx_manager_add* [event name] @username \n Adds an event manager. \n \n"
				+ "*/fx_manager_list* [event name] \n List of event managers . \n \n"
				+ "*/fx_manager_del* [eventName] [username] \n Removes an event manager. \n \n"
				+ "*/fx_leaderboard* [optional: count] \n Gives the top scores. \n \n"
				+ "*/fx_contest_add* [contest] [points earned] \n Adds a w<contest. \n \n"
				+ "*/fx_score* [userName] \n Show a user's scores \n"
				+ "*/fxadmin_list* \n List1 of all administrators. \n \n";

		String fxAdminCommands = "*/fxadmin_add* @username \n Adds an administrator. \n \n"
				+ "*/fxadmin_del* @username \n Removes an administrator. \n \n"
				+ "*/fxadmin_param* [parameter_name] [parameter_value] \n Set the value of a parameter \n \n"
				+ "*/fxadmin_list_params* \n List all parameters \n \n";

		return "/fx_help\nList of the FX bot commands:\n" + fxCommands;

	}

	public void handleMessage(JsonNode jsonNode) {
		if (!jsonNode.has("subtype")) {
			realMessageReward.rewardReadMessage(jsonNode);

			System.out.println(jsonNode.get("text").asText());

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
