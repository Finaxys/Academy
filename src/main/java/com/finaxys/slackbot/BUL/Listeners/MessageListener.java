package com.finaxys.slackbot.BUL.Listeners;

import allbegray.slack.rtm.EventListener;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Interfaces.ChannelLeftService;
import com.finaxys.slackbot.BUL.Interfaces.InnovateService;
import com.finaxys.slackbot.BUL.Interfaces.NewTribeJoinedService;
import com.finaxys.slackbot.BUL.Interfaces.RealMessageReward;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.DAL.Action;
import com.finaxys.slackbot.DAL.DebugMode;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUserEvent;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.ActionService;
import com.finaxys.slackbot.interfaces.DebugModeService;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.HelpService;
import com.finaxys.slackbot.interfaces.RoleService;
import com.finaxys.slackbot.interfaces.SlackUserEventService;
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
	private RoleService roleService;

	@Autowired
	private SlackApiAccessService slackApiAccessService;

	@Autowired
	private EventService eventService;

	@Autowired
	private SlackUserService slackUserService;

	@Autowired
	private SlackUserEventService slackUserEventService;

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
		

		switch (command[0]) {

		case "fx_help":
			if (command.length == 1)
				SlackBot.postMessage(channelId, helpService.fx_help(), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_help doesn't take arguments.", debugModeService.isOnDebugMode());
			break;
		/* work but have to do changes 
		case "fx_set_me_as_admin":
			if (command.length == 2)
				SlackBot.postMessage(channelId, slackUserService.setCurrentUserAsAdmin(userId, command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_set_me_as_admin takes 1 argument : the superAdmin's password.",
						debugModeService.isOnDebugMode());
			break;
		
		case "fxadmin_add":
			if (command.length == 2)
				SlackBot.postMessage(channelId, slackUserService.addUserAsAdmin(userId, command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fxadmin_add takes 1 argument : the user you want to add, ex: @atef",
						debugModeService.isOnDebugMode());
			break;
			
		
		*/
		case "fxadmin_enable_debug":
			if (command.length == 1) {
				if (slackUserService.isAdmin(userId))
					SlackBot.postMessage(channelId, debugModeService.enableDebugMode(), debugModeService.isOnDebugMode());
				else
					SlackBot.postMessage(channelId, "You need to be administrator to run the command " + command[0],
							debugModeService.isOnDebugMode()); 
				
			}
			else
				SlackBot.postMessage(channelId, "fxadmin_enable_debug does not take arguments",
						debugModeService.isOnDebugMode());
			break;
			
		case "fxadmin_disable_debug":
			if (command.length == 1)
				SlackBot.postMessage(channelId, debugModeService.disableDebugMode(), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fxadmin_disable_debug does not take arguments",
						debugModeService.isOnDebugMode());
			break;
		case "fx_events_by_date":
			if (command.length == 2)
				SlackBot.postMessage(channelId, getEventsByDate(command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_events_by_date takes 1 argument : date of the events.",
						debugModeService.isOnDebugMode());
			break;

		case "fx_events_by_type":
			if (command.length == 2)
				SlackBot.postMessage(channelId, getEventsByType(command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId,
						"fx_events_by_type takes 1 argument : type of event (group or individual).",
						debugModeService.isOnDebugMode());
			break;

		/* work but have to do changes 
		case "fx_event_score_list":
			if (command.length == 2)
				SlackBot.postMessage(channelId, listScoreForEvent(command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_event_score_list takes 1 argument : name of event.",
						debugModeService.isOnDebugMode());
			break;

		*/

		
		case "fx_action_add":
			if (command.length == 5)
				SlackBot.postMessage(channelId,
						eventService.addEventAction(command[1], command[2], command[3], Integer.parseInt(command[4])),
						debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId,
						"fx_event_action_add takes 4 arguments : name of event, name of action, description of action, number of points.",
						debugModeService.isOnDebugMode());
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
		/*
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
			if (command.length == 2 && isInteger(command[1]))
				SlackBot.postMessage(channelId, listScores(command[1]), debugModeService.isOnDebugMode());
			else if (command.length == 1)
				SlackBot.postMessage(channelId, listScores(""), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_leaderboard take the number of the manager to display their score",
						debugModeService.isOnDebugMode());
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
				SlackBotTimer timer = new SlackBotTimer();
				timer.capture();
				List<Event> events = eventService.getAll();
				timer.capture();

				if (events.isEmpty())
					SlackBot.postMessage(
							channelId, "There is no previous events! Come on create one !" + timer,
							debugModeService.isOnDebugMode());
				else
					SlackBot.postMessage(channelId, eventService.getStringFromList(events) + timer,
							debugModeService.isOnDebugMode());
			} else
				SlackBot.postMessage(channelId, "fx_event_list doesn't take arguments", debugModeService.isOnDebugMode());
			break;

		case "fx_event_add":
			if (command.length == 4 && (command[3].equals("group") || command[3].equals("individual")))
				SlackBot.postMessage(channelId, eventService.addEvent(command, jsonNode), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId,
						"fx_event_add takes 3 arguments : name of event, description of event, type of event (group|individual).",
						debugModeService.isOnDebugMode());
			break;

		
		case "fx_event_named":
			if (command.length == 2)
				SlackBot.postMessage(channelId, getEventByName(command[1]), debugModeService.isOnDebugMode());
			else
				SlackBot.postMessage(channelId, "fx_event_named takes only one argument: event_name",
						debugModeService.isOnDebugMode());
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
				SlackBot.postMessage(channelId, eventService.addActionToSlackuser(command[1], command[2], command[3]),
						debugModeService.isOnDebugMode());
			} else
				SlackBot.postMessage(channelId,
						"fx_event_action_add_to_user takes 3 arguments: name of event, code of action, name of user.",
						debugModeService.isOnDebugMode());
			break;

		default:
			//SlackBot.postMessage(channelId, "hi " + message, debugModeService.isOnDebugMode());
			break;
		}
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

	public String listScores(String text) {

		SlackBotTimer timer = new SlackBotTimer();

		String messageText = "fx_leaderboard " + text + "\n";

		int size = text.isEmpty() ? -1 : Integer.parseInt(text);

		if (size == 0)
			return messageText + "You must enter a positive integer." + timer;

		List<SlackUser> users = slackUserService.getAllOrderedByScore(size);

		for (SlackUser profile : users) {
			messageText += profile.getName() + " " + profile.calculateScore() + "\n";
		}

		return messageText;// + timer;
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
	
	public String getEventsByDate(String text) {

		SlackBotTimer timer = new SlackBotTimer();

		DateMatcher dateMatcher = new DateMatcher();

		if (!dateMatcher.match(text.trim()))
			return "Date format : yyyy-MM-dd";// + timer;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date wantedDate = new Date();
		try {
			wantedDate = dateFormat.parse(text);
		} catch (Exception e) {
		}

		List<Event> events = eventService.getEventByDate(wantedDate);

		timer.capture();

		if (events.isEmpty())
			return "There are no events on this date: " + text; // + timer;
		else
			return "List of events on date : " + wantedDate + "\n" + eventService.getStringFromList(events);// + timer;
	}

	public String getEventsByType(String eventType) {

		SlackBotTimer timer = new SlackBotTimer();

		List<Event> events = eventService.getEventByType(eventType);

		if (events.isEmpty())
			return "No events with type: " + eventType; // + timer;

		return "List of events with type : " + eventType + " \n" + eventService.getStringFromList(events);// + timer;

	}

	public String getEventByName(String arguments) {
		SlackBotTimer timer = new SlackBotTimer();
		String fxevent = "";

		Log.info("fx_event_named " + arguments);

		Event event = eventService.getEventByName(arguments);

		timer.capture();

		if (event == null)
			fxevent = "fx_event_named " + arguments + "\n" + "Nonexistent event.";// + timer;
		return fxevent;

	}

	public String listScoreForEvent(String arguments) {

		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();

		String eventName = arguments.trim();
		Event event = eventService.getEventByName(eventName);

		if (event == null)
			return "fx_event_score_list " + arguments + " \n" + "No such event ! Check the event name";// + timer;

		timer.capture();

		List<SlackUserEvent> listEvents = slackUserEventService.getAllByEvent(event);
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

	private String getHelpCommands(JsonNode jsonNode) {
		String fxCommands = "Helloooooooooooooooooooooo Audric !\n*/fx_event_list* \n List all the events. \n \n"
				+ "*/fx_event_add* [event name] [description] [group|individual] \n Adds a new event. \n \n"
				+ "*/fx_event_named* [event name] \n Gives an event details. \n \n"
				+ "*/fx_events_by_date* [yyyy-mm-dd] \n List of events by date \n \n"
				+ "*/fx_events_by_type* [group|individual] \n List of events by type \n \n"
				+ "*/fx_event_score_list*  [event name] event \n Gives the score list of a given event. \n \n"
				+ "*/fx_event_del* [event name] \n Removes an event! \n \n"
				+ "*/fx_event_score_add* @username [points] [name] \n Adds a score to an event attendee. \n \n"
				+ "*/fx_action_add* \n [code] [action name] [points] \n \n"
				+ "*/fx_action_score_add* \n [event name] [user name] [action code] \n \n"
				+ "*/fx_manager_add* [event name] [username] \n Adds an event manager. \n \n"
				+ "*/fx_manager_list* [event name] \n List of event managers . \n \n"
				+ "*/fx_manager_del* [event name] [username] \n Removes an event manager. \n \n"
				+ "*/fx_leaderboard* [optional: count] \n Gives the top scores. \n \n"
				+ "*/fx_contest_add* [contest] [points earned] \n Adds a w<contest. \n \n"
				+ "*/fx_score* [user name] \n Show a user's scores \n"
				+ "*/fxadmin_list* \n List of all administrators. \n \n";

		String fxAdminCommands = "*/fxadmin_add* @username \n Adds an administrator. \n \n"
				+ "*/fxadmin_del* @username \n Removes an administrator. \n \n"
				+ "*/fxadmin_param* [parameter_name] [parameter_value] \n Set the value of a parameter \n \n"
				+ "*/fxadmin_list_params* \n List all parameters \n \n";

		String id = jsonNode.get("user").asText();
		return "/fx_help\nList of the FX bot commands:\n" + fxCommands
				+ (slackUserService.isAdmin(id) ? " \n " + fxAdminCommands : "");

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
