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
import com.finaxys.slackbot.BUL.Matchers.EventScoreArgumentsMatcher;
import com.finaxys.slackbot.BUL.Matchers.OneUsernameArgumentMatcher;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.Role;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUserEvent;
import com.finaxys.slackbot.Utilities.Timer;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.SlackUserEventService;

@RestController
@RequestMapping("/scores")
public class ScoreWebService extends BaseWebService {
	@Autowired
	private SlackApiAccessService slackApiAccessService;

	@Autowired
	Repository<SlackUser, String> slackUserRepository;

	@Autowired
	Repository<Event, Integer> eventRepository;

	@Autowired
	Repository<SlackUserEvent, String> slackUserEventRepository;

	@Autowired
	Repository<Role, Integer> roleRepository;
	
	@Autowired
	EventService eventService;
	
	@Autowired
	SlackUserEventService slackUserEventService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> addEventScore(@RequestParam("token") String appVerificationToken,
												  @RequestParam("team_domain") String slackTeam, 
												  @RequestParam("text") String arguments,
												  @RequestParam("user_id") String eventManagerId) {

		Timer timer = new Timer();

		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);

		timer.capture();

		EventScoreArgumentsMatcher eventScoreArgumentsMatcher = new EventScoreArgumentsMatcher();

		if (!eventScoreArgumentsMatcher.isCorrect(arguments))
			return newResponseEntity("/fx_event_score_add " + arguments + " \n "
					+ "Arguments should suit ' .... @Username ... 20 ..... <eventName> event ..' Pattern !"
					+ timer, true);

		String userId = eventScoreArgumentsMatcher.getFinaxysProfileId(arguments);
		String eventName = eventScoreArgumentsMatcher.getEventName(arguments);

		timer.capture();

		List<Event> events = eventRepository.getByCriterion("name", eventName);
		
		timer.capture();

		if (events.size() == 0)
			return newResponseEntity("Nonexistent event" + timer, true);

		//TODO
		if (!isEventManager(eventManagerId, eventName) && !isAdmin(eventManagerId))
			return newResponseEntity("/fx_event_score_add " + arguments + "\n"
					+ "You are neither admin nor a event manager !" + timer, true);

		int score = Integer.parseInt(eventScoreArgumentsMatcher.getScore(arguments));

		SlackUser user = slackUserRepository.findById(userId);
		
		if(user==null){
			user = new SlackUser(userId,slackApiAccessService.getUser(userId).getName());
			slackUserRepository.saveOrUpdate(user);
		}
		Event event = eventService.getEventByName(eventName);
		
		SlackUserEvent slackUserEvent = slackUserEventService.getSlackUserEvent(event, user);
		
		if (slackUserEvent!=null) {
			slackUserEvent.addScore(score);
		}
		else {
			slackUserEvent = new SlackUserEvent(score, event, user);
		}

		SlackUserEvent slackUserEvent2 = slackUserEvent;
		
		timer.capture();

		try {
			timer.capture();

			new Thread(()-> {slackUserEventRepository.saveOrUpdate(slackUserEvent2);}).start();
			
			timer.capture();
		} catch (Exception e) {
			return newResponseEntity(
					"/fx_event_score_add " + arguments + " \n"
							+ "A problem has occured! The user may have a score for this event already !" + timer,
					true);
		}

		return newResponseEntity("/fx_event_score_add " + arguments + " \n" + "Score has been added !" + timer,
				true);
	}

	@RequestMapping(value = "/listOne", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> listScoreForEvent(@RequestParam("token") String appVerificationToken,
													  @RequestParam("team_domain") String slackTeam, 
													  @RequestParam("text") String arguments) {

		Timer timer = new Timer();

		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);
		
		timer.capture();
		
		String eventName = arguments.trim();
		List<Event> events = eventRepository.getByCriterion("name", eventName);

		if (events.size() == 0)
			return newResponseEntity(
					"/fx_event_score_list " + arguments + " \n" + "No such event ! Check the event name" +timer,
					true);

		Event event = events.get(0);
		
		timer.capture();
		
		List<SlackUserEvent> listEvents = slackUserEventRepository.getByCriterion("event",
				event);

		if (listEvents.size() == 0)
			newResponseEntity(
					"/fx_event_score_list " + arguments + " \n" + "No score has been saved till the moment !" + timer ,
					true);

		String textMessage = "List of scores of " + event.getName() + " :" + " \n ";

		for (SlackUserEvent slackUserEvent: listEvents) {
			SlackUser slackUser = slackUserEvent.getSlackUser();


			textMessage += "<@" + slackUser.getSlackUserId() + "|" + slackUser.getName() + "> "
					+ slackUserEvent.getScore() + " \n";
		}

		return newResponseEntity("/fx_event_score_list " + arguments + " \n" + textMessage + timer, true);
	}

	@RequestMapping(value = "/listAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> scoreList(@RequestParam("token") String appVerificationToken,
											  @RequestParam("team_domain") String slackTeam, 
											  @RequestParam("text") String arguments) {
		
		Timer timer = new Timer();
		
		if (noAccess(appVerificationToken, slackTeam))
			return noAccessResponseEntity(appVerificationToken, slackTeam);

		OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
		
		timer.capture();
		
		if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
			return newResponseEntity("/fx_score  : " + arguments + " \n " + "Arguments should be :@Username" + timer , true);

		String profileId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
		SlackUser slackUser = slackUserRepository.findById(profileId);
		
		timer.capture();
		
		return newResponseEntity("<@" + slackUser.getSlackUserId() + "|"
				+ slackApiAccessService.getUser(profileId).getName() + "> score :" + eventService.getGlobalScore(slackUser) + timer , true);
	}
}

