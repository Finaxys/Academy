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
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBot;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.HelpService;
import com.finaxys.slackbot.interfaces.RoleService;
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
	

	public MessageListener() {
	}

	private void analyseMessage(JsonNode jsonNode) {
		String message = jsonNode.get("text").asText().trim();
		String[] command = message.split(" ");
		switch (command[0]) {
		case "fx_help":
			if(command.length == 1) {
				SlackBot.postMessageToDebugChannelAsync(helpService.fx_help());
			}
			else
			{
				SlackBot.postMessageToDebugChannelAsync("fx_help ne prend pas d'arguments");
				
			}
			break;
			
		case "fx_event_list":
			if(command.length == 1) {
				SlackBotTimer timer = new SlackBotTimer();

				timer.capture();

				List<Event> events = eventService.getAll();

				timer.capture();

				if (events.isEmpty())
					SlackBot.postMessageToDebugChannelAsync(
							"/fx_event SlackBot_list" + " \n " + "There no previous events! Come on create one!" + timer);

				else
					SlackBot.postMessageToDebugChannelAsync(eventService.getStringFromList(events) + timer);
			}
			else
			{
				SlackBot.postMessageToDebugChannelAsync("fx_event_list ne prend pas d'arguments");

			}
			break;

		case "fx_event_add":
			if (command.length == 4)
			{
				SlackBotTimer timer = new SlackBotTimer();
				timer.capture();

				Event event = new Event(command[1], command[2], command[3]);
				System.out.println(event.toString());

				if (eventService.getEventByName(command[1]) != null)
					SlackBot.postMessageToDebugChannelAsync("/fx_event_add " + command[1] + " " + command[2] + " " + command[3] + " \n " + " :  Cet évènement existe déjà !" + timer);
				else 
				{
					new Thread(() -> {
						eventService.save(event);
						SlackUser user = slackUserService.get(jsonNode.get("UserId").asText().trim());
						Role role = new Role("event_manager", user, event);
						roleService.save(role);
					}).start();
					timer.capture();
					SlackBot.postMessageToDebugChannelAsync(command[1] + " has been successfully added !");
				}
				timer.capture();
			}
			else
			{
				SlackBot.postMessageToDebugChannelAsync("fx_event_add takes 3 arguments");
			}

		case "fx_event_named" : 
			if(command.length == 2) {
				SlackBot.postMessageToDebugChannelAsync(getEventByName(command[1]));
			}
			else 
			{
				SlackBot.postMessageToDebugChannelAsync("fx_event_named takes only one argument: event_name");
			}
			break;

		default:
			break;
		}

	}

	public  String getEventByName(String arguments) {
		SlackBotTimer timer = new SlackBotTimer();
		String fxevent = "";

		Event event = eventService.getEventByName(arguments);

		timer.capture();

		if (event == null) 
			return "fx_event_named " + arguments + "\n" + "Nonexistent event." + timer;
		else
			return event.toString() + timer; 
	}
	
	private static String getHelpCommands() {
		String fxCommands = "*/fx_event_list* \n List all the events. \n \n"
				+ "*/fx_event_add* [event name] [description] [group|individual] \n Adds a new event. \n \n"
				+ "*/fx_event_named* [event name] \n Gives an event details. \n \n"
				+ "*/fx_events_by_date* [yyyy-mm-dd] \n List of events by date \n \n"
				+ "*/fx_events_by_type* [group|individual] \n List of events by type \n \n"
				+ "*/fx_event_score_list*  [eventName] event \n Gives the score list of a given event. \n \n"
				+ "*/fx_event_del* \n Removes an event! \n \n"
				+ "*/fx_event_score_add* @username [points] [name] \n Adds a score to an event attendee. \n \n"
				+ "*/fx_action_add* \n [Code] [ActionName] [Points] \n \n"
				+ "*/fx_action_score_add* \n [EventName] [UserName] [ActionCode] \n \n"
				+ "*/fx_manager_add* [event name] @username \n Adds an event manager. \n \n"
				+ "*/fx_manager_list* [event name] \n List of event managers . \n \n"
				+ "*/fx_manager_del* eventName @username \n Removes an event manager. \n \n"
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
