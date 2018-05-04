package com.finaxys.slackbot.WebServices;
							
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Message;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.SlackUserService;


@RestController

@RequestMapping("/events")
public class EventManagerWebService extends BaseWebService {

	@Autowired
	private SlackApiAccessService slackApiAccessService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private SlackUserService slackUserService;

	@RequestMapping(value = "/event_manager/fx_manager_add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create(	@RequestParam("text") String arguments,
			   								@RequestParam("user_id") String adminUserId) {
		
		SlackBotTimer timer = new SlackBotTimer();
		
		Log.info("/fx_manager_add text="+arguments+" user_id="+adminUserId);
		
		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_manager_add");

		String profileId   = argumentsSplitter.getUserId();
		String profileName = argumentsSplitter.getUserName();
		String eventName   = argumentsSplitter.getString(0);
		String userId = "";
		List<SlackUser> allUsers = slackUserService.getAll();
		
		// GET USER ID OF THE SELECTED USER IN PARAMETER!
		for(SlackUser user : allUsers) {
			System.out.println(user.getName()+"  | "+ profileId +" |  "+ profileName);
			if (user.getName().equals(profileId)) {
				userId = user.getSlackUserId();
			}
		}
		 
		if(userId.isEmpty()) {
			SlackUser newUser = new SlackUser(profileId,profileName);
			newUser = slackUserService.save(newUser);
			
		    allUsers = slackUserService.getAll();
			
			// GET USER ID OF THE SELECTED USER IN PARAMETER!
			for(SlackUser user : allUsers) {
				if (user.getName().equals(profileId)) {
					userId = user.getSlackUserId();
				}
			}
			System.out.println("--------------- NEW USER ID ---------- "+userId);
			//return newResponseEntity("/fx_manager_add  : " + arguments + " the user is not valid!" + timer, true);

		}
		Event event = eventService.getEventByName(eventName);

		if (event==null)
		{
			return newResponseEntity("/fx_manager_add : " + arguments + " event does not exist " + timer, true);
		}

		
		if (isEventManager(adminUserId, eventName) || isAdmin(adminUserId)) {
			timer.capture();
			SlackUser slackUser = slackUserService.get(userId);
			timer.capture();

			slackUser = (slackUser == null) ? new SlackUser(profileId, profileName) : slackUser;

			if(slackUserService.isEventManager(userId, eventName))
				return newResponseEntity("/fx_manager_add  : " + arguments + "\n " + "<@" + profileId + "|"
						+ slackApiAccessService.getUser(slackUser.getSlackUserId()).getName()
						+ "> is already a manager!" + timer, true);

			
			Role role = new Role("event_manager",slackUser,event);

			slackUser.getRoles().add(role);

			SlackUser slackUser2 = slackUser;

			new Thread(()->{	slackUserService.save(slackUser2);	}).start();

			
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

		Log.info("/fx_manager_del " + arguments);

		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_manager_del");
		
		String slackUserId = argumentsSplitter.getUserId();
		String eventName   = argumentsSplitter.getString(0);
		String userIdArgs = "";
		List<SlackUser> users = slackUserService.getAll();
		for(SlackUser user : users) {
			if (user.getName().equals(slackUserId)) {
				userIdArgs = user.getSlackUserId();
			}
		}
		
		
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
					if (role.getSlackUser().getSlackUserId().equals(userIdArgs)) {

						Role role2 = role;
						
						new Thread(() -> {	roleService.remove(role2);	}).start();	

						Message message = new Message("/fx_manager_del : " + arguments + "\n " + "<@" + slackUserId
								+ "|" + slackApiAccessService.getUser(userIdArgs).getName() + timer
								+ "> is no more a event manager!");

						Log.info(message.getText());

						return newResponseEntity(message);
					}
				}
				

				timer.capture();

				Message message = new Message("/fx_manager_del : " + arguments + "\n " + "<@" + slackUserId + "|"
						+ slackApiAccessService.getUser(userIdArgs).getName()
						+ "> is already not a event manager!");

				Log.info(message.getText());

				return newResponseEntity(message);
		}

		Message message = new Message(
				"/fx_manager_del : " + arguments + "\n " + "You are neither an admin nor a event manager");


		return newResponseEntity(message);
	}

	@RequestMapping(value = "/event_manager/fx_manager_list", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventManagers(@RequestParam("text") String arguments) {

		Log.info("/fx_manager_list ");
		SlackBotTimer timer = new SlackBotTimer();
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
				messageText += "<@" +  role.getSlackUser().getSlackUserId() + "|"
						+ slackApiAccessService.getUser(role.getSlackUser().getSlackUserId()).getName() + "> \n";
			}
			Message message = new Message("/fx_manager_list " + "\n " + messageText + timer);

			Log.info(message.getText());

			return newResponseEntity(message);
		}

		Message message = new Message("/fx_manager_list :" + "\n " + messageText + " No event managers are found\n" );

		Log.info(message.getText());

		return

		newResponseEntity(message);
	}

}
