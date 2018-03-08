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
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUserEvent;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
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

	@RequestMapping(value = "/fx_event_score_add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> addEventScore(@RequestParam("text") String arguments,
												  @RequestParam("user_id") String eventManagerId) {

		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();
		
		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_event_score_add");
		
		String userId = argumentsSplitter.getUserId();
		String eventName = argumentsSplitter.getString(0);
		
		String userIdArgs = "";
		List<SlackUser> allUsers = slackUserService.getAll();
		
		// GET USER ID OF THE SELECTED USER IN PARAMETER!
		for(SlackUser user : allUsers) {
			if (user.getName().equals(userId)) {
				userIdArgs = user.getSlackUserId();
			}
		}
		
		
		int score = Integer.parseInt(argumentsSplitter.getIntegers(0));
		
		timer.capture();

		Event event = eventService.getEventByName(eventName);
		
		timer.capture();

		if (event==null)
			return newResponseEntity("Nonexistent event" + timer, true);

		//TODO
		if (!isEventManager(eventManagerId, eventName) && !isAdmin(eventManagerId))
			return newResponseEntity("/fx_event_score_add " + arguments + "\n"
					+ "You are neither admin nor a event manager !" + timer, true);


		SlackUser user = slackUserService.get(userIdArgs);
		
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

	@RequestMapping(value = "/fx_event_score_list", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> listScoreForEvent(@RequestParam("text") String arguments) {

		SlackBotTimer timer = new SlackBotTimer();

		timer.capture();
		
		String eventName = arguments.trim();
		Event event = eventService.getEventByName(eventName);

		if (event == null)
			return newResponseEntity(
					"/fx_event_score_list " + arguments + " \n" + "No such event ! Check the event name" +timer,
					true);
		
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

	@RequestMapping(value = "/fx_score", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> scoreList(@RequestParam("text") String arguments) {
		
		SlackBotTimer timer = new SlackBotTimer();
		
		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_score");
		
		String userSlackId = argumentsSplitter.getUserId();

		SlackUser slackUser = slackUserService.get(userSlackId);
		
		timer.capture();
		
		return newResponseEntity("<@" + slackUser.getSlackUserId() + "|"
				+ slackApiAccessService.getUser(userSlackId).getName() + "> score :" + eventService.getGlobalScore(slackUser) + timer , true);
	}

}

