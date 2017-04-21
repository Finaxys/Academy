package com.finaxys.slackbot.WebServices;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import com.finaxys.slackbot.BUL.Matchers.CreateEventMatcher;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.BUL.Matchers.EventTypeMatcher;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.RoleService;
import com.finaxys.slackbot.interfaces.SlackUserService;

@RestController
@RequestMapping("/events")
public class EventsWebService extends BaseWebService {

	@Autowired
	private SlackUserService slackUserService;

	@Autowired
	private EventService eventService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private SlackApiAccessService slackApiAccessService;

	@RequestMapping(value = "/fx_events_by_type", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByType(@RequestParam("text") String arguments) {

		SlackBotTimer timer = new SlackBotTimer();

		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_events_by_type");

		String eventType = argumentsSplitter.getEventType();
		System.out.println(eventType + "-------------");
		List<Event> events = eventService.getEventByType(eventType);

		if (events.isEmpty())
			return newResponseEntity(
					" /fx_events_by_type " + arguments + " \n " + "No events with type" + arguments + timer, true);

		return newResponseEntity(
				" /fx_events_by_type " + arguments + " \n " + eventService.getStringFromList(events) + timer, true);

	}

	@RequestMapping(value = "/fx_event_named", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByName(@RequestParam("text") String arguments) {
		SlackBotTimer timer = new SlackBotTimer();

		Log.info("/fx_event_named " + arguments);

		Event event = eventService.getEventByName(arguments);

		timer.capture();

		if (event == null)
			return newResponseEntity("/fx_event_named " + arguments + "\n" + "Nonexistent event." + timer, true);
		else
			return newResponseEntity("/fx_events_named " + arguments + "\n " + event + timer, true);

	}

	@RequestMapping(value = "/fx_events_by_date", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByDate(@RequestParam("text") String text) {

		SlackBotTimer timer = new SlackBotTimer();

		DateMatcher dateMatcher = new DateMatcher();

		if (!dateMatcher.match(text.trim()))
			return newResponseEntity(" /fx_events_by_date " + text + " \n " + "Date format: yyyy-MM-dd" + timer, true);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date wantedDate = new Date();
		try {
			wantedDate = dateFormat.parse(text);
		} catch (Exception e) {
		}

		List<Event> events = eventService.getEventByDate(wantedDate);

		timer.capture();

		if (events.isEmpty())
			return newResponseEntity(
					" /fx_events_by_date " + text + " \n " + "There are no events on this date: " + text + timer, true);
		else
			return newResponseEntity(eventService.getStringFromList(events) + timer, true);
	}

	@RequestMapping(value = "/fx_event_list", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getAllEvents(@RequestParam("token") String appVerificationToken,
			@RequestParam("team_domain") String slackTeam) {

		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();

		List<Event> events = eventService.getAll();

		timer.capture();

		if (events.isEmpty())
			return newResponseEntity(
					"/fx_event_list" + " \n " + "There no previous events! Come on create one!" + timer, true);

		else
			return newResponseEntity(eventService.getStringFromList(events) + timer, true);
	}

	@RequestMapping(value = "/fx_event_add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create(@RequestParam("text") String arguments,
			@RequestParam("user_id") String userId) {
		SlackBotTimer timer = new SlackBotTimer();

		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_event_add");

		String eventType = argumentsSplitter.getEventType();
		String eventName = argumentsSplitter.getString(0);
		String eventDescription = argumentsSplitter.getString(1);

		timer.capture();

		Event event = new Event(eventName, eventDescription, eventType);

		if (eventService.getEventByName(eventName) != null)
			return newResponseEntity("/fx_event_add " + arguments + " \n " + "event already exists !" + timer, true);

		new Thread(() -> {
			eventService.save(event);
			SlackUser user = slackUserService.get(userId);
			Role role = new Role("event_manager", user, event);
			roleService.save(role);
		}).start();

		timer.capture();

		return newResponseEntity("/fx_event_add " + arguments + " \n " + "event added correctly !" + timer, true);
	}

	@RequestMapping(value = "/fx_event_del", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> remove(@RequestParam("user_id") String profileId,
			@RequestParam("text") String arguments) {

		SlackBotTimer timer = new SlackBotTimer();

		String eventName = arguments.trim();
		Event event = eventService.getEventByName(eventName);

		timer.capture();

		if (event == null)
			return newResponseEntity("fx_event_del " + "\n" + "Non existent event." + timer, true);

		if (!isEventManager(profileId, eventName) && !isAdmin(profileId))
			return newResponseEntity("fx_event_del " + "\n" + "You are neither an admin nor a event manager!" + timer,
					true);

		timer.capture();

		new Thread(() -> {
			List<Role> roles = roleService.getAllByEvent(event);

			for (Role role : roles) {
				roleService.remove(role);
			}
			eventService.remove(event);

		}).start();

		timer.capture();

		return newResponseEntity("fx_event_del " + arguments + " \n " + "Event successfully removed." + timer, true);
	}
}
