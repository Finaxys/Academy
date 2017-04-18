package com.finaxys.slackbot.WebServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finaxys.slackbot.DAL.Challenge;
import com.finaxys.slackbot.DAL.SlackUser;
import com.finaxys.slackbot.DAL.SlackUser_Challenge;
import com.finaxys.slackbot.DAL.Repository;

@RestController
public class TestController {

	@Autowired
	Repository<SlackUser, String> users;

	@Autowired
	Repository<Challenge, Integer> challenges;

	@RequestMapping("/test")
	public void test() {

		SlackUser user = users.findById("U4BCK26CU");

		System.out.println(user);
		
		System.out.println(user.getFinaxysProfile_challenges().size());
		System.out.println(user.getRoles()==null?"null":user.getRoles().size());
		// user.setScore(90);
		
		Challenge finaxysChallenge = challenges.findById(15);
		SlackUser_Challenge fpc = new SlackUser_Challenge(13, finaxysChallenge.getId(), user.getId());
		user.getFinaxysProfile_challenges().add(fpc);

		
		
		finaxysChallenge = challenges.findById(11);
		fpc = new SlackUser_Challenge(13, finaxysChallenge.getId(), user.getId());
		user.getFinaxysProfile_challenges().add(fpc);

		finaxysChallenge = challenges.findById(12);
		fpc = new SlackUser_Challenge(13, finaxysChallenge.getId(), user.getId());
		user.getFinaxysProfile_challenges().add(fpc);

		finaxysChallenge = challenges.findById(13);
		fpc = new SlackUser_Challenge(13, finaxysChallenge.getId(), user.getId());

		user.getFinaxysProfile_challenges().add(fpc);
		
		users.saveOrUpdate(user);
		
		
		user = users.findById("U4BCK26CU");

		System.out.println(user);
		
		System.out.println(user.getFinaxysProfile_challenges().size());
		System.out.println(user.getRoles()==null?"null":user.getRoles().size());
		

	}
}
