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
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.EventService;
import com.finaxys.slackbot.interfaces.SlackUserEventService;
import com.finaxys.slackbot.interfaces.SlackUserService;

@RestController
@RequestMapping("/scores")
public class ScoreWebService extends BaseWebService {
	@Autowired
	private SlackApiAccessService slackApiAccessService;

	@Autowired
	EventService eventService;

	@Autowired
	SlackUserService slackUserService;
	
	@Autowired
	SlackUserEventService slackUserEventService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> addEventScore(@RequestParam("text") String arguments,
												  @RequestParam("user_id") String eventManagerId) {

		SlackBotTimer timer = new SlackBotTimer();

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

		//SlackUser user = slackUserRepository.findById(userId);
		SlackUser user = slackUserService.get(userId);
		
		if(user==null){
			user = new SlackUser(userId,slackApiAccessService.getUser(userId).getName());
			slackUserService.save(user);
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

			new Thread(()-> {slackUserEventService.save(slackUserEvent2);}).start();
			
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
	public ResponseEntity<JsonNode> listScoreForEvent(@RequestParam("text") String arguments) {

		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();
		
		String eventName = arguments.trim();
		List<Event> events = eventRepository.getByCriterion("name", eventName);

		if (events.size() == 0)
			return newResponseEntity(
					"/fx_event_score_list " + arguments + " \n" + "No such event ! Check the event name" +timer,
					true);

		Event event = events.get(0);
		
		timer.capture();
		
		List<SlackUserEvent> listEvents = slackUserEventService.getAllByEvent(event);

		if (listEvents ==null)
			return newResponseEntity(
					"/fx_event_score_list " + arguments + " \n" + "No score has been saved till the moment !" + timer ,
					true);

		String textMessage = "Leaderboard of " + event.getName() + " :" + " \n ";

		for (SlackUserEvent slackUserEvent: listEvents) {
			SlackUser slackUser = slackUserEvent.getSlackUser();


			textMessage += "<@" + slackUser.getSlackUserId() + "|" + slackUser.getName() + "> "
					+ slackUserEvent.getScore() + " \n";
		}

		return newResponseEntity("/fx_event_score_list " + arguments + " \n" + textMessage + timer, true);
	}

	@RequestMapping(value = "/listAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> scoreList(@RequestParam("text") String arguments) {
		
		SlackBotTimer timer = new SlackBotTimer();
		

		OneUsernameArgumentMatcher oneUsernameArgumentsMatcher = new OneUsernameArgumentMatcher();
		
		timer.capture();
		
		if (!oneUsernameArgumentsMatcher.isCorrect(arguments))
			return newResponseEntity("/fx_score  : " + arguments + " \n " + "Arguments should be :@Username" + timer , true);

		String userSlackId = oneUsernameArgumentsMatcher.getUserIdArgument(arguments);
		SlackUser slackUser = slackUserService.get(userSlackId);
		
		timer.capture();
		
		return newResponseEntity("<@" + slackUser.getSlackUserId() + "|"
				+ slackApiAccessService.getUser(userSlackId).getName() + "> score :" + eventService.getGlobalScore(slackUser) + timer , true);
	}
}

