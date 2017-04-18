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

@RestController
public class TestController {

	@Autowired
	Repository<SlackUser, String> users;

	@Autowired
	Repository<Event, Integer> challenges;
	
	@Autowired
	ScoreService ss;
	@Autowired
	Repository<EventScore, Integer> es;
	
	
	@RequestMapping("/test2")
	public void test2() {
		System.out.println(es.getPersistentClass());
		
		for (EventScore es2 : es.getAll()) {
			System.out.println(es2.getPoints());
		}
		
		//System.out.println(ss.getScore("SENT_A_REAL_MESSAGE"));
	}
	
	@RequestMapping("/test")
	public void test() {

		SlackUser user = users.findById("U4BCK26CU");

		System.out.println(user);
		
		System.out.println(user.getSlackUserEvents().size());
		System.out.println(user.getRoles()==null?"null":user.getRoles().size());
		// user.setScore(90);
		
		Event finaxysChallenge = challenges.findById(15);
		SlackUser_Event fpc = new SlackUser_Event(13, finaxysChallenge.getId(), user.getId());
		user.getSlackUserEvents().add(fpc);

		
		
		finaxysChallenge = challenges.findById(11);
		fpc = new SlackUser_Event(13, finaxysChallenge.getId(), user.getId());
		user.getSlackUserEvents().add(fpc);

		finaxysChallenge = challenges.findById(12);
		fpc = new SlackUser_Event(13, finaxysChallenge.getId(), user.getId());
		user.getSlackUserEvents().add(fpc);

		finaxysChallenge = challenges.findById(13);
		fpc = new SlackUser_Event(13, finaxysChallenge.getId(), user.getId());

		user.getSlackUserEvents().add(fpc);
		
		users.saveOrUpdate(user);
		
		
		user = users.findById("U4BCK26CU");

		System.out.println(user);
		
		System.out.println(user.getSlackUserEvents().size());
		System.out.println(user.getRoles()==null?"null":user.getRoles().size());
		

	}
}
