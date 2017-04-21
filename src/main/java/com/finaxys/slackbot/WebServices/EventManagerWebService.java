package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Matchers.EventManagerArgumentsMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.SlackUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/events")
public class EventManagerWebService extends BaseWebService {

	@Autowired
	private SlackApiAccessService slackApiAccessService;
	
	@Autowired
	private SlackUserService slackUserService;

	@RequestMapping(value = "/event_manager/fx_manager_add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create(	@RequestParam("text") String arguments,
			   								@RequestParam("user_id") String adminSlackUserId) {
		SlackBotTimer timer = new SlackBotTimer();

		Log.info("/fx_manager_add  ");
		
		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_manager_add");
		
		String profileId   = argumentsSplitter.getUserId();
		String profileName = argumentsSplitter.getUserName();
		String eventName   = argumentsSplitter.getString(0);
		
		Event event = eventService.getEventByName(eventName);
		
		if (event==null)
		{
			return newResponseEntity("/fx_manager_add : " + arguments + " event does not exist " + timer, true);
		}
		
		if (isEventManager(adminSlackUserId, eventName) || isAdmin(adminSlackUserId)) {
			timer.capture();
			SlackUser slackUser = slackUserService.get(profileId);

			timer.capture();

			slackUser = (slackUser == null) ? new SlackUser(profileId, profileName) : slackUser;

			slackUserService.save(slackUser);

			Role role = new Role("event_manager",slackUser,event);

			roleService.save(role);
			timer.capture();

			return newResponseEntity("/fx_manager_add  : " + arguments + "\n " + "<@" + profileId + "|"
					+ slackApiAccessService.getUser(slackUser.getSlackUserId()).getName()
					+ "> has just became a event manager!" + timer, true);
		}

		return newResponseEntity("/fx_manager_add  : " + arguments + " you are not a event manager!" + timer, true);
	}

	@RequestMapping(value = "/event_manager/fx_manager_del", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> remove( @RequestParam("user_id") String userId,
			   								@RequestParam("text") String arguments) {
										   
		SlackBotTimer timer = new SlackBotTimer();

		Log.info("/fx_manager_del" + arguments);
		
		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_manager_del");
		
		String slackUserId = argumentsSplitter.getUserId();
		String eventName   = argumentsSplitter.getString(0);

		Event event = eventService.getEventByName(eventName);

		timer.capture();
		
		if (event == null) {
			Message message = new Message("event doesn't exist");

			Log.info(message.getText());

			return newResponseEntity(message);
		}
		
		if (isEventManager(userId, eventName) || isAdmin(userId)) {
				
				Object[] roles = roleService.getAll().stream()
						.filter(e -> e.getEvent() != null && e.getEvent().equals(event))
						.toArray();
				timer.capture();

				for (Object r : roles) {
					Role role = (Role)r;
					if (role.getSlackUser().getSlackUserId().equals(slackUserId)) {
						roleService.remove(role);

						Message message = new Message("/fx_manager_del : " + arguments + "\n " + "<@" + slackUserId
								+ "|" + slackApiAccessService.getUser(slackUserId).getName()
								+ "> is no more a event manager!");

						Log.info(message.getText());

						return newResponseEntity(message);
					}
				}

				timer.capture();

				Message message = new Message("/fx_manager_del : " + arguments + "\n " + "<@" + slackUserId + "|"
						+ slackApiAccessService.getUser(slackUserId).getName()
						+ "> is already not a event manager!");

				Log.info(message.getText());

				return newResponseEntity(message);
		}

		Message message = new Message(
				"/fx_manager_del : " + arguments + "\n " + "You are neither an admin nor a event manager");

		Log.info(message.getText());

		return newResponseEntity(message);
	}

	@RequestMapping(value = "/event_manager/fx_manager_list", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventManagers(@RequestParam("text") String arguments) {

		Log.info("/fx_manager_list");

		String eventName = arguments.trim();
		Event event = eventService.getEventByName(eventName);

		if (event == null) {
			Message message = new Message("Event nonexistent");

			Log.info(message.getText());

			return newResponseEntity(message);
		}
		Object[] roles = roleService.getAll().stream()
				.filter(e -> e.getEvent() != null && e.getEvent().equals(event))
				.toArray();
		System.out.println(roles.length);
		String messageText = "List of Event managers list:\n";
		if (roles.length > 0) {
			for (Object r : roles) {
				Role role = (Role) r;
				messageText += "<@" + role.getSlackUser().getSlackUserId() + "|"
						+ slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName() + "> \n";
			}
			Message message = new Message("/fx_manager_list " + "\n " + messageText);

			Log.info(message.getText());

			return newResponseEntity(message);
		}

		Message message = new Message("/fx_manager_list :" + "\n " + messageText + " No event managers are found");

		Log.info(message.getText());

		return

		newResponseEntity(message);
	}

}
