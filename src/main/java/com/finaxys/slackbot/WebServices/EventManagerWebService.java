package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.BUL.Matchers.EventManagerArgumentsMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventManagerWebService extends BaseWebService {

	@Autowired
	private SlackApiAccessService slackApiAccessService;

	@Autowired
	Repository<SlackUser, String> finaxysProfileRepository;

	@Autowired
	Repository<Role, Integer> roleRepository;

	@Autowired
	Repository<Event, Integer> eventRepository;

	@RequestMapping(value = "/event_manager/new", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create(@RequestParam("token") String appVerificationToken,
			@RequestParam("team_domain") String slackTeam, @RequestParam("text") String arguments,
			@RequestParam("user_id") String adminFinaxysProfileId) {

		Timer timer = new Timer();

		Log.info("/fx_manager_add  ");

		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);

		timer.capture();

		EventManagerArgumentsMatcher eventManagerArgumentsMatcher = new EventManagerArgumentsMatcher();

		if (!eventManagerArgumentsMatcher.isCorrect(arguments))
			return newResponseEntity(
					"/fx_manager_add  :" + arguments + "\n " + "Arguments should be :[event name] @Username" + timer,
					true);
		timer.capture();

		String profileId = eventManagerArgumentsMatcher.getUserIdArgument(arguments);
		String profileName = eventManagerArgumentsMatcher.getUserNameArgument(arguments);
		String eventName = eventManagerArgumentsMatcher.getEventName(arguments);

		if (isEventManager(adminFinaxysProfileId, eventName) || isAdmin(adminFinaxysProfileId)) {
			timer.capture();
			SlackUser finaxysProfile = finaxysProfileRepository.findById(profileId);

			timer.capture();
			List<Event> events = eventRepository.getByCriterion("name", eventName);

			finaxysProfile = (finaxysProfile == null) ? new SlackUser(profileId, profileName) : finaxysProfile;

			finaxysProfileRepository.saveOrUpdate(finaxysProfile);
			timer.capture();
			if (events.size() == 0)
				return newResponseEntity("/fx_manager_add  :" + arguments + "\n " + "event does not exist" + timer,
						true);

			Role role = new Role();

			role.setRole("event_manager");
			role.setEvent(events.get(0));
			role.setSlackUser(finaxysProfile);

			roleRepository.saveOrUpdate(role);
			timer.capture();

			return newResponseEntity("/fx_manager_add  : " + arguments + "\n " + "<@" + profileId + "|"
					+ slackApiAccessService.getUser(finaxysProfile.getSlackUserId()).getName()
					+ "> has just became a event manager!" + timer, true);
		}

		return newResponseEntity("/fx_manager_add  : " + arguments + " you are not a event manager!" + timer, true);
	}

	@RequestMapping(value = "/event_manager/remove", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> remove(@RequestParam("token") String appVerificationToken,
			@RequestParam("team_domain") String slackTeam, @RequestParam("user_id") String userId,
			@RequestParam("text") String arguments) {
		Timer timer = new Timer();

		Log.info("/fx_manager_del" + arguments);

		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);

		EventManagerArgumentsMatcher eventManagerArgumentsMatcher = new EventManagerArgumentsMatcher();
		timer.capture();
		if (!eventManagerArgumentsMatcher.isCorrect(arguments)) {
			Message message = new Message(
					"/fx_manager_del :" + arguments + "\n " + "Arguments should be :[event name] @Username");
			Log.info(message.getText().toString());
			return newResponseEntity(message);
		}

		timer.capture();

		String finaxysProfileId = eventManagerArgumentsMatcher.getUserIdArgument(arguments);
		String eventName = eventManagerArgumentsMatcher.getEventName(arguments);

		List<Event> events = eventRepository.getByCriterion("name", eventName);

		timer.capture();

		if (isEventManager(userId, eventName) || isAdmin(userId)) {
			if (events.size() == 0) {
				Message message = new Message("event doesn't exist");

				Log.info(message.getText());

				return newResponseEntity(message);
			} else {
				Event event = events.get(0);
				List<Role> roles = roleRepository.getByCriterion("eventId", event.getEventId());

				timer.capture();

				for (Role role : roles) {
					if (role.getSlackUser().getSlackUserId().equals(finaxysProfileId)) {
						roleRepository.delete(role);

						Message message = new Message("/fx_manager_del : " + arguments + "\n " + "<@" + finaxysProfileId
								+ "|" + slackApiAccessService.getUser(finaxysProfileId).getName()
								+ "> is no more a event manager!");

						Log.info(message.getText());

						return newResponseEntity(message);
					}
				}

				timer.capture();

				Message message = new Message("/fx_manager_del : " + arguments + "\n " + "<@" + finaxysProfileId + "|"
						+ slackApiAccessService.getUser(finaxysProfileId).getName()
						+ "> is already not a event manager!");

				Log.info(message.getText());

				return newResponseEntity(message);
			}
		}

		Message message = new Message(
				"/fx_manager_del : " + arguments + "\n " + "You are neither an admin nor a event manager");

		Log.info(message.getText());

		return newResponseEntity(message);
	}

	@RequestMapping(value = "/event_manager/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventManagers(@RequestParam("token") String appVerificationToken,
			@RequestParam("team_domain") String slackTeam, @RequestParam("text") String arguments) {

		Log.info("/fx_manager_list");

		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);

		String eventName = arguments.trim();
		List<Event> events = eventRepository.getByCriterion("name", eventName);

		if (events.size() == 0) {
			Message message = new Message("Event nonexistent");

			Log.info(message.getText());

			return newResponseEntity(message);
		}
		Object[] roles = roleRepository.getAll().stream()
				.filter(e -> e.getEvent() != null && e.getEvent().getEventId().equals(events.get(0).getEventId()))
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
