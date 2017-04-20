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
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.Utilities.Log;
import com.finaxys.slackbot.Utilities.SlackBotTimer;

@RestController
@RequestMapping("/events")
public class EventsWebService extends BaseWebService {

	@Autowired
	private Repository<Event, Integer> eventRepository;

	@Autowired
	Repository<SlackUser, String> slackUserRepository;
	
	@Autowired
	private SlackApiAccessService slackApiAccessService;

	@Autowired
	Repository<Role, Integer> roleRepository;

	


	private String getStringFromList(List<Event> events) 
	{
		String result = "";

		for (Event event : events) 
		{
			result += 	"Event name: "
						+ event.getName() 
						+ ", number of participants: " 
						+ event.getAttendees().size() 
						+ " \n ";
		}

		return result;
	}


	@RequestMapping(value = "/type", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByType(@RequestParam("text")  String text) 
	{

		SlackBotTimer timer = new SlackBotTimer();
		
		EventTypeMatcher eventTypeMatcher = new EventTypeMatcher();
		
		if (!eventTypeMatcher.match(text))
			return newResponseEntity(	" /fx_events_by_type " 
										+ text 
										+ " \n " + "type has to be:[group|individual]" 
										+ timer , true);
		timer.capture();
		
		List<Event> events = eventRepository.getByCriterion("type", text);
		
		if (events.isEmpty())
			return newResponseEntity(	" /fx_events_by_type " 
										+ text 
										+ " \n " 
										+ "No events with type" 
										+ text 
										+ timer ,true);

		return newResponseEntity(	" /fx_events_by_type " 
									+ text 
									+ " \n " 
									+ getStringFromList(events) 
									+ timer , true);

	}


	@RequestMapping(value = "/name", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getEventsByName(@RequestParam("text") 	String text) {
		SlackBotTimer timer = new SlackBotTimer();
		
		Log.info("/fx_event_named " + text);
		
		List<Event> events = eventRepository.getByCriterion("name", text);
		
		timer.capture();
		
		if (events.isEmpty())
			return newResponseEntity(	"/fx_event_named "
										+text
										+"\n"
										+"Nonexistent event." 
										+ timer,true);
		else
			return newResponseEntity( 	"/fx_events_named " 
										+ text 
										+ "\n " 
										+ getStringFromList(events) 
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
		
		List<Event> events = eventRepository.getByCriterion("creationDate", wantedDate);
		
		timer.capture();
		
		if (events.isEmpty())
			return newResponseEntity(	" /fx_events_by_date " 
										+ text 
										+ " \n " 
										+ "There are no events on this date: " 
										+ text 
										+ timer ,true);
		else
			return newResponseEntity(getStringFromList(events) + timer ,true);
	}


	@RequestMapping(value = "/listAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> getAllEvents(	@RequestParam("token") 	  		String appVerificationToken,
													@RequestParam("team_domain") 	String slackTeam) {
		
		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();
		
		List<Event> events = eventRepository.getAll();
		
		timer.capture();
		
		if (events.isEmpty())
			return newResponseEntity(	"/fx_event_list" 
										+ " \n " 
										+ "There no previous events! Come on create one!" 
										+ timer, true);
		
		else
			return newResponseEntity(getStringFromList(events) + timer, true);
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
			
			for(int i=1; i<indexOfType; i++){
				event.setName(event.getName()+ " " + eventInfo.get(i));
			}
			event.setType(eventInfo.get(indexOfType));
			event.setDescription(eventInfo.get(indexOfType+1));
			
			for(int i=indexOfType+2; i<eventInfo.size(); i++){
				event.setDescription(event.getDescription()+ " " + eventInfo.get(i));
			}
			
			new Thread(() -> { 
				
				eventRepository.addEntity(event);
				
				Role role = new Role();
				
				SlackUser user = slackUserRepository.findById(userId);
				if(user == null){
					String userName = slackApiAccessService.getUser(userId).getName();
					user = new SlackUser(userId, userName);
					slackUserRepository.addEntity(user);
				}
				
				role.setRole		  ("event_manager");
				role.setSlackUser	  (user);
				role.setEvent  		  (event);
				
				roleRepository.addEntity(role);
				
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
		List<Event> events 	  = eventRepository.getByCriterion("name",eventName);
		
		timer.capture();
		
		if(events.size()==0)
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
		
		Event  event =  events.get(0);
		
		new Thread(()->{
			List<Role> roles = roleRepository.getByCriterion("eventId",events.get(0).getEventId());
			
			for(Role role : roles) {
				roleRepository.delete(role);
			}
			eventRepository.delete(event);
			
		}).start();
		
		timer.capture();
		
		return newResponseEntity(	"fx_event_del "
									+text
									+" \n "
									+"Event successfully removed." 
									+ timer , true);
	}
}
