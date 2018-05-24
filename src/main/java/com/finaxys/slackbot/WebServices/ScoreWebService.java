package com.finaxys.slackbot.WebServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.finaxys.slackbot.BUL.Classes.SlackApiAccessService;
import com.finaxys.slackbot.Utilities.ArgumentsSplitter;
import com.finaxys.slackbot.Utilities.SlackBotTimer;
import com.finaxys.slackbot.interfaces.EventService;
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
	
	@RequestMapping(value = "/fx_score", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<JsonNode> scoreList(@RequestParam("text") String arguments) {
		
		SlackBotTimer timer = new SlackBotTimer();
		
		ArgumentsSplitter argumentsSplitter = new ArgumentsSplitter(arguments, "/fx_score");

		return newResponseEntity("Nonexistent event" + timer, true);
		/*
		String userSlackId = argumentsSplitter.getUserId();

		SlackUser slackUser = slackUserService.get(userSlackId);
		
		timer.capture();
		
		return newResponseEntity("<@" + slackUser.getSlackUserId() + "|"
				+ slackApiAccessService.getUser(userSlackId).getName() + "> score :" + eventService.getGlobalScore(slackUser) + timer , true);
				*/
	}

}

