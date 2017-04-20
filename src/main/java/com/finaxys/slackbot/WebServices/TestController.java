package com.finaxys.slackbot.WebServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finaxys.slackbot.BUL.Classes.ScoreService;
import com.finaxys.slackbot.DAL.Event;
import com.finaxys.slackbot.DAL.EventScore;
import com.finaxys.slackbot.DAL.Repository;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUser_Event;
import com.finaxys.slackbot.services.SlackUserService;

@RestController
public class TestController {
	
	
	@Autowired
	private SlackUserService slackUserService;
	
	@RequestMapping("/test")
	public void test() {
		System.out.println(slackUserService);
		System.out.println(slackUserService.get("U4V5ZLK9C").getName());
	}
	
}
