package com.finaxys.slackbot.BUL.Listeners;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.ActionService;
import com.finaxys.slackbot.interfaces.DebugModeService;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.HelpService;
import com.finaxys.slackbot.interfaces.RoleService;
import com.finaxys.slackbot.interfaces.SlackUserService;

import allbegray.slack.rtm.EventListener;

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
	private RoleService roleService;

	@Autowired
	private SlackApiAccessService slackApiAccessService;

	@Autowired
	private EventService eventService;

	@Autowired
	private SlackUserService slackUserService;

	@Autowired
	private ActionService actionService;
	
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
		
		System.out.println("************************");
		//System.out.println(jsonNode);
		System.out.println(SlackBot.getSlackWebApiClient().getUserInfo(userId));
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

		/* to do fix cascade problems and do changes 
		case "fx_event_action_del":
			if (command.length == 3)
				SlackBot.postMessage(channelId, eventService.removeEventAction(command[1], command[2]),
						debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId,
						"fx_event_action_del takes 2 arguments : name of event, name of action.",
						debugModeService.isOnDebugMode());
			break;
		*/
		/*SlackBotTimer timer = new SlackBotTimer();
		case "fx_manager_list":
			if (command.length == 2)
				SlackBot.postMessage(channelId, getEventManagers(command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_event_score_add takes 1 argument : name of event.",
						debugModeService.isOnDebugMode());
			break;
		*/
	

		/*
		case "fx_manager_add":
			if (command.length == 3)
				SlackBot.postMessage(channelId, create(command[1], command[2], jsonNode.get("user").asText()),
						debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_manager_add takes 2 arguments : name of user, name of event.",
						debugModeService.isOnDebugMode());
			break;
		*/
		/* to do changes, add parameter event to specify scores of an event only */
		case "fx_leaderboard":
			if (command.length == 2)
				response = listScores(command[1]);
			else if(command.length == 1)
				response = listScores("");
			else
				response = "fx_leaderboard take 0 or 1 argument : the number of rows you want to display or an event name.";
			break;

		/*
		case "fx_manager_remove":
			if (command.length == 3)
				SlackBot.postMessage(channelId, remove(command[1], command[2], jsonNode.get("user").asText()),
						debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId,
						"fx_manager_remove takes 2 arguments : name of manager to remove, name of event.",
						debugModeService.isOnDebugMode());
			break;
		*/
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

		/* fix cascades problems 
		case "fx_event_del":
			if (command.length == 2)
				SlackBot.postMessage(channelId, removeEventByName(command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_event_del takes 1 argument: name of event.",
						debugModeService.isOnDebugMode());
			break;
		*/
		/* will be deleted 
		case "fx_event_join":
			if (command.length == 2)
				SlackBot.postMessage(channelId, eventService.joinEvent(userId, command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_event_join takes 1 argument: name of event.",
						debugModeService.isOnDebugMode());
			break;
		*/
		
		/* will be deleted 
		case "fx_action_add":
			if (command.length == 4)
				SlackBot.postMessage(channelId, addAction(command), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId,
						"fx_action_add takes 3 arguments : code of action, name of action, number of points.",
						debugModeService.isOnDebugMode());
			break;
		*/
		
		/*case "fx_action_get":
			if (command.length == 2)
				SlackBot.postMessage(channelId,
						actionService.getActionByCode(command[1]) != null
								? actionService.getActionByCode(command[1]).getCode()
								: "0",
						debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_action_add takes 1 argument: name of action",
						debugModeService.isOnDebugMode());
			break;
		*/
			
		/*
		case "fx_event_get":
			if (command.length == 2)
				SlackBot.postMessage(channelId,
						eventService.getEventByName(command[1]) != null
								? eventService.getEventByName(command[1]).getEventScores().toArray().toString()
								: "0",
						debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_action_add takes 1 argument: name of event.",
						debugModeService.isOnDebugMode());
			break;
		*/
			
		case "fx_action_performed":
			if (command.length == 4) {
				response = eventService.addActionToSlackuser(command[1], command[2], command[3]);
			} else
				response = "fx_event_action_add_to_user takes 3 arguments: name of event, code of action, name of user.";
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

	
	public String removeEventByName(String arguments) {
		SlackBotTimer timer = new SlackBotTimer();
		String fxevent = "";

		Event event = eventService.getEventByName(arguments);

		timer.capture();

		if (event == null)
			return "Error, can't find the event: " + arguments + ".\n";
		else {
			eventService.remove(event);
			return arguments + " has been removed ! ";
		}
	}

	public String addAction(String[] commands) {
		SlackBotTimer timer = new SlackBotTimer();
		timer.capture();

		try {
			String code = commands[1];
			int points = Integer.parseInt(commands[3]);

			Action action = new Action(code, commands[2], points);

			if (actionService.getActionByCode(code) != null)
				return "/fx_action_add " + code + " " + commands[2] + " " + points + " \n "
						+ " :  This action already exists ! ";
			else {
				new Thread(() -> {
					actionService.save(action);
				}).start();
				timer.capture();

				return "The action named " + commands[2] + " has been successfully added ! ";
			}
		} catch (NumberFormatException e) {
			return "fx_action_add failed. Please, check the arguments types. ";// + timer;
		}
	}

	public String remove(String managerName, String eventName, String adminId) {

		SlackBotTimer timer = new SlackBotTimer();

		String userIdArgs = "";
		List<SlackUser> users = slackUserService.getAll();
		for (SlackUser user : users) {
			if (user.getName().equals(managerName)) {
				userIdArgs = user.getSlackUserId();
			}
		}

		Event event = eventService.getEventByName(eventName);

		timer.capture();

		if (event == null) {

			return "event doesn't exist";
		}

		if (slackUserService.isEventManager(adminId, eventName) || slackUserService.isAdmin(adminId)) {
			Object[] roles = roleService.getAll().stream()
					.filter(e -> e.getEvent() != null && e.getEvent().equals(event)).toArray();
			timer.capture();

			for (Object r : roles) {
				Role role = (Role) r;
				if (role.getSlackUser().getSlackUserId().equals(userIdArgs)) {

					Role role2 = role;

					new Thread(() -> {
						roleService.remove(role2);
					}).start();

					return managerName + " is no more a event manager!";
				}
			}

			timer.capture();

			return managerName + " is already not a event manager!";
		}

		return "You are neither an admin nor an event manager";
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
				messageText += profile.getName() + " " + profile.calculateScore() + "\n";
		}
		else {
			Event event = eventService.getEventByName(parameter.trim());

			if (event == null)
				return "No such event ! Check the event name";
			messageText = "Leaderboard of " + event.getName() + " :" + " \n ";
			for (SlackUser slackUser : slackUserService.getAll()) {
				if (slackUser.getActions() == null) {
					slackUser.setActions(new HashSet<>());
				}
				if (slackUser.getActions().size() != 0)
					messageText += slackUser.getName() + " : "
							+ slackUser.calculateScore(eventService.getEventByName(parameter.trim()))
							+ "\n";
			}
		}
		return messageText;
	}

	/*
	public String addActionScore(String eventName, String userName, String actionCode) {

		SlackBotTimer timer = new SlackBotTimer();
		List<SlackUser> allUsers = slackUserService.getAll();
		SlackUser admin = new SlackUser();

		for (SlackUser user : allUsers) {
			if (user.getName().equals(userName)) {
				if (!slackUserService.isAdmin(user.getSlackUserId()))
					return "You are not an admin!";// + timer;
				else {
					admin = user;
					break;
				}
			}
		}

		Event event = eventService.getEventByName(eventName);

		if (event == null)
			return "Event does not exist ";// + timer;

		Action action = actionService.getActionByCode(actionCode);

		if (action == null)
			return "Action does not exist ";// + timer;

		eventService.addScore(event, admin.getSlackUserId(), action);

		return "Score successfully added for the action ";// + timer;
	}
*/
	public String create(String profileName, String eventName, String adminUserId) {

		SlackBotTimer timer = new SlackBotTimer();

		String userId = "";
		List<SlackUser> allUsers = slackUserService.getAll();

		// GET USER ID OF THE SELECTED USER IN PARAMETER!
		for (SlackUser user : allUsers) {
			System.out.println(user.getName() + "  | " + profileName + " |  " + profileName);
			if (user.getName().equals(profileName)) {
				userId = user.getSlackUserId();
				break;
			}
		}

		if (userId.isEmpty()) {

			String newId = "UA";
			for (int i = 0; i < 6; i++) {
				int c2 = (int) Math.random() * (9 - 0);
				int c1 = (int) Math.random() * (74 - 50);
				int c3 = (int) Math.random() * (1 - 0);
				if (c3 == 1)
					newId += (char) c1;
				else
					newId += c2;
			}
			SlackUser newUser = new SlackUser(newId, profileName);
			newUser = slackUserService.save(newUser);

			allUsers = slackUserService.getAll();

			// GET USER ID OF THE SELECTED USER IN PARAMETER!
			for (SlackUser user : allUsers) {
				if (user.getName().equals(profileName)) {
					userId = user.getSlackUserId();
					break;
				}
			}

		}
		Event event = eventService.getEventByName(eventName);

		if (event == null) {
			return "/fx_manager_add : " + eventName + " event does not exist ";// + timer;
		}

		if (slackUserService.isEventManager(adminUserId, eventName) || slackUserService.isAdmin(adminUserId)) {
			timer.capture();
			SlackUser slackUser = slackUserService.get(userId);
			timer.capture();

			slackUser = (slackUser == null) ? new SlackUser(userId, profileName) : slackUser;

			if (slackUserService.isEventManager(userId, eventName))
				return "fx_manager_add  : " + profileName + " is already a manager!";// + timer;

			Role role = new Role("event_manager", slackUser, event);

			slackUser.getRoles().add(role);

			SlackUser slackUser2 = slackUser;

			new Thread(() -> {
				slackUserService.save(slackUser2);
			}).start();

			timer.capture();

			return "/fx_manager_add  : " + profileName + " has just became a event manager!";// + timer;
		}

		return "fx_manager_add  :   you are not a event manager!";// + timer;
	}

	public String getEventManagers(String arguments) {

		Log.info("fx_manager_list ");
		SlackBotTimer timer = new SlackBotTimer();
		String eventName = arguments.trim();
		Event event = eventService.getEventByName(eventName);

		if (event == null)
			return "Event nonexistent";
		Object[] roles = roleService.getAll().stream().filter(e -> e.getEvent() != null && e.getEvent().equals(event))
				.toArray();
		System.out.println(roles.length);
		String messageText = "List of Event managers list:\n";
		if (roles.length > 0) {
			for (Object r : roles) {
				Role role = (Role) r;
				messageText += "<@" + role.getSlackUser().getSlackUserId() + "|"
						+ slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName() + "> \n";
			}
			return "fx_manager_list " + "\n " + messageText;// + timer;
		}

		return "fx_manager_list :" + "\n " + messageText + " No event managers are found\n";
	}
/*
	public String addEventScore(String eventName, String userId, String scoreValue) {

		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();

		String userIdArgs = "";
		List<SlackUser> allUsers = slackUserService.getAll();

		// GET USER ID OF THE SELECTED USER IN PARAMETER!
		for (SlackUser user : allUsers) {
			if (user.getName().equals(userId)) {
				userIdArgs = user.getSlackUserId();
			}
		}

		int score = Integer.parseInt(scoreValue);

		timer.capture();

		Event event = eventService.getEventByName(eventName);

		timer.capture();

		if (event == null)
			return "Nonexistent event";// + timer;

		// TODO
		// if (!isEventManager(eventManagerId, eventName) && !isAdmin(eventManagerId))
		// return "/fx_event_score_add " + eventName + "\n" + "You are neither admin nor
		// a event manager !" + timer;
		SlackUser user = null;
		if (!userIdArgs.equals(""))
			user = slackUserService.get(userIdArgs);
		else
			return "A problem has occured! user " + userId + " not found !";// + timer;

		SlackUserEvent slackUserEvent = slackUserEventService.getSlackUserEvent(event, user);

		if (slackUserEvent != null) {
			slackUserEvent.addScore(score);
		} else {
			slackUserEvent = new SlackUserEvent(score, event, user);

		}
		slackUserService.updateScore(userIdArgs, score);

		SlackUserEvent slackUserEvent2 = slackUserEvent;

		timer.capture();

		try {
			timer.capture();

			new Thread(() -> {
				slackUserEventService.save(slackUserEvent2);
			}).start();

			timer.capture();
		} catch (Exception e) {
			return "/fx_event_score_add " + eventName + " \n"
					+ "A problem has occured! The user may have a score for this event already !";// + timer;
		}

		return "/fx_event_score_add " + eventName + " \n" + "Score has been added !";// + timer;
	}
*/
	
	
	public String listScoreForEvent(String arguments) {

		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();

		String eventName = arguments.trim();
		Event event = eventService.getEventByName(eventName);

		if (event == null)
			return "fx_event_score_list " + arguments + " \n" + "No such event ! Check the event name";// + timer;

		timer.capture();

		/*
		 * if (listEvents == null) return "fx_event_score_list " + arguments + " \n" +
		 * "No score has been saved till the moment !";// + timer;
		 */
		String textMessage = "Leaderboard of " + event.getName() + " :" + " \n ";
		for (SlackUser slackUser : slackUserService.getAll()) {
			if (slackUser.getActions() == null) {
				System.out.println("delete **********");
				slackUser.setActions(new HashSet<>());
			}

			if (slackUser.getActions().size() != 0)
				textMessage += slackUser.getName() + " : "
						+ slackUser.calculateScore(eventService.getEventByName(eventName))
						+ "\n";
		}
		return "/fx_event_score_list " + arguments + " \n" + textMessage;// + timer;
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
