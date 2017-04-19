package com.finaxys.slackbot.WebServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaxys.slackbot.BUL.Matchers.EventTypeMatcher;
import com.finaxys.slackbot.BUL.Matchers.CreateEventMatcher;
import com.finaxys.slackbot.BUL.Matchers.DateMatcher;
import com.finaxys.slackbot.DAL.*;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.Settings;
import com.finaxys.slackbot.Utilities.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsWebService extends BaseWebService {

	@Autowired
	private Repository<Event, Integer> eventRepository;

	@Autowired
	Repository<SlackUser, String> finaxysProfileRepository;

	@Autowired
	Repository<Role, Integer> roleRepository;

	private boolean requestParametersAreValid(String... parameters) 
	{
		for (String parameter : parameters) 
		{
			if (parameter == null || parameter.isEmpty())
				return false;
		}

		return true;
	}


	private String getStringFromList(List<Event> events) 
	{
		String result = "";

		for (Event event : events) 
		{
			result += "Event name: " + event.getName() + ", number of participants: " + event.getAttendees().size() + " \n ";
		}

		return result;
	}


	@RequestMapping(value = "/type", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByType(@RequestParam("text") 		 String text,
														@RequestParam("token") 		 String appVerificationToken,
														@RequestParam("team_domain") String slackTeam) {

		Timer timer = new Timer();
		
		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();

		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return newResponseEntity( " /fx_events_by_type " + text + " \n " + "There was a problem treating your request. Please try again." + timer , true);
		
		EventTypeMatcher eventTypeMatcher = new EventTypeMatcher();
		
		if (!eventTypeMatcher.match(text))
			return newResponseEntity(" /fx_events_by_type " + text + " \n " + "type has to be:[group|individual]" + timer , true);
		timer.capture();
		
		List<Event> events = eventRepository.getByCriterion("type", text);
		
		if (events.isEmpty())
			return newResponseEntity(" /fx_events_by_type " + text + " \n " + "No events with type" + text + timer ,true);

		return newResponseEntity(" /fx_events_by_type " + text + " \n " + getStringFromList(events) + timer , true);

	}


	@RequestMapping(value = "/name", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByName(@RequestParam("text") 		 String text,
														@RequestParam("token") 		 String appVerificationToken,
														@RequestParam("team_domain") String slackTeam) {
		Timer timer = new Timer();
		
		Log.info("/fx_event_named " + text);
		
		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();
		
		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return newResponseEntity( " /fx_event_named " + text + " \n " + "There was a problem treating your request. Please try again." + timer , true);
		
		
		List<Event> events = eventRepository.getByCriterion("name", text);
		
		timer.capture();
		
		if (events.isEmpty())
			return newResponseEntity("/fx_event_named "+text+"\n"+"Nonexistent event." + timer,true);
		else
			return newResponseEntity( "/fx_events_named " + text + "\n " + getStringFromList(events) + timer , true);

	}


	@RequestMapping(value = "/date", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByDate(@RequestParam("text")  		  String text,
														@RequestParam("token") 		  String appVerificationToken,
														@RequestParam("team_domain")  String slackTeam) {

		Timer timer = new Timer();
		
		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();

		if (!requestParametersAreValid(new String[]{text, appVerificationToken, slackTeam}))
			return newResponseEntity( " /fx_events_by_date " + text + " \n " + "There was a problem treating your request. Please try again." + timer , true);

		DateMatcher dateMatcher = new DateMatcher();
		
		if (!dateMatcher.match(text.trim()))
			return newResponseEntity(" /fx_events_by_date " + text + " \n " + "Date format: yyyy-MM-dd" + timer ,true);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date wantedDate = new Date();
		try 
		{
			wantedDate = dateFormat.parse(text);
		} 
		catch (Exception e) 
		{
		}
		
		List<Event> events = eventRepository.getByCriterion("creationDate", wantedDate);
		
		timer.capture();
		
		if (events.isEmpty())
			return newResponseEntity(" /fx_events_by_date " + text + " \n " + "There are no events on this date: " + text + timer ,true);
		else
			return newResponseEntity(getStringFromList(events) + timer ,true);
	}


	@RequestMapping(value = "/listAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getAllEvents(@RequestParam("token") 	  String appVerificationToken,
													 @RequestParam("team_domain") String slackTeam) {
		
		Timer timer = new Timer();

		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();
		
		if (!requestParametersAreValid(new String[]{appVerificationToken, slackTeam}))
			return newResponseEntity(" /fx_event_list " + " \n " + "There was a problem treating your request. Please try again." + timer, true);
		
		timer.capture();
		
		List<Event> events = eventRepository.getAll();
		
		timer.capture();
		
		if (events.isEmpty())
			return newResponseEntity("/fx_event_list" + " \n " + "There no previous events! Come on create one!" + timer, true);
		else
			return newResponseEntity(getStringFromList(events) + timer, true);
	}
	

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create(@RequestParam("token") 		String appVerificationToken,
										   @RequestParam("team_domain") String slackTeam,
										   @RequestParam("text") 		String text,
										   @RequestParam("user_id") 	String userId) {
		
		Timer timer = new Timer();

		if(noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);
		timer.capture();
		
		if (!requestParametersAreValid(new String[]{text,appVerificationToken, slackTeam}))
			return newResponseEntity(" /fx_event_add " + text+ " \n " + "There was a problem treating your request. Please try again." + timer, true);


		CreateEventMatcher createEventMatcher = new CreateEventMatcher();
		
		timer.capture();
		
		if (!createEventMatcher.match(text.trim()))
			return  newResponseEntity(" /fx_event_add "+text+" \n "+"Your request should have the following format: [eventName],[group|individual],[descriptionText]" + timer ,true);
		else 
		{	
			timer.capture();
			
			String[]  eventInfo = text.trim().split(",");
			Event event 	= new Event();
			
			event.setName		(eventInfo[0]);
			event.setType		(eventInfo[1]);
			event.setDescription(eventInfo[2]);
			
			new Thread(new Runnable()
			{
				public void run()
				{
					eventRepository.addEntity(event);
					
					Role role = new Role();
					
					role.setRole		  ("event_manager");
					role.setSlackUser(finaxysProfileRepository.findById(userId));
					role.setEvent  (event);
					
					roleRepository.addEntity(role);
					
					newResponseEntity("/fx_event_add "+text+" \n "+"Traitement terminé" + timer , true);
				}
			}).start();

			
			timer.capture();
			
			return newResponseEntity("/fx_event_add "+text+" \n "+"Traitement en cours" + timer , true);
		}

	}

	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> remove(@RequestParam("token") 		String appVerificationToken,
										   @RequestParam("team_domain") String slackTeam,
										   @RequestParam("user_id") 	String profileId,
										   @RequestParam("text") 		String text) {
		
		Timer timer = new Timer();

		if(noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();
		
		String  		eventName = text.trim();
		List<Event> events 	  = eventRepository.getByCriterion("name",eventName);
		
		timer.capture();
		
		if(events.size()==0)
			return newResponseEntity("fx_event_del "+"\n"+"Non existent event." + timer ,true);

		if (!requestParametersAreValid(new String[]{text,appVerificationToken, slackTeam}))
			return newResponseEntity(" /fx_event_del " + text+ " \n " + "There was a problem treating your request. Please try again." + timer , true);

		if(!isEventManager(profileId,eventName) && !isAdmin(profileId))
			return newResponseEntity("fx_event_del "+"\n"+"You are neither an admin nor a event manager!" + timer ,true);
		
		timer.capture();
		
		Event  event =  events.get(0);
		
		new Thread(new Runnable()
		{
			public void run()
			{
				List<Role> roles = roleRepository.getByCriterion("eventId",events.get(0).getEventId());
				
				for(Role role : roles)
				{
					roleRepository.delete(role);
				}
				
				eventRepository.delete(event);
			}
		}).start();
		
		timer.capture();
		
		return newResponseEntity("fx_event_del "+text+" \n "+"Event successfully removed." + timer , true);
	}
}
