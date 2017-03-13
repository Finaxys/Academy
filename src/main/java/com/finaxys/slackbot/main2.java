package com.finaxys.slackbot;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.finaxys.slackbot.BUL.Interfaces.SlackBotCommandService;
import com.finaxys.slackbot.Configuration.Classes.SpringContext;
import com.finaxys.slackbot.Domains.FinaxysProfile;
import com.finaxys.slackbot.Utilities.SlackBot;

import allbegray.slack.SlackClientFactory;
import allbegray.slack.webapi.SlackWebApiClient;

public class main2 {
	static AbstractApplicationContext context = new AnnotationConfigApplicationContext(SpringContext.class);

	public static void main(String[] args) {
		SlackBotCommandService slackBotCommandServiceImpl = (SlackBotCommandService) context
				.getBean("slackBotCommandServiceImpl");
		SlackWebApiClient webApiClient = SlackBot.getSlackWebApiClient();
		List<FinaxysProfile> users = slackBotCommandServiceImpl.listerUsers(10);
		for (int i = 0; i < users.size(); i++) {
			System.out.println(webApiClient.getUserInfo((users.get(i).getId())).getName() + " " + users.get(i).getScore());
		}
	}
}
