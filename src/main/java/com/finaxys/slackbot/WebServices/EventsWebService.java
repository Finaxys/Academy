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

	@RequestMapping(value = "/type", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByType(@RequestParam("text")  String arguments) 
	{

		SlackBotTimer timer = new SlackBotTimer();
		
		EventTypeMatcher eventTypeMatcher = new EventTypeMatcher();
		
		if (!eventTypeMatcher.match(arguments))
			return newResponseEntity(	" /fx_events_by_type " 
										+ arguments 
										+ " \n " + "type has to be:[group|individual]" 
										+ timer , true);
		timer.capture();
		
		List<Event> events = eventService.getEventByType(arguments);
		
		if (events.isEmpty())
			return newResponseEntity(	" /fx_events_by_type " 
										+ arguments 
										+ " \n " 
										+ "No events with type" 
										+ arguments 
										+ timer ,true);

		return newResponseEntity(	" /fx_events_by_type " 
									+ arguments 
									+ " \n " 
									+ eventService.getStringFromList(events)
									+ timer , true);

	}


	@RequestMapping(value = "/name", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByName(@RequestParam("text") 	String arguments) {
		SlackBotTimer timer = new SlackBotTimer();
		
		Log.info("/fx_event_named " + arguments);
		
		Event event = eventService.getEventByName(arguments);
		
		timer.capture();
		
		if (event == null)
			return newResponseEntity(	"/fx_event_named "
										+arguments
										+"\n"
										+"Nonexistent event." 
										+ timer,true);
		else
			return newResponseEntity( 	"/fx_events_named " 
										+ arguments 
										+ "\n " 
										+ event
										+ timer , true);

	}


	@RequestMapping(value = "/date", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByDate(@RequestParam("text")  		  String text) {

		SlackBotTimer timer = new SlackBotTimer();

		DateMatcher dateMatcher = new DateMatcher();
		
		if (!dateMatcher.match(text.trim()))
			return newResponseEntity(	" /fx_events_by_date " 
										+ text 
										+ " \n " 
										+ "Date format: yyyy-MM-dd" 
										+ timer ,true);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date wantedDate = new Date();
		try 
		{
			wantedDate = dateFormat.parse(text);
		} 
		catch (Exception e) 
		{
		}
		
		List<Event> events = eventService.getEventByDate(wantedDate);
		
		timer.capture();
		
		if (events.isEmpty())
			return newResponseEntity(	" /fx_events_by_date " 
										+ text 
										+ " \n " 
										+ "There are no events on this date: " 
										+ text 
										+ timer ,true);
		else
			return newResponseEntity(eventService.getStringFromList(events)+ timer ,true);
	}


	@RequestMapping(value = "/listAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getAllEvents(	@RequestParam("token") 	  		String appVerificationToken,
													@RequestParam("team_domain") 	String slackTeam) {
		
		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();
		
		List<Event> events = eventService.getAll();
		
		timer.capture();
		
		if (events.isEmpty())
			return newResponseEntity(	"/fx_event_list" 
										+ " \n " 
										+ "There no previous events! Come on create one!" 
										+ timer, true);
		
		else
			return newResponseEntity(eventService.getStringFromList(events)+ timer, true);
	}
	

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> create( @RequestParam("text") 		String text,
			   								@RequestParam("user_id") 	String userId) {
		SlackBotTimer timer = new SlackBotTimer();

		
		CreateEventMatcher createEventMatcher = new CreateEventMatcher();
		
		timer.capture();
		
		if (!createEventMatcher.match(text.trim()))
			return  newResponseEntity(	" /fx_event_add "
										+text+" \n "
										+"Your request should have the following format: [eventName] [group|individual] [descriptionText]" 
										+ timer ,true);
		else 
		{	
			timer.capture();
			
			List<String> eventInfo = Arrays.asList((text.trim().split(" ")));
			Event event 	= new Event();
			
			int indexOfType = eventInfo.lastIndexOf("group") > eventInfo.lastIndexOf("individual") ?  
								eventInfo.lastIndexOf("group") : eventInfo.lastIndexOf("individual");
			
			event.setName(eventInfo.get(0));
			event.setCreationDate(new Date());
			for(int i=1; i<indexOfType; i++){
				event.setName(event.getName()+ " " + eventInfo.get(i));
			}
			event.setType(eventInfo.get(indexOfType));
			event.setDescription(eventInfo.get(indexOfType+1));
			
			for(int i=indexOfType+2; i<eventInfo.size(); i++){
				event.setDescription(event.getDescription()+ " " + eventInfo.get(i));
			}
			
			new Thread(() -> { 
				
				eventService.save(event);
								
				SlackUser user = slackUserService.get(userId);
				Role role = new Role("event_manager", user, event);
				role.setRole		  ("event_manager");
				role.setSlackUser	  (user);
				role.setEvent  		  (event);
				
				roleService.save(role);
				
				newResponseEntity(	"/fx_event_add "
									+text+" \n "
									+"Traitement terminé" 
									+ timer , true);
		}
		).start();
			
			timer.capture();
			
			return newResponseEntity(	"/fx_event_add "
										+text
										+" \n "
										+"Traitement en cours" 
										+ timer , true);
		}
	}

	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> remove( @RequestParam("user_id") 	String profileId,
			   								@RequestParam("text") 		String text) {
		
		SlackBotTimer timer = new SlackBotTimer();
		
		String  		eventName = text.trim();
		Event event = eventService.getEventByName(eventName);
		
		timer.capture();
		
		if(event==null)
			return newResponseEntity(	"fx_event_del "
										+"\n"
										+"Non existent event." 
										+ timer ,true);

		if(!isEventManager(profileId,eventName) && !isAdmin(profileId))
			return newResponseEntity(	"fx_event_del "
										+"\n"
										+"You are neither an admin nor a event manager!" 
										+ timer ,true);
		
		timer.capture();
				
		new Thread(()->{
			List<Role> roles = roleService.getAllByEvent(event);
			
			for(Role role : roles) {
				roleService.remove(role);
			}
			eventService.remove(event);
			
		}).start();
		
		timer.capture();
		
		return newResponseEntity(	"fx_event_del "
									+text
									+" \n "
									+"Event successfully removed." 
									+ timer , true);
	}
}
